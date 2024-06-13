package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {

    public static final TagKey<EntityType<?>> ATTACKS_WITH_BASHING = TagKey.of(RegistryKeys.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_bashing"));
    public static final TagKey<EntityType<?>> ATTACKS_WITH_PIERCING = TagKey.of(RegistryKeys.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_piercing"));
    public static final TagKey<EntityType<?>> ATTACKS_WITH_SLASHING = TagKey.of(RegistryKeys.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_slashing"));

    public static final TagKey<DamageType> IS_TRUE_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("is_true_damage"));
    public static final TagKey<DamageType> APPLIES_BLEEDING = TagKey.of(RegistryKeys.DAMAGE_TYPE, OverhauledDamage.identifier("applies_bleeding"));

    public static final TagKey<Item> CAN_PARRY = TagKey.of(RegistryKeys.ITEM, OverhauledDamage.identifier("can_parry"));

}
