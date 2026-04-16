package com.github.theredbrain.overhauleddamage.entity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import org.jspecify.annotations.Nullable;

public interface UsesCustomDamageType {

	@Nullable
	default ResourceKey<DamageType> overhauleddamage$getCustomDamageType() {
		return null;
	}
}
