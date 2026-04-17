package com.github.theredbrain.overhauleddamage.config;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.Identifier;
import java.util.HashMap;

@ConvertFrom(fileName = "server.json5", folder = "overhauleddamage")
public class ServerConfig extends Config {

	public ServerConfig() {
		super(OverhauledDamage.identifier("server"));
	}

	public ValidatedBoolean damage_interrupts_item_usage = new ValidatedBoolean(true);

	public ValidatedBoolean enable_hit_stun_mechanic = new ValidatedBoolean(true);

	public HitStun hitStun = new HitStun();

	public static class HitStun extends ConfigSection {

		public ValidatedAny<HitStunSettings> default_hit_stun_settings = new ValidatedAny<>(new HitStunSettings(1, 0.0));

		public ValidatedIdentifier attribute = new ValidatedIdentifier(Identifier.parse("overhauleddamage:max_stagger_build_up"));

		public ValidatedIdentifier hit_stun_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:hit_stun"));

		public ValidatedMap<String, HitStunSettings> hit_stun_settings = new ValidatedMap<>(new HashMap<>() {{
			put("overhauleddamage:mob_slashing_damage_type", new HitStunSettings(0, 0.0));
		}}, new ValidatedString(), new ValidatedAny<>(new HitStunSettings()));

		@Translation(prefix = "overhauleddamage.server.hit_sun_settings")
		public static class HitStunSettings implements Walkable {

			public HitStunSettings() {
				new HitStunSettings(1, 0.0);
			}

			public HitStunSettings(int duration, double required_attribute_threshold) {
				this.duration = duration;
				this.required_attribute_threshold = required_attribute_threshold;
			}

			public int duration;
			public double required_attribute_threshold;

			public String toString() {
				return "duration: " + this.duration + ", required_attribute_threshold: " + this.required_attribute_threshold;
			}
		}
	}

	public ValidatedBoolean disable_jump_crit_mechanic = new ValidatedBoolean(true);

	public DamageTypes damageTypes = new DamageTypes();

	public static class DamageTypes extends ConfigSection {

