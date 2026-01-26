package com.github.theredbrain.overhauleddamage.compatibility;

import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.LivingEntity;

public class StaminaAttributesIntegration {

    public static float getCurrentStamina(LivingEntity livingEntity) {
        return ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
    }

    public static void addStamina(LivingEntity livingEntity, float amount) {
        ((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
    }

}
