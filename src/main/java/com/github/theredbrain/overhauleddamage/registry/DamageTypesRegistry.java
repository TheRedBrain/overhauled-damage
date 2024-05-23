package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DamageTypesRegistry {
    // region status effect damage types
    public static final RegistryKey<DamageType> BLEEDING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("bleeding_damage_type"));
    public static final RegistryKey<DamageType> BURNING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("burning_damage_type"));
    public static final RegistryKey<DamageType> POISON_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("poison_damage_type"));
    public static final RegistryKey<DamageType> SHOCKED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("shocked_damage_type"));
    // endregion status effect damage types

    // region player damage types
    public static final RegistryKey<DamageType> MOB_BASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("mob_bashing_damage_type"));
    public static final RegistryKey<DamageType> MOB_PIERCING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("mob_piercing_damage_type"));
    public static final RegistryKey<DamageType> MOB_SLASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("mob_slashing_damage_type"));
    // endregion player damage types
}
