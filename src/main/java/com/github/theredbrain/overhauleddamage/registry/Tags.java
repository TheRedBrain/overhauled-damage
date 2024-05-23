package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
    //region EntityTags
    public static final TagKey<EntityType<?>> ATTACKS_WITH_BASHING = TagKey.of(RegistryKeys.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_bashing"));
    public static final TagKey<EntityType<?>> ATTACKS_WITH_PIERCING = TagKey.of(RegistryKeys.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_piercing"));
    public static final TagKey<EntityType<?>> ATTACKS_WITH_SLASHING = TagKey.of(RegistryKeys.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_slashing"));
    //endregion EntityTags
    //region DamageTypeTags
    public static final TagKey<DamageType> IS_TRUE_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("is_true_damage"));

    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_1"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_2"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_3"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_4"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_5"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_6"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_7"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_8"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_0_9"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_bashing_division_of_1"));
    
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_1"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_2"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_3"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_4"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_5"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_6"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_7"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_8"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_0_9"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_piercing_division_of_1"));
    
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_1"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_2"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_3"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_4"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_5"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_6"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_7"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_8"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_0_9"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_slashing_division_of_1"));
    
    public static final TagKey<DamageType> APPLIES_BLEEDING = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("applies_bleeding"));

    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_1"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_2"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_3"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_4"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_5"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_6"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_7"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_8"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_0_9"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_poison_division_of_1"));

    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_1"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_2"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_3"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_4"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_5"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_6"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_7"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_8"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_0_9"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_fire_division_of_1"));

    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_1"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_2"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_3"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_4"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_5"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_6"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_7"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_8"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_0_9"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_frost_division_of_1"));

    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_1"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_2"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_3"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_4"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_5"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_6"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_7"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_8"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_0_9"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("has_lightning_division_of_1"));

    //endregion DamageTypeTags
    //region ItemTags
    // enables functionality
    public static final TagKey<Item> CAN_PARRY = TagKey.of(RegistryKeys.ITEM, OverhauledDamage.identifier("can_parry"));

    //endregion ItemTags
}
