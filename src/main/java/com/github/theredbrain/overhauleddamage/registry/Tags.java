package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class Tags {

	public static final TagKey<EntityType<?>> ATTACKS_WITH_BASHING = TagKey.create(Registries.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_bashing"));
	public static final TagKey<EntityType<?>> ATTACKS_WITH_PIERCING = TagKey.create(Registries.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_piercing"));
	public static final TagKey<EntityType<?>> ATTACKS_WITH_SLASHING = TagKey.create(Registries.ENTITY_TYPE, OverhauledDamage.identifier("attacks_with_slashing"));

	public static final TagKey<DamageType> IS_TRUE_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("is_true_damage"));
	public static final TagKey<DamageType> APPLIES_BLEEDING = TagKey.create(Registries.DAMAGE_TYPE, OverhauledDamage.identifier("applies_bleeding"));

	public static final TagKey<Item> CAN_PARRY = TagKey.create(Registries.ITEM, OverhauledDamage.identifier("can_parry"));

}
