package com.github.theredbrain.overhauleddamage.mixin.entity;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
@SuppressWarnings("UnreachableCode")
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow
	public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Shadow
	public abstract void damageArmor(DamageSource source, float amount);

	@Shadow
	public abstract int getArmor();

	@Shadow
	public abstract double getAttributeValue(EntityAttribute attribute);

	@Shadow
	public abstract void stopUsingItem();

	@Shadow
	public abstract boolean blockedByShield(DamageSource source);

	@Shadow
	public abstract boolean isBlocking();

	@Shadow
	protected ItemStack activeItemStack;
	@Unique
	private int bleedingTickTimer = 0;
	@Unique
	private int bleedingReductionDelayTimer = 0;
	@Unique
	private int burnTickTimer = 0;
	@Unique
	private int burnReductionDelayTimer = 0;
	@Unique
	private int freezeTickTimer = 0;
	@Unique
	private int freezeReductionDelayTimer = 0;
	@Unique
	private int staggerTickTimer = 0;
	@Unique
	private int staggerReductionDelayTimer = 0;
	@Unique
	private int poisonTickTimer = 0;
	@Unique
	private int poisonReductionDelayTimer = 0;
	@Unique
	private int shockTickTimer = 0;
	@Unique
	private int shockReductionDelayTimer = 0;
	@Unique
	private int blockingTime = 0;

	@Unique
	private static final TrackedData<Float> BLEEDING_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	@Unique
	private static final TrackedData<Float> BURN_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	@Unique
	private static final TrackedData<Float> FREEZE_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	@Unique
	private static final TrackedData<Float> STAGGER_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	@Unique
	private static final TrackedData<Float> POISON_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	@Unique
	private static final TrackedData<Float> SHOCK_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	protected void overhauleddamage$initDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(BLEEDING_BUILD_UP, 0.0F);
		this.dataTracker.startTracking(BURN_BUILD_UP, 0.0F);
		this.dataTracker.startTracking(FREEZE_BUILD_UP, 0.0F);
		this.dataTracker.startTracking(POISON_BUILD_UP, 0.0F);
		this.dataTracker.startTracking(STAGGER_BUILD_UP, 0.0F);
		this.dataTracker.startTracking(SHOCK_BUILD_UP, 0.0F);

	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void overhauleddamage$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(OverhauledDamage.ADDITIONAL_BASHING_DAMAGE)
				.add(OverhauledDamage.INCREASED_BASHING_DAMAGE)
				.add(OverhauledDamage.BASHING_RESISTANCE)

				.add(OverhauledDamage.ADDITIONAL_PIERCING_DAMAGE)
				.add(OverhauledDamage.INCREASED_PIERCING_DAMAGE)
				.add(OverhauledDamage.PIERCING_RESISTANCE)

				.add(OverhauledDamage.ADDITIONAL_SLASHING_DAMAGE)
				.add(OverhauledDamage.INCREASED_SLASHING_DAMAGE)
				.add(OverhauledDamage.SLASHING_RESISTANCE)

				.add(OverhauledDamage.BLOCKED_PHYSICAL_DAMAGE)

				.add(OverhauledDamage.MAX_BLEEDING_BUILD_UP)
				.add(OverhauledDamage.BLEEDING_DURATION)
				.add(OverhauledDamage.BLEEDING_TICK_THRESHOLD)
				.add(OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION)
				.add(OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION_DELAY_THRESHOLD)

				.add(OverhauledDamage.ADDITIONAL_FROST_DAMAGE)
				.add(OverhauledDamage.INCREASED_FROST_DAMAGE)
				.add(OverhauledDamage.BLOCKED_FROST_DAMAGE)
				.add(OverhauledDamage.FROST_RESISTANCE)
				.add(OverhauledDamage.MAX_FREEZE_BUILD_UP)
				.add(OverhauledDamage.FREEZE_DURATION)
				.add(OverhauledDamage.FREEZE_TICK_THRESHOLD)
				.add(OverhauledDamage.FREEZE_BUILD_UP_REDUCTION)
				.add(OverhauledDamage.FREEZE_BUILD_UP_REDUCTION_DELAY_THRESHOLD)

				.add(OverhauledDamage.ADDITIONAL_FIRE_DAMAGE)
				.add(OverhauledDamage.INCREASED_FIRE_DAMAGE)
				.add(OverhauledDamage.BLOCKED_FIRE_DAMAGE)
				.add(OverhauledDamage.FIRE_RESISTANCE)
				.add(OverhauledDamage.MAX_BURN_BUILD_UP)
				.add(OverhauledDamage.BURN_DURATION)
				.add(OverhauledDamage.BURN_TICK_THRESHOLD)
				.add(OverhauledDamage.BURN_BUILD_UP_REDUCTION)
				.add(OverhauledDamage.BURN_BUILD_UP_REDUCTION_DELAY_THRESHOLD)

				.add(OverhauledDamage.ADDITIONAL_LIGHTNING_DAMAGE)
				.add(OverhauledDamage.INCREASED_LIGHTNING_DAMAGE)
				.add(OverhauledDamage.BLOCKED_LIGHTNING_DAMAGE)
				.add(OverhauledDamage.LIGHTNING_RESISTANCE)
				.add(OverhauledDamage.MAX_SHOCK_BUILD_UP)
				.add(OverhauledDamage.SHOCK_DURATION)
				.add(OverhauledDamage.SHOCK_TICK_THRESHOLD)
				.add(OverhauledDamage.SHOCK_BUILD_UP_REDUCTION)
				.add(OverhauledDamage.SHOCK_BUILD_UP_REDUCTION_DELAY_THRESHOLD)

				.add(OverhauledDamage.ADDITIONAL_POISON_DAMAGE)
				.add(OverhauledDamage.INCREASED_POISON_DAMAGE)
				.add(OverhauledDamage.BLOCKED_POISON_DAMAGE)
				.add(OverhauledDamage.POISON_RESISTANCE)
				.add(OverhauledDamage.MAX_POISON_BUILD_UP)
				.add(OverhauledDamage.POISON_DURATION)
				.add(OverhauledDamage.POISON_TICK_THRESHOLD)
				.add(OverhauledDamage.POISON_BUILD_UP_REDUCTION)
				.add(OverhauledDamage.POISON_BUILD_UP_REDUCTION_DELAY_THRESHOLD)

				.add(OverhauledDamage.MAX_STAGGER_BUILD_UP)
				.add(OverhauledDamage.STAGGER_DURATION)
				.add(OverhauledDamage.STAGGER_TICK_THRESHOLD)
				.add(OverhauledDamage.STAGGER_BUILD_UP_REDUCTION)
				.add(OverhauledDamage.STAGGER_BUILD_UP_REDUCTION_DELAY_THRESHOLD)

				.add(OverhauledDamage.BLOCK_FORCE)
				.add(OverhauledDamage.PARRY_BONUS)
				.add(OverhauledDamage.PARRY_WINDOW)

				.add(OverhauledDamage.BLOCK_STAMINA_COST)
				.add(OverhauledDamage.PARRY_STAMINA_COST)

				.add(OverhauledDamage.DAMAGE_TAKEN_MULTIPLIER)
				.add(OverhauledDamage.DAMAGE_TAKEN_FROM_MANA_MULTIPLIER)
				.add(OverhauledDamage.DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER)
		;
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void overhauleddamage$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

		if (nbt.contains("bleeding_build_up", NbtElement.NUMBER_TYPE)) {
			this.overhauleddamage$setBleedingBuildUp(nbt.getFloat("bleeding_build_up"));
		}

		if (nbt.contains("burn_build_up", NbtElement.NUMBER_TYPE)) {
			this.overhauleddamage$setBurnBuildUp(nbt.getFloat("burn_build_up"));
		}

		if (nbt.contains("freeze_build_up", NbtElement.NUMBER_TYPE)) {
			this.overhauleddamage$setFreezeBuildUp(nbt.getFloat("freeze_build_up"));
		}

		if (nbt.contains("poison_build_up", NbtElement.NUMBER_TYPE)) {
			this.overhauleddamage$setPoisonBuildUp(nbt.getFloat("poison_build_up"));
		}

		if (nbt.contains("stagger_build_up", NbtElement.NUMBER_TYPE)) {
			this.overhauleddamage$setStaggerBuildUp(nbt.getFloat("stagger_build_up"));
		}

		if (nbt.contains("shock_build_up", NbtElement.NUMBER_TYPE)) {
			this.overhauleddamage$setShockBuildUp(nbt.getFloat("shock_build_up"));
		}

	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void overhauleddamage$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

		nbt.putFloat("bleeding_build_up", this.overhauleddamage$getBleedingBuildUp());

		nbt.putFloat("burn_build_up", this.overhauleddamage$getBurnBuildUp());

		nbt.putFloat("freeze_build_up", this.overhauleddamage$getFreezeBuildUp());

		nbt.putFloat("poison_build_up", this.overhauleddamage$getPoisonBuildUp());

		nbt.putFloat("stagger_build_up", this.overhauleddamage$getStaggerBuildUp());

		nbt.putFloat("shock_build_up", this.overhauleddamage$getShockBuildUp());

	}

	// disables the vanilla armor calculation
	@WrapOperation(
			method = "applyArmorToDamage",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")
	)
	public boolean overhauleddamage$wrap_bypassesArmor(DamageSource instance, TagKey<DamageType> tag, Operation<Boolean> original) {
		return OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_armor_overhaul || original.call(instance, tag);
	}

	// disables the vanilla shield blocking when blocking overhaul is enabled, the "blocking_requires_stamina" option is excluded from this
	@WrapOperation(
			method = "damage",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;blockedByShield(Lnet/minecraft/entity/damage/DamageSource;)Z")
	)
	public boolean overhauleddamage$wrap_blockedByShield(LivingEntity instance, DamageSource source, Operation<Boolean> original) {
		return !OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_blocking_overhaul && original.call(instance, source) && (OverhauledDamage.getCurrentStamina((LivingEntity) (Object) this) > 0 || !OverhauledDamage.SERVER_CONFIG.damageCalculation.blocking_requires_stamina || !OverhauledDamage.isStaminaAttributesLoaded);
	}

	@ModifyVariable(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"), argsOnly = true)
	private float overhauleddamage$applyDamage(float old, DamageSource source) {
		return ((DuckLivingEntityMixin) (Object) this).overhauleddamage$calculateOverhauledDamage(source, old);
	}

	@Override
	public float overhauleddamage$calculateOverhauledDamage(DamageSource source, float amount) {
		var serverConfig = OverhauledDamage.SERVER_CONFIG;
		boolean enable_debug_log = serverConfig.damageCalculation.enable_debug_log;
		if (enable_debug_log) {
			OverhauledDamage.info("----- start of new damage calculation log -----");
			OverhauledDamage.info("");
			OverhauledDamage.info("entity taken damage : " + this.getName().getString());
			OverhauledDamage.info("");
			OverhauledDamage.info("damage source : " + source.toString());
			OverhauledDamage.info("");
			OverhauledDamage.info("damage amount : " + amount);
			OverhauledDamage.info("");
		}

		// TODO remove in 1.21.1 as vanilla has an attribute doing this
//		StatusEffect fall_immune_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(serverConfig.fall_immune_status_effect_identifier));
//		if (source.isIn(DamageTypeTags.IS_FALL) && fall_immune_status_effect != null && this.hasStatusEffect(fall_immune_status_effect)) {
//			return 0.0F;
//		}

		LivingEntity attacker = null;
		if (source.getAttacker() instanceof LivingEntity) {
			attacker = (LivingEntity) source.getAttacker();
		}
		if (enable_debug_log) {
			if (attacker != null) {
				OverhauledDamage.info("entity dealing damage : " + attacker.getName().getString());
				OverhauledDamage.info("");
			}
		}

		// TODO should this be done here??
		//  or maybe before resistance is applied?
		//  or maybe after the additional/increased damage is applied?
//		if (serverConfig.enable_damage_taken_multiplier_attribute) {
//			float damage_taken_multiplier = ((DuckLivingEntityMixin) (Object) this).overhauleddamage$getDamageTakenMultiplier();
//			amount *= damage_taken_multiplier;
//			if (enable_debug_log) {
//				OverhauledDamage.info("damage taken multiplier attribute is enabled");
//				OverhauledDamage.info("damage taken multiplier: " + damage_taken_multiplier);
//				OverhauledDamage.info("damage amount after damage taken multiplier: " + amount);
//			}
//		}

		float applied_damage = 0;
		float true_amount = 0;

		if (source.isIn(Tags.IS_TRUE_DAMAGE)) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- is true damage ---");
				OverhauledDamage.info("");
				OverhauledDamage.info("--- true damage can't be blocked and is not reduced by armor, protection or resistances ---");
				OverhauledDamage.info("");
				OverhauledDamage.info("--- true damage does not apply effect build ups ---");
				OverhauledDamage.info("");
			}
			true_amount = amount;
			amount = 0;
		}

		if (amount > 0) {

			// fallback
			ServerConfig.AttackTypeMultipliers damage_type_multiplier = null;

			ValidatedMap<String, ServerConfig.AttackTypeMultipliers> damage_type_multipliers = serverConfig.damageTypes.damage_type_multipliers;

			String damageTypeId = "";
			Optional<RegistryKey<DamageType>> optional = source.getTypeRegistryEntry().getKey();

			if (optional.isPresent()) {
				damageTypeId = optional.get().getValue().toString();
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
				damage_type_multiplier = serverConfig.damageTypes.default_damage_type_multipliers;
			}
			if (enable_debug_log) {
				OverhauledDamage.info("used damage_type_multipliers : " + damage_type_multiplier);
				OverhauledDamage.info("");
			}

			// default values
			float generic_amount = amount * damage_type_multiplier.generic;
			float bashing_amount = amount * damage_type_multiplier.bashing;
			float piercing_amount = amount * damage_type_multiplier.piercing;
			float slashing_amount = amount * damage_type_multiplier.slashing;
			float poison_amount = amount * damage_type_multiplier.poison;
			float fire_amount = amount * damage_type_multiplier.fire;
			float frost_amount = amount * damage_type_multiplier.frost;
			float lightning_amount = amount * damage_type_multiplier.lightning;
			if (enable_debug_log) {
				OverhauledDamage.info("--- initial attack amounts ---");
				OverhauledDamage.info("generic_amount : " + generic_amount);
				OverhauledDamage.info("bashing_amount : " + bashing_amount);
				OverhauledDamage.info("piercing_amount : " + piercing_amount);
				OverhauledDamage.info("slashing_amount : " + slashing_amount);
				OverhauledDamage.info("poison_amount : " + poison_amount);
				OverhauledDamage.info("fire_amount : " + fire_amount);
				OverhauledDamage.info("frost_amount : " + frost_amount);
				OverhauledDamage.info("lightning_amount : " + lightning_amount);
				OverhauledDamage.info("");
			}

			if (attacker != null) {
				bashing_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalBashingDamage() + bashing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedBashingDamage();
				piercing_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalPiercingDamage() + piercing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedPiercingDamage();
				slashing_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalSlashingDamage() + slashing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedSlashingDamage();
				fire_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalFireDamage() + fire_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedFireDamage();
				frost_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalFrostDamage() + frost_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedFrostDamage();
				lightning_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalLightningDamage() + lightning_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedLightningDamage();
				poison_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalPoisonDamage() + poison_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedPoisonDamage();
			}
			if (enable_debug_log) {
				OverhauledDamage.info("--- attack amounts after additions ---");
				OverhauledDamage.info("generic_amount : " + generic_amount);
				OverhauledDamage.info("bashing_amount : " + bashing_amount);
				OverhauledDamage.info("piercing_amount : " + piercing_amount);
				OverhauledDamage.info("slashing_amount : " + slashing_amount);
				OverhauledDamage.info("poison_amount : " + poison_amount);
				OverhauledDamage.info("fire_amount : " + fire_amount);
				OverhauledDamage.info("frost_amount : " + frost_amount);
				OverhauledDamage.info("lightning_amount : " + lightning_amount);
				OverhauledDamage.info("");
			}

			//
			boolean triedBlocking = false;

			// region shield blocks
			if (serverConfig.damageCalculation.enable_blocking_overhaul) {
				ItemStack shieldItemStack = this.activeItemStack;
				if (this.isBlocking() && this.blockedByShield(source) && (OverhauledDamage.getCurrentStamina((LivingEntity) (Object) this) > 0 || !serverConfig.damageCalculation.blocking_requires_stamina || !OverhauledDamage.isStaminaAttributesLoaded)) {
					// a parry is tried, if the blocking time < the parry window of the blocking entity, the blocking entity can parry at all and the blocking item is in the 'can_parry' item tag
					boolean tryParry = this.overhauleddamage$canParry() && this.blockingTime <= ((DuckLivingEntityMixin) this).overhauleddamage$getParryWindow() && source.getAttacker() != null && source.getAttacker() instanceof LivingEntity && shieldItemStack.isIn(Tags.CAN_PARRY);
					double parryBonus = tryParry ? ((DuckLivingEntityMixin) this).overhauleddamage$getParryBonus() : 1;

					triedBlocking = true;

					if (enable_debug_log) {
						OverhauledDamage.info("--- blocking/parrying ---");
						OverhauledDamage.info("");
						OverhauledDamage.info("tryParry : " + tryParry);
						OverhauledDamage.info("");
						OverhauledDamage.info("parryBonus : " + parryBonus);
						OverhauledDamage.info("");
					}
					float blockedBashingDamage;
					float blockedPiercingDamage;
					float blockedSlashingDamage;
					float blockedFireDamage;
					float blockedFrostDamage;
					float blockedLightningDamage;
					float blockedPoisonDamage;

					if (serverConfig.damageCalculation.blockingOverhaul.blocked_damage_calculation_works_with_flat_values) {
						if (enable_debug_log) {
							OverhauledDamage.info("blocked damage calculation uses flat values");
							OverhauledDamage.info("");
						}
						blockedBashingDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
						blockedPiercingDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
						blockedSlashingDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
						blockedFireDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFireDamage() * parryBonus);
						blockedFrostDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFrostDamage() * parryBonus);
						blockedLightningDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedLightningDamage() * parryBonus);
						blockedPoisonDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPoisonDamage() * parryBonus);
					} else {
						if (enable_debug_log) {
							OverhauledDamage.info("blocked damage calculation uses percentage values");
							OverhauledDamage.info("");
						}
						blockedBashingDamage = (float) (bashing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
						blockedPiercingDamage = (float) (piercing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
						blockedSlashingDamage = (float) (slashing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
						blockedFireDamage = (float) (fire_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFireDamage() * parryBonus / 100);
						blockedFrostDamage = (float) (frost_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFrostDamage() * parryBonus / 100);
						blockedLightningDamage = (float) (lightning_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedLightningDamage() * parryBonus / 100);
						blockedPoisonDamage = (float) (poison_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPoisonDamage() * parryBonus / 100);
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
					// reduce the stamina of the blocking/parrying entity by the block/parry stamina cost
					OverhauledDamage.addStamina(((LivingEntity) (Object) this), tryParry ? -((DuckLivingEntityMixin) this).overhauleddamage$getParryStaminaCost() : -((DuckLivingEntityMixin) this).overhauleddamage$getBlockStaminaCost());

					// when no stamina is left after blocking/parrying the block/parry was not successful and the damage is not reduced
					if (OverhauledDamage.getCurrentStamina((LivingEntity) (Object) this) >= 0 || !OverhauledDamage.isStaminaAttributesLoaded) {

						boolean isStaggered = false;

						// apply stagger based on left over damage
						ServerConfig.AttackTypeMultipliers stagger_multipliers = serverConfig.damageCalculation.stagger_multipliers;
						if (enable_debug_log) {
							OverhauledDamage.info("--- apply stagger based on left over damage ---");
							OverhauledDamage.info("");
							OverhauledDamage.info("stagger_multipliers : " + stagger_multipliers);
							OverhauledDamage.info("");
						}
						float appliedStagger = (generic_amount * stagger_multipliers.generic) + ((bashing_amount - blockedBashingDamage) * stagger_multipliers.bashing) + ((piercing_amount - blockedPiercingDamage) * stagger_multipliers.piercing) + ((slashing_amount - blockedSlashingDamage) * stagger_multipliers.slashing) + ((poison_amount - blockedPoisonDamage) * stagger_multipliers.poison) + ((fire_amount - blockedFireDamage) * stagger_multipliers.fire) + ((frost_amount - blockedFrostDamage) * stagger_multipliers.frost) + ((lightning_amount - blockedLightningDamage) * stagger_multipliers.lightning);
						if (appliedStagger > 0) {
							if (enable_debug_log) {
								OverhauledDamage.info("appliedStagger : " + appliedStagger);
								OverhauledDamage.info("");
							}
							this.overhauleddamage$addStaggerBuildUp(appliedStagger);
							isStaggered = this.overhauleddamage$getStaggerBuildUp() >= this.overhauleddamage$getMaxStaggerBuildUp();
						}

						// block/parry was successful
						if (!isStaggered) {
							bashing_amount -= blockedBashingDamage;
							piercing_amount -= blockedPiercingDamage;
							slashing_amount -= blockedSlashingDamage;
							fire_amount -= blockedFireDamage;
							frost_amount -= blockedFrostDamage;
							lightning_amount -= blockedLightningDamage;
							poison_amount -= blockedPoisonDamage;

							if (tryParry) {

								if (attacker != null) {
									((DuckLivingEntityMixin) attacker).overhauleddamage$addStaggerBuildUp(((DuckLivingEntityMixin) attacker).overhauleddamage$getMaxStaggerBuildUp());
									if (enable_debug_log) {
										OverhauledDamage.info("--- successful parries stagger the attacker ---");
										OverhauledDamage.info("");
									}
								}
							} else {
								if (attacker != null) {
									float applied_knockback = ((DuckLivingEntityMixin) this).overhauleddamage$getBlockForce();
									attacker.takeKnockback(applied_knockback, attacker.getX() - this.getX(), attacker.getZ() - this.getZ());
									if (enable_debug_log) {
										OverhauledDamage.info("--- successful blocks apply knockback to the attacker ---");
										OverhauledDamage.info("applied_knockback : " + applied_knockback);
										OverhauledDamage.info("");
									}
								}
							}
							float totalBlockedDamage = blockedBashingDamage + blockedPiercingDamage + blockedSlashingDamage + blockedFireDamage + blockedFrostDamage + blockedLightningDamage + blockedPoisonDamage;
							if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity && totalBlockedDamage > 0.0f && totalBlockedDamage < 3.4028235E37f) {
								serverPlayerEntity.increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(totalBlockedDamage * 10.0f));
							}

							if (tryParry) {
								this.getWorld().playSoundFromEntity(null, this, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0F, 1.2F + this.getWorld().random.nextFloat() * 0.4F);
							} else {
								this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
							}
							if (enable_debug_log) {
								OverhauledDamage.info("--- attack amounts after blocking/parrying ---");
								OverhauledDamage.info("generic_amount : " + generic_amount);
								OverhauledDamage.info("bashing_amount : " + bashing_amount);
								OverhauledDamage.info("piercing_amount : " + piercing_amount);
								OverhauledDamage.info("slashing_amount : " + slashing_amount);
								OverhauledDamage.info("poison_amount : " + poison_amount);
								OverhauledDamage.info("fire_amount : " + fire_amount);
								OverhauledDamage.info("frost_amount : " + frost_amount);
								OverhauledDamage.info("lightning_amount : " + lightning_amount);
								OverhauledDamage.info("");
							}
						} else {
							this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
							if (enable_debug_log) {
								OverhauledDamage.info("--- blocking/parrying failed because the damage was too high ---");
								OverhauledDamage.info("");
							}
						}
					} else if (enable_debug_log) {
						OverhauledDamage.info("--- blocking/parrying failed because stamina was too low ---");
						OverhauledDamage.info("");
					}
				} else if (enable_debug_log) {
					OverhauledDamage.info("--- no blocking/parrying was tried ---");
					OverhauledDamage.info("");
				}
			}
			// endregion shield blocks

			// region apply protection
			if (!source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS) && serverConfig.damageCalculation.enable_protection_overhaul) {

				// the protection enchantments reduce damage, with a default value of 2 percent reduction per enchantment level
				float protection = (float) (EnchantmentHelper.getProtectionAmount(this.getArmorItems(), source) * serverConfig.damageCalculation.protectionOverhaul.protection_damage_reduction_per_level);

				// band-aid solution to prevent fall damage being reduced a second time by Feather Falling
				if (source.isIn(DamageTypeTags.IS_FALL)) {
					protection = 0.0F;
				}

				if (enable_debug_log) {
					OverhauledDamage.info("protection : " + protection);
					OverhauledDamage.info("");
				}

				// the different attack types also have a protection_multiplier
				ServerConfig.AttackTypeMultipliers protection_multipliers = serverConfig.damageCalculation.protectionOverhaul.protection_multipliers;

				if (enable_debug_log) {
					OverhauledDamage.info("protection_multipliers : " + protection_multipliers);
					OverhauledDamage.info("");
				}

				generic_amount = generic_amount - (generic_amount * protection * protection_multipliers.generic / 100);

				bashing_amount = bashing_amount - (bashing_amount * protection * protection_multipliers.bashing / 100);

				piercing_amount = piercing_amount - (piercing_amount * protection * protection_multipliers.piercing / 100);

				slashing_amount = slashing_amount - (slashing_amount * protection * protection_multipliers.slashing / 100);

				poison_amount = poison_amount - (poison_amount * protection * protection_multipliers.poison / 100);

				fire_amount = fire_amount - (fire_amount * protection * protection_multipliers.fire / 100);

				frost_amount = frost_amount - (frost_amount * protection * protection_multipliers.frost / 100);

				lightning_amount = lightning_amount - (lightning_amount * protection * protection_multipliers.lightning / 100);

				if (enable_debug_log) {
					OverhauledDamage.info("--- attack amounts after protection ---");
					OverhauledDamage.info("generic_amount : " + generic_amount);
					OverhauledDamage.info("bashing_amount : " + bashing_amount);
					OverhauledDamage.info("piercing_amount : " + piercing_amount);
					OverhauledDamage.info("slashing_amount : " + slashing_amount);
					OverhauledDamage.info("poison_amount : " + poison_amount);
					OverhauledDamage.info("fire_amount : " + fire_amount);
					OverhauledDamage.info("frost_amount : " + frost_amount);
					OverhauledDamage.info("lightning_amount : " + lightning_amount);
					OverhauledDamage.info("");
				}
			} else if (enable_debug_log) {
				if (!serverConfig.damageCalculation.enable_protection_overhaul) {
					OverhauledDamage.info("protection overhaul not active");
				}
				if (source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
					OverhauledDamage.info("damage bypasses protection");
				}
			}
			// endregion apply protection

			// region apply armor
			if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR) && serverConfig.damageCalculation.enable_armor_overhaul) {
				float armorDamage = 0.0F;
				if (serverConfig.damageCalculation.armorOverhaul.armor_calculation_works_with_flat_values) {
					// TODO this calculation needs a serious overhaul
					// armorToughness now directly determines how effective armor is
					// effective armor reduces damage by its amount
					// armor is more or less effective against different attack types
					float effectiveArmor = this.getArmor();

					if (serverConfig.damageCalculation.armorOverhaul.enable_armor_toughness_attribute) {
						if (enable_debug_log) {
							OverhauledDamage.info("armor toughness is enabled");
							OverhauledDamage.info("");
						}
						effectiveArmor *= (float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
					} else if (enable_debug_log) {
						OverhauledDamage.info("armor toughness is disabled");
						OverhauledDamage.info("");
					}

					if (enable_debug_log) {
						OverhauledDamage.info("armor calculation uses flat values");
						OverhauledDamage.info("effective_armor : " + effectiveArmor);
						OverhauledDamage.info("");
					}

					if (piercing_amount * 1.25 <= effectiveArmor) {
						effectiveArmor -= (float) (piercing_amount * 1.25);
						piercing_amount = 0;
					} else {
						piercing_amount -= (float) (effectiveArmor * 0.75);
						effectiveArmor = 0;
					}

					if (bashing_amount <= effectiveArmor) {
						effectiveArmor -= bashing_amount;
						bashing_amount = 0;
					} else {
						bashing_amount -= effectiveArmor;
						effectiveArmor = 0;
					}

					if (fire_amount <= effectiveArmor) {
						effectiveArmor -= fire_amount;
						fire_amount = 0;
					} else {
						fire_amount -= effectiveArmor;
						effectiveArmor = 0;
					}

					if (slashing_amount <= effectiveArmor) {
						effectiveArmor -= slashing_amount;
						slashing_amount = 0;
					} else {
						slashing_amount -= effectiveArmor;
						slashing_amount = (float) (slashing_amount * 1.25); // slashing damage not blocked by armor deals more damage
						effectiveArmor = 0;
					}
					armorDamage = this.getArmor() - effectiveArmor;
				} else {
					// this is the alternative armor calculation
					// armor reduces damage on a percentage base
					// 1 armor point = 1 percent reduction
					// armor toughness is a multiplier to this
					float effective_armor = this.getArmor();

					if (serverConfig.damageCalculation.armorOverhaul.enable_armor_toughness_attribute) {
						if (enable_debug_log) {
							OverhauledDamage.info("armor toughness is enabled");
							OverhauledDamage.info("");
						}
						effective_armor *= (float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
					} else if (enable_debug_log) {
						OverhauledDamage.info("armor toughness is disabled");
						OverhauledDamage.info("");
					}

					if (enable_debug_log) {
						OverhauledDamage.info("armor calculation uses percentage values");
						OverhauledDamage.info("effective_armor : " + effective_armor);
						OverhauledDamage.info("");
					}

					// notable difference to the first method:
					// armor is not reduced when reducing the damage amount of one attack_type

					// the different attack types have an armor_multiplier on their own
					ServerConfig.AttackTypeMultipliers armor_multipliers = serverConfig.damageCalculation.armorOverhaul.armor_multipliers;

					if (enable_debug_log) {
						OverhauledDamage.info("armor_multipliers: " + armor_multipliers);
						OverhauledDamage.info("");
					}
					float generic_armor_damage = generic_amount * effective_armor * armor_multipliers.generic / 100;
					generic_amount = generic_amount - generic_armor_damage;

					float bashing_armor_damage = bashing_amount * effective_armor * armor_multipliers.bashing / 100;
					bashing_amount = bashing_amount - bashing_armor_damage;

					float piercing_armor_damage = piercing_amount * effective_armor * armor_multipliers.piercing / 100;
					piercing_amount = piercing_amount - piercing_armor_damage;

					float slashing_armor_damage = slashing_amount * effective_armor * armor_multipliers.slashing / 100;
					slashing_amount = slashing_amount - slashing_armor_damage;

					float poison_armor_damage = poison_amount * effective_armor * armor_multipliers.poison / 100;
					poison_amount = poison_amount - poison_armor_damage;

					float fire_armor_damage = fire_amount * effective_armor * armor_multipliers.fire / 100;
					fire_amount = fire_amount - fire_armor_damage;

					float frost_armor_damage = frost_amount * effective_armor * armor_multipliers.frost / 100;
					frost_amount = frost_amount - frost_armor_damage;

					float lightning_armor_damage = (lightning_amount * effective_armor * armor_multipliers.lightning / 100);
					lightning_amount = lightning_amount - lightning_armor_damage;

					armorDamage = generic_armor_damage + bashing_armor_damage + piercing_armor_damage + slashing_armor_damage + poison_armor_damage + fire_armor_damage + frost_armor_damage + lightning_armor_damage;

				}
				this.damageArmor(source, armorDamage);
				if (enable_debug_log) {
					OverhauledDamage.info("damage applied to equipped armor: " + armorDamage);
					OverhauledDamage.info("");
					OverhauledDamage.info("--- attack amounts after armor ---");
					OverhauledDamage.info("generic_amount : " + generic_amount);
					OverhauledDamage.info("bashing_amount : " + bashing_amount);
					OverhauledDamage.info("piercing_amount : " + piercing_amount);
					OverhauledDamage.info("slashing_amount : " + slashing_amount);
					OverhauledDamage.info("poison_amount : " + poison_amount);
					OverhauledDamage.info("fire_amount : " + fire_amount);
					OverhauledDamage.info("frost_amount : " + frost_amount);
					OverhauledDamage.info("lightning_amount : " + lightning_amount);
					OverhauledDamage.info("");
				}
			} else if (enable_debug_log) {
				if (serverConfig.damageCalculation.enable_armor_overhaul) {
					OverhauledDamage.info("armor overhaul not active");
				} else {
					OverhauledDamage.info("damage bypasses armor");
				}
			}
			// endregion apply armor

			// region apply resistances
			bashing_amount = bashing_amount - (bashing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBashingResistance()) / 100;

			piercing_amount = piercing_amount - (piercing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getPiercingResistance()) / 100;

			slashing_amount = slashing_amount - (slashing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getSlashingResistance()) / 100;

			poison_amount = poison_amount - (poison_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getPoisonResistance()) / 100;

			fire_amount = fire_amount - (fire_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getFireResistance()) / 100;

			frost_amount = frost_amount - (frost_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getFrostResistance()) / 100;

			lightning_amount = lightning_amount - (lightning_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getLightningResistance()) / 100;
			// endregion apply resistances

			if (enable_debug_log) {
				OverhauledDamage.info("--- attack amounts after resistances ---");
				OverhauledDamage.info("generic_amount : " + generic_amount);
				OverhauledDamage.info("bashing_amount : " + bashing_amount);
				OverhauledDamage.info("piercing_amount : " + piercing_amount);
				OverhauledDamage.info("slashing_amount : " + slashing_amount);
				OverhauledDamage.info("poison_amount : " + poison_amount);
				OverhauledDamage.info("fire_amount : " + fire_amount);
				OverhauledDamage.info("frost_amount : " + frost_amount);
				OverhauledDamage.info("lightning_amount : " + lightning_amount);
				OverhauledDamage.info("");
			}

			ServerConfig.AttackTypeMultipliers applied_damage_multipliers = serverConfig.damageCalculation.applied_damage_multipliers;
			applied_damage = (generic_amount * applied_damage_multipliers.generic) + (bashing_amount * applied_damage_multipliers.bashing) + (piercing_amount * applied_damage_multipliers.piercing) + (slashing_amount * applied_damage_multipliers.slashing) + (poison_amount * applied_damage_multipliers.poison) + (fire_amount * applied_damage_multipliers.fire) + (frost_amount * applied_damage_multipliers.frost) + (lightning_amount * applied_damage_multipliers.lightning);

			if (enable_debug_log) {
				OverhauledDamage.info("--- apply damage by increasing effect build ups ---");
				OverhauledDamage.info("applied_damage_multipliers : " + applied_damage_multipliers);
				OverhauledDamage.info("applied_damage : " + applied_damage);
				OverhauledDamage.info("");
			}

			// taking damage interrupts eating food, drinking potions, etc
			if (applied_damage > 0.0f && !this.isBlocking() && serverConfig.damage_interrupts_item_usage) {
				this.stopUsingItem();
			}

			// apply bleeding
			ServerConfig.AttackTypeMultipliers bleeding_multipliers = serverConfig.damageCalculation.bleeding_multipliers;
			if (source.isIn(Tags.APPLIES_BLEEDING)) {
				float applied_bleeding = (generic_amount * bleeding_multipliers.generic) + (bashing_amount * bleeding_multipliers.bashing) + (piercing_amount * bleeding_multipliers.piercing) + (slashing_amount * bleeding_multipliers.slashing) + (poison_amount * bleeding_multipliers.poison) + (fire_amount * bleeding_multipliers.fire) + (frost_amount * bleeding_multipliers.frost) + (lightning_amount * bleeding_multipliers.lightning);

				if (enable_debug_log) {
					OverhauledDamage.info("--- apply bleeding ---");
					OverhauledDamage.info("bleeding_multipliers : " + bleeding_multipliers);
				}

				if (applied_bleeding > 0) {
					if (enable_debug_log) {
						OverhauledDamage.info("applied_bleeding : " + applied_bleeding);
						OverhauledDamage.info("");
					}
					this.overhauleddamage$addBleedingBuildUp(applied_bleeding);
				} else if (enable_debug_log) {
					OverhauledDamage.info("no bleeding was applied");
					OverhauledDamage.info("");
				}
			}

			// apply burning
			if (fire_amount > 0) {
				this.overhauleddamage$addBurnBuildUp(fire_amount);
			}

			// apply chilled and frozen
			if (frost_amount > 0) {
				StatusEffect chilled_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(serverConfig.buildUpEffects.chilled_status_effect_identifier));
				if (chilled_status_effect != null) {
					int chilledDuration = (int) Math.ceil(frost_amount);
					StatusEffectInstance statusEffectInstance = this.getStatusEffect(chilled_status_effect);
					if (statusEffectInstance != null) {
						chilledDuration = chilledDuration + statusEffectInstance.getDuration();
					}
					this.addStatusEffect(new StatusEffectInstance(chilled_status_effect, chilledDuration, 0, false, false, true));
				}
				this.overhauleddamage$addFreezeBuildUp(frost_amount);
			}

			if (!triedBlocking) {
				// apply stagger
				ServerConfig.AttackTypeMultipliers stagger_multipliers = serverConfig.damageCalculation.stagger_multipliers;
				if (enable_debug_log) {
					OverhauledDamage.info("--- apply stagger when no blocking was tried ---");
					OverhauledDamage.info("stagger_multipliers : " + stagger_multipliers);
				}
				float appliedStagger = (generic_amount * stagger_multipliers.generic) + (bashing_amount * stagger_multipliers.bashing) + (piercing_amount * stagger_multipliers.piercing) + (slashing_amount * stagger_multipliers.slashing) + (poison_amount * stagger_multipliers.poison) + (fire_amount * stagger_multipliers.fire) + (frost_amount * stagger_multipliers.frost) + (lightning_amount * stagger_multipliers.lightning);
				if (appliedStagger > 0) {
					if (enable_debug_log) {
						OverhauledDamage.info("appliedStagger : " + appliedStagger);
						OverhauledDamage.info("");
					}
					this.overhauleddamage$addStaggerBuildUp(appliedStagger);
				} else if (enable_debug_log) {
					OverhauledDamage.info("no stagger was applied");
					OverhauledDamage.info("");
				}
			}

			// apply poison
			if (poison_amount > 0) {
				this.overhauleddamage$addPoisonBuildUp(poison_amount);
			}

			// apply shocked
			if (lightning_amount > 0) {
				this.overhauleddamage$addShockBuildUp(lightning_amount);
			}
		}

		float health_damage = applied_damage + true_amount;

		float damageTakenFromMana = this.overhauleddamage$getDamageTakenFromManaMultiplier();
		float damageTakenFromStamina = this.overhauleddamage$getDamageTakenFromStaminaMultiplier();
		float mana_damage = 0.0F;
		float stamina_damage = 0.0F;
		if (damageTakenFromMana > 0 && OverhauledDamage.isManaAttributesLoaded) {
			mana_damage = health_damage * damageTakenFromMana;
			OverhauledDamage.addMana(((LivingEntity) (Object) this), mana_damage);
		}
		if (damageTakenFromStamina > 0 && OverhauledDamage.isStaminaAttributesLoaded) {
			stamina_damage = health_damage * damageTakenFromStamina;
			OverhauledDamage.addStamina(((LivingEntity) (Object) this), stamina_damage);
		}
		health_damage = health_damage - mana_damage - stamina_damage;
		if (enable_debug_log) {
			OverhauledDamage.info("--- apply damage by reducing health / mana / stamina ---");
			OverhauledDamage.info("health_damage : " + health_damage);
			OverhauledDamage.info("");
			OverhauledDamage.info("mana_damage : " + mana_damage);
			OverhauledDamage.info("");
			OverhauledDamage.info("stamina_damage : " + stamina_damage);
			OverhauledDamage.info("");
		}

		return health_damage;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void overhauleddamage$tick(CallbackInfo ci) {
		if (!this.getWorld().isClient) {

			if (this.isBlocking()) {
				this.blockingTime++;
			} else if (this.blockingTime > 0) {
				this.blockingTime = 0;
			}

			if (this.overhauleddamage$getBleedingBuildUp() > 0) {
				this.bleedingTickTimer++;
				if (this.bleedingReductionDelayTimer < this.overhauleddamage$getBleedingBuildUpReductionDelayThreshold()) {
					this.bleedingReductionDelayTimer++;
					this.bleedingTickTimer = 0;
				}
				if (this.bleedingTickTimer >= this.overhauleddamage$getBleedingTickThreshold()
						&& this.bleedingReductionDelayTimer >= this.overhauleddamage$getBleedingBuildUpReductionDelayThreshold()) {
					if (this.overhauleddamage$getBleedingBuildUp() >= this.overhauleddamage$getMaxBleedingBuildUp()) {
						StatusEffect bleeding_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.bleeding_status_effect_identifier));
						if (bleeding_status_effect != null) {
							this.addStatusEffect(new StatusEffectInstance(bleeding_status_effect, this.overhauleddamage$getBleedingDuration(), 0, false, false, true));
						}
//                        this.overhauleddamage$setBleedingBuildUp(-this.overhauleddamage$getMaxBleedingBuildUp()); // TODO should bleeding be more difficult to apply after bleeding was applied?
						this.overhauleddamage$setBleedingBuildUp(0);
					} else {
						this.overhauleddamage$addBleedingBuildUp(-this.overhauleddamage$getBleedingBuildUpReduction());
					}
					this.bleedingTickTimer = 0;
				}
			}

			if (this.overhauleddamage$getBurnBuildUp() > 0) {
				this.burnTickTimer++;
				if (this.burnReductionDelayTimer < this.overhauleddamage$getBurnBuildUpReductionDelayThreshold()) {
					this.burnReductionDelayTimer++;
					this.burnTickTimer = 0;
				}
				if (this.burnTickTimer >= this.overhauleddamage$getBurnTickThreshold()
						&& this.burnReductionDelayTimer >= this.overhauleddamage$getBurnBuildUpReductionDelayThreshold()) {
					if (this.overhauleddamage$getBurnBuildUp() >= this.overhauleddamage$getMaxBurnBuildUp()) {
						StatusEffect burning_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.burning_status_effect_identifier));
						if (burning_status_effect != null) {
							int burnDuration = this.overhauleddamage$getBurnDuration();
							StatusEffectInstance statusEffectInstance = this.getStatusEffect(burning_status_effect);
							if (statusEffectInstance != null) {
								burnDuration = burnDuration + statusEffectInstance.getDuration();
							}
							this.addStatusEffect(new StatusEffectInstance(burning_status_effect, burnDuration, 0, false, false, true));
						}
						this.overhauleddamage$setBurnBuildUp(0);
					} else {
						this.overhauleddamage$addBurnBuildUp(-this.overhauleddamage$getBurnBuildUpReduction());
					}
					this.burnTickTimer = 0;
				}
			}

			if (this.overhauleddamage$getFreezeBuildUp() > 0) {
				this.freezeTickTimer++;
				if (this.freezeReductionDelayTimer < this.overhauleddamage$getFreezeBuildUpReductionDelayThreshold()) {
					this.freezeReductionDelayTimer++;
					this.freezeTickTimer = 0;
				}
				if (this.freezeTickTimer >= this.overhauleddamage$getFreezeTickThreshold()
						&& this.freezeReductionDelayTimer >= this.overhauleddamage$getFreezeBuildUpReductionDelayThreshold()) {
					if (this.overhauleddamage$getFreezeBuildUp() >= this.overhauleddamage$getMaxFreezeBuildUp()) {
						StatusEffect freeze_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.frozen_status_effect_identifier));
						if (freeze_status_effect != null) {
							this.addStatusEffect(new StatusEffectInstance(freeze_status_effect, this.overhauleddamage$getFreezeDuration(), 0, false, false, true));
						}
						this.overhauleddamage$setFreezeBuildUp(0);
					} else {
						this.overhauleddamage$addFreezeBuildUp(-this.overhauleddamage$getFreezeBuildUpReduction());
					}
					this.freezeTickTimer = 0;
				}
			}

			if (this.overhauleddamage$getStaggerBuildUp() > 0) {
				this.staggerTickTimer++;
				if (this.staggerReductionDelayTimer < this.overhauleddamage$getStaggerBuildUpReductionDelayThreshold()) {
					this.staggerReductionDelayTimer++;
					this.staggerTickTimer = 0;
				}
				if (this.staggerTickTimer >= this.overhauleddamage$getStaggerTickThreshold()
						&& this.staggerReductionDelayTimer >= this.overhauleddamage$getStaggerBuildUpReductionDelayThreshold()) {
					if (this.overhauleddamage$getStaggerBuildUp() >= this.overhauleddamage$getMaxStaggerBuildUp()) {
						StatusEffect staggered_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.staggered_status_effect_identifier));
						if (staggered_status_effect != null) {
							this.addStatusEffect(new StatusEffectInstance(staggered_status_effect, this.overhauleddamage$getStaggerDuration(), 0, false, false, true));
						}
						this.overhauleddamage$setStaggerBuildUp(0);
					} else {
						this.overhauleddamage$addStaggerBuildUp(-this.overhauleddamage$getStaggerBuildUpReduction());
					}
					this.staggerTickTimer = 0;
				}
			}

			if (this.overhauleddamage$getPoisonBuildUp() > 0) {
				this.poisonTickTimer++;
				if (this.poisonReductionDelayTimer < this.overhauleddamage$getPoisonBuildUpReductionDelayThreshold()) {
					this.poisonReductionDelayTimer++;
					this.poisonTickTimer = 0;
				}
				if (this.poisonTickTimer >= this.overhauleddamage$getPoisonTickThreshold()
						&& this.poisonReductionDelayTimer >= this.overhauleddamage$getPoisonBuildUpReductionDelayThreshold()) {
					if (this.overhauleddamage$getPoisonBuildUp() >= this.overhauleddamage$getMaxPoisonBuildUp()) {
						int poisonAmplifier = 0;
						StatusEffect poison_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.poison_status_effect_identifier));
						if (poison_status_effect != null) {
							StatusEffectInstance statusEffectInstance = this.getStatusEffect(poison_status_effect);
							if (statusEffectInstance != null) {
								poisonAmplifier = statusEffectInstance.getAmplifier() + 1;
							}
							this.addStatusEffect(new StatusEffectInstance(poison_status_effect, this.overhauleddamage$getPoisonDuration(), poisonAmplifier, false, false, true));
						}
						this.overhauleddamage$setPoisonBuildUp(0);
					} else {
						this.overhauleddamage$addPoisonBuildUp(-this.overhauleddamage$getPoisonBuildUpReduction());
					}
					this.poisonTickTimer = 0;
				}
			}

			if (this.overhauleddamage$getShockBuildUp() > 0) {
				this.shockTickTimer++;
				if (this.shockReductionDelayTimer < this.overhauleddamage$getShockBuildUpReductionDelayThreshold()) {
					this.shockReductionDelayTimer++;
					this.shockTickTimer = 0;
				}
				if (this.shockTickTimer >= this.overhauleddamage$getShockTickThreshold()
						&& this.shockReductionDelayTimer >= this.overhauleddamage$getShockBuildUpReductionDelayThreshold()) {
					if (this.overhauleddamage$getShockBuildUp() >= this.overhauleddamage$getMaxShockBuildUp()) {
						StatusEffect shocked_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.shocked_status_effect_identifier));
						if (shocked_status_effect != null) {
							this.addStatusEffect(new StatusEffectInstance(shocked_status_effect, this.overhauleddamage$getShockDuration(), 0, false, false, false));
						}
						this.overhauleddamage$setShockBuildUp(0);
					} else {
						this.overhauleddamage$addShockBuildUp(-this.overhauleddamage$getShockBuildUpReduction());
					}
					this.shockTickTimer = 0;
				}
			}
		}
	}

	// blocking is now active instantly
	@Inject(method = "isBlocking", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	public void overhauleddamage$isBlocking(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_blocking_overhaul);
	}

	@Override
	public float overhauleddamage$getAdditionalBashingDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.ADDITIONAL_BASHING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getIncreasedBashingDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.INCREASED_BASHING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getBashingResistance() {
		return (float) this.getAttributeValue(OverhauledDamage.BASHING_RESISTANCE);
	}

	@Override
	public float overhauleddamage$getAdditionalPiercingDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.ADDITIONAL_PIERCING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getIncreasedPiercingDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.INCREASED_PIERCING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getPiercingResistance() {
		return (float) this.getAttributeValue(OverhauledDamage.PIERCING_RESISTANCE);
	}

	@Override
	public float overhauleddamage$getAdditionalSlashingDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.ADDITIONAL_SLASHING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getIncreasedSlashingDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.INCREASED_SLASHING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getSlashingResistance() {
		return (float) this.getAttributeValue(OverhauledDamage.SLASHING_RESISTANCE);
	}

	@Override
	public float overhauleddamage$getBlockedPhysicalDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.BLOCKED_PHYSICAL_DAMAGE);
	}

	// region bleeding build up
	@Override
	public void overhauleddamage$addBleedingBuildUp(float amount) {
		StatusEffect bleeding_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.bleeding_status_effect_identifier));
		if (bleeding_status_effect == null) {
			if (this.overhauleddamage$getBleedingBuildUp() > 0) {
				this.overhauleddamage$setBleedingBuildUp(0);
			}
		} else {
			if (this.overhauleddamage$getMaxBleedingBuildUp() != -1.0f && !this.hasStatusEffect(bleeding_status_effect)) {
				float f = this.overhauleddamage$getBleedingBuildUp();
				this.overhauleddamage$setBleedingBuildUp(f + amount);
				if (this.overhauleddamage$getBleedingBuildUp() > this.overhauleddamage$getMaxBleedingBuildUp()) {
					this.bleedingTickTimer = this.overhauleddamage$getBleedingTickThreshold();
				} else if (amount > 0) {
					this.bleedingTickTimer = 0;
					this.bleedingReductionDelayTimer = 0;
				}
			}
		}
	}

	@Override
	public float overhauleddamage$getBleedingBuildUp() {
		return this.dataTracker.get(BLEEDING_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setBleedingBuildUp(float bleedingBuildUp) {
		this.dataTracker.set(BLEEDING_BUILD_UP, MathHelper.clamp(bleedingBuildUp, 0, this.overhauleddamage$getMaxBleedingBuildUp()));
	}

	@Override
	public float overhauleddamage$getMaxBleedingBuildUp() {
		return (float) this.getAttributeValue(OverhauledDamage.MAX_BLEEDING_BUILD_UP);
	}

	@Override
	public int overhauleddamage$getBleedingDuration() {
		return (int) this.getAttributeValue(OverhauledDamage.BLEEDING_DURATION);
	}

	@Override
	public int overhauleddamage$getBleedingTickThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.BLEEDING_TICK_THRESHOLD);
	}

	@Override
	public int overhauleddamage$getBleedingBuildUpReduction() {
		return (int) this.getAttributeValue(OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION);
	}

	@Override
	public int overhauleddamage$getBleedingBuildUpReductionDelayThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION_DELAY_THRESHOLD);
	}
	// endregion bleeding build up

	// region fire

	@Override
	public float overhauleddamage$getAdditionalFireDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.ADDITIONAL_FIRE_DAMAGE);
	}

	@Override
	public float overhauleddamage$getIncreasedFireDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.INCREASED_FIRE_DAMAGE);
	}

	@Override
	public float overhauleddamage$getBlockedFireDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.BLOCKED_FIRE_DAMAGE);
	}

	@Override
	public float overhauleddamage$getFireResistance() {
		return (float) this.getAttributeValue(OverhauledDamage.FIRE_RESISTANCE);
	}

	@Override
	public void overhauleddamage$addBurnBuildUp(float amount) {
		if (this.overhauleddamage$getMaxBurnBuildUp() != -1.0f) {
			this.overhauleddamage$setBurnBuildUp(this.overhauleddamage$getBurnBuildUp() + amount);
			if (this.overhauleddamage$getBurnBuildUp() > this.overhauleddamage$getMaxBurnBuildUp()) {
				this.burnTickTimer = this.overhauleddamage$getBurnTickThreshold();
			} else if (amount > 0) {
				this.burnTickTimer = 0;
				this.burnReductionDelayTimer = 0;
			}
		}
	}

	@Override
	public float overhauleddamage$getBurnBuildUp() {
		return this.dataTracker.get(BURN_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setBurnBuildUp(float burnBuildUp) {
		this.dataTracker.set(BURN_BUILD_UP, MathHelper.clamp(burnBuildUp, 0, this.overhauleddamage$getMaxBurnBuildUp()));
	}

	@Override
	public float overhauleddamage$getMaxBurnBuildUp() {
		return (float) this.getAttributeValue(OverhauledDamage.MAX_BURN_BUILD_UP);
	}

	@Override
	public int overhauleddamage$getBurnDuration() {
		return (int) this.getAttributeValue(OverhauledDamage.BURN_DURATION);
	}

	@Override
	public int overhauleddamage$getBurnTickThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.BURN_TICK_THRESHOLD);
	}

	@Override
	public int overhauleddamage$getBurnBuildUpReduction() {
		return (int) this.getAttributeValue(OverhauledDamage.BURN_BUILD_UP_REDUCTION);
	}

	@Override
	public int overhauleddamage$getBurnBuildUpReductionDelayThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.BURN_BUILD_UP_REDUCTION_DELAY_THRESHOLD);
	}
	// endregion fire

	// region frost

	@Override
	public float overhauleddamage$getAdditionalFrostDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.ADDITIONAL_FROST_DAMAGE);
	}

	@Override
	public float overhauleddamage$getIncreasedFrostDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.INCREASED_FROST_DAMAGE);
	}

	@Override
	public float overhauleddamage$getBlockedFrostDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.BLOCKED_FROST_DAMAGE);
	}

	@Override
	public float overhauleddamage$getFrostResistance() {
		return (float) this.getAttributeValue(OverhauledDamage.FROST_RESISTANCE);
	}

	@Override
	public void overhauleddamage$addFreezeBuildUp(float amount) {
		StatusEffect freeze_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.frozen_status_effect_identifier));
		if (this.overhauleddamage$getMaxFreezeBuildUp() != -1.0f && freeze_status_effect != null && !this.hasStatusEffect(freeze_status_effect)) {
			float f = this.overhauleddamage$getFreezeBuildUp();
			this.overhauleddamage$setFreezeBuildUp(f + amount);
			if (this.overhauleddamage$getFreezeBuildUp() > this.overhauleddamage$getMaxFreezeBuildUp()) {
				this.freezeTickTimer = this.overhauleddamage$getFreezeTickThreshold();
			} else if (amount > 0) {
				this.freezeTickTimer = 0;
				this.freezeReductionDelayTimer = 0;
			}
		}
	}

	@Override
	public float overhauleddamage$getFreezeBuildUp() {
		return this.dataTracker.get(FREEZE_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setFreezeBuildUp(float freezeBuildUp) {
		this.dataTracker.set(FREEZE_BUILD_UP, MathHelper.clamp(freezeBuildUp, 0, this.overhauleddamage$getMaxFreezeBuildUp()));
	}

	@Override
	public float overhauleddamage$getMaxFreezeBuildUp() {
		return (float) this.getAttributeValue(OverhauledDamage.MAX_FREEZE_BUILD_UP);
	}

	@Override
	public int overhauleddamage$getFreezeDuration() {
		return (int) this.getAttributeValue(OverhauledDamage.FREEZE_DURATION);
	}

	@Override
	public int overhauleddamage$getFreezeTickThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.FREEZE_TICK_THRESHOLD);
	}

	@Override
	public int overhauleddamage$getFreezeBuildUpReduction() {
		return (int) this.getAttributeValue(OverhauledDamage.FREEZE_BUILD_UP_REDUCTION);
	}

	@Override
	public int overhauleddamage$getFreezeBuildUpReductionDelayThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.FREEZE_BUILD_UP_REDUCTION_DELAY_THRESHOLD);
	}
	// endregion frost

	// region stagger build up
	@Override
	public void overhauleddamage$addStaggerBuildUp(float amount) {
		StatusEffect staggered_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.SERVER_CONFIG.buildUpEffects.staggered_status_effect_identifier));
		if (this.overhauleddamage$getMaxStaggerBuildUp() != -1.0f && staggered_status_effect != null && !this.hasStatusEffect(staggered_status_effect)) {
			float f = this.overhauleddamage$getStaggerBuildUp();
			this.overhauleddamage$setStaggerBuildUp(f + amount);
			if (this.overhauleddamage$getStaggerBuildUp() > this.overhauleddamage$getMaxStaggerBuildUp()) {
				this.staggerTickTimer = this.overhauleddamage$getStaggerTickThreshold();
			} else if (amount > 0) {
				this.staggerTickTimer = 0;
				this.staggerReductionDelayTimer = 0;
			}
		}
	}

	@Override
	public float overhauleddamage$getStaggerBuildUp() {
		return this.dataTracker.get(STAGGER_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setStaggerBuildUp(float staggerBuildUp) {
		this.dataTracker.set(STAGGER_BUILD_UP, MathHelper.clamp(staggerBuildUp, 0, this.overhauleddamage$getMaxStaggerBuildUp()));
	}

	@Override
	public float overhauleddamage$getMaxStaggerBuildUp() {
		return (float) this.getAttributeValue(OverhauledDamage.MAX_STAGGER_BUILD_UP);
	}

	@Override
	public int overhauleddamage$getStaggerDuration() {
		return (int) this.getAttributeValue(OverhauledDamage.STAGGER_DURATION);
	}

	@Override
	public int overhauleddamage$getStaggerTickThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.STAGGER_TICK_THRESHOLD);
	}

	@Override
	public int overhauleddamage$getStaggerBuildUpReduction() {
		return (int) this.getAttributeValue(OverhauledDamage.STAGGER_BUILD_UP_REDUCTION);
	}

	@Override
	public int overhauleddamage$getStaggerBuildUpReductionDelayThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.STAGGER_BUILD_UP_REDUCTION_DELAY_THRESHOLD);
	}
	// endregion stagger build up

	// region poison

	@Override
	public float overhauleddamage$getAdditionalPoisonDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.ADDITIONAL_POISON_DAMAGE);
	}

	@Override
	public float overhauleddamage$getIncreasedPoisonDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.INCREASED_POISON_DAMAGE);
	}

	@Override
	public float overhauleddamage$getBlockedPoisonDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.BLOCKED_POISON_DAMAGE);
	}

	@Override
	public float overhauleddamage$getPoisonResistance() {
		return (float) this.getAttributeValue(OverhauledDamage.POISON_RESISTANCE);
	}

	@Override
	public void overhauleddamage$addPoisonBuildUp(float amount) {
		if (this.overhauleddamage$getMaxPoisonBuildUp() != -1.0f) {
			float f = this.overhauleddamage$getPoisonBuildUp();
			this.overhauleddamage$setPoisonBuildUp(f + amount);
			if (this.overhauleddamage$getPoisonBuildUp() > this.overhauleddamage$getMaxPoisonBuildUp()) {
				this.poisonTickTimer = this.overhauleddamage$getPoisonTickThreshold();
			} else if (amount > 0) {
				this.poisonTickTimer = 0;
				this.poisonReductionDelayTimer = 0;
			}
		}
	}

	@Override
	public float overhauleddamage$getPoisonBuildUp() {
		return this.dataTracker.get(POISON_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setPoisonBuildUp(float poisonBuildUp) {
		this.dataTracker.set(POISON_BUILD_UP, MathHelper.clamp(poisonBuildUp, 0, this.overhauleddamage$getMaxPoisonBuildUp()));
	}

	@Override
	public float overhauleddamage$getMaxPoisonBuildUp() {
		return (float) this.getAttributeValue(OverhauledDamage.MAX_POISON_BUILD_UP);
	}

	@Override
	public int overhauleddamage$getPoisonDuration() {
		return (int) this.getAttributeValue(OverhauledDamage.POISON_DURATION);
	}

	@Override
	public int overhauleddamage$getPoisonTickThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.POISON_TICK_THRESHOLD);
	}

	@Override
	public int overhauleddamage$getPoisonBuildUpReduction() {
		return (int) this.getAttributeValue(OverhauledDamage.POISON_BUILD_UP_REDUCTION);
	}

	@Override
	public int overhauleddamage$getPoisonBuildUpReductionDelayThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.POISON_BUILD_UP_REDUCTION_DELAY_THRESHOLD);
	}
	// endregion poison

	// region lightning

	@Override
	public float overhauleddamage$getAdditionalLightningDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.ADDITIONAL_LIGHTNING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getIncreasedLightningDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.INCREASED_LIGHTNING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getBlockedLightningDamage() {
		return (float) this.getAttributeValue(OverhauledDamage.BLOCKED_LIGHTNING_DAMAGE);
	}

	@Override
	public float overhauleddamage$getLightningResistance() {
		return (float) this.getAttributeValue(OverhauledDamage.LIGHTNING_RESISTANCE);
	}

	@Override
	public void overhauleddamage$addShockBuildUp(float amount) {
		if (this.overhauleddamage$getMaxShockBuildUp() != -1.0f) {
			float f = this.overhauleddamage$getShockBuildUp();
			this.overhauleddamage$setShockBuildUp(f + amount);
			if (this.overhauleddamage$getShockBuildUp() > this.overhauleddamage$getMaxShockBuildUp()) {
				this.shockTickTimer = this.overhauleddamage$getShockTickThreshold();
			} else if (amount > 0) {
				this.shockTickTimer = 0;
				this.shockReductionDelayTimer = 0;
			}
		}
	}

	@Override
	public float overhauleddamage$getShockBuildUp() {
		return this.dataTracker.get(SHOCK_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setShockBuildUp(float shockBuildUp) {
		this.dataTracker.set(SHOCK_BUILD_UP, MathHelper.clamp(shockBuildUp, 0, this.overhauleddamage$getMaxShockBuildUp()));
	}

	@Override
	public float overhauleddamage$getMaxShockBuildUp() {
		return (float) this.getAttributeValue(OverhauledDamage.MAX_SHOCK_BUILD_UP);
	}

	@Override
	public int overhauleddamage$getShockDuration() {
		return (int) this.getAttributeValue(OverhauledDamage.SHOCK_DURATION);
	}

	@Override
	public int overhauleddamage$getShockTickThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.SHOCK_TICK_THRESHOLD);
	}

	@Override
	public int overhauleddamage$getShockBuildUpReduction() {
		return (int) this.getAttributeValue(OverhauledDamage.SHOCK_BUILD_UP_REDUCTION);
	}

	@Override
	public int overhauleddamage$getShockBuildUpReductionDelayThreshold() {
		return (int) this.getAttributeValue(OverhauledDamage.SHOCK_BUILD_UP_REDUCTION_DELAY_THRESHOLD);
	}
	// endregion lightning

	@Override
	public float overhauleddamage$getBlockForce() {
		return (float) this.getAttributeValue(OverhauledDamage.BLOCK_FORCE);
	}

	@Override
	public float overhauleddamage$getParryBonus() {
		return (float) this.getAttributeValue(OverhauledDamage.PARRY_BONUS);
	}

	@Override
	public float overhauleddamage$getParryWindow() {
		return (float) this.getAttributeValue(OverhauledDamage.PARRY_WINDOW);
	}

	@Override
	public float overhauleddamage$getBlockStaminaCost() {
		return (float) this.getAttributeValue(OverhauledDamage.BLOCK_STAMINA_COST);
	}

	@Override
	public float overhauleddamage$getParryStaminaCost() {
		return (float) this.getAttributeValue(OverhauledDamage.PARRY_STAMINA_COST);
	}

	@Override
	public boolean overhauleddamage$canParry() {
		return false;
	}

	@Override
	public int overhauleddamage$getBlockingTime() {
		return this.blockingTime;
	}

	@Override
	public float overhauleddamage$getDamageTakenMultiplier() {
		return (float) this.getAttributeValue(OverhauledDamage.DAMAGE_TAKEN_MULTIPLIER);
	}

	@Override
	public float overhauleddamage$getDamageTakenFromManaMultiplier() {
		return (float) this.getAttributeValue(OverhauledDamage.DAMAGE_TAKEN_FROM_MANA_MULTIPLIER);
	}

	@Override
	public float overhauleddamage$getDamageTakenFromStaminaMultiplier() {
		return (float) this.getAttributeValue(OverhauledDamage.DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER);
	}
}
