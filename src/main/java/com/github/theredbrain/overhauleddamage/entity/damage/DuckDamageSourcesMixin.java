package com.github.theredbrain.overhauleddamage.entity.damage;

import net.minecraft.entity.damage.DamageSource;

public interface DuckDamageSourcesMixin {
    DamageSource overhauleddamage$bleeding();
    DamageSource overhauleddamage$burning();
    DamageSource overhauleddamage$poison();
    DamageSource overhauleddamage$shocked();
}
