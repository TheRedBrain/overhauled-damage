package com.github.theredbrain.overhauleddamage.entity;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class LivingEntityHelper {

	// region --- overhauled damage calculation ---
	public static float calculateOverhauledDamage(ServerLevel serverLevel, LivingEntity livingEntity, DamageSource source, float amount) {
		ServerConfig serverConfig = OverhauledDamage.SERVER_CONFIG;
		if (!OverhauledDamage.SERVER_CONFIG.enable_overhauled_damage_calculation.get()) {
			return amount;
		}
		boolean enable_debug_log = serverConfig.overhauled_damage_calculation.enable_debug_log.get();
		if (enable_debug_log) {
			OverhauledDamage.info("----- start of new damage calculation log -----");
			OverhauledDamage.info("");
			OverhauledDamage.info("entity taken damage : " + livingEntity.getName().getString());
			OverhauledDamage.info("damage source : " + source.toString());
			OverhauledDamage.info("damage amount : " + amount);
			OverhauledDamage.info("");
		}
		if (amount <= 0) {
			if (enable_debug_log) {
				OverhauledDamage.info("nothing to calculate for zero damage");
				OverhauledDamage.info("");
			}
			return amount;
		}

		LivingEntity attacker = null;
		if (source.getEntity() instanceof LivingEntity) {
			attacker = (LivingEntity) source.getEntity();
		}
		if (enable_debug_log) {
			if (attacker != null) {
				OverhauledDamage.info("entity dealing damage : " + attacker.getName().getString());
				OverhauledDamage.info("");
			}
		}

		if (source.is(Tags.IS_TRUE_DAMAGE)) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- is true damage ---");
				OverhauledDamage.info("");
				OverhauledDamage.info("--- true damage can't be blocked and is not reduced by armor, protection or resistances ---");
				OverhauledDamage.info("");
				OverhauledDamage.info("--- true damage does not apply effect build ups ---");
				OverhauledDamage.info("");
			}
			return calculateAppliedHealthDamage(livingEntity, enable_debug_log, amount);
		}

		// fallback
		ServerConfig.DamageCalculation.AttackTypeMultipliers damage_type_multiplier = null;

		ValidatedMap<String, ServerConfig.DamageCalculation.AttackTypeMultipliers> damage_type_multipliers = serverConfig.overhauled_damage_calculation.damage_type_multipliers;

		String damageTypeId = "";
		Optional<ResourceKey<DamageType>> optional = source.typeHolder().unwrapKey();

		if (optional.isPresent()) {
			damageTypeId = optional.get().identifier().toString();
		}
		if (!damageTypeId.isEmpty()) {
			if (enable_debug_log) {
				OverhauledDamage.info("damage type : " + damageTypeId);
				OverhauledDamage.info("");
			}
			damage_type_multiplier = damage_type_multipliers.get(damageTypeId);
		}
		if (damage_type_multiplier == null) {
			if (enable_debug_log) {
				OverhauledDamage.info("using default_damage_type_multipliers");
				OverhauledDamage.info("");
			}
			damage_type_multiplier = serverConfig.overhauled_damage_calculation.default_damage_type_multipliers.get();
		}
		if (enable_debug_log) {
			OverhauledDamage.info("used damage_type_multipliers : " + damage_type_multiplier.toString());
			OverhauledDamage.info("");
		}

		AttackTypeDamageAmounts attack_type_damage_amounts = new AttackTypeDamageAmounts(
				amount * damage_type_multiplier.generic,
				amount * damage_type_multiplier.bashing,
				amount * damage_type_multiplier.piercing,
				amount * damage_type_multiplier.slashing,
				amount * damage_type_multiplier.poison,
				amount * damage_type_multiplier.fire,
				amount * damage_type_multiplier.frost,
				amount * damage_type_multiplier.lightning
		);
		if (enable_debug_log) {
			OverhauledDamage.info("--- initial attack amounts ---");
			OverhauledDamage.info("generic_amount : " + attack_type_damage_amounts.generic_amount);
			OverhauledDamage.info("bashing_amount : " + attack_type_damage_amounts.bashing_amount);
			OverhauledDamage.info("piercing_amount : " + attack_type_damage_amounts.piercing_amount);
			OverhauledDamage.info("slashing_amount : " + attack_type_damage_amounts.slashing_amount);
			OverhauledDamage.info("poison_amount : " + attack_type_damage_amounts.poison_amount);
			OverhauledDamage.info("fire_amount : " + attack_type_damage_amounts.fire_amount);
			OverhauledDamage.info("frost_amount : " + attack_type_damage_amounts.frost_amount);
			OverhauledDamage.info("lightning_amount : " + attack_type_damage_amounts.lightning_amount);
			OverhauledDamage.info("");
		}

		if (attacker != null) {
			attack_type_damage_amounts = addAdditionalAttackAmountsFromAttacker(attack_type_damage_amounts, enable_debug_log, attacker);
		}

		boolean apply_stagger_and_knockback_outside_of_shield_blocking = true;
		if (OverhauledDamage.isBlockingOverhaulEnabled()) {
			Pair<Boolean, AttackTypeDamageAmounts> pair = applyShieldBlocking(attack_type_damage_amounts, serverLevel, serverConfig, enable_debug_log, livingEntity, source, attacker);
			apply_stagger_and_knockback_outside_of_shield_blocking = pair.getFirst();
			attack_type_damage_amounts = pair.getSecond();
		}

		if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			if (enable_debug_log) {
				OverhauledDamage.info("damage bypasses armor");
			}
		} else if (serverConfig.overhauled_damage_calculation.enable_armor_overhaul.get()) {
			attack_type_damage_amounts = calculateDamageAmountAfterArmor(attack_type_damage_amounts, serverConfig, enable_debug_log, livingEntity, source);
		} else if (enable_debug_log) {
			OverhauledDamage.info("armor overhaul not active");
		}

		attack_type_damage_amounts = calculateDamageAmountAfterResistances(attack_type_damage_amounts, enable_debug_log, livingEntity);

		applyHitStun(livingEntity, serverConfig, enable_debug_log, damageTypeId);

		applyBuildUps(livingEntity, source, serverConfig, enable_debug_log, apply_stagger_and_knockback_outside_of_shield_blocking, attack_type_damage_amounts);

		ServerConfig.DamageCalculation.AttackTypeMultipliers applied_damage_multipliers = serverConfig.overhauled_damage_calculation.applied_damage_multipliers.get();

		if (enable_debug_log) {
			OverhauledDamage.info("--- damage applied to resources like health is multiplied ---");
			OverhauledDamage.info("applied_damage_multipliers : " + applied_damage_multipliers);
			OverhauledDamage.info("");
		}

		amount = (attack_type_damage_amounts.generic_amount * applied_damage_multipliers.generic)
				+ (attack_type_damage_amounts.bashing_amount * applied_damage_multipliers.bashing)
				+ (attack_type_damage_amounts.piercing_amount * applied_damage_multipliers.piercing)
				+ (attack_type_damage_amounts.slashing_amount * applied_damage_multipliers.slashing)
				+ (attack_type_damage_amounts.poison_amount * applied_damage_multipliers.poison)
				+ (attack_type_damage_amounts.fire_amount * applied_damage_multipliers.fire)
				+ (attack_type_damage_amounts.frost_amount * applied_damage_multipliers.frost)
				+ (attack_type_damage_amounts.lightning_amount * applied_damage_multipliers.lightning);

		float applied_knockback = calculateAndApplyKnockback(livingEntity, source, serverConfig, enable_debug_log, apply_stagger_and_knockback_outside_of_shield_blocking, attack_type_damage_amounts);

		// taking actual damage interrupts eating food, drinking potions, etc
		if (!livingEntity.isBlocking() && ((amount > 0.0f && serverConfig.overhauled_damage_calculation.damage_interrupts_item_usage.get()) || (applied_knockback > 0.0f && serverConfig.overhauled_damage_calculation.knockback_overhaul.knockback_interrupts_item_usage.get()))) {
			if (enable_debug_log) {
				OverhauledDamage.info("item usage was stopped");
				OverhauledDamage.info("");
			}
			livingEntity.releaseUsingItem();
		}

		return calculateAppliedHealthDamage(livingEntity, enable_debug_log, amount);
	}

	public static AttackTypeDamageAmounts addAdditionalAttackAmountsFromAttacker(AttackTypeDamageAmounts attackTypeDamageAmounts, boolean enable_debug_log, LivingEntity attacker) {
		AttackTypeDamageAmounts newAttackTypeDamageAmounts = new AttackTypeDamageAmounts(
				attackTypeDamageAmounts.generic_amount,
				(((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalBashingDamage() + attackTypeDamageAmounts.bashing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedBashingDamage(),
				(((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalPiercingDamage() + attackTypeDamageAmounts.piercing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedPiercingDamage(),
				(((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalSlashingDamage() + attackTypeDamageAmounts.slashing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedSlashingDamage(),
				(((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalFireDamage() + attackTypeDamageAmounts.fire_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedFireDamage(),
				(((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalFrostDamage() + attackTypeDamageAmounts.frost_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedFrostDamage(),
				(((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalLightningDamage() + attackTypeDamageAmounts.lightning_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedLightningDamage(),
				(((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalPoisonDamage() + attackTypeDamageAmounts.poison_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedPoisonDamage()

		);
		if (enable_debug_log) {
			OverhauledDamage.info("--- attack amounts after additions ---");
			OverhauledDamage.info("generic_amount : " + newAttackTypeDamageAmounts.generic_amount);
			OverhauledDamage.info("bashing_amount : " + newAttackTypeDamageAmounts.bashing_amount);
			OverhauledDamage.info("piercing_amount : " + newAttackTypeDamageAmounts.piercing_amount);
			OverhauledDamage.info("slashing_amount : " + newAttackTypeDamageAmounts.slashing_amount);
			OverhauledDamage.info("poison_amount : " + newAttackTypeDamageAmounts.poison_amount);
			OverhauledDamage.info("fire_amount : " + newAttackTypeDamageAmounts.fire_amount);
			OverhauledDamage.info("frost_amount : " + newAttackTypeDamageAmounts.frost_amount);
			OverhauledDamage.info("lightning_amount : " + newAttackTypeDamageAmounts.lightning_amount);
			OverhauledDamage.info("");
		}
		return newAttackTypeDamageAmounts;
	}

	public static Pair<Boolean, AttackTypeDamageAmounts> applyShieldBlocking(AttackTypeDamageAmounts attackTypeDamageAmounts, ServerLevel serverLevel, ServerConfig serverConfig, boolean enable_debug_log, LivingEntity livingEntity, DamageSource source, @Nullable LivingEntity attacker) {

		ItemStack shieldItemStack = livingEntity.getUseItem();
		BlocksAttacks blocksAttacks = shieldItemStack.get(DataComponents.BLOCKS_ATTACKS);

		if (blocksAttacks == null) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- no blocking/parrying was tried ---");
				OverhauledDamage.info("");
			}
			return new Pair<>(true, attackTypeDamageAmounts);
		}
		if (blocksAttacks.bypassedBy().map(t -> t.contains(source.typeHolder())).orElse(false)) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- no blocking/parrying was tried ---");
				OverhauledDamage.info("");
			}
			return new Pair<>(true, attackTypeDamageAmounts);
		}
		if (source.getDirectEntity() instanceof AbstractArrow abstractArrow && abstractArrow.getPierceLevel() > 0) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- piercing projectile bypassed blocking ---");
				OverhauledDamage.info("");
			}
			return new Pair<>(true, attackTypeDamageAmounts);
		}
		if (OverhauledDamage.currentStaminaAllowsBlocking(livingEntity)) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- blocking/parrying failed because stamina was too low ---");
				OverhauledDamage.info("");
			}
			return new Pair<>(true, attackTypeDamageAmounts);
		}

		// a parry is tried, if the blocking time < the parry window of the blocking entity, the blocking entity can parry at all and the blocking item is in the 'can_parry' item tag
		boolean tryParry = OverhauledDamage.canParry(livingEntity, source, shieldItemStack);
		double parryBonus = OverhauledDamage.getParryMultiplier(livingEntity, tryParry);
		if (enable_debug_log) {
			OverhauledDamage.info("--- blocking/parrying ---");
			OverhauledDamage.info("");
			OverhauledDamage.info("tryParry : " + tryParry);
			OverhauledDamage.info("");
			OverhauledDamage.info("parryBonus : " + parryBonus);
			OverhauledDamage.info("");
		}

		// reduce the stamina of the blocking/parrying entity by the block/parry stamina cost
		OverhauledDamage.applyBlockAttackStaminaCost(livingEntity, tryParry);

		// when stamina is lower than zero after blocking/parrying the block/parry was not successful and the damage is not reduced
		if (OverhauledDamage.getCurrentStamina(livingEntity) < 0) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- blocking/parrying attempt consumed too much stamina ---");
				OverhauledDamage.info("");
			}
			return new Pair<>(true, attackTypeDamageAmounts);
		}

		float blockedBashingDamage;
		float blockedPiercingDamage;
		float blockedSlashingDamage;
		float blockedFireDamage;
		float blockedFrostDamage;
		float blockedLightningDamage;
		float blockedPoisonDamage;

		if (serverConfig.overhauled_damage_calculation.blocking_overhaul.blocked_damage_calculation_works_with_flat_values.get()) {
			if (enable_debug_log) {
				OverhauledDamage.info("blocked damage calculation uses flat values");
				OverhauledDamage.info("");
			}
			blockedBashingDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
			blockedPiercingDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
			blockedSlashingDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
			blockedFireDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFireDamage() * parryBonus);
			blockedFrostDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFrostDamage() * parryBonus);
			blockedLightningDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedLightningDamage() * parryBonus);
			blockedPoisonDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPoisonDamage() * parryBonus);
		} else {
			if (enable_debug_log) {
				OverhauledDamage.info("blocked damage calculation uses percentage values");
				OverhauledDamage.info("");
			}
			blockedBashingDamage = (float) (attackTypeDamageAmounts.bashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
			blockedPiercingDamage = (float) (attackTypeDamageAmounts.piercing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
			blockedSlashingDamage = (float) (attackTypeDamageAmounts.slashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
			blockedFireDamage = (float) (attackTypeDamageAmounts.fire_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFireDamage() * parryBonus / 100);
			blockedFrostDamage = (float) (attackTypeDamageAmounts.frost_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFrostDamage() * parryBonus / 100);
			blockedLightningDamage = (float) (attackTypeDamageAmounts.lightning_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedLightningDamage() * parryBonus / 100);
			blockedPoisonDamage = (float) (attackTypeDamageAmounts.poison_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPoisonDamage() * parryBonus / 100);
		}
		if (enable_debug_log) {
			OverhauledDamage.info("--- blocked damage amounts ---");
			OverhauledDamage.info("blockedBashingDamage : " + blockedBashingDamage);
			OverhauledDamage.info("blockedPiercingDamage : " + blockedPiercingDamage);
			OverhauledDamage.info("blockedSlashingDamage : " + blockedSlashingDamage);
			OverhauledDamage.info("blockedFireDamage : " + blockedFireDamage);
			OverhauledDamage.info("blockedFrostDamage : " + blockedFrostDamage);
			OverhauledDamage.info("blockedLightningDamage : " + blockedLightningDamage);
			OverhauledDamage.info("blockedPoisonDamage : " + blockedPoisonDamage);
			OverhauledDamage.info("");
		}

		if (applyStaggerBasedOnLeftOverDamage(
				livingEntity,
				serverConfig,
				enable_debug_log,
				attackTypeDamageAmounts.generic_amount,
				attackTypeDamageAmounts.bashing_amount - blockedBashingDamage,
				attackTypeDamageAmounts.piercing_amount - blockedPiercingDamage,
				attackTypeDamageAmounts.slashing_amount - blockedSlashingDamage,
				attackTypeDamageAmounts.poison_amount - blockedPoisonDamage,
				attackTypeDamageAmounts.fire_amount - blockedFireDamage,
				attackTypeDamageAmounts.frost_amount - blockedFrostDamage,
				attackTypeDamageAmounts.lightning_amount - blockedLightningDamage
		)) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- blocking/parrying failed because the damage was too high ---");
				OverhauledDamage.info("");
			}
			return new Pair<>(false, attackTypeDamageAmounts);
		}

		AttackTypeDamageAmounts newAttackTypeDamageAmounts = new AttackTypeDamageAmounts(
				attackTypeDamageAmounts.generic_amount,
				attackTypeDamageAmounts.bashing_amount - blockedBashingDamage,
				attackTypeDamageAmounts.piercing_amount - blockedPiercingDamage,
				attackTypeDamageAmounts.slashing_amount - blockedSlashingDamage,
				attackTypeDamageAmounts.poison_amount - blockedPoisonDamage,
				attackTypeDamageAmounts.fire_amount - blockedFireDamage,
				attackTypeDamageAmounts.frost_amount - blockedFrostDamage,
				attackTypeDamageAmounts.lightning_amount - blockedLightningDamage

		);
		if (attacker != null) {
			if (tryParry) {

				addStaggerBuildUp(attacker, ((DuckLivingEntityMixin) attacker).overhauleddamage$getMaxStaggerBuildUp());
				if (enable_debug_log) {
					OverhauledDamage.info("--- successful parries stagger the attacker ---");
					OverhauledDamage.info("");
				}
			} else {
				ServerConfig.DamageCalculation.AttackTypeMultipliers negative_block_force_multipliers = serverConfig.overhauled_damage_calculation.blocking_overhaul.negative_block_force_multipliers.get();
				double applied_knock_back = OverhauledDamage.getAppliedBlockingKnockback(livingEntity, attacker, shieldItemStack, false, attackTypeDamageAmounts.generic_amount * negative_block_force_multipliers.generic + blockedBashingDamage * negative_block_force_multipliers.bashing + blockedPiercingDamage * negative_block_force_multipliers.piercing + blockedSlashingDamage * negative_block_force_multipliers.slashing + blockedPoisonDamage * negative_block_force_multipliers.poison + blockedFireDamage * negative_block_force_multipliers.fire + blockedFrostDamage * negative_block_force_multipliers.frost + blockedLightningDamage * negative_block_force_multipliers.lightning);

				if (enable_debug_log) {
					OverhauledDamage.info("--- successful blocks apply knockback ---");
					// blocked damage is multiplied based on attack type
					// result is subtracted from defenders block force
					// the total block force is multiplied by a global modifier (supplied by Blocking Overhaul)
					// if end result is positive, the attacker gets knocked back
					// if end result is negative, the defender is knocked back
					OverhauledDamage.info("applied_knockback : " + applied_knock_back);
					OverhauledDamage.info("");
					OverhauledDamage.info("when applied_knock_back is greater zero, it's applied to the attacker");
					OverhauledDamage.info("");
					OverhauledDamage.info("when applied_knock_back is lesser zero, its absolute value is applied to the blocking entity");
					OverhauledDamage.info("");
				}
				if (applied_knock_back > 0.0) {
					attacker.knockback(applied_knock_back, livingEntity.getX() - attacker.getX(), livingEntity.getZ() - attacker.getZ());
				} else if (applied_knock_back < 0.0) {
					livingEntity.knockback(Math.abs(applied_knock_back), attacker.getX() - livingEntity.getX(), attacker.getZ() - livingEntity.getZ());
				}
			}
		}
		float totalBlockedDamage = blockedBashingDamage + blockedPiercingDamage + blockedSlashingDamage + blockedFireDamage + blockedFrostDamage + blockedLightningDamage + blockedPoisonDamage;
		if (livingEntity instanceof ServerPlayer serverPlayerEntity && totalBlockedDamage > 0.0f && totalBlockedDamage < 3.4028235E37f) {
			serverPlayerEntity.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(totalBlockedDamage * 10.0f));
		}

		OverhauledDamage.playBlockingSoundEvent(serverLevel, livingEntity, shieldItemStack, tryParry);

		if (enable_debug_log) {
			OverhauledDamage.info("--- attack amounts after blocking/parrying ---");
			OverhauledDamage.info("generic_amount : " + newAttackTypeDamageAmounts.generic_amount);
			OverhauledDamage.info("bashing_amount : " + newAttackTypeDamageAmounts.bashing_amount);
			OverhauledDamage.info("piercing_amount : " + newAttackTypeDamageAmounts.piercing_amount);
			OverhauledDamage.info("slashing_amount : " + newAttackTypeDamageAmounts.slashing_amount);
			OverhauledDamage.info("poison_amount : " + newAttackTypeDamageAmounts.poison_amount);
			OverhauledDamage.info("fire_amount : " + newAttackTypeDamageAmounts.fire_amount);
			OverhauledDamage.info("frost_amount : " + newAttackTypeDamageAmounts.frost_amount);
			OverhauledDamage.info("lightning_amount : " + newAttackTypeDamageAmounts.lightning_amount);
			OverhauledDamage.info("");
		}
		return new Pair<>(false, newAttackTypeDamageAmounts);
	}

	/**
	 * @return whether livingEntity has been staggered
	 */
	public static boolean applyStaggerBasedOnLeftOverDamage(LivingEntity livingEntity, ServerConfig serverConfig, boolean enable_debug_log, float generic_amount, float bashing_amount, float piercing_amount, float slashing_amount, float poison_amount, float fire_amount, float frost_amount, float lightning_amount) {
		ServerConfig.DamageCalculation.AttackTypeMultipliers stagger_multipliers = serverConfig.overhauled_damage_calculation.stagger_multipliers.get();
		if (enable_debug_log) {
			OverhauledDamage.info("--- apply stagger based on left over damage ---");
			OverhauledDamage.info("");
			OverhauledDamage.info("stagger_multipliers : " + stagger_multipliers.toString());
			OverhauledDamage.info("");
		}
		float appliedStagger = (generic_amount * stagger_multipliers.generic) + (bashing_amount * stagger_multipliers.bashing) + (piercing_amount * stagger_multipliers.piercing) + (slashing_amount * stagger_multipliers.slashing) + (poison_amount * stagger_multipliers.poison) + (fire_amount * stagger_multipliers.fire) + (frost_amount * stagger_multipliers.frost) + (lightning_amount * stagger_multipliers.lightning);
		if (appliedStagger > 0) {
			if (enable_debug_log) {
				OverhauledDamage.info("appliedStagger : " + appliedStagger);
				OverhauledDamage.info("");
			}
			addStaggerBuildUp(livingEntity, appliedStagger);
			return getStaggerBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp();
		}

		return false;
	}

	public static AttackTypeDamageAmounts calculateDamageAmountAfterArmor(AttackTypeDamageAmounts attackTypeDamageAmounts, ServerConfig serverConfig, boolean enable_debug_log, LivingEntity livingEntity, DamageSource source) {

		float effective_armor = livingEntity.getArmorValue();

		if (serverConfig.overhauled_damage_calculation.armor_overhaul.enable_armor_toughness_attribute.get()) {
			effective_armor *= (float) livingEntity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
			if (enable_debug_log) {
				OverhauledDamage.info("armor toughness is enabled");
				OverhauledDamage.info("");
				OverhauledDamage.info("effective_armor (armor * armor_toughness) : " + effective_armor);
				OverhauledDamage.info("");
			}
		} else if (enable_debug_log) {
			OverhauledDamage.info("armor toughness is disabled");
			OverhauledDamage.info("");
			OverhauledDamage.info("armor : " + effective_armor);
			OverhauledDamage.info("");
		}

		float armorDamage;
		AttackTypeDamageAmounts newAttackTypeDamageAmounts;

//		if (serverConfig.overhauled_damage_calculation.armor_overhaul.armor_calculation_works_with_flat_values.get()) {
//			// TODO this calculation needs a serious overhaul
//			// effective armor reduces damage by its amount
//			// armor is more or less effective against different attack types
//			if (enable_debug_log) {
//				OverhauledDamage.info("armor calculation uses flat values");
//				OverhauledDamage.info("effective_armor : " + effective_armor);
//				OverhauledDamage.info("");
//			}
//
//			float newPiercingDamage;
//			float newBashingDamage;
//			float newFireDamage;
//			float newPoisonDamage;
//			float newFrostDamage;
//			float newLightningDamage;
//			float newSlashingDamage;
//
//			if (piercing_amount * 1.25 <= effective_armor) {
//				effective_armor -= (float) (piercing_amount * 1.25);
//				piercing_amount = 0;
//			} else {
//				piercing_amount -= (float) (effective_armor * 0.75);
//				effective_armor = 0;
//			}
//
//			if (attackTypeDamageAmounts.bashing_amount <= effective_armor) {
//				effective_armor -= attackTypeDamageAmounts.bashing_amount;
//				newBashingDamage = 0;
//			} else {
//				newBashingDamage = attackTypeDamageAmounts.bashing_amount - effective_armor;
//				effective_armor = 0;
//			}
//
//			if (fire_amount <= effective_armor) {
//				effective_armor -= fire_amount;
//				fire_amount = 0;
//			} else {
//				fire_amount -= effective_armor;
//				effective_armor = 0;
//			}
//
//			if (slashing_amount <= effective_armor) {
//				effective_armor -= slashing_amount;
//				slashing_amount = 0;
//			} else {
//				slashing_amount -= effective_armor;
//				slashing_amount = (float) (slashing_amount * 1.25); // slashing damage not blocked by armor deals more damage
//				effective_armor = 0;
//			}
//			armorDamage = livingEntity.getArmorValue() - effective_armor;
//			newAttackTypeDamageAmounts = new AttackTypeDamageAmounts(
//					attackTypeDamageAmounts.generic_amount,
//					newBashingDamage,
//					attackTypeDamageAmounts.piercing_amount - piercing_armor_damage,
//					attackTypeDamageAmounts.slashing_amount - slashing_armor_damage,
//					attackTypeDamageAmounts.poison_amount - poison_armor_damage,
//					attackTypeDamageAmounts.fire_amount - fire_armor_damage,
//					attackTypeDamageAmounts.frost_amount - frost_armor_damage,
//					attackTypeDamageAmounts.lightning_amount - lightning_armor_damage
//
//			);
//		} else {
		// this is the alternative armor calculation
		// armor reduces damage on a percentage base
		// 1 armor point = 1 percent reduction
		if (enable_debug_log) {
			OverhauledDamage.info("armor calculation uses percentage values");
			OverhauledDamage.info("");
			OverhauledDamage.info("armor reduces each attack_type_amount separately");
			OverhauledDamage.info("");
			OverhauledDamage.info("1 armor point = 1 percent reduction ");
			OverhauledDamage.info("");
		}

		// the different attack types have an armor_multiplier on their own
		ServerConfig.DamageCalculation.ArmorOverhaul.ArmorMultipliers armor_multipliers = serverConfig.overhauled_damage_calculation.armor_overhaul.armor_multipliers.get();

		if (enable_debug_log) {
			OverhauledDamage.info("armor_multipliers: " + armor_multipliers.toString());
			OverhauledDamage.info("");
		}
		float generic_armor_damage = attackTypeDamageAmounts.generic_amount * effective_armor * armor_multipliers.generic / 100;
		float bashing_armor_damage = attackTypeDamageAmounts.bashing_amount * effective_armor * armor_multipliers.bashing / 100;
		float piercing_armor_damage = attackTypeDamageAmounts.piercing_amount * effective_armor * armor_multipliers.piercing / 100;
		float slashing_armor_damage = attackTypeDamageAmounts.slashing_amount * effective_armor * armor_multipliers.slashing / 100;
		float poison_armor_damage = attackTypeDamageAmounts.poison_amount * effective_armor * armor_multipliers.poison / 100;
		float fire_armor_damage = attackTypeDamageAmounts.fire_amount * effective_armor * armor_multipliers.fire / 100;
		float frost_armor_damage = attackTypeDamageAmounts.frost_amount * effective_armor * armor_multipliers.frost / 100;
		float lightning_armor_damage = attackTypeDamageAmounts.lightning_amount * effective_armor * armor_multipliers.lightning / 100;

		armorDamage = generic_armor_damage + bashing_armor_damage + piercing_armor_damage + slashing_armor_damage + poison_armor_damage + fire_armor_damage + frost_armor_damage + lightning_armor_damage;
		newAttackTypeDamageAmounts = new AttackTypeDamageAmounts(
				attackTypeDamageAmounts.generic_amount - generic_armor_damage,
				attackTypeDamageAmounts.bashing_amount - bashing_armor_damage,
				attackTypeDamageAmounts.piercing_amount - piercing_armor_damage,
				attackTypeDamageAmounts.slashing_amount - slashing_armor_damage,
				attackTypeDamageAmounts.poison_amount - poison_armor_damage,
				attackTypeDamageAmounts.fire_amount - fire_armor_damage,
				attackTypeDamageAmounts.frost_amount - frost_armor_damage,
				attackTypeDamageAmounts.lightning_amount - lightning_armor_damage

		);

//		}

		livingEntity.hurtArmor(source, armorDamage);
		if (enable_debug_log) {
			OverhauledDamage.info("damage applied to equipped armor: " + armorDamage);
			OverhauledDamage.info("");
			OverhauledDamage.info("--- attack amounts after armor ---");
			OverhauledDamage.info("generic_amount : " + newAttackTypeDamageAmounts.generic_amount);
			OverhauledDamage.info("bashing_amount : " + newAttackTypeDamageAmounts.bashing_amount);
			OverhauledDamage.info("piercing_amount : " + newAttackTypeDamageAmounts.piercing_amount);
			OverhauledDamage.info("slashing_amount : " + newAttackTypeDamageAmounts.slashing_amount);
			OverhauledDamage.info("poison_amount : " + newAttackTypeDamageAmounts.poison_amount);
			OverhauledDamage.info("fire_amount : " + newAttackTypeDamageAmounts.fire_amount);
			OverhauledDamage.info("frost_amount : " + newAttackTypeDamageAmounts.frost_amount);
			OverhauledDamage.info("lightning_amount : " + newAttackTypeDamageAmounts.lightning_amount);
			OverhauledDamage.info("");
		}
		return newAttackTypeDamageAmounts;
	}

	public static AttackTypeDamageAmounts calculateDamageAmountAfterResistances(AttackTypeDamageAmounts attackTypeDamageAmounts, boolean enable_debug_log, LivingEntity livingEntity) {
		AttackTypeDamageAmounts newAttackTypeDamageAmounts = new AttackTypeDamageAmounts(
				attackTypeDamageAmounts.generic_amount,
				attackTypeDamageAmounts.bashing_amount - (attackTypeDamageAmounts.bashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBashingResistance()) / 100,
				attackTypeDamageAmounts.piercing_amount - (attackTypeDamageAmounts.piercing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPiercingResistance()) / 100,
				attackTypeDamageAmounts.slashing_amount - (attackTypeDamageAmounts.slashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getSlashingResistance()) / 100,
				attackTypeDamageAmounts.poison_amount - (attackTypeDamageAmounts.poison_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonResistance()) / 100,
				attackTypeDamageAmounts.fire_amount - (attackTypeDamageAmounts.fire_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFireResistance()) / 100,
				attackTypeDamageAmounts.frost_amount - (attackTypeDamageAmounts.frost_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFrostResistance()) / 100,
				attackTypeDamageAmounts.lightning_amount - (attackTypeDamageAmounts.lightning_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getLightningResistance()) / 100

		);
		if (enable_debug_log) {
			OverhauledDamage.info("--- attack amounts after resistances ---");
			OverhauledDamage.info("generic_amount : " + newAttackTypeDamageAmounts.generic_amount);
			OverhauledDamage.info("bashing_amount : " + newAttackTypeDamageAmounts.bashing_amount);
			OverhauledDamage.info("piercing_amount : " + newAttackTypeDamageAmounts.piercing_amount);
			OverhauledDamage.info("slashing_amount : " + newAttackTypeDamageAmounts.slashing_amount);
			OverhauledDamage.info("poison_amount : " + newAttackTypeDamageAmounts.poison_amount);
			OverhauledDamage.info("fire_amount : " + newAttackTypeDamageAmounts.fire_amount);
			OverhauledDamage.info("frost_amount : " + newAttackTypeDamageAmounts.frost_amount);
			OverhauledDamage.info("lightning_amount : " + newAttackTypeDamageAmounts.lightning_amount);
			OverhauledDamage.info("");
		}
		return newAttackTypeDamageAmounts;
	}

	public static void applyHitStun(LivingEntity livingEntity, ServerConfig serverConfig, boolean enable_debug_log, String damageTypeId) {

		if (!serverConfig.overhauled_damage_calculation.enable_hit_stun_mechanic.get()) {
			if (enable_debug_log) {
				OverhauledDamage.info("no hit stun was applied");
				OverhauledDamage.info("");
			}
			return;
		}
		Optional<Holder.Reference<Attribute>> attribute = BuiltInRegistries.ATTRIBUTE.get(serverConfig.overhauled_damage_calculation.hit_stun_mechanic.hit_stun_counter_attribute_identifier.get());
		if (attribute.isEmpty()) {
			if (enable_debug_log) {
				OverhauledDamage.info("no hit stun was applied");
				OverhauledDamage.info("");
			}
			return;
		}
		if (!livingEntity.getAttributes().hasAttribute(attribute.get())) {
			if (enable_debug_log) {
				OverhauledDamage.info("no hit stun was applied");
				OverhauledDamage.info("");
			}
			return;
		}

		ServerConfig.DamageCalculation.HitStunMechanic.HitStunSettings hitStunSettings = serverConfig.overhauled_damage_calculation.hit_stun_mechanic.hit_stun_settings.get(damageTypeId);
		if (hitStunSettings == null) {
			hitStunSettings = serverConfig.overhauled_damage_calculation.hit_stun_mechanic.default_hit_stun_settings.get();
		}

		double attributeValue = livingEntity.getAttributeValue(attribute.get());
		if (attributeValue <= hitStunSettings.required_attribute_threshold) {
			// apply hit stun
			Optional<Holder.Reference<MobEffect>> hit_stun_status_effect = BuiltInRegistries.MOB_EFFECT.get(serverConfig.overhauled_damage_calculation.hit_stun_mechanic.hit_stun_status_effect_identifier.get());
			if (hit_stun_status_effect.isPresent()) {
				livingEntity.addEffect(new MobEffectInstance(hit_stun_status_effect.get(), hitStunSettings.duration, 0, false, false, true));
			}
		}
	}

	public static void applyBuildUps(LivingEntity livingEntity, DamageSource source, ServerConfig serverConfig, boolean enable_debug_log, boolean shouldApplyStaggerAndKnockbackOutsideOfShieldBlocking, AttackTypeDamageAmounts attackTypeDamageAmounts) {

		if (enable_debug_log) {
			OverhauledDamage.info("--- apply damage by increasing effect build ups ---");
			OverhauledDamage.info("");
		}

		// apply bleeding
		ServerConfig.DamageCalculation.AttackTypeMultipliers bleeding_multipliers = serverConfig.overhauled_damage_calculation.bleeding_multipliers.get();
		if (source.is(Tags.APPLIES_BLEEDING)) {
			float applied_bleeding = (attackTypeDamageAmounts.generic_amount * bleeding_multipliers.generic) + (attackTypeDamageAmounts.bashing_amount * bleeding_multipliers.bashing) + (attackTypeDamageAmounts.piercing_amount * bleeding_multipliers.piercing) + (attackTypeDamageAmounts.slashing_amount * bleeding_multipliers.slashing) + (attackTypeDamageAmounts.poison_amount * bleeding_multipliers.poison) + (attackTypeDamageAmounts.fire_amount * bleeding_multipliers.fire) + (attackTypeDamageAmounts.frost_amount * bleeding_multipliers.frost) + (attackTypeDamageAmounts.lightning_amount * bleeding_multipliers.lightning);

			if (enable_debug_log) {
				OverhauledDamage.info("--- apply bleeding build up ---");
				OverhauledDamage.info("bleeding_multipliers : " + bleeding_multipliers);
			}

			if (applied_bleeding > 0) {
				addBleedingBuildUp(livingEntity, applied_bleeding);
				if (enable_debug_log) {
					OverhauledDamage.info("applied bleeding build up : " + applied_bleeding);
					OverhauledDamage.info("");
				}
			} else if (enable_debug_log) {
				OverhauledDamage.info("no bleeding build up was applied");
				OverhauledDamage.info("");
			}
		}

		if (enable_debug_log) {
			OverhauledDamage.info("--- apply burn build up ---");
		}
		// apply burn
		if (attackTypeDamageAmounts.fire_amount > 0) {
			addBurnBuildUp(livingEntity, attackTypeDamageAmounts.fire_amount);
			if (enable_debug_log) {
				OverhauledDamage.info("applied burn build up : " + attackTypeDamageAmounts.fire_amount);
				OverhauledDamage.info("");
			}
		} else if (enable_debug_log) {
			OverhauledDamage.info("no burn build up was applied");
			OverhauledDamage.info("");
		}

		// apply chilled effect and freeze build up
		if (enable_debug_log) {
			OverhauledDamage.info("--- apply chilled effect and freeze build up ---");
		}
		if (attackTypeDamageAmounts.frost_amount > 0) {
			Optional<Holder.Reference<MobEffect>> chilled_status_effect = BuiltInRegistries.MOB_EFFECT.get(serverConfig.build_up_effects.freeze_build_up.chilled_mob_effect_identifier.get());
			if (chilled_status_effect.isPresent()) {
				int chilledDuration = (int) Math.ceil(attackTypeDamageAmounts.frost_amount * serverConfig.build_up_effects.freeze_build_up.chilled_duration_multiplier);
				int existingChilledDuration = 0;
				int chilledAmplifier = 0;
				MobEffectInstance statusEffectInstance = livingEntity.getEffect(chilled_status_effect.get());
				if (statusEffectInstance != null) {
					chilledDuration = chilledDuration + statusEffectInstance.getDuration();
					if (serverConfig.build_up_effects.freeze_build_up.chilled_duration_is_additive.get()) {
						existingChilledDuration = statusEffectInstance.getDuration();
					}
					if (serverConfig.build_up_effects.freeze_build_up.chilled_amplifier_is_additive.get()) {
						chilledAmplifier = statusEffectInstance.getAmplifier();
					}
				}
				livingEntity.addEffect(new MobEffectInstance(
						chilled_status_effect.get(),
						chilledDuration + existingChilledDuration,
						chilledAmplifier,
						false,
						false,
						true));
				if (enable_debug_log) {
					OverhauledDamage.info("applied chilled effect with duration of : " + chilledDuration + existingChilledDuration + " and amplifier of : " + chilledAmplifier);
					OverhauledDamage.info("");
				}
			} else if (enable_debug_log) {
				OverhauledDamage.info("no chilled effect was applied");
				OverhauledDamage.info("");
			}
			addFreezeBuildUp(livingEntity, attackTypeDamageAmounts.frost_amount);
			if (enable_debug_log) {
				OverhauledDamage.info("applied freeze build up : " + attackTypeDamageAmounts.frost_amount);
				OverhauledDamage.info("");
			}
		} else if (enable_debug_log) {
			OverhauledDamage.info("no chilled effect was applied");
			OverhauledDamage.info("no freeze build up was applied");
			OverhauledDamage.info("");
		}

		// apply stagger if no stagger was applied during shield blocking calculation
		if (shouldApplyStaggerAndKnockbackOutsideOfShieldBlocking) {
			ServerConfig.DamageCalculation.AttackTypeMultipliers stagger_multipliers = serverConfig.overhauled_damage_calculation.stagger_multipliers.get();
			if (enable_debug_log) {
				OverhauledDamage.info("--- apply stagger when no blocking was tried ---");
				OverhauledDamage.info("stagger_multipliers : " + stagger_multipliers.toString());
			}
			float appliedStagger = (attackTypeDamageAmounts.generic_amount * stagger_multipliers.generic) + (attackTypeDamageAmounts.bashing_amount * stagger_multipliers.bashing) + (attackTypeDamageAmounts.piercing_amount * stagger_multipliers.piercing) + (attackTypeDamageAmounts.slashing_amount * stagger_multipliers.slashing) + (attackTypeDamageAmounts.poison_amount * stagger_multipliers.poison) + (attackTypeDamageAmounts.fire_amount * stagger_multipliers.fire) + (attackTypeDamageAmounts.frost_amount * stagger_multipliers.frost) + (attackTypeDamageAmounts.lightning_amount * stagger_multipliers.lightning);
			if (appliedStagger > 0) {
				if (enable_debug_log) {
					OverhauledDamage.info("appliedStagger : " + appliedStagger);
					OverhauledDamage.info("");
				}
				addStaggerBuildUp(livingEntity, appliedStagger);
			} else if (enable_debug_log) {
				OverhauledDamage.info("no stagger was applied");
				OverhauledDamage.info("");
			}
		}

		// apply poison build up
		if (enable_debug_log) {
			OverhauledDamage.info("--- apply poison build up ---");
		}
		if (attackTypeDamageAmounts.poison_amount > 0) {
			if (enable_debug_log) {
				OverhauledDamage.info("applied poison build up : " + attackTypeDamageAmounts.poison_amount);
				OverhauledDamage.info("");
			}
			addPoisonBuildUp(livingEntity, attackTypeDamageAmounts.poison_amount);
		} else if (enable_debug_log) {
			OverhauledDamage.info("no poison build up was applied");
			OverhauledDamage.info("");
		}

		// apply shock build up
		if (enable_debug_log) {
			OverhauledDamage.info("--- apply shock build up ---");
		}
		if (attackTypeDamageAmounts.lightning_amount > 0) {
			if (enable_debug_log) {
				OverhauledDamage.info("applied shock build up : " + attackTypeDamageAmounts.lightning_amount);
				OverhauledDamage.info("");
			}
			addShockBuildUp(livingEntity, attackTypeDamageAmounts.lightning_amount);
		} else if (enable_debug_log) {
			OverhauledDamage.info("no shock build up was applied");
			OverhauledDamage.info("");
		}
	}

	public static float calculateAndApplyKnockback(LivingEntity livingEntity, DamageSource source, ServerConfig serverConfig, boolean enable_debug_log, boolean shouldApplyStaggerAndKnockbackOutsideOfShieldBlocking, AttackTypeDamageAmounts attackTypeDamageAmounts) {
		if (!shouldApplyStaggerAndKnockbackOutsideOfShieldBlocking || source.is(DamageTypeTags.NO_KNOCKBACK)) {

			if (enable_debug_log) {
				OverhauledDamage.info("--- no knockback is applied ---");
				OverhauledDamage.info("");
			}

			return 0.0F;
		}

		ServerConfig.DamageCalculation.AttackTypeMultipliers applied_knockback_multipliers = serverConfig.overhauled_damage_calculation.knockback_overhaul.applied_knockback_multipliers.get();
		float applied_knockback = ((attackTypeDamageAmounts.generic_amount * applied_knockback_multipliers.generic)
				+ (attackTypeDamageAmounts.bashing_amount * applied_knockback_multipliers.bashing)
				+ (attackTypeDamageAmounts.piercing_amount * applied_knockback_multipliers.piercing)
				+ (attackTypeDamageAmounts.slashing_amount * applied_knockback_multipliers.slashing)
				+ (attackTypeDamageAmounts.poison_amount * applied_knockback_multipliers.poison)
				+ (attackTypeDamageAmounts.fire_amount * applied_knockback_multipliers.fire)
				+ (attackTypeDamageAmounts.frost_amount * applied_knockback_multipliers.frost)
				+ (attackTypeDamageAmounts.lightning_amount * applied_knockback_multipliers.lightning)
		) * serverConfig.overhauled_damage_calculation.knockback_overhaul.global_knockback_multiplier.get();


		double d = 0.0;
		double e = 0.0;
		if (source.getDirectEntity() instanceof Projectile projectile) {
			DoubleDoubleImmutablePair doubleDoubleImmutablePair = projectile.calculateHorizontalHurtKnockbackDirection(livingEntity, source);
			d = -doubleDoubleImmutablePair.leftDouble();
			e = -doubleDoubleImmutablePair.rightDouble();
		} else if (source.getSourcePosition() != null) {
			d = source.getSourcePosition().x() - livingEntity.getX();
			e = source.getSourcePosition().z() - livingEntity.getZ();
		}

		if (enable_debug_log) {
			OverhauledDamage.info("--- knockback is applied ---");
			OverhauledDamage.info("applied_knockback (before knockback resistance) : " + applied_knockback);
			OverhauledDamage.info("");
		}
		livingEntity.knockback(applied_knockback, d, e);

		livingEntity.indicateDamage(d, e);

		return applied_knockback;
	}

	public static float calculateAppliedHealthDamage(LivingEntity livingEntity, boolean enable_debug_log, float amount) {
//		return amount;
		 /*
		 TODO do these attributes need a rework? maybe clamp them between 0 and 100
		    could also add 2 new attributes "damage_taken_from_mana" /stamina
		    used to calculate the amount taken from health damage
		    the multiplier is then added on top
		    health_damage should probably be minimum zero
		    */
		float damageTakenFromMana = ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getDamageTakenFromManaMultiplier();
		float damageTakenFromStamina = ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getDamageTakenFromStaminaMultiplier();
		float mana_damage = 0.0F;
		float stamina_damage = 0.0F;
		if (damageTakenFromMana > 0 && OverhauledDamage.isManaAttributesLoaded) {
			mana_damage = amount * damageTakenFromMana;
			OverhauledDamage.addMana(livingEntity, -mana_damage);
		}
		if (damageTakenFromStamina > 0 && OverhauledDamage.isStaminaAttributesLoaded) {
			stamina_damage = amount * damageTakenFromStamina;
			OverhauledDamage.addStamina(livingEntity, -stamina_damage);
		}
		amount = amount - mana_damage - stamina_damage;
		if (enable_debug_log) {
			OverhauledDamage.info("--- apply damage by reducing health / mana / stamina ---");
			OverhauledDamage.info("mana_damage : " + mana_damage);
			OverhauledDamage.info("");
			OverhauledDamage.info("stamina_damage : " + stamina_damage);
			OverhauledDamage.info("");
			OverhauledDamage.info("health_damage : " + amount);
			OverhauledDamage.info("this is further reduced by absorption");
			OverhauledDamage.info("");
		}

//		if (!source.isIn(Tags.NO_APPLIED_DAMAGE)) {
//			ServerConfig.DamageCalculation.AttackTypeMultipliers applied_damage_multipliers = serverConfig.damageCalculation.applied_damage_multipliers.get();
//			applied_damage = (generic_amount * applied_damage_multipliers.generic) + (bashing_amount * applied_damage_multipliers.bashing) + (piercing_amount * applied_damage_multipliers.piercing) + (slashing_amount * applied_damage_multipliers.slashing) + (poison_amount * applied_damage_multipliers.poison) + (fire_amount * applied_damage_multipliers.fire) + (frost_amount * applied_damage_multipliers.frost) + (lightning_amount * applied_damage_multipliers.lightning);
//
//			if (enable_debug_log) {
//				OverhauledDamage.info("--- damage applied to resources like health is multiplied ---");
//				OverhauledDamage.info("applied_damage_multipliers : " + applied_damage_multipliers);
//				OverhauledDamage.info("");
//			}
//		} else {
//			applied_damage = 0.0F;
//			if (enable_debug_log) {
//				OverhauledDamage.info("--- no damage is applied to resources like health ---");
//				OverhauledDamage.info("");
//			}
//		}

		return amount;
	}
	// endregion --- overhauled damage calculation ---

	public static void tick(LivingEntity livingEntity) {

		if (!livingEntity.level().isClientSide()) {

			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$delayEffectBuildUpTick()) {
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setDelayEffectBuildUpTick(false);
				return;
			}

			((DuckLivingEntityMixin) livingEntity).overhauleddamage$setIsMoving(!livingEntity.oldPosition().equals(livingEntity.position()));

			ServerConfig serverConfig = OverhauledDamage.SERVER_CONFIG;

			// bleeding
			if (getBleedingBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp()) {
				Optional<Holder.Reference<MobEffect>> bleeding_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.bleeding_build_up.mob_effect_identifier.get());
				if (bleeding_status_effect.isPresent()) {
					int existingBleedingDuration = 0;
					int bleedingAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(bleeding_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.build_up_effects.bleeding_build_up.duration_is_additive.get()) {
							existingBleedingDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.build_up_effects.bleeding_build_up.amplifier_is_additive.get()) {
							bleedingAmplifier = statusEffectInstance.getAmplifier();
						}
					}
					livingEntity.addEffect(new MobEffectInstance(bleeding_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingDuration() + existingBleedingDuration, bleedingAmplifier, false, false, true));
				}
				setBleedingBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReductionDelayThreshold());
			}
			if (getBleedingBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReductionDelayThreshold()) {
					addBleedingBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				}
			}

			// burn
			if (getBurnBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp()) {
				Optional<Holder.Reference<MobEffect>> burn_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.burn_build_up.mob_effect_identifier.get());
				if (burn_status_effect.isPresent()) {
					int existingBurnDuration = 0;
					int burnAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(burn_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.build_up_effects.burn_build_up.duration_is_additive.get()) {
							existingBurnDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.build_up_effects.burn_build_up.amplifier_is_additive.get()) {
							burnAmplifier = statusEffectInstance.getAmplifier() + 1;
						}
					}
					livingEntity.addEffect(new MobEffectInstance(burn_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnDuration() + existingBurnDuration, burnAmplifier, false, false, true));
				}
				setBurnBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReductionDelayThreshold());
			}
			if (getBurnBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReductionDelayThreshold()) {
					addBurnBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				}
			}

			// freeze
			if (getFreezeBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp()) {
				Optional<Holder.Reference<MobEffect>> freeze_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.freeze_build_up.mob_effect_identifier.get());
				if (freeze_status_effect.isPresent()) {
					int existingFreezeDuration = 0;
					int freezeAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(freeze_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.build_up_effects.freeze_build_up.duration_is_additive.get()) {
							existingFreezeDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.build_up_effects.freeze_build_up.amplifier_is_additive.get()) {
							freezeAmplifier = statusEffectInstance.getAmplifier();
						}
					}
					livingEntity.addEffect(new MobEffectInstance(freeze_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeDuration() + existingFreezeDuration, freezeAmplifier, false, false, true));
				}
				setFreezeBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReductionDelayThreshold());
			}
			if (getFreezeBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReductionDelayThreshold()) {
					addFreezeBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				}
			}

			// stagger
			if (getStaggerBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp()) {
				Optional<Holder.Reference<MobEffect>> staggered_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.stagger_build_up.mob_effect_identifier.get());
				if (staggered_status_effect.isPresent()) {
					int existingStaggerDuration = 0;
					int staggerAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(staggered_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.build_up_effects.stagger_build_up.duration_is_additive.get()) {
							existingStaggerDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.build_up_effects.stagger_build_up.amplifier_is_additive.get()) {
							staggerAmplifier = statusEffectInstance.getAmplifier();
						}
					}
					livingEntity.addEffect(new MobEffectInstance(staggered_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerDuration() + existingStaggerDuration, staggerAmplifier, false, false, true));
				}
				setStaggerBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReductionDelayThreshold());
			}
			if (getStaggerBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReductionDelayThreshold()) {
					addStaggerBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				}
			}

			// poison
			if (getPoisonBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp()) {
				Optional<Holder.Reference<MobEffect>> poison_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.poison_build_up.mob_effect_identifier.get());
				if (poison_status_effect.isPresent()) {
					int existingPoisonDuration = 0;
					int poisonAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(poison_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.build_up_effects.poison_build_up.duration_is_additive.get()) {
							existingPoisonDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.build_up_effects.poison_build_up.amplifier_is_additive.get()) {
							poisonAmplifier = statusEffectInstance.getAmplifier() + 1;
						}
					}
					livingEntity.addEffect(new MobEffectInstance(poison_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonDuration() + existingPoisonDuration, poisonAmplifier, false, false, true));
				}
				setPoisonBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReductionDelayThreshold());
			}
			if (getPoisonBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReductionDelayThreshold()) {
					addPoisonBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				}
			}

			// shock
			if (getShockBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp()) {
				Optional<Holder.Reference<MobEffect>> shocked_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.shock_build_up.mob_effect_identifier.get());
				if (shocked_status_effect.isPresent()) {
					int existingShockDuration = 0;
					int shockAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(shocked_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.build_up_effects.shock_build_up.duration_is_additive.get()) {
							existingShockDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.build_up_effects.shock_build_up.amplifier_is_additive.get()) {
							shockAmplifier = statusEffectInstance.getAmplifier() + 1;
						}
					}
					livingEntity.addEffect(new MobEffectInstance(shocked_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockDuration() + existingShockDuration, shockAmplifier, false, false, false));
				}
				setShockBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReductionDelayThreshold());
			}
			if (getShockBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReductionDelayThreshold()) {
					addShockBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				}
			}
		}
	}

	// region --- effect build-ups ---
	public static float getBleedingBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getBleedingBuildUp(livingEntity);
	}

	public static void setBleedingBuildUp(LivingEntity livingEntity, float bleedingBuildUp) {
		DataAttachmentHelper.setBleedingBuildUp(livingEntity, bleedingBuildUp);
	}

	public static void addBleedingBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> bleeding_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.bleeding_build_up.mob_effect_identifier.get());
		if (bleeding_status_effect.isEmpty()) {
			if (getBleedingBuildUp(livingEntity) > 0) {
				setBleedingBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp() != -1.0f && !livingEntity.hasEffect(bleeding_status_effect.get())) {
				double f = getBleedingBuildUp(livingEntity);
				setBleedingBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp()));
				if (getBleedingBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				}
			}
		}
	}

	public static float getBurnBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getBurnBuildUp(livingEntity);
	}

	public static void setBurnBuildUp(LivingEntity livingEntity, float burnBuildUp) {
		DataAttachmentHelper.setBurnBuildUp(livingEntity, burnBuildUp);
	}

	public static void addBurnBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> burn_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.burn_build_up.mob_effect_identifier.get());
		if (burn_status_effect.isEmpty()) {
			if (getBurnBuildUp(livingEntity) > 0) {
				setBurnBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp() != -1.0f) {
				setBurnBuildUp(livingEntity, (float) Mth.clamp(getBurnBuildUp(livingEntity) + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp()));
				if (getBurnBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				}
			}
		}
	}

	public static float getFreezeBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getFreezeBuildUp(livingEntity);
	}

	public static void setFreezeBuildUp(LivingEntity livingEntity, float freezeBuildUp) {
		DataAttachmentHelper.setFreezeBuildUp(livingEntity, freezeBuildUp);
	}

	public static void addFreezeBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> freeze_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.freeze_build_up.mob_effect_identifier.get());
		if (freeze_status_effect.isEmpty()) {
			if (getFreezeBuildUp(livingEntity) > 0) {
				setFreezeBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp() != -1.0f && !livingEntity.hasEffect(freeze_status_effect.get())) {
				double f = getFreezeBuildUp(livingEntity);
				setFreezeBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp()));
				if (getFreezeBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				}
			}
		}
	}

	public static float getStaggerBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getStaggerBuildUp(livingEntity);
	}

	public static void setStaggerBuildUp(LivingEntity livingEntity, float staggerBuildUp) {
		DataAttachmentHelper.setStaggerBuildUp(livingEntity, staggerBuildUp);
	}

	public static void addStaggerBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> staggered_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.stagger_build_up.mob_effect_identifier.get());
		if (staggered_status_effect.isEmpty()) {
			if (getStaggerBuildUp(livingEntity) > 0) {
				setStaggerBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp() != -1.0f && !livingEntity.hasEffect(staggered_status_effect.get())) {
				double f = getStaggerBuildUp(livingEntity);
				setStaggerBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp()));
				if (getStaggerBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				}
			}
		}
	}

	public static float getPoisonBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getPoisonBuildUp(livingEntity);
	}

	public static void setPoisonBuildUp(LivingEntity livingEntity, float poisonBuildUp) {
		DataAttachmentHelper.setPoisonBuildUp(livingEntity, poisonBuildUp);
	}

	public static void addPoisonBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> poison_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.poison_build_up.mob_effect_identifier.get());
		if (poison_status_effect.isEmpty()) {
			if (getPoisonBuildUp(livingEntity) > 0) {
				setPoisonBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp() != -1.0f) {
				double f = getPoisonBuildUp(livingEntity);
				setPoisonBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp()));
				if (getPoisonBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				}
			}
		}
	}

	public static float getShockBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getShockBuildUp(livingEntity);
	}

	public static void setShockBuildUp(LivingEntity livingEntity, float shockBuildUp) {
		DataAttachmentHelper.setShockBuildUp(livingEntity, shockBuildUp);
	}

	public static void addShockBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> shock_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.build_up_effects.shock_build_up.mob_effect_identifier.get());
		if (shock_status_effect.isEmpty()) {
			if (getShockBuildUp(livingEntity) > 0) {
				setShockBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp() != -1.0f) {
				double f = getShockBuildUp(livingEntity);
				setShockBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp()));
				if (getShockBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				}
			}
		}
	}
	// endregion --- effect build-ups ---

	public record AttackTypeDamageAmounts(
			float generic_amount,
			float bashing_amount,
			float piercing_amount,
			float slashing_amount,
			float poison_amount,
			float fire_amount,
			float frost_amount,
			float lightning_amount
	) {

	}
}
