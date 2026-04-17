package com.github.theredbrain.overhauleddamage.mixin.world.damagesource;

import com.github.theredbrain.overhauleddamage.entity.UsesCustomDamageType;
import com.github.theredbrain.overhauleddamage.registry.DamageTypesRegistry;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import com.github.theredbrain.overhauleddamage.world.damagesource.DuckDamageSourcesMixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin implements DuckDamageSourcesMixin {

	@Shadow
	public abstract DamageSource source(ResourceKey<DamageType> key, @Nullable Entity cause);

	@WrapMethod(method = "mobAttack")
	public DamageSource overhauleddamage$wrap_mobAttack(final LivingEntity mob, Operation<DamageSource> original) {
		if (mob instanceof UsesCustomDamageType usesCustomDamageType) {
			ResourceKey<DamageType> customDamageType = usesCustomDamageType.overhauleddamage$getCustomDamageType();
			if (customDamageType != null) {
				return this.source(customDamageType, mob);
			}
		}
		if (mob.is(Tags.ATTACKS_WITH_BASHING)) {
			return this.source(DamageTypesRegistry.MOB_BASHING_DAMAGE_TYPE, mob);
		} else if (mob.is(Tags.ATTACKS_WITH_PIERCING)) {
			return this.source(DamageTypesRegistry.MOB_PIERCING_DAMAGE_TYPE, mob);
		} else if (mob.is(Tags.ATTACKS_WITH_SLASHING)) {
			return this.source(DamageTypesRegistry.MOB_SLASHING_DAMAGE_TYPE, mob);
		}
		return original.call(mob);
	}

	@Override
	public DamageSource overhauleddamage$bleeding(final LivingEntity mob) {
		return this.source(DamageTypesRegistry.BLEEDING_DAMAGE_TYPE, mob);
	}

	@Override
	public DamageSource overhauleddamage$burning(final LivingEntity mob) {
		return this.source(DamageTypesRegistry.BURNING_DAMAGE_TYPE, mob);
	}

	@Override
	public DamageSource overhauleddamage$poison(final LivingEntity mob) {
		return this.source(DamageTypesRegistry.POISON_DAMAGE_TYPE, mob);
	}

	@Override
	public DamageSource overhauleddamage$shocked(final LivingEntity mob) {
		return this.source(DamageTypesRegistry.SHOCKED_DAMAGE_TYPE, mob);
	}
}
