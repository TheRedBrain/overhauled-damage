package com.github.theredbrain.overhauleddamage.entity;

import com.github.theredbrain.overhauleddamage.registry.DataAttachmentRegistry;
import net.minecraft.world.entity.LivingEntity;

public class DataAttachmentHelper {

	public static float getBleedingBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.BLEEDING_BUILD_UP, 0.0F);
	}

	public static void setBleedingBuildUp(LivingEntity livingEntity, float bleedingBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.BLEEDING_BUILD_UP, bleedingBuildUp);
	}

	public static float getBurnBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.BURN_BUILD_UP, 0.0F);
	}

	public static void setBurnBuildUp(LivingEntity livingEntity, float burnBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.BURN_BUILD_UP, burnBuildUp);
	}

	public static float getFreezeBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.FREEZE_BUILD_UP, 0.0F);
	}

	public static void setFreezeBuildUp(LivingEntity livingEntity, float freezeBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.FREEZE_BUILD_UP, freezeBuildUp);
	}

	public static float getStaggerBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.STAGGER_BUILD_UP, 0.0F);
	}

	public static void setStaggerBuildUp(LivingEntity livingEntity, float staggerBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.STAGGER_BUILD_UP, staggerBuildUp);
	}

	public static float getPoisonBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.POISON_BUILD_UP, 0.0F);
	}

	public static void setPoisonBuildUp(LivingEntity livingEntity, float poisonBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.POISON_BUILD_UP, poisonBuildUp);
	}

	public static float getShockBuildUp(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.SHOCK_BUILD_UP, 0.0F);
	}

	public static void setShockBuildUp(LivingEntity livingEntity, float shockBuildUp) {
		livingEntity.setAttached(DataAttachmentRegistry.SHOCK_BUILD_UP, shockBuildUp);
	}

}
