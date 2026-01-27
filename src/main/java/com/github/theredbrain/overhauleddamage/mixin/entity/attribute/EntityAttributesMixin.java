package com.github.theredbrain.overhauleddamage.mixin.entity.attribute;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Attributes.class)
public class EntityAttributesMixin {
	static {
		OverhauledDamage.ADDITIONAL_BASHING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("additional_bashing_damage"), new RangedAttribute("attribute.name.additional_bashing_damage", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.INCREASED_BASHING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("increased_bashing_damage"), new RangedAttribute("attribute.name.increased_bashing_damage", 1.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.BASHING_RESISTANCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("bashing_resistance"), new RangedAttribute("attribute.name.bashing_resistance", 0.0, -1024.0, 1024.0).setSyncable(true));

		OverhauledDamage.ADDITIONAL_PIERCING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("additional_piercing_damage"), new RangedAttribute("attribute.name.additional_piercing_damage", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.INCREASED_PIERCING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("increased_piercing_damage"), new RangedAttribute("attribute.name.increased_piercing_damage", 1.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.PIERCING_RESISTANCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("piercing_resistance"), new RangedAttribute("attribute.name.piercing_resistance", 0.0, -1024.0, 1024.0).setSyncable(true));

		OverhauledDamage.ADDITIONAL_SLASHING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("additional_slashing_damage"), new RangedAttribute("attribute.name.additional_slashing_damage", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.INCREASED_SLASHING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("increased_slashing_damage"), new RangedAttribute("attribute.name.increased_slashing_damage", 1.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.SLASHING_RESISTANCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("slashing_resistance"), new RangedAttribute("attribute.name.slashing_resistance", 0.0, -1024.0, 1024.0).setSyncable(true));

		OverhauledDamage.BLOCKED_PHYSICAL_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("blocked_physical_damage"), new RangedAttribute("attribute.name.blocked_physical_damage", 0.0, 0.0, 1024.0).setSyncable(true));

		OverhauledDamage.MAX_BLEEDING_BUILD_UP = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("max_bleeding_build_up"), new RangedAttribute("attribute.name.max_bleeding_build_up", 20.0, -1.0, 1024.0).setSyncable(true));
		OverhauledDamage.BLEEDING_DURATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("bleeding_duration"), new RangedAttribute("attribute.name.bleeding_duration", 201.0, 1.0, 1000000.0).setSyncable(true));
		OverhauledDamage.BLEEDING_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("bleeding_tick_threshold"), new RangedAttribute("attribute.name.bleeding_tick_threshold", 20.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("bleeding_build_up_reduction"), new RangedAttribute("attribute.name.bleeding_build_up_reduction", 1.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("bleeding_build_up_reduction_delay_threshold"), new RangedAttribute("attribute.name.bleeding_build_up_reduction_delay_threshold", 40.0, 0.0, 1024.0).setSyncable(true));

		OverhauledDamage.ADDITIONAL_FROST_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("additional_frost_damage"), new RangedAttribute("attribute.name.additional_frost_damage", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.INCREASED_FROST_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("increased_frost_damage"), new RangedAttribute("attribute.name.increased_frost_damage", 1.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.BLOCKED_FROST_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("blocked_frost_damage"), new RangedAttribute("attribute.name.blocked_frost_damage", 0.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.FROST_RESISTANCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("frost_resistance"), new RangedAttribute("attribute.name.frost_resistance", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.MAX_FREEZE_BUILD_UP = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("max_freeze_build_up"), new RangedAttribute("attribute.name.max_freeze_build_up", 20.0, -1.0, 1024.0).setSyncable(true));
		OverhauledDamage.FREEZE_DURATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("freeze_duration"), new RangedAttribute("attribute.name.freeze_duration", 200.0, 1.0, 1000000.0).setSyncable(true));
		OverhauledDamage.FREEZE_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("freeze_tick_threshold"), new RangedAttribute("attribute.name.freeze_tick_threshold", 20.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.FREEZE_BUILD_UP_REDUCTION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("freeze_build_up_reduction"), new RangedAttribute("attribute.name.freeze_build_up_reduction", 1.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.FREEZE_BUILD_UP_REDUCTION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("freeze_build_up_reduction_delay_threshold"), new RangedAttribute("attribute.name.freeze_build_up_reduction_delay_threshold", 40.0, 0.0, 1024.0).setSyncable(true));

		OverhauledDamage.ADDITIONAL_FIRE_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("additional_fire_damage"), new RangedAttribute("attribute.name.additional_fire_damage", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.INCREASED_FIRE_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("increased_fire_damage"), new RangedAttribute("attribute.name.increased_fire_damage", 1.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.BLOCKED_FIRE_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("blocked_fire_damage"), new RangedAttribute("attribute.name.blocked_fire_damage", 0.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.FIRE_RESISTANCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("fire_resistance"), new RangedAttribute("attribute.name.fire_resistance", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.MAX_BURN_BUILD_UP = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("max_burn_build_up"), new RangedAttribute("attribute.name.max_burn_build_up", 20.0, -1.0, 1024.0).setSyncable(true));
		OverhauledDamage.BURN_DURATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("burn_duration"), new RangedAttribute("attribute.name.burn_duration", 351.0, 1.0, 1000000.0).setSyncable(true));
		OverhauledDamage.BURN_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("burn_tick_threshold"), new RangedAttribute("attribute.name.burn_tick_threshold", 20.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.BURN_BUILD_UP_REDUCTION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("burn_build_up_reduction"), new RangedAttribute("attribute.name.burn_build_up_reduction", 1.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.BURN_BUILD_UP_REDUCTION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("burn_build_up_reduction_delay_threshold"), new RangedAttribute("attribute.name.burn_build_up_reduction_delay_threshold", 40.0, 0.0, 1024.0).setSyncable(true));

		OverhauledDamage.ADDITIONAL_LIGHTNING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("additional_lightning_damage"), new RangedAttribute("attribute.name.additional_lightning_damage", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.INCREASED_LIGHTNING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("increased_lightning_damage"), new RangedAttribute("attribute.name.increased_lightning_damage", 1.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.BLOCKED_LIGHTNING_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("blocked_lightning_damage"), new RangedAttribute("attribute.name.blocked_lightning_damage", 0.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.LIGHTNING_RESISTANCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("lightning_resistance"), new RangedAttribute("attribute.name.lightning_resistance", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.MAX_SHOCK_BUILD_UP = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("max_shock_build_up"), new RangedAttribute("attribute.name.max_shock_build_up", 20.0, -1.0, 1024.0).setSyncable(true));
		OverhauledDamage.SHOCK_DURATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("shock_duration"), new RangedAttribute("attribute.name.shock_duration", 1.0, 1.0, 1000000.0).setSyncable(true));
		OverhauledDamage.SHOCK_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("shock_tick_threshold"), new RangedAttribute("attribute.name.shock_tick_threshold", 20.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.SHOCK_BUILD_UP_REDUCTION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("shock_build_up_reduction"), new RangedAttribute("attribute.name.shock_build_up_reduction", 1.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.SHOCK_BUILD_UP_REDUCTION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("shock_build_up_reduction_delay_threshold"), new RangedAttribute("attribute.name.shock_build_up_reduction_delay_threshold", 40.0, 0.0, 1024.0).setSyncable(true));

		OverhauledDamage.ADDITIONAL_POISON_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("additional_poison_damage"), new RangedAttribute("attribute.name.additional_poison_damage", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.INCREASED_POISON_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("increased_poison_damage"), new RangedAttribute("attribute.name.increased_poison_damage", 1.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.BLOCKED_POISON_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("blocked_poison_damage"), new RangedAttribute("attribute.name.blocked_poison_damage", 0.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.POISON_RESISTANCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("poison_resistance"), new RangedAttribute("attribute.name.poison_resistance", 0.0, -1024.0, 1024.0).setSyncable(true));
		OverhauledDamage.MAX_POISON_BUILD_UP = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("max_poison_build_up"), new RangedAttribute("attribute.name.max_poison_build_up", 20.0, -1.0, 1024.0).setSyncable(true));
		OverhauledDamage.POISON_DURATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("poison_duration"), new RangedAttribute("attribute.name.poison_duration", 201.0, 1.0, 1000000.0).setSyncable(true));
		OverhauledDamage.POISON_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("poison_tick_threshold"), new RangedAttribute("attribute.name.poison_tick_threshold", 20.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.POISON_BUILD_UP_REDUCTION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("poison_build_up_reduction"), new RangedAttribute("attribute.name.poison_build_up_reduction", 1.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.POISON_BUILD_UP_REDUCTION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("poison_build_up_reduction_delay_threshold"), new RangedAttribute("attribute.name.poison_build_up_reduction_delay_threshold", 40.0, 0.0, 1024.0).setSyncable(true));

		OverhauledDamage.MAX_STAGGER_BUILD_UP = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("max_stagger_build_up"), new RangedAttribute("attribute.name.max_stagger_build_up", 20.0, -1.0, 1024.0).setSyncable(true));
		OverhauledDamage.STAGGER_DURATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("stagger_duration"), new RangedAttribute("attribute.name.stagger_duration", 200.0, 1.0, 1000000.0).setSyncable(true));
		OverhauledDamage.STAGGER_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("stagger_tick_threshold"), new RangedAttribute("attribute.name.stagger_tick_threshold", 20.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.STAGGER_BUILD_UP_REDUCTION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("stagger_build_up_reduction"), new RangedAttribute("attribute.name.stagger_build_up_reduction", 1.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.STAGGER_BUILD_UP_REDUCTION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("stagger_build_up_reduction_delay_threshold"), new RangedAttribute("attribute.name.stagger_build_up_reduction_delay_threshold", 40.0, 0.0, 1024.0).setSyncable(true));

		OverhauledDamage.DAMAGE_TAKEN_FROM_MANA_MULTIPLIER = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("damage_taken_from_mana_multiplier"), new RangedAttribute("attribute.name.damage_taken_from_mana_multiplier", 0.0, 0.0, 1024.0).setSyncable(true));
		OverhauledDamage.DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, OverhauledDamage.identifier("damage_taken_from_stamina_multiplier"), new RangedAttribute("attribute.name.damage_taken_from_stamina_multiplier", 0.0, 0.0, 1024.0).setSyncable(true));
	}
}
