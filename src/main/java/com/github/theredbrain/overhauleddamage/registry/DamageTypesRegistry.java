package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypesRegistry {
	public static final ResourceKey<DamageType> MOB_BASHING_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("mob_bashing_damage_type"));
	public static final ResourceKey<DamageType> MOB_PIERCING_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("mob_piercing_damage_type"));
	public static final ResourceKey<DamageType> MOB_SLASHING_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("mob_slashing_damage_type"));

	public static final ResourceKey<DamageType> BLEEDING_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("bleeding_damage_type"));
	public static final ResourceKey<DamageType> BURNING_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("burning_damage_type"));
	public static final ResourceKey<DamageType> POISON_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("poison_damage_type"));
	public static final ResourceKey<DamageType> SHOCKED_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("shocked_damage_type"));
}
