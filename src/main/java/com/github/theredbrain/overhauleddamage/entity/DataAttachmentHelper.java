package com.github.theredbrain.overhauleddamage.entity;

import com.github.theredbrain.overhauleddamage.registry.DataAttachmentRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class DataAttachmentHelper {

	public static double getBleedingBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrSet(DataAttachmentRegistry.BLEEDING_BUILD_UP, 0.0);
	}

	public static void setBleedingBuildUp(LivingEntity livingEntity, double bleedingBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.BLEEDING_BUILD_UP, Mth.clamp(bleedingBuildUp, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp()));
	}

	public static double getBurnBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrSet(DataAttachmentRegistry.BURN_BUILD_UP, 0.0);
	}

	public static void setBurnBuildUp(LivingEntity livingEntity, double burnBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.BURN_BUILD_UP, Mth.clamp(burnBuildUp, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp()));
	}

	public static double getFreezeBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrSet(DataAttachmentRegistry.FREEZE_BUILD_UP, 0.0);
	}

	public static void setFreezeBuildUp(LivingEntity livingEntity, double freezeBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.FREEZE_BUILD_UP, Mth.clamp(freezeBuildUp, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp()));
	}

	public static double getStaggerBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrSet(DataAttachmentRegistry.STAGGER_BUILD_UP, 0.0);
	}

	public static void setStaggerBuildUp(LivingEntity livingEntity, double staggerBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.STAGGER_BUILD_UP, Mth.clamp(staggerBuildUp, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp()));
	}

	public static double getPoisonBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrSet(DataAttachmentRegistry.POISON_BUILD_UP, 0.0);
	}

	public static void setPoisonBuildUp(LivingEntity livingEntity, double poisonBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.POISON_BUILD_UP, Mth.clamp(poisonBuildUp, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp()));
	}

	public static double getShockBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrSet(DataAttachmentRegistry.SHOCK_BUILD_UP, 0.0);
	}

	public static void setShockBuildUp(LivingEntity livingEntity, double shockBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.SHOCK_BUILD_UP, Mth.clamp(shockBuildUp, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp()));
	}

}
