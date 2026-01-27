package com.github.theredbrain.overhauleddamage.mixin.entity;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.entity.LivingEntityHelper;
import com.google.common.collect.HashMultimap;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

	@Shadow
	public abstract double getAttributeValue(Holder<Attribute> attribute);

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
	private static final EntityDataAccessor<Float> BLEEDING_BUILD_UP = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

	@Unique
	private static final EntityDataAccessor<Float> BURN_BUILD_UP = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

	@Unique
	private static final EntityDataAccessor<Float> FREEZE_BUILD_UP = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

	@Unique
	private static final EntityDataAccessor<Float> STAGGER_BUILD_UP = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

	@Unique
	private static final EntityDataAccessor<Float> POISON_BUILD_UP = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

	@Unique
	private static final EntityDataAccessor<Float> SHOCK_BUILD_UP = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "defineSynchedData", at = @At("RETURN"))
	protected void overhauleddamage$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BLEEDING_BUILD_UP, 0.0F);
		builder.define(BURN_BUILD_UP, 0.0F);
		builder.define(FREEZE_BUILD_UP, 0.0F);
		builder.define(POISON_BUILD_UP, 0.0F);
		builder.define(STAGGER_BUILD_UP, 0.0F);
		builder.define(SHOCK_BUILD_UP, 0.0F);

	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void overhauleddamage$createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
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

				.add(OverhauledDamage.DAMAGE_TAKEN_FROM_MANA_MULTIPLIER)
				.add(OverhauledDamage.DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER)
		;
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void overhauleddamage$readCustomDataFromNbt(ValueInput view, CallbackInfo ci) {

		this.overhauleddamage$setBleedingBuildUp(view.getFloatOr("bleeding_build_up", 0.0F));

		this.overhauleddamage$setBurnBuildUp(view.getFloatOr("burn_build_up", 0.0F));

		this.overhauleddamage$setFreezeBuildUp(view.getFloatOr("freeze_build_up", 0.0F));

		this.overhauleddamage$setPoisonBuildUp(view.getFloatOr("poison_build_up", 0.0F));

		this.overhauleddamage$setStaggerBuildUp(view.getFloatOr("stagger_build_up", 0.0F));

		this.overhauleddamage$setShockBuildUp(view.getFloatOr("shock_build_up", 0.0F));

	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void overhauleddamage$writeCustomDataToNbt(ValueOutput view, CallbackInfo ci) {

		view.putFloat("bleeding_build_up", this.overhauleddamage$getBleedingBuildUp());

		view.putFloat("burn_build_up", this.overhauleddamage$getBurnBuildUp());

		view.putFloat("freeze_build_up", this.overhauleddamage$getFreezeBuildUp());

		view.putFloat("poison_build_up", this.overhauleddamage$getPoisonBuildUp());

		view.putFloat("stagger_build_up", this.overhauleddamage$getStaggerBuildUp());

		view.putFloat("shock_build_up", this.overhauleddamage$getShockBuildUp());

	}

	// disables the vanilla armor calculation
	@WrapOperation(
			method = "getDamageAfterArmorAbsorb",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z")
	)
	public boolean overhauleddamage$wrap_bypassesArmor(DamageSource instance, TagKey<DamageType> tag, Operation<Boolean> original) {
		return OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_armor_overhaul.get() || original.call(instance, tag);
	}
	@WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F"))
	private float overhauleddamage$wrap_getDamageBlockedAmount(LivingEntity instance, ServerLevel world, DamageSource source, float amount, Operation<Float> original) {
		if (OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_blocking_overhaul.get()) {
			return 0.0F;
		} else {
			return original.call(instance, world, source, amount);
		}
	}

	@Definition(id = "modifyAppliedDamage", method = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F")
	@Expression("? = ?.modifyAppliedDamage(?, ?)")
	@ModifyVariable(method = "actuallyHurt", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER), argsOnly = true)
	private float overhauleddamage$modify_applyDamage(float value, @Local(argsOnly = true) ServerLevel serverLevel, @Local(argsOnly = true) DamageSource source) {
		return LivingEntityHelper.calculateOverhauledDamage(serverLevel, ((LivingEntity) (Object) this), source, value);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void overhauleddamage$tick(CallbackInfo ci) {
		LivingEntityHelper.tick(((LivingEntity) (Object) this));
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
	public float overhauleddamage$getBleedingBuildUp() {
		return this.entityData.get(BLEEDING_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setBleedingBuildUp(float bleedingBuildUp) {
		this.entityData.set(BLEEDING_BUILD_UP, Mth.clamp(bleedingBuildUp, 0, this.overhauleddamage$getMaxBleedingBuildUp()));
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

	@Override
	public int overhauleddamage$getBleedingTickTimer() {
		return this.bleedingTickTimer;
	}

	@Override
	public void overhauleddamage$setBleedingTickTimer(int bleedingTickTimer) {
		this.bleedingTickTimer = bleedingTickTimer;
	}

	@Override
	public int overhauleddamage$getBleedingReductionDelayTimer() {
		return this.bleedingReductionDelayTimer;
	}

	@Override
	public void overhauleddamage$setBleedingReductionDelayTimer(int bleedingReductionDelayTimer) {
		this.bleedingReductionDelayTimer = bleedingReductionDelayTimer;
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
	public float overhauleddamage$getBurnBuildUp() {
		return this.entityData.get(BURN_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setBurnBuildUp(float burnBuildUp) {
		this.entityData.set(BURN_BUILD_UP, Mth.clamp(burnBuildUp, 0, this.overhauleddamage$getMaxBurnBuildUp()));
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

	@Override
	public int overhauleddamage$getBurnTickTimer() {
		return this.burnTickTimer;
	}

	@Override
	public void overhauleddamage$setBurnTickTimer(int burnTickTimer) {
		this.burnTickTimer = burnTickTimer;
	}

	@Override
	public int overhauleddamage$getBurnReductionDelayTimer() {
		return this.burnReductionDelayTimer;
	}

	@Override
	public void overhauleddamage$setBurnReductionDelayTimer(int burnReductionDelayTimer) {
		this.burnReductionDelayTimer = burnReductionDelayTimer;
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
	public float overhauleddamage$getFreezeBuildUp() {
		return this.entityData.get(FREEZE_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setFreezeBuildUp(float freezeBuildUp) {
		this.entityData.set(FREEZE_BUILD_UP, Mth.clamp(freezeBuildUp, 0, this.overhauleddamage$getMaxFreezeBuildUp()));
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

	@Override
	public int overhauleddamage$getFreezeTickTimer() {
		return this.freezeTickTimer;
	}

	@Override
	public void overhauleddamage$setFreezeTickTimer(int freezeTickTimer) {
		this.freezeTickTimer = freezeTickTimer;
	}

	@Override
	public int overhauleddamage$getFreezeReductionDelayTimer() {
		return this.freezeReductionDelayTimer;
	}

	@Override
	public void overhauleddamage$setFreezeReductionDelayTimer(int freezeReductionDelayTimer) {
		this.freezeReductionDelayTimer = freezeReductionDelayTimer;
	}
	// endregion frost

	// region stagger build up
	@Override
	public float overhauleddamage$getStaggerBuildUp() {
		return this.entityData.get(STAGGER_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setStaggerBuildUp(float staggerBuildUp) {
		this.entityData.set(STAGGER_BUILD_UP, Mth.clamp(staggerBuildUp, 0, this.overhauleddamage$getMaxStaggerBuildUp()));
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

	@Override
	public int overhauleddamage$getStaggerTickTimer() {
		return this.staggerTickTimer;
	}

	@Override
	public void overhauleddamage$setStaggerTickTimer(int staggerTickTimer) {
		this.staggerTickTimer = staggerTickTimer;
	}

	@Override
	public int overhauleddamage$getStaggerReductionDelayTimer() {
		return this.staggerReductionDelayTimer;
	}

	@Override
	public void overhauleddamage$setStaggerReductionDelayTimer(int staggerReductionDelayTimer) {
		this.staggerReductionDelayTimer = staggerReductionDelayTimer;
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
	public float overhauleddamage$getPoisonBuildUp() {
		return this.entityData.get(POISON_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setPoisonBuildUp(float poisonBuildUp) {
		this.entityData.set(POISON_BUILD_UP, Mth.clamp(poisonBuildUp, 0, this.overhauleddamage$getMaxPoisonBuildUp()));
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

	@Override
	public int overhauleddamage$getPoisonTickTimer() {
		return this.poisonTickTimer;
	}

	@Override
	public void overhauleddamage$setPoisonTickTimer(int poisonTickTimer) {
		this.poisonTickTimer = poisonTickTimer;
	}

	@Override
	public int overhauleddamage$getPoisonReductionDelayTimer() {
		return this.poisonReductionDelayTimer;
	}

	@Override
	public void overhauleddamage$setPoisonReductionDelayTimer(int poisonReductionDelayTimer) {
		this.poisonReductionDelayTimer = poisonReductionDelayTimer;
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
	public float overhauleddamage$getShockBuildUp() {
		return this.entityData.get(SHOCK_BUILD_UP);
	}

	@Override
	public void overhauleddamage$setShockBuildUp(float shockBuildUp) {
		this.entityData.set(SHOCK_BUILD_UP, Mth.clamp(shockBuildUp, 0, this.overhauleddamage$getMaxShockBuildUp()));
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

	@Override
	public int overhauleddamage$getShockTickTimer() {
		return this.shockTickTimer;
	}

	@Override
	public void overhauleddamage$setShockTickTimer(int shockTickTimer) {
		this.shockTickTimer = shockTickTimer;
	}

	@Override
	public int overhauleddamage$getShockReductionDelayTimer() {
		return this.shockReductionDelayTimer;
	}

	@Override
	public void overhauleddamage$setShockReductionDelayTimer(int shockReductionDelayTimer) {
		this.shockReductionDelayTimer = shockReductionDelayTimer;
	}
	// endregion lightning

	@Override
	public float overhauleddamage$getDamageTakenFromManaMultiplier() {
		return (float) this.getAttributeValue(OverhauledDamage.DAMAGE_TAKEN_FROM_MANA_MULTIPLIER);
	}

	@Override
	public float overhauleddamage$getDamageTakenFromStaminaMultiplier() {
		return (float) this.getAttributeValue(OverhauledDamage.DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER);
	}

}
