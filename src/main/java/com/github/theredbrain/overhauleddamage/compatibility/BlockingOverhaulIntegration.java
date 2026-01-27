package com.github.theredbrain.overhauleddamage.compatibility;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import com.github.theredbrain.blockingoverhaul.entity.DuckLivingEntityMixin;
import com.github.theredbrain.blockingoverhaul.entity.LivingEntityHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BlockingOverhaulIntegration {

	public static void applyBlockAttackStaminaCost(LivingEntity livingEntity, boolean parried) {
		BlockingOverhaul.applyBlockAttackStaminaCost(livingEntity, parried);
	}

	public static void playBlockingSoundEvent(ServerLevel serverLevel, LivingEntity livingEntity, ItemStack itemStack, boolean parried) {
		BlockingOverhaul.playBlockingSoundEvent(serverLevel, livingEntity, itemStack, parried);
	}

	public static boolean canParry(LivingEntity livingEntity, DamageSource damageSource, ItemStack shieldItemStack) {
		return LivingEntityHelper.canParry(livingEntity, damageSource, shieldItemStack);
	}

	public static double getParryMultiplier(LivingEntity livingEntity, boolean parried) {
		return parried ? ((DuckLivingEntityMixin) livingEntity).blockingoverhaul$getParryMultiplier() : 1.0;
	}

	public static double getAppliedBlockingKnockback(LivingEntity defender, LivingEntity attacker, ItemStack blockingItemStack, boolean parried, double additionalAttackKnockback) {
		return LivingEntityHelper.getAppliedBlockingKnockback(defender, attacker, blockingItemStack, parried, additionalAttackKnockback);
	}

	public static boolean currentStaminaAllowsBlocking(LivingEntity livingEntity) {
		return BlockingOverhaul.currentStaminaAllowsBlocking(livingEntity);
	}

}
