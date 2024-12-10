package com.github.theredbrain.overhauleddamage.config;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;

import java.util.HashMap;

@ConvertFrom(fileName = "server.json5", folder = "overhauleddamage")
public class ServerConfig extends Config {

	public ServerConfig() {
		super(OverhauledDamage.identifier("server"));
	}
//	@Comment("""
//			When set to 'true', taking damage interrupts the using of items
//			Does not apply when the player is blocking or when damage is in
//			"is_true_damage" tag.
//			""")
	public boolean damage_interrupts_item_usage = true;
//	@Comment("Set to 'false' for the vanilla behaviour")
	public boolean disable_jump_crit_mechanic = true;


	public DamageTypes damageTypes = new DamageTypes();

	public static class DamageTypes extends ConfigSection {
		//	@Comment("""
//			Damage types not present in the map below, use these multipliers
//
//			The array must contain exactly eight (8) values.
//			They correspond to the attack types like so:
//			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
//			""")
		public AttackTypeMultipliers default_damage_type_multipliers = new AttackTypeMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		//	@Comment("""
//			Damage types in this map use the corresponding multipliers when calculating the different elemental and physical damage amounts inflicted by that damage type.
//
//			The array must contain exactly eight (8) values.
//			They correspond to the attack types like so:
//			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
//			""")
		public ValidatedMap<String, AttackTypeMultipliers> damage_type_multipliers = new ValidatedMap<>(new HashMap<>() {{
			put("minecraft:arrow", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:cactus", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:falling_stalactite", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:fireball", new AttackTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
			put("minecraft:hot_floor", new AttackTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
			put("minecraft:in_fire", new AttackTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
			put("minecraft:lightning_bolt", new AttackTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
			put("minecraft:mob_attack", new AttackTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:stalagmite", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:sting", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:sweet_berry_bush", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:thorns", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:trident", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("overhauleddamage:mob_bashing_damage_type", new AttackTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("overhauleddamage:mob_piercing_damage_type", new AttackTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("overhauleddamage:mob_slashing_damage_type", new AttackTypeMultipliers(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		}}, new ValidatedString(), new ValidatedAny<>(new AttackTypeMultipliers()));
	}

	public DamageCalculation damageCalculation = new DamageCalculation();

	public static class DamageCalculation extends ConfigSection {

		public boolean enable_blocking_overhaul = false;
		public BlockingOverhaul blockingOverhaul = new BlockingOverhaul();

		public static class BlockingOverhaul extends ConfigSection {

			public boolean blocking_requires_stamina = true;
			public boolean blocked_damage_calculation_works_with_flat_values = false;
		}
		//	@Comment("When set to 'true', blocking requires the player to have at least 1 stamina. Has no effect when 'Stamina Attributes' is not installed.")
		//	@Comment("""
//			When damage is in the 'overhauleddamage:applies_bleeding' damage_type tag, the bleeding amount is calculated as the sum of the attack_type_amounts * their_respective_bleeding_multiplier.
//
//			The array must contain exactly eight (8) values.
//			They correspond to the attack types like so:
//			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
//
//			Default: [0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0, 0.0]
//			""")
		public AttackTypeMultipliers bleeding_multipliers = new AttackTypeMultipliers(0.0F, 0.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F);
		//	@Comment("""
//			The applied stagger is calculated as the sum of the attack_type_amounts * their_respective_stagger_multiplier.
//
//			The array must contain exactly eight (8) values.
//			They correspond to the attack types like so:
//			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
//
//			Default: [0.0, 0.75, 0.5, 0.5, 0.0, 0.0, 0.0, 0.5]
//			""")
		public AttackTypeMultipliers stagger_multipliers = new AttackTypeMultipliers(0.0F, 0.75F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.5F);
		//	@Comment("""
//			When set to 'true', blocking reduces damage by 1 point per blocked_damage attribute point.
//
//			When set to false, this is changed to 1 percent per blocked_damage attribute point.
//
//			Default: false
//			""")
		//	@Comment("""
//			When set to 'true', armor reduces damage by 1 point per armor point.
//
//			When set to false, this is changed to 1 percent per armor point.
//
//			Default: false
//			""")
		public boolean armor_calculation_works_with_flat_values = false;
		//	@Comment("""
//			When set to 'true', armor toughness is multiplying armor.
//
//			When set to 'false', armor toughness is ignored.
//
//			Default: false
//			""")
		public boolean enable_armor_toughness_attribute = false;
		//	@Comment("""
//			When set to 'true', "overhauleddamage:generic.damage_taken_multiplier" is multiplying every damage taken.
//
//			When set to 'false', "overhauleddamage:generic.damage_taken_multiplier" is ignored.
//
//			Default: true
//			""")
//		public boolean enable_damage_taken_multiplier_attribute = true; // TODO
		//	@Comment("""
//			Damage reduction by armor is modified based on the attack type.
//			Each fraction of damage is reduced individually.
//
//			The array must contain exactly eight (8) values.
//			They correspond to the attack types like so:
//			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
//
//			Default: [1.0, 1.0, 0.5, 1.5, 0.0, 1.0, 0.0, 0.0]
//			""")
		public AttackTypeMultipliers armor_multipliers = new AttackTypeMultipliers(1.0F, 1.0F, 0.5F, 1.5F, 0.0F, 1.0F, 0.0F, 0.0F);
		//	@Comment("""
//			When set to 'true', all protection enchantments except feather falling are disabled.
//
//			Note that the 'normal' protection enchantment is still used by the damage calculation, but the specialised enchantments are disabled completely
//
//			Default: true
//			""")
		public boolean enable_protection_enchantment_override = true;
		//	@Comment("""
//			The protection enchantment was changed to reduce damage by x percent per enchantment level, where x is defined here.
//
//			Default: 2.0, meaning 2% reduction per level
//			""")
		public double protection_damage_reduction_per_level = 2.0;
		//	@Comment("""
//			Damage reduction by the protection enchantment is modified based on the attack type.
//			Each fraction of damage is reduced individually.
//
//			The array must contain exactly eight (8) values.
//			They correspond to the attack types like so:
//			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
//
//			Default: [1.0, 1.0, 0.5, 0.6, 0.0, 1.0, 0.0, 0.0]
//			Example: By default the slashing part of each attack is reduced by 1.2 % per enchantment level. (0.6 * 2%)
//			""")
		public AttackTypeMultipliers protection_multipliers = new AttackTypeMultipliers(1.0F, 1.0F, 0.5F, 0.6F, 0.0F, 1.0F, 0.0F, 0.0F);
		//	@Comment("""
//			Damage dealt to health is modified based on the attack type.
//
//			The array must contain exactly eight (8) values.
//			They correspond to the attack types like so:
//			{generic, bashing, piercing, slashing, poison, fire, frost, lightning}
//
//			Default: [1.0, 1.0, 1.0, 1.25, 0.0, 0.0, 0.0, 0.0]
//			""")
		public AttackTypeMultipliers applied_damage_multipliers = new AttackTypeMultipliers(1.0F, 1.0F, 1.0F, 1.25F, 0.0F, 0.0F, 0.0F, 0.0F);
		//	@Comment("""
//			Enables the debug lines for the damage calculation. This can be helpful when testing / balancing.
//			It will log calculation details EVERYTIME ANY ENTITY GETS HIT, so use this with care.
//			""")
		public boolean enable_debug_log = false;
	}

	public BuildUpEffects buildUpEffects = new BuildUpEffects();

	public static class BuildUpEffects extends ConfigSection {
//		@Comment("This status effect is applied when the bleeding build-up reaches the threshold")
		public String bleeding_status_effect_identifier = "variousstatuseffects:bleeding";
//		@Comment("This status effect is applied when the burning build-up reaches the threshold")
		public String burning_status_effect_identifier = "variousstatuseffects:burning";
//		@Comment("This status effect is applied when frost damage is received")
		public String chilled_status_effect_identifier = "variousstatuseffects:chilled";
//		@Comment("This status effect is applied when the freeze build-up reaches the threshold")
		public String frozen_status_effect_identifier = "variousstatuseffects:frozen";
//		@Comment("This status effect is applied when the poison build-up reaches the threshold")
		public String poison_status_effect_identifier = "variousstatuseffects:poison";
//		@Comment("This status effect is applied when the shock build-up reaches the threshold")
		public String shocked_status_effect_identifier = "variousstatuseffects:shocked";
//		@Comment("This status effect is applied when the stagger build-up reaches the threshold")
		public String staggered_status_effect_identifier = "variousstatuseffects:staggered";
	}

	@Translation(prefix = "overhauleddamage.server.attack_type_multipliers")
	public static class AttackTypeMultipliers implements Walkable {
		
		public AttackTypeMultipliers() {
			new AttackTypeMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		}
		public AttackTypeMultipliers(float generic, float bashing, float piercing, float slashing, float poison, float fire, float frost, float lightning) {
			this.generic = generic;
			this.bashing = bashing;
			this.piercing = piercing;
			this.slashing = slashing;
			this.poison = poison;
			this.fire = fire;
			this.frost = frost;
			this.lightning = lightning;
		}
		public float generic;
		public float bashing;
		public float piercing;
		public float slashing;
		public float poison;
		public float fire;
		public float frost;
		public float lightning;
	}
}
