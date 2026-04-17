package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.world.effect.BleedingMobEffect;
import com.github.theredbrain.overhauleddamage.world.effect.BurningMobEffect;
import com.github.theredbrain.overhauleddamage.world.effect.CustomPoisonMobEffect;
import com.github.theredbrain.overhauleddamage.world.effect.OverhauledDamageEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MobEffectsRegistry {

	public static final MobEffect BLEEDING = new BleedingMobEffect();
	public static final MobEffect BURNING = new BurningMobEffect();
	public static final MobEffect CHILLED = new OverhauledDamageEffect(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.chilled_effect.effect_color.toInt());
	public static final MobEffect FROZEN = new OverhauledDamageEffect(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.frozen_effect.effect_color.toInt());
	public static final MobEffect POISON = new CustomPoisonMobEffect();
	public static final MobEffect SHOCKED = new OverhauledDamageEffect(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.shocked_effect.effect_color.toInt());
	public static final MobEffect STAGGERED = new OverhauledDamageEffect(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.stagger_effect.effect_color.toInt());
	public static final MobEffect HIT_STUN = new OverhauledDamageEffect(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.hit_stun_effect.effect_color.toInt());

	public static void registerEffects() {
		ServerConfig serverConfig = OverhauledDamage.SERVER_CONFIG;
		// --- Attribute Modifiers ---
		CHILLED
				.addAttributeModifier(Attributes.MOVEMENT_SPEED, OverhauledDamage.identifier("effect.chilled_effect"), serverConfig.status_effects.chilled_effect.movement_speed_total_multiplier.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
				.addAttributeModifier(Attributes.ATTACK_SPEED, OverhauledDamage.identifier("effect.chilled_effect"), serverConfig.status_effects.chilled_effect.attack_speed_total_multiplier.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
		;
		HIT_STUN
				.addAttributeModifier(Attributes.MOVEMENT_SPEED, OverhauledDamage.identifier("effect.hit_stun_effect"), serverConfig.status_effects.hit_stun_effect.movement_speed_total_multiplier.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
				.addAttributeModifier(Attributes.ATTACK_SPEED, OverhauledDamage.identifier("effect.hit_stun_effect"), serverConfig.status_effects.hit_stun_effect.attack_speed_total_multiplier.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
		;
		OverhauledDamage.addModdedAttributesToEffects();

		// --- Configuration ---
		OverhauledDamage.configureEffects();

		// --- Registration ---
		OverhauledDamage.BLEEDING = register("bleeding", BLEEDING);
		OverhauledDamage.BURNING = register("burning", BURNING);
		OverhauledDamage.CHILLED = register("chilled", CHILLED);
		OverhauledDamage.FROZEN = register("frozen", FROZEN);
		OverhauledDamage.POISON = register("poison", POISON);
		OverhauledDamage.SHOCKED = register("shocked", SHOCKED);
		OverhauledDamage.STAGGERED = register("staggered", STAGGERED);
		OverhauledDamage.HIT_STUN = register("hit_stun", HIT_STUN);
	}

	private static Holder<MobEffect> register(String identifierString, MobEffect mobEffect) {
		return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, OverhauledDamage.identifier(identifierString), mobEffect);
	}
}
