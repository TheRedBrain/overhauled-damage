package com.github.theredbrain.overhauleddamage.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BeneficialStatusEffect extends StatusEffect {

    public BeneficialStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
    }
}
