package com.github.theredbrain.overhauleddamage.compatibility;

import net.minecraft.world.entity.LivingEntity;

public class ManaAttributesIntegration {

	public static float getCurrentMana(LivingEntity livingEntity) {
		return 0.0F;//((ManaUsingEntity) livingEntity).manaattributes$getMana(); TODO re-enable Mana Attributes
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
//		((ManaUsingEntity) livingEntity).manaattributes$addMana(amount); TODO re-enable Mana Attributes
	}

}
