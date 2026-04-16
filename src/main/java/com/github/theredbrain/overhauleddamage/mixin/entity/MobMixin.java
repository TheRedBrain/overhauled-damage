package com.github.theredbrain.overhauleddamage.mixin.entity;

import com.github.theredbrain.overhauleddamage.entity.UsesCustomDamageType;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
public abstract class MobMixin implements UsesCustomDamageType {
}
