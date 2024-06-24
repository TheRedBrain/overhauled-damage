package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DamageTypesRegistry {
	public static final RegistryKey<DamageType> MOB_BASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("mob_bashing_damage_type"));
	public static final RegistryKey<DamageType> MOB_PIERCING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("mob_piercing_damage_type"));
	public static final RegistryKey<DamageType> MOB_SLASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("mob_slashing_damage_type"));
}
