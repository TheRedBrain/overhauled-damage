package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.advancements.criterion.OverhauledDamageEntityPredicate;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class EntitySubPredicateTypeRegistry {

	public static void init() {
	}

	private static <T extends EntitySubPredicate> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
		return Registry.register(
				BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE,
				id,
				mapCodec
		);
	}

	static {
		OverhauledDamage.OVERHAULED_DAMAGE_ENTITY_PREDICATE = register(OverhauledDamage.identifier("overhauled_damage_entity"), OverhauledDamageEntityPredicate.CODEC);
	}
}
