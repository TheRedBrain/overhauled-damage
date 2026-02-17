package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.world.item.enchantment.AddBleedingBuildUpEnchantmentEntityEffect;
import com.github.theredbrain.overhauleddamage.world.item.enchantment.AddBurnBuildUpEnchantmentEntityEffect;
import com.github.theredbrain.overhauleddamage.world.item.enchantment.AddFreezeBuildUpEnchantmentEntityEffect;
import com.github.theredbrain.overhauleddamage.world.item.enchantment.AddPoisonBuildUpEnchantmentEntityEffect;
import com.github.theredbrain.overhauleddamage.world.item.enchantment.AddShockBuildUpEnchantmentEntityEffect;
import com.github.theredbrain.overhauleddamage.world.item.enchantment.AddStaggerBuildUpEnchantmentEntityEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class EnchantmentEntityEffectRegistry {

	public static void init() {
	}

	private static <T extends EnchantmentEntityEffect> MapCodec<T> register(Identifier id, MapCodec<T> codec) {
		return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, id, codec);
	}

	static {
		OverhauledDamage.ADD_BLEEDING_BUILD_UP = register(OverhauledDamage.identifier("add_bleeding_build_up"), AddBleedingBuildUpEnchantmentEntityEffect.CODEC);
		OverhauledDamage.ADD_BURN_BUILD_UP = register(OverhauledDamage.identifier("add_burn_build_up"), AddBurnBuildUpEnchantmentEntityEffect.CODEC);
		OverhauledDamage.ADD_FREEZE_BUILD_UP = register(OverhauledDamage.identifier("add_freeze_build_up"), AddFreezeBuildUpEnchantmentEntityEffect.CODEC);
		OverhauledDamage.ADD_POISON_BUILD_UP = register(OverhauledDamage.identifier("add_poison_build_up"), AddPoisonBuildUpEnchantmentEntityEffect.CODEC);
		OverhauledDamage.ADD_SHOCK_BUILD_UP = register(OverhauledDamage.identifier("add_shock_build_up"), AddShockBuildUpEnchantmentEntityEffect.CODEC);
		OverhauledDamage.ADD_STAGGER_BUILD_UP = register(OverhauledDamage.identifier("add_stagger_build_up"), AddStaggerBuildUpEnchantmentEntityEffect.CODEC);
	}

}
