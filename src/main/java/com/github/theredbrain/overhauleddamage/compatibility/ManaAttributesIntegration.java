package com.github.theredbrain.overhauleddamage.compatibility;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import net.minecraft.world.entity.LivingEntity;

public class ManaAttributesIntegration {

	public static float getCurrentMana(LivingEntity livingEntity) {
		return ((ManaUsingEntity) livingEntity).manaattributes$getMana();
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
		((ManaUsingEntity) livingEntity).manaattributes$addMana(amount);
	}

}
