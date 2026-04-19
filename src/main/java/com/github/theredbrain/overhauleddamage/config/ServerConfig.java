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

	public ValidatedBoolean disable_jump_crit_mechanic = new ValidatedBoolean(true);

	public ValidatedBoolean enable_overhauled_damage_calculation = new ValidatedBoolean(true);

	public DamageCalculation overhauled_damage_calculation = new DamageCalculation();

	public static class DamageCalculation extends ConfigSection {

		public DamageTypes damage_types = new DamageTypes();

		public static class DamageTypes extends ConfigSection {

			public ValidatedAny<DamageTypeMultipliers> default_damage_type_multipliers = new ValidatedAny<>(new DamageTypeMultipliers(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

			public ValidatedMap<String, DamageTypeMultipliers> damage_type_multipliers = new ValidatedMap<>(new HashMap<>() {{
				// bashing
				put("minecraft:fall",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:ender_pearl",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:fly_into_wall",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:falling_block",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:falling_anvil",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:mob_attack",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:mob_attack_no_aggro",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:player_attack",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:spit",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:wind_charge",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:thrown",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:explosion",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:player_explosion",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:bad_respawn_point",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:mace_smash",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("overhauleddamage:mob_bashing_damage_type",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				// piercing
				put("minecraft:cactus",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:sweet_berry_bush",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:stalagmite",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:falling_stalactite",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:spear",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:arrow",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:trident",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:mob_projectile",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:thorns",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("overhauleddamage:mob_piercing_damage_type",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				// slashing
				put("overhauleddamage:mob_slashing_damage_type",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				// fire
				put("minecraft:in_fire",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				put("minecraft:campfire",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				put("minecraft:on_fire",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				put("minecraft:lava",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				put("minecraft:hot_floor",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				// frost
				put("minecraft:freeze",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F));
				// lightning
				put("minecraft:lightning_bolt",
						new DamageTypeMultipliers(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
				// combinations
				put("minecraft:sting",
						new DamageTypeMultipliers(0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:fireworks",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				put("minecraft:fireball",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				put("minecraft:unattributed_fireball",
						new DamageTypeMultipliers(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F));
				put("minecraft:wither_skull",
						new DamageTypeMultipliers(0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				put("minecraft:sonic_boom",
						new DamageTypeMultipliers(0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
				/* use default:
				"minecraft:in_wall"
				"minecraft:cramming"
				"minecraft:drown"
				"minecraft:starve"
				"minecraft:out_of_world"
				"minecraft:generic"
				"minecraft:magic" TODO
				"minecraft:wither"
				"minecraft:dragon_breath" TODO
				"minecraft:dry_out"
				"minecraft:indirect_magic" TODO
				"minecraft:outside_border"
				"minecraft:generic_kill"
				 */
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

		public ValidatedBoolean enable_blocking_overhaul = new ValidatedBoolean(true);

		public BlockingOverhaul blocking_overhaul = new BlockingOverhaul();

		public static class BlockingOverhaul extends ConfigSection {

			public ValidatedBoolean blocked_damage_calculation_works_with_flat_values = new ValidatedBoolean(false);

			public ValidatedAny<AttackTypeMultipliers> negative_block_force_multipliers = new ValidatedAny<>(new AttackTypeMultipliers(0.0F, 0.75F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F));

		}

		public ValidatedBoolean enable_armor_overhaul = new ValidatedBoolean(true);

		public ArmorOverhaul armor_overhaul = new ArmorOverhaul();

		public static class ArmorOverhaul extends ConfigSection {

//			public ValidatedBoolean armor_calculation_works_with_flat_values = new ValidatedBoolean(false);

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

		public ValidatedBoolean damage_interrupts_item_usage = new ValidatedBoolean(true);

		public ValidatedBoolean enable_hit_stun_mechanic = new ValidatedBoolean(true);

		public HitStunMechanic hit_stun_mechanic = new HitStunMechanic();

		public static class HitStunMechanic extends ConfigSection {

			public ValidatedAny<HitStunSettings> default_hit_stun_settings = new ValidatedAny<>(new HitStunSettings(1, 0.0));

			public ValidatedIdentifier hit_stun_counter_attribute_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:max_stagger_build_up"));

			public ValidatedIdentifier hit_stun_status_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:hit_stun"));

			public ValidatedMap<String, HitStunSettings> hit_stun_settings = new ValidatedMap<>(new HashMap<>() {
			}, new ValidatedString(), new ValidatedAny<>(new HitStunSettings()));

			@Translation(prefix = "overhauleddamage.server.hit_stun_mechanic.hit_sun_setting")
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

		@Translation(prefix = "overhauleddamage.server.overhauled_damage_calculation.attack_type_multipliers")
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

	public BuildUpEffects build_up_effects = new BuildUpEffects();

	public static class BuildUpEffects extends ConfigSection {

		public BleedingSection bleeding_build_up = new BleedingSection();

		@Translation(prefix = "overhauleddamage.server.build_up_effect")
		public static class BleedingSection extends ConfigSection {

			public ValidatedIdentifier mob_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:bleeding"));
			public ValidatedBoolean duration_is_additive = new ValidatedBoolean(false);
			public ValidatedBoolean amplifier_is_additive = new ValidatedBoolean(false);
		}

		public BurnSection burn_build_up = new BurnSection();

		@Translation(prefix = "overhauleddamage.server.build_up_effect")
		public static class BurnSection extends ConfigSection {

			public ValidatedIdentifier mob_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:burning"));
			public ValidatedBoolean duration_is_additive = new ValidatedBoolean(true);
			public ValidatedBoolean amplifier_is_additive = new ValidatedBoolean(false);
		}

		public FreezeSection freeze_build_up = new FreezeSection();

		@Translation(prefix = "overhauleddamage.server.build_up_effect")
		public static class FreezeSection extends ConfigSection {

			@Translation(prefix = "overhauleddamage.server.freeze_build_up")
			public ValidatedIdentifier chilled_mob_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:chilled"));
			@Translation(prefix = "overhauleddamage.server.freeze_build_up")
			public double chilled_duration_multiplier = 1.0;
			@Translation(prefix = "overhauleddamage.server.freeze_build_up")
			public ValidatedBoolean chilled_duration_is_additive = new ValidatedBoolean(false);
			@Translation(prefix = "overhauleddamage.server.freeze_build_up")
			public ValidatedBoolean chilled_amplifier_is_additive = new ValidatedBoolean(false);
			public ValidatedIdentifier mob_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:frozen"));
			public ValidatedBoolean duration_is_additive = new ValidatedBoolean(false);
			public ValidatedBoolean amplifier_is_additive = new ValidatedBoolean(false);
		}

		public PoisonSection poison_build_up = new PoisonSection();

		@Translation(prefix = "overhauleddamage.server.build_up_effect")
		public static class PoisonSection extends ConfigSection {

			public ValidatedIdentifier mob_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:poison"));
			public ValidatedBoolean duration_is_additive = new ValidatedBoolean(false);
			public ValidatedBoolean amplifier_is_additive = new ValidatedBoolean(true);
		}

		public ShockSection shock_build_up = new ShockSection();

		@Translation(prefix = "overhauleddamage.server.build_up_effect")
		public static class ShockSection extends ConfigSection {

			public ValidatedIdentifier mob_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:shocked"));
			public ValidatedBoolean duration_is_additive = new ValidatedBoolean(false);
			public ValidatedBoolean amplifier_is_additive = new ValidatedBoolean(false);
		}

		public StaggerSection stagger_build_up = new StaggerSection();

		@Translation(prefix = "overhauleddamage.server.build_up_effect")
		public static class StaggerSection extends ConfigSection {

			public ValidatedIdentifier mob_effect_identifier = new ValidatedIdentifier(Identifier.parse("overhauleddamage:staggered"));
			public ValidatedBoolean duration_is_additive = new ValidatedBoolean(false);
			public ValidatedBoolean amplifier_is_additive = new ValidatedBoolean(false);
		}
	}

	public StatusEffects status_effects = new StatusEffects();

	public static class StatusEffects extends ConfigSection {

		public BleedingSection bleeding_effect = new BleedingSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class BleedingSection extends ConfigSection {
			public ValidatedInt tick_update_threshold = new ValidatedInt(20);
			@Translation(prefix = "overhauleddamage.server.bleeding_effect")
			public ValidatedBoolean moving_doubles_damage = new ValidatedBoolean(true);
			@Translation(prefix = "overhauleddamage.server.bleeding_effect")
			public ValidatedFloat max_health_multiplier = new ValidatedFloat(0.1F);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public BurningSection burning_effect = new BurningSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class BurningSection extends ConfigSection {
			public ValidatedInt tick_update_threshold = new ValidatedInt(50);
			@Translation(prefix = "overhauleddamage.server.burning_effect")
			public ValidatedFloat damage_per_tick = new ValidatedFloat(2.0F);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public ChilledSection chilled_effect = new ChilledSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class ChilledSection extends ConfigSection {
			public ValidatedDouble movement_speed_total_multiplier = new ValidatedDouble(-0.15);
			public ValidatedDouble attack_speed_total_multiplier = new ValidatedDouble(-0.15);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public FrozenSection frozen_effect = new FrozenSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class FrozenSection extends ConfigSection {
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public PoisonSection poison_effect = new PoisonSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class PoisonSection extends ConfigSection {
			public ValidatedInt tick_update_threshold = new ValidatedInt(25);
			@Translation(prefix = "overhauleddamage.server.poison_effect")
			public ValidatedFloat amplifier_multiplier = new ValidatedFloat(1.0F);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public ShockedSection shocked_effect = new ShockedSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class ShockedSection extends ConfigSection {
//			public ValidatedDouble additional_damage_taken = new ValidatedDouble(25.0);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public HitStunSection hit_stun_effect = new HitStunSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class HitStunSection extends ConfigSection {
//			public ValidatedDouble additional_roll_distance = new ValidatedDouble(0.0);
			public ValidatedDouble movement_speed_total_multiplier = new ValidatedDouble(-0.7);
			public ValidatedDouble attack_speed_total_multiplier = new ValidatedDouble(-0.1);
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}

		public StaggerSection stagger_effect = new StaggerSection();

		@Translation(prefix = "overhauleddamage.server.status_effect")
		public static class StaggerSection extends ConfigSection {
			public ValidatedColor effect_color = new ValidatedColor(1, 1, 1);
		}
	}

	public NaturalPlayerAttributeValuesSection natural_player_attribute_values = new NaturalPlayerAttributeValuesSection();

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