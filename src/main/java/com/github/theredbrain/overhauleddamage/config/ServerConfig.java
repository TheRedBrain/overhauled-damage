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
	public boolean damage_interrupts_item_usage = true;

	public boolean disable_jump_crit_mechanic = true;

	public DamageTypes damageTypes = new DamageTypes();

	public static class DamageTypes extends ConfigSection {

		public AttackTypeMultipliers default_damage_type_multipliers = new AttackTypeMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

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

		public boolean blocking_requires_stamina = true;

		public boolean enable_blocking_overhaul = true;

		public BlockingOverhaul blockingOverhaul = new BlockingOverhaul();

		public static class BlockingOverhaul extends ConfigSection {

			public boolean blocked_damage_calculation_works_with_flat_values = false;
		}

		public boolean enable_protection_overhaul = true;

		public ProtectionOverhaul protectionOverhaul = new ProtectionOverhaul();

		public static class ProtectionOverhaul extends ConfigSection {

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

		}

		public boolean enable_armor_overhaul = true;

		public ArmorOverhaul armorOverhaul = new ArmorOverhaul();

		public static class ArmorOverhaul extends ConfigSection {

			public boolean armor_calculation_works_with_flat_values = false;

			public boolean enable_armor_toughness_attribute = false;

			public AttackTypeMultipliers armor_multipliers = new AttackTypeMultipliers(1.0F, 1.0F, 0.5F, 1.5F, 0.0F, 1.0F, 0.0F, 0.0F);

		}

		//	@Comment("""
//			When set to 'true', "overhauleddamage:generic.damage_taken_multiplier" is multiplying every damage taken.
//
//			When set to 'false', "overhauleddamage:generic.damage_taken_multiplier" is ignored.
//
//			Default: true
//			""")
//		public boolean enable_damage_taken_multiplier_attribute = true; // TODO

		public AttackTypeMultipliers bleeding_multipliers = new AttackTypeMultipliers(0.0F, 0.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F);

		public AttackTypeMultipliers stagger_multipliers = new AttackTypeMultipliers(0.0F, 0.75F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.5F);

		public AttackTypeMultipliers applied_damage_multipliers = new AttackTypeMultipliers(1.0F, 1.0F, 1.0F, 1.25F, 0.0F, 0.0F, 0.0F, 0.0F);

		public boolean enable_debug_log = false;
	}

	public BuildUpEffects buildUpEffects = new BuildUpEffects();

	public static class BuildUpEffects extends ConfigSection {
		public String bleeding_status_effect_identifier = "variousstatuseffects:bleeding";
		public boolean should_bleeding_duration_be_additive = false;
		public boolean should_bleeding_amplifier_be_additive = false;
		public String burn_status_effect_identifier = "variousstatuseffects:burning";
		public boolean should_burn_duration_be_additive = true;
		public boolean should_burn_amplifier_be_additive = false;
		public String chilled_status_effect_identifier = "variousstatuseffects:chilled";
		public double chilled_duration_multiplier = 1.0;
		public boolean should_chilled_duration_be_additive = false;
		public boolean should_chilled_amplifier_be_additive = false;
		public String freeze_status_effect_identifier = "variousstatuseffects:frozen";
		public boolean should_freeze_duration_be_additive = false;
		public boolean should_freeze_amplifier_be_additive = false;
		public String poison_status_effect_identifier = "variousstatuseffects:poison";
		public boolean should_poison_duration_be_additive = false;
		public boolean should_poison_amplifier_be_additive = true;
		public String shock_status_effect_identifier = "variousstatuseffects:shocked";
		public boolean should_shock_duration_be_additive = false;
		public boolean should_shock_amplifier_be_additive = false;
		public String stagger_status_effect_identifier = "variousstatuseffects:staggered";
		public boolean should_stagger_duration_be_additive = false;
		public boolean should_stagger_amplifier_be_additive = false;
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

		public String toString() {
			return "generic: " + this.generic + ", bashing: " + this.bashing + ", piercing: " + this.piercing + ", slashing: " + this.slashing + ", poison: " + this.poison + ", fire: " + this.fire + ", frost: " + this.frost + ", lightning: " + this.lightning;
		}
	}
}
