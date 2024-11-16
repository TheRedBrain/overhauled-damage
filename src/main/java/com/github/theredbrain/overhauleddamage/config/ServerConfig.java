package com.github.theredbrain.overhauleddamage.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.LinkedHashMap;

@Config(
		name = "server"
)
public class ServerConfig implements ConfigData {
	@Comment("""
			When set to 'true', taking damage interrupts the using of items
			Does not apply when the player is blocking or when damage is in
			"is_true_damage" tag.
			""")
	public boolean damage_interrupts_item_usage = true;
	@Comment("Set to 'false' for the vanilla behaviour")
	public boolean disable_jump_crit_mechanic = true;
	@Comment("When set to 'true', blocking requires the player to have at least 1 stamina. Has no effect when 'Stamina Attributes' is not installed.")
	public boolean blocking_requires_stamina = true;
	@Comment("This status effect is applied when the bleeding build-up reaches the threshold")
	public String bleeding_status_effect_identifier = "variousstatuseffects:bleeding";
	@Comment("This status effect is applied when the burning build-up reaches the threshold")
	public String burning_status_effect_identifier = "variousstatuseffects:burning";
	@Comment("This status effect is applied when frost damage is received")
	public String chilled_status_effect_identifier = "variousstatuseffects:chilled";
	@Comment("This status effect is applied when the freeze build-up reaches the threshold")
	public String frozen_status_effect_identifier = "variousstatuseffects:frozen";
	@Comment("This status effect is applied when the poison build-up reaches the threshold")
	public String poison_status_effect_identifier = "variousstatuseffects:poison";
	@Comment("This status effect is applied when the shock build-up reaches the threshold")
	public String shocked_status_effect_identifier = "variousstatuseffects:shocked";
	@Comment("This status effect is applied when the stagger build-up reaches the threshold")
	public String staggered_status_effect_identifier = "variousstatuseffects:staggered";
	@Comment("This status effect prevents all fall damage")
	public String fall_immune_status_effect_identifier = "variousstatuseffects:fall_immune";
	@Comment("This status effect multiplies incoming damage based on its amplifier")
	public String calamity_status_effect_identifier = "variousstatuseffects:calamity";
	@Comment("""
			When damage is in the 'overhauleddamage:applies_bleeding' damage_type tag, the bleeding amount is calculated as the sum of the attack_type_amounts * their_respective_bleeding_multiplier.
						
			The array must contain exactly eight (8) values.
			They correspond to the attack types like so:
			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
			
			Default: [0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0, 0.0]
			""")
	public Float[] bleeding_multipliers = new Float[]{0.0F, 0.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F};
	@Comment("""
			The applied stagger is calculated as the sum of the attack_type_amounts * their_respective_stagger_multiplier.
						
			The array must contain exactly eight (8) values.
			They correspond to the attack types like so:
			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
			
			Default: [0.0, 0.75, 0.5, 0.5, 0.0, 0.0, 0.0, 0.5]
			""")
	public Float[] stagger_multipliers = new Float[]{0.0F, 0.75F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.5F};
	@Comment("""
			When set to 'true', blocking reduces damage by 1 point per blocked_damage attribute point.
			
			When set to false, this is changed to 1 percent per blocked_damage attribute point.
			
			Default: false
			""")
	public boolean blocked_damage_calculation_works_with_flat_values = false;
	@Comment("""
			When set to 'true', armor reduces damage by 1 point per armor point.
			
			When set to false, this is changed to 1 percent per armor point.
			
			In both cases armor toughness is a multiplier, with a suggested default of 1.0 (which would have no effect).
			(Overhauled Damage does not change the base value of the armor toughness attribute, this has to be done via other methods/mods)
			
			Default: false
			""")
	public boolean armor_calculation_works_with_flat_values = false;
	@Comment("""
			Damage reduction by armor is modified based on the attack type.
			Each fraction of damage is reduced individually.
						
			The array must contain exactly eight (8) values.
			They correspond to the attack types like so:
			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
			
			Default: [1.0, 1.0, 0.5, 1.5, 0.0, 1.0, 0.0, 0.0]
			""")
	public Float[] armor_multipliers = new Float[]{1.0F, 1.0F, 0.5F, 1.5F, 0.0F, 1.0F, 0.0F, 0.0F};
	@Comment("""
			When set to 'true', all protection enchantments except feather falling are disabled.
			
			Note that the 'normal' protection enchantment is still used by the damage calculation, but the specialised enchantments are disabled completely
			
			Default: true
			""")
	public boolean enable_protection_enchantment_override = true;
	@Comment("""
			The protection enchantment was changed to reduce damage by x percent per enchantment level, where x is defined here.
			
			Default: 2.0, meaning 2% reduction per level
			""")
	public double protection_damage_reduction_per_level = 2.0;
	@Comment("""
			Damage reduction by the protection enchantment is modified based on the attack type.
			Each fraction of damage is reduced individually.
						
			The array must contain exactly eight (8) values.
			They correspond to the attack types like so:
			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
			
			Default: [1.0, 1.0, 0.5, 0.6, 0.0, 1.0, 0.0, 0.0]
			Example: By default the slashing part of each attack is reduced by 1.2 % per enchantment level. (0.6 * 2%)
			""")
	public Float[] protection_multipliers = new Float[]{1.0F, 1.0F, 0.5F, 0.6F, 0.0F, 1.0F, 0.0F, 0.0F};
	@Comment("""
			Damage dealt to health is modified based on the attack type.
						
			The array must contain exactly eight (8) values.
			They correspond to the attack types like so:
			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
			
			Default: [1.0, 1.0, 1.0, 1.25, 0.0, 0.0, 0.0, 0.0]
			""")
	public Float[] applied_damage_multipliers = new Float[]{1.0F, 1.0F, 1.0F, 1.25F, 0.0F, 0.0F, 0.0F, 0.0F};
	@Comment("""
			Damage types in this map use the corresponding multipliers when calculating the different elemental and physical damage amounts inflicted by that damage type.
						
			The array must contain exactly eight (8) values.
			They correspond to the attack types like so:
			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
						
			Damage types not present here use the default of
			[1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
			""")
	public LinkedHashMap<String, Float[]> damage_type_multipliers = new LinkedHashMap<>() {{
		put("minecraft:arrow", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:cactus", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:falling_stalactite", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:fireball", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F});
		put("minecraft:hot_floor", new Float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F});
		put("minecraft:in_fire", new Float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F});
		put("minecraft:lightning_bolt", new Float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F});
		put("minecraft:mob_attack", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:stalagmite", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:sting", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:sweet_berry_bush", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:thorns", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("minecraft:trident", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("overhauleddamage:mob_bashing_damage_type", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("overhauleddamage:mob_piercing_damage_type", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
		put("overhauleddamage:mob_slashing_damage_type", new Float[]{0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F});
	}};

	public ServerConfig() {

	}
}
