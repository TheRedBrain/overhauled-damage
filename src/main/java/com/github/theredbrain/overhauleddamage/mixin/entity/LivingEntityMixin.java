package com.github.theredbrain.overhauleddamage.mixin.entity;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow
	public abstract void damageArmor(DamageSource source, float amount);

	@Shadow
	public abstract int getArmor();

	@Shadow
	public abstract ItemStack getOffHandStack();

	@Shadow
	public abstract void stopUsingItem();

	@Shadow
	public abstract float getAbsorptionAmount();

	@Shadow
	public abstract void setAbsorptionAmount(float amount);

	@Shadow
	public abstract boolean blockedByShield(DamageSource source);

	@Shadow
	public abstract boolean isBlocking();

	@Shadow
	public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

	@Shadow
	public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

	@Shadow
	public abstract @Nullable StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> effect);

	@Unique
	private int bleedingTickTimer = 0;
	@Unique
	private int burnTickTimer = 0;
	@Unique
	private int freezeTickTimer = 0;
	@Unique
	private int staggerTickTimer = 0;
	@Unique
	private int poisonTickTimer = 0;
	@Unique
	private int shockTickTimer = 0;
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
	protected void overhauleddamage$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(BLEEDING_BUILD_UP, 0.0F);
		builder.add(BURN_BUILD_UP, 0.0F);
		builder.add(FREEZE_BUILD_UP, 0.0F);
		builder.add(POISON_BUILD_UP, 0.0F);
		builder.add(STAGGER_BUILD_UP, 0.0F);
		builder.add(SHOCK_BUILD_UP, 0.0F);

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

				.add(OverhauledDamage.ADDITIONAL_FROST_DAMAGE)
				.add(OverhauledDamage.INCREASED_FROST_DAMAGE)
				.add(OverhauledDamage.BLOCKED_FROST_DAMAGE)
				.add(OverhauledDamage.FROST_RESISTANCE)
				.add(OverhauledDamage.MAX_FREEZE_BUILD_UP)
				.add(OverhauledDamage.FREEZE_DURATION)
				.add(OverhauledDamage.FREEZE_TICK_THRESHOLD)
				.add(OverhauledDamage.FREEZE_BUILD_UP_REDUCTION)

				.add(OverhauledDamage.ADDITIONAL_FIRE_DAMAGE)
				.add(OverhauledDamage.INCREASED_FIRE_DAMAGE)
				.add(OverhauledDamage.BLOCKED_FIRE_DAMAGE)
				.add(OverhauledDamage.FIRE_RESISTANCE)
				.add(OverhauledDamage.MAX_BURN_BUILD_UP)
				.add(OverhauledDamage.BURN_DURATION)
				.add(OverhauledDamage.BURN_TICK_THRESHOLD)
				.add(OverhauledDamage.BURN_BUILD_UP_REDUCTION)

				.add(OverhauledDamage.ADDITIONAL_LIGHTNING_DAMAGE)
				.add(OverhauledDamage.INCREASED_LIGHTNING_DAMAGE)
				.add(OverhauledDamage.BLOCKED_LIGHTNING_DAMAGE)
				.add(OverhauledDamage.LIGHTNING_RESISTANCE)
				.add(OverhauledDamage.MAX_SHOCK_BUILD_UP)
				.add(OverhauledDamage.SHOCK_DURATION)
				.add(OverhauledDamage.SHOCK_TICK_THRESHOLD)
				.add(OverhauledDamage.SHOCK_BUILD_UP_REDUCTION)

				.add(OverhauledDamage.ADDITIONAL_POISON_DAMAGE)
				.add(OverhauledDamage.INCREASED_POISON_DAMAGE)
				.add(OverhauledDamage.BLOCKED_POISON_DAMAGE)
				.add(OverhauledDamage.POISON_RESISTANCE)
				.add(OverhauledDamage.MAX_POISON_BUILD_UP)
				.add(OverhauledDamage.POISON_DURATION)
				.add(OverhauledDamage.POISON_TICK_THRESHOLD)
				.add(OverhauledDamage.POISON_BUILD_UP_REDUCTION)

				.add(OverhauledDamage.MAX_STAGGER_BUILD_UP)
				.add(OverhauledDamage.STAGGER_DURATION)
				.add(OverhauledDamage.STAGGER_TICK_THRESHOLD)
				.add(OverhauledDamage.STAGGER_BUILD_UP_REDUCTION)

				.add(OverhauledDamage.BLOCK_FORCE)
				.add(OverhauledDamage.PARRY_BONUS)
				.add(OverhauledDamage.PARRY_WINDOW)

				.add(OverhauledDamage.BLOCK_STAMINA_COST)
				.add(OverhauledDamage.PARRY_STAMINA_COST)
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
	@Redirect(
			method = "applyArmorToDamage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
			)
	)
	public boolean overhauleddamage$redirect_bypassesArmor(DamageSource instance, TagKey<DamageType> tag) {
		return true;
	}

	// disables the vanilla shield blocking
	@Redirect(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;blockedByShield(Lnet/minecraft/entity/damage/DamageSource;)Z"
			)
	)
	public boolean overhauleddamage$redirect_blockedByShield(LivingEntity instance, DamageSource source) {
		return false;
	}

	@ModifyVariable(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"), argsOnly = true)
	private float overhauleddamage$applyDamage(float old, DamageSource source) {
		return ((DuckLivingEntityMixin) (Object) this).overhauleddamage$calculateOverhauledDamage(source, old);
	}

	@Override
	public float overhauleddamage$calculateOverhauledDamage(DamageSource source, float amount) {

		var serverConfig = OverhauledDamage.serverConfig;
		Optional<RegistryEntry.Reference<StatusEffect>> fall_immune_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(serverConfig.fall_immune_status_effect_identifier));
		if (source.isIn(DamageTypeTags.IS_FALL) && fall_immune_status_effect.isPresent() && this.hasStatusEffect(fall_immune_status_effect.get())) {
			return 0.0F;
		}

		Optional<RegistryEntry.Reference<StatusEffect>> staggered_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(serverConfig.staggered_status_effect_identifier));
		if (staggered_status_effect.isPresent() && this.hasStatusEffect(staggered_status_effect.get())) {
			amount = amount * 2;
		}

		Optional<RegistryEntry.Reference<StatusEffect>> calamity_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(serverConfig.calamity_status_effect_identifier));
		if (calamity_status_effect.isPresent()) {
			StatusEffectInstance calamityEffectInstance = this.getStatusEffect(calamity_status_effect.get());
			if (calamityEffectInstance != null) {
				amount = amount * 2 + calamityEffectInstance.getAmplifier();
			}
		}

		float f = amount;
		amount = Math.max(amount - this.getAbsorptionAmount(), 0.0F);
		this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - amount));
		float g = f - amount;
		if (g > 0.0F && g < 3.4028235E37F) {
			Entity var6 = source.getAttacker();
			if (var6 instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.increaseStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(g * 10.0F));
			}
		}

		if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
			this.damageArmor(source, amount);
		}

		float applied_damage = 0;
		float true_amount = 0;

		// true damage is
		if (source.isIn(Tags.IS_TRUE_DAMAGE)) {
			true_amount = amount;
			amount = 0;
		}

		if (amount > 0) {

			LinkedHashMap<String, Float[]> damage_type_multipliers = serverConfig.damage_type_multipliers;

			String damageTypeId = "";
			Float[] damage_type_multiplier = null;
			Optional<RegistryKey<DamageType>> optional = source.getTypeRegistryEntry().getKey();

			if (optional.isPresent()) {
				damageTypeId = optional.get().getValue().toString();
			}
			if (!damageTypeId.isEmpty()) {
				damage_type_multiplier = damage_type_multipliers.get(damageTypeId);
			}

			float bashing_amount = 0.0F;
			float piercing_amount = 0.0F;
			float slashing_amount = 0.0F;
			float poison_amount = 0.0F;
			float fire_amount = 0.0F;
			float frost_amount = 0.0F;
			float lightning_amount = 0.0F;

			if (damage_type_multiplier != null && damage_type_multiplier.length == 7) {
				bashing_amount = amount * damage_type_multiplier[0];
				piercing_amount = amount * damage_type_multiplier[1];
				slashing_amount = amount * damage_type_multiplier[2];
				poison_amount = amount * damage_type_multiplier[3];
				fire_amount = amount * damage_type_multiplier[4];
				frost_amount = amount * damage_type_multiplier[5];
				lightning_amount = amount * damage_type_multiplier[6];
			}

			LivingEntity attacker = null;
			if (source.getAttacker() instanceof LivingEntity) {
				attacker = (LivingEntity) source.getAttacker();
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

			// region shield blocks
			ItemStack shieldItemStack = this.getOffHandStack();
			if (this.isBlocking() && this.blockedByShield(source) && (((StaminaUsingEntity) this).staminaattributes$getStamina() > 0 || serverConfig.blocking_requires_stamina)) {
				// try to parry the attack
				boolean tryParry = this.overhauleddamage$canParry() && this.blockingTime <= ((DuckLivingEntityMixin) this).overhauleddamage$getParryWindow() && source.getAttacker() != null && source.getAttacker() instanceof LivingEntity && shieldItemStack.isIn(Tags.CAN_PARRY);
				double parryBonus = tryParry ? ((DuckLivingEntityMixin) this).overhauleddamage$getParryBonus() : 1;
				float blockedBashingDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
				float blockedPiercingDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
				float blockedSlashingDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
				float blockedFireDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFireDamage() * parryBonus);
				float blockedFrostDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFrostDamage() * parryBonus);
				float blockedLightningDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedLightningDamage() * parryBonus);
				float blockedPoisonDamage = (float) (((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPoisonDamage() * parryBonus);

				((StaminaUsingEntity) this).staminaattributes$addStamina(tryParry ? -((DuckLivingEntityMixin) this).overhauleddamage$getParryStaminaCost() : -((DuckLivingEntityMixin) this).overhauleddamage$getBlockStaminaCost());

				if (((StaminaUsingEntity) this).staminaattributes$getStamina() >= 0) {

					boolean isStaggered = false;
					// apply stagger based on left over damage
					float appliedStagger = (float) Math.max(((bashing_amount - blockedBashingDamage) * 0.75 + (piercing_amount - blockedPiercingDamage) * 0.5 + (slashing_amount - blockedSlashingDamage) * 0.5 + (lightning_amount - blockedLightningDamage) * 0.5), 0);
					if (appliedStagger > 0) {
						this.overhauleddamage$addStaggerBuildUp(appliedStagger);
						isStaggered = this.overhauleddamage$getStaggerBuildUp() >= this.overhauleddamage$getMaxStaggerBuildUp();

					}

					// parry was successful
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
								// attacker is staggered
								((DuckLivingEntityMixin) attacker).overhauleddamage$addStaggerBuildUp(((DuckLivingEntityMixin) attacker).overhauleddamage$getMaxStaggerBuildUp());
							}
						} else {
							// normal blocking applies knockback to the attacker
							if (attacker != null) {
								attacker.takeKnockback(((DuckLivingEntityMixin) this).overhauleddamage$getBlockForce(), attacker.getX() - this.getX(), attacker.getZ() - this.getZ());
							}
						}
						float totalBlockedDamage = blockedBashingDamage + blockedPiercingDamage + blockedSlashingDamage + blockedFireDamage + blockedFrostDamage + blockedLightningDamage + blockedPoisonDamage;
						if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity && totalBlockedDamage > 0.0f && totalBlockedDamage < 3.4028235E37f) {
							serverPlayerEntity.increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(totalBlockedDamage * 10.0f));
						}

						this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
					} else {
						this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
					}
				}
			}
			// endregion shield blocks

			// region apply resistances/armor
			// armorToughness now directly determines how effective armor is
			// effective armor reduces damage by its amount
			float effectiveArmor = this.getArmor() * (float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);

			// TODO think about this more
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

			bashing_amount = bashing_amount - (bashing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getBashingResistance()) / 100;

			piercing_amount = piercing_amount - (piercing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getPiercingResistance()) / 100;

			slashing_amount = slashing_amount - (slashing_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getSlashingResistance()) / 100;

			poison_amount = poison_amount - (poison_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getPoisonResistance()) / 100;

			fire_amount = fire_amount - (fire_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getFireResistance()) / 100;

			frost_amount = frost_amount - (frost_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getFrostResistance()) / 100;

			lightning_amount = lightning_amount - (lightning_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getLightningResistance()) / 100;
			// endregion apply resistances/armor

			applied_damage = piercing_amount + bashing_amount + slashing_amount;

			// taking damage interrupts eating food, drinking potions, etc
			if (applied_damage > 0.0f && !this.isBlocking() && serverConfig.damage_interrupts_item_usage) {
				this.stopUsingItem();
			}

			// apply bleeding
			float appliedBleeding = (float) ((piercing_amount * 0.5) + (slashing_amount * 0.5));
			if (appliedBleeding > 0 && source.isIn(Tags.APPLIES_BLEEDING)) {
				this.overhauleddamage$addBleedingBuildUp(appliedBleeding);
			}

			// apply burning
			if (fire_amount > 0) {
				this.overhauleddamage$addBurnBuildUp(fire_amount);
			}

			// apply chilled and frozen
			if (frost_amount > 0) {
				Optional<RegistryEntry.Reference<StatusEffect>> chilled_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(serverConfig.chilled_status_effect_identifier));
				if (chilled_status_effect.isPresent()) {
					int chilledDuration = (int) Math.ceil(frost_amount);
					StatusEffectInstance statusEffectInstance = this.getStatusEffect(chilled_status_effect.get());
					if (statusEffectInstance != null) {
						chilledDuration = chilledDuration + statusEffectInstance.getDuration();
					}
					this.addStatusEffect(new StatusEffectInstance(chilled_status_effect.get(), chilledDuration, 0, false, false, true));
				}
				this.overhauleddamage$addFreezeBuildUp(frost_amount);
			}

			// apply stagger
			float appliedStagger = (float) ((bashing_amount * 0.75) + (piercing_amount * 0.5) + (slashing_amount * 0.5) + (lightning_amount * 0.5));
			if (appliedStagger > 0) {
				this.overhauleddamage$addStaggerBuildUp(appliedStagger);
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

		return applied_damage + true_amount;
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
				if (this.bleedingTickTimer >= this.overhauleddamage$getBleedingTickThreshold()) {
					if (this.overhauleddamage$getBleedingBuildUp() >= this.overhauleddamage$getMaxBleedingBuildUp()) {
						Optional<RegistryEntry.Reference<StatusEffect>> bleeding_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.bleeding_status_effect_identifier));
						if (bleeding_status_effect.isPresent()) {
							this.addStatusEffect(new StatusEffectInstance(bleeding_status_effect.get(), this.overhauleddamage$getBleedingDuration(), 0, false, false, true));
						}
//                        this.overhauleddamage$setBleedingBuildUp(-this.overhauleddamage$getMaxBleedingBuildUp()); // TODO play test
						this.overhauleddamage$setBleedingBuildUp(0);
					} else {
						this.overhauleddamage$addBleedingBuildUp(-this.overhauleddamage$getBleedingBuildUpReduction());
					}
					this.bleedingTickTimer = 0;
				}
			}

			if (this.overhauleddamage$getBurnBuildUp() > 0) {
				this.burnTickTimer++;
				if (this.burnTickTimer >= this.overhauleddamage$getBurnTickThreshold()) {
					if (this.overhauleddamage$getBurnBuildUp() >= this.overhauleddamage$getMaxBurnBuildUp()) {
						Optional<RegistryEntry.Reference<StatusEffect>> burning_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.burning_status_effect_identifier));
						if (burning_status_effect.isPresent()) {
							int burnDuration = this.overhauleddamage$getBurnDuration();
							StatusEffectInstance statusEffectInstance = this.getStatusEffect(burning_status_effect.get());
							if (statusEffectInstance != null) {
								burnDuration = burnDuration + statusEffectInstance.getDuration();
							}
							this.addStatusEffect(new StatusEffectInstance(burning_status_effect.get(), burnDuration, 0, false, false, true));
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
				if (this.freezeTickTimer >= this.overhauleddamage$getFreezeTickThreshold()) {
					if (this.overhauleddamage$getFreezeBuildUp() >= this.overhauleddamage$getMaxFreezeBuildUp()) {
						Optional<RegistryEntry.Reference<StatusEffect>> freeze_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.frozen_status_effect_identifier));
						if (freeze_status_effect.isPresent()) {
							this.addStatusEffect(new StatusEffectInstance(freeze_status_effect.get(), this.overhauleddamage$getFreezeDuration(), 0, false, false, true));
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
				if (this.staggerTickTimer >= this.overhauleddamage$getStaggerTickThreshold()) {
					if (this.overhauleddamage$getStaggerBuildUp() >= this.overhauleddamage$getMaxStaggerBuildUp()) {
						Optional<RegistryEntry.Reference<StatusEffect>> staggered_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.staggered_status_effect_identifier));
						if (staggered_status_effect.isPresent()) {
							this.addStatusEffect(new StatusEffectInstance(staggered_status_effect.get(), this.overhauleddamage$getStaggerDuration(), 0, false, false, true));
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
				if (this.poisonTickTimer >= this.overhauleddamage$getPoisonTickThreshold()) {
					if (this.overhauleddamage$getPoisonBuildUp() >= this.overhauleddamage$getMaxPoisonBuildUp()) {
						int poisonAmplifier = 0;
						Optional<RegistryEntry.Reference<StatusEffect>> poison_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.poison_status_effect_identifier));
						if (poison_status_effect.isPresent()) {
							StatusEffectInstance statusEffectInstance = this.getStatusEffect(poison_status_effect.get());
							if (statusEffectInstance != null) {
								poisonAmplifier = statusEffectInstance.getAmplifier() + 1;
							}
							this.addStatusEffect(new StatusEffectInstance(poison_status_effect.get(), this.overhauleddamage$getPoisonDuration(), poisonAmplifier, false, false, true));
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
				if (this.shockTickTimer >= this.overhauleddamage$getShockTickThreshold()) {
					if (this.overhauleddamage$getShockBuildUp() >= this.overhauleddamage$getMaxShockBuildUp()) {
						Optional<RegistryEntry.Reference<StatusEffect>> shocked_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.shocked_status_effect_identifier));
						if (shocked_status_effect.isPresent()) {
							this.addStatusEffect(new StatusEffectInstance(shocked_status_effect.get(), this.overhauleddamage$getShockDuration(), 0, false, false, false));
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
		cir.setReturnValue(true);
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
		Optional<RegistryEntry.Reference<StatusEffect>> bleeding_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.bleeding_status_effect_identifier));
		if (bleeding_status_effect.isEmpty()) {
			if (this.overhauleddamage$getBleedingBuildUp() > 0) {
				this.overhauleddamage$setBleedingBuildUp(0);
			}
		} else {
			if (this.overhauleddamage$getMaxBleedingBuildUp() != -1.0f && !this.hasStatusEffect(bleeding_status_effect.get())) {
				float f = this.overhauleddamage$getBleedingBuildUp();
				this.overhauleddamage$setBleedingBuildUp(f + amount);
				if (this.overhauleddamage$getBleedingBuildUp() > this.overhauleddamage$getMaxBleedingBuildUp()) {
					this.bleedingTickTimer = this.overhauleddamage$getBleedingTickThreshold();
				} else if (amount > 0) {
					this.bleedingTickTimer = 0;
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
		Optional<RegistryEntry.Reference<StatusEffect>> freeze_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.frozen_status_effect_identifier));
		if (this.overhauleddamage$getMaxFreezeBuildUp() != -1.0f && freeze_status_effect.isPresent() && !this.hasStatusEffect(freeze_status_effect.get())) {
			float f = this.overhauleddamage$getFreezeBuildUp();
			this.overhauleddamage$setFreezeBuildUp(f + amount);
			if (this.overhauleddamage$getFreezeBuildUp() > this.overhauleddamage$getMaxFreezeBuildUp()) {
				this.freezeTickTimer = this.overhauleddamage$getFreezeTickThreshold();
			} else if (amount > 0) {
				this.freezeTickTimer = 0;
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
	// endregion frost

	// region stagger build up
	@Override
	public void overhauleddamage$addStaggerBuildUp(float amount) {
		Optional<RegistryEntry.Reference<StatusEffect>> staggered_status_effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(OverhauledDamage.serverConfig.staggered_status_effect_identifier));
		if (this.overhauleddamage$getMaxStaggerBuildUp() != -1.0f && staggered_status_effect.isPresent() && !this.hasStatusEffect(staggered_status_effect.get())) {
			float f = this.overhauleddamage$getStaggerBuildUp();
			this.overhauleddamage$setStaggerBuildUp(f + amount);
			if (this.overhauleddamage$getStaggerBuildUp() > this.overhauleddamage$getMaxStaggerBuildUp()) {
				this.staggerTickTimer = this.overhauleddamage$getStaggerTickThreshold();
			} else if (amount > 0) {
				this.staggerTickTimer = 0;
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
}
