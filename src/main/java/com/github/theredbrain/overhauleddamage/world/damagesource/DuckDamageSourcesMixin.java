package com.github.theredbrain.overhauleddamage.world.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface DuckDamageSourcesMixin {
	DamageSource overhauleddamage$bleeding(LivingEntity mob);

	DamageSource overhauleddamage$burning(LivingEntity mob);

	DamageSource overhauleddamage$poison(LivingEntity mob);

	DamageSource overhauleddamage$shocked(LivingEntity mob);
}
