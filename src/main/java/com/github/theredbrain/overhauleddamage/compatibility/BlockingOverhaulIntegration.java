package com.github.theredbrain.overhauleddamage.compatibility;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;

public class BlockingOverhaulIntegration {

	public static void applyBlockAttackStaminaCost(LivingEntity livingEntity, boolean parried) {
		// TODO
	}

	public static boolean canParry(LivingEntity livingEntity, DamageSource damageSource, ItemStack shieldItemStack) {
		return true; // TODO
	}

	public static double getParryMultiplier(LivingEntity livingEntity, boolean parried) {
		return 1.0; // TODO
	}

	public static double getAppliedBlockingKnockback(LivingEntity defender, LivingEntity attacker, ItemStack blockingItemStack, boolean parried, double additionalAttackKnockback) {
		return 0.0; // TODO
	}

	public static boolean currentStaminaAllowsBlocking(LivingEntity livingEntity) {
//		return true;//BlockingOverhaul.currentStaminaAllowsBlocking(livingEntity);
	}

}