		public ValidatedAny<DamageTypeMultipliers> default_damage_type_multipliers = new ValidatedAny<>(new DamageTypeMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		public ValidatedMap<String, DamageTypeMultipliers> damage_type_multipliers = new ValidatedMap<>(new HashMap<>() {{
			put("minecraft:arrow", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:cactus", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:falling_stalactite", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:fireball", new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
			put("minecraft:hot_floor", new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
			put("minecraft:in_fire", new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
			put("minecraft:lightning_bolt", new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
			put("minecraft:mob_attack", new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:stalagmite", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:sting", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:sweet_berry_bush", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:thorns", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("minecraft:trident", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("overhauleddamage:mob_bashing_damage_type", new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("overhauleddamage:mob_piercing_damage_type", new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
			put("overhauleddamage:mob_slashing_damage_type", new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		}}, new ValidatedString(), new ValidatedAny<>(new DamageTypeMultipliers()));

		@Translation(prefix = "overhauleddamage.server.attack_type_multipliers")
		public static class DamageTypeMultipliers implements Walkable {

			public DamageTypeMultipliers() {
				new DamageTypeMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			}

			public DamageTypeMultipliers(float generic, float bashing, float piercing, float slashing, float poison, float fire, float frost, float lightning) {
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

	public ValidatedBoolean enable_overhauled_damage_calculation = new ValidatedBoolean(true);

	public DamageCalculation damageCalculation = new DamageCalculation();

	public static class DamageCalculation extends ConfigSection {

		public ValidatedBoolean enable_blocking_overhaul = new ValidatedBoolean(true);

		public BlockingOverhaul blockingOverhaul = new BlockingOverhaul();

		public static class BlockingOverhaul extends ConfigSection {

			public ValidatedBoolean blocked_damage_calculation_works_with_flat_values = new ValidatedBoolean(false);

			public ValidatedAny<AttackTypeMultipliers> negative_block_force_multipliers = new ValidatedAny<>(new AttackTypeMultipliers(0.0F, 0.75F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F));

		}

		public ValidatedBoolean enable_protection_overhaul = new ValidatedBoolean(true);

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
			public ValidatedAny<ProtectionMultipliers> protection_multipliers = new ValidatedAny<>(new ProtectionMultipliers(1.0F, 1.0F, 0.5F, 0.6F, 0.0F, 1.0F, 0.0F, 0.0F));


			@Translation(prefix = "overhauleddamage.server.attack_type_multipliers")
			public static class ProtectionMultipliers implements Walkable {

				public ProtectionMultipliers() {
					new ProtectionMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				}

				public ProtectionMultipliers(float generic, float bashing, float piercing, float slashing, float poison, float fire, float frost, float lightning) {
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

		public ValidatedBoolean enable_armor_overhaul = new ValidatedBoolean(true);

		public ArmorOverhaul armorOverhaul = new ArmorOverhaul();

		public static class ArmorOverhaul extends ConfigSection {

			public ValidatedBoolean armor_calculation_works_with_flat_values = new ValidatedBoolean(false);

			public ValidatedBoolean enable_armor_toughness_attribute = new ValidatedBoolean(false);

			public ValidatedAny<ArmorMultipliers> armor_multipliers = new ValidatedAny<>(new ArmorMultipliers(1.0F, 1.0F, 0.5F, 1.5F, 0.0F, 1.0F, 0.0F, 0.0F));


			@Translation(prefix = "overhauleddamage.server.attack_type_multipliers")
			public static class ArmorMultipliers implements Walkable {

				public ArmorMultipliers() {
					new ArmorMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				}

				public ArmorMultipliers(float generic, float bashing, float piercing, float slashing, float poison, float fire, float frost, float lightning) {
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

		//	@Comment("""
//			When set to 'true', "overhauleddamage:generic.damage_taken_multiplier" is multiplying every damage taken.
//
//			When set to 'false', "overhauleddamage:generic.damage_taken_multiplier" is ignored.
//
//			Default: true
//			""")
//		public boolean enable_damage_taken_multiplier_attribute = true; // TODO

		public ValidatedAny<AttackTypeMultipliers> bleeding_multipliers = new ValidatedAny<>(new AttackTypeMultipliers(0.0F, 0.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F));

		public ValidatedAny<AttackTypeMultipliers> stagger_multipliers = new ValidatedAny<>(new AttackTypeMultipliers(0.0F, 0.75F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.5F));

		public ValidatedAny<AttackTypeMultipliers> applied_damage_multipliers = new ValidatedAny<>(new AttackTypeMultipliers(1.0F, 1.0F, 1.0F, 1.25F, 0.0F, 0.0F, 0.0F, 0.0F));

		public ValidatedBoolean enable_debug_log = new ValidatedBoolean(false);

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

	public BuildUpEffects buildUpEffects = new BuildUpEffects();

	public static class BuildUpEffects extends ConfigSection {
		public ValidatedIdentifier bleeding_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:bleeding"));
		public ValidatedBoolean should_bleeding_duration_be_additive = new ValidatedBoolean(false);
		public ValidatedBoolean should_bleeding_amplifier_be_additive = new ValidatedBoolean(false);
		public ValidatedIdentifier burn_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:burning"));
		public ValidatedBoolean should_burn_duration_be_additive = new ValidatedBoolean(true);
		public ValidatedBoolean should_burn_amplifier_be_additive = new ValidatedBoolean(false);
		public ValidatedIdentifier chilled_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:chilled"));
		public double chilled_duration_multiplier = 1.0;
		public ValidatedBoolean should_chilled_duration_be_additive = new ValidatedBoolean(false);
		public ValidatedBoolean should_chilled_amplifier_be_additive = new ValidatedBoolean(false);
		public ValidatedIdentifier freeze_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:frozen"));
		public ValidatedBoolean should_freeze_duration_be_additive = new ValidatedBoolean(false);
		public ValidatedBoolean should_freeze_amplifier_be_additive = new ValidatedBoolean(false);
		public ValidatedIdentifier poison_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:poison"));
		public ValidatedBoolean should_poison_duration_be_additive = new ValidatedBoolean(false);
		public ValidatedBoolean should_poison_amplifier_be_additive = new ValidatedBoolean(true);
		public ValidatedIdentifier shock_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:shocked"));
		public ValidatedBoolean should_shock_duration_be_additive = new ValidatedBoolean(false);
		public ValidatedBoolean should_shock_amplifier_be_additive = new ValidatedBoolean(false);
		public ValidatedIdentifier stagger_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:staggered"));
		public ValidatedBoolean should_stagger_duration_be_additive = new ValidatedBoolean(false);
		public ValidatedBoolean should_stagger_amplifier_be_additive = new ValidatedBoolean(false);
	}

	public StatusEffects status_effects = new StatusEffects();

	public static class StatusEffects extends ConfigSection {

		public BleedingSection bleeding_effect = new BleedingSection();

		public static class BleedingSection extends ConfigSection {
			public ValidatedInt tick_update_threshold = new ValidatedInt(20);
			public ValidatedBoolean moving_doubles_damage = new ValidatedBoolean(true);
			public ValidatedFloat max_health_multiplier = new ValidatedFloat(0.1F);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public BurningSection burning_effect = new BurningSection();

		public static class BurningSection extends ConfigSection {
			public ValidatedInt tick_update_threshold = new ValidatedInt(50);
			public ValidatedFloat damage_per_tick = new ValidatedFloat(2.0F);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public ChilledSection chilled_effect = new ChilledSection();

		public static class ChilledSection extends ConfigSection {
			public ValidatedDouble movement_speed_total_multiplier = new ValidatedDouble(-0.15);
			public ValidatedDouble attack_speed_total_multiplier = new ValidatedDouble(-0.15);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public FrozenSection frozen_effect = new FrozenSection();

		public static class FrozenSection extends ConfigSection {
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public PoisonSection poison_effect = new PoisonSection();

		public static class PoisonSection extends ConfigSection {
			public ValidatedInt tick_update_threshold = new ValidatedInt(25);
			public ValidatedFloat amplifier_multiplier = new ValidatedFloat(1.0F);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public ShockedSection shocked_effect = new ShockedSection();

		public static class ShockedSection extends ConfigSection {
//			public ValidatedDouble additional_damage_taken = new ValidatedDouble(25.0);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public HitStunSection hit_stun_effect = new HitStunSection();

		public static class HitStunSection extends ConfigSection {
//			public ValidatedDouble additional_roll_distance = new ValidatedDouble(0.0);
			public ValidatedDouble movement_speed_total_multiplier = new ValidatedDouble(-0.7);
			public ValidatedDouble attack_speed_total_multiplier = new ValidatedDouble(-0.1);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public StaggerSection stagger_effect = new StaggerSection();

		public static class StaggerSection extends ConfigSection {
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}
	}

	public NaturalPlayerAttributeValuesSection naturalPlayerAttributeValues = new NaturalPlayerAttributeValuesSection();

	public static class NaturalPlayerAttributeValuesSection extends ConfigSection {
		public ValidatedDouble natural_armor_toughness = new ValidatedDouble(0.0, 1024.0, 0.0);
		public ValidatedDouble natural_additional_bashing_damage = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_increased_bashing_damage = new ValidatedDouble(1.0, 1024.0, -1024.0);
		public ValidatedDouble natural_bashing_resistance = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_additional_piercing_damage = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_increased_piercing_damage = new ValidatedDouble(1.0, 1024.0, -1024.0);
		public ValidatedDouble natural_piercing_resistance = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_additional_slashing_damage = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_increased_slashing_damage = new ValidatedDouble(1.0, 1024.0, -1024.0);
		public ValidatedDouble natural_slashing_resistance = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_blocked_physical_damage = new ValidatedDouble(0.0, 1024.0, 0.0);
		public ValidatedDouble natural_max_bleeding_build_up = new ValidatedDouble(20.0, 1024.0, -1.0);
		public ValidatedDouble natural_bleeding_duration = new ValidatedDouble(201.0, 1000000.0, 1.0);
		public ValidatedDouble natural_bleeding_tick_threshold = new ValidatedDouble(20.0, 1024.0, 0.0);
		public ValidatedDouble natural_bleeding_build_up_reduction = new ValidatedDouble(1.0, 1024.0, 0.0);
		public ValidatedDouble natural_bleeding_build_up_reduction_delay_threshold = new ValidatedDouble(40.0, 1024.0, 0.0);
		public ValidatedDouble natural_additional_frost_damage = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_increased_frost_damage = new ValidatedDouble(1.0, 1024.0, -1024.0);
		public ValidatedDouble natural_blocked_frost_damage = new ValidatedDouble(0.0, 1024.0, 0.0);
		public ValidatedDouble natural_frost_resistance = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_max_freeze_build_up = new ValidatedDouble(20.0, 1024.0, -1.0);
		public ValidatedDouble natural_freeze_duration = new ValidatedDouble(200.0, 1000000.0, 1.0);
		public ValidatedDouble natural_freeze_tick_threshold = new ValidatedDouble(20.0, 1024.0, 0.0);
		public ValidatedDouble natural_freeze_build_up_reduction = new ValidatedDouble(1.0, 1024.0, 0.0);
		public ValidatedDouble natural_freeze_build_up_reduction_delay_threshold = new ValidatedDouble(40.0, 1024.0, 0.0);
		public ValidatedDouble natural_additional_fire_damage = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_increased_fire_damage = new ValidatedDouble(1.0, 1024.0, -1024.0);
		public ValidatedDouble natural_blocked_fire_damage = new ValidatedDouble(0.0, 1024.0, 0.0);
		public ValidatedDouble natural_fire_resistance = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_max_burn_build_up = new ValidatedDouble(20.0, 1024.0, -1.0);
		public ValidatedDouble natural_burn_duration = new ValidatedDouble(351.0, 1000000.0, 1.0);
		public ValidatedDouble natural_burn_tick_threshold = new ValidatedDouble(20.0, 1024.0, 0.0);
		public ValidatedDouble natural_burn_build_up_reduction = new ValidatedDouble(1.0, 1024.0, 0.0);
		public ValidatedDouble natural_burn_build_up_reduction_delay_threshold = new ValidatedDouble(40.0, 1024.0, 0.0);
		public ValidatedDouble natural_additional_lightning_damage = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_increased_lightning_damage = new ValidatedDouble(1.0, 1024.0, -1024.0);
		public ValidatedDouble natural_blocked_lightning_damage = new ValidatedDouble(0.0, 1024.0, 0.0);
		public ValidatedDouble natural_lightning_resistance = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_max_shock_build_up = new ValidatedDouble(20.0, 1024.0, -1.0);
		public ValidatedDouble natural_shock_duration = new ValidatedDouble(10.0, 1000000.0, 1.0);
		public ValidatedDouble natural_shock_tick_threshold = new ValidatedDouble(20.0, 1024.0, 0.0);
		public ValidatedDouble natural_shock_build_up_reduction = new ValidatedDouble(1.0, 1024.0, 0.0);
		public ValidatedDouble natural_shock_build_up_reduction_delay_threshold = new ValidatedDouble(40.0, 1024.0, 0.0);
		public ValidatedDouble natural_additional_poison_damage = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_increased_poison_damage = new ValidatedDouble(1.0, 1024.0, -1024.0);
		public ValidatedDouble natural_blocked_poison_damage = new ValidatedDouble(0.0, 1024.0, 0.0);
		public ValidatedDouble natural_poison_resistance = new ValidatedDouble(0.0, 1024.0, -1024.0);
		public ValidatedDouble natural_max_poison_build_up = new ValidatedDouble(20.0, 1024.0, -1.0);
		public ValidatedDouble natural_poison_duration = new ValidatedDouble(201.0, 1000000.0, 1.0);
		public ValidatedDouble natural_poison_tick_threshold = new ValidatedDouble(20.0, 1024.0, 0.0);
		public ValidatedDouble natural_poison_build_up_reduction = new ValidatedDouble(1.0, 1024.0, 0.0);
		public ValidatedDouble natural_poison_build_up_reduction_delay_threshold = new ValidatedDouble(40.0, 1024.0, 0.0);
		public ValidatedDouble natural_max_stagger_build_up = new ValidatedDouble(20.0, 1024.0, -1.0);
		public ValidatedDouble natural_stagger_duration = new ValidatedDouble(200.0, 1000000.0, 1.0);
		public ValidatedDouble natural_stagger_tick_threshold = new ValidatedDouble(20.0, 1024.0, 0.0);
		public ValidatedDouble natural_stagger_build_up_reduction = new ValidatedDouble(1.0, 1024.0, 0.0);
		public ValidatedDouble natural_stagger_build_up_reduction_delay_threshold = new ValidatedDouble(40.0, 1024.0, 0.0);
		public ValidatedDouble natural_damage_taken_from_mana_multiplier = new ValidatedDouble(0.0, 1024.0, 0.0);
		public ValidatedDouble natural_damage_taken_from_stamina_multiplier = new ValidatedDouble(0.0, 1024.0, 0.0);
	}
}