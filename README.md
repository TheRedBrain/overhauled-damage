# Overhauled Damage

A complete overhaul to several mechanics revolving around damage. It is inspired by games like Dark Souls and Valheim and aims to deepen Minecraft's combat.

# Effect Build-Ups

Effect Build-Ups are a resource (like health) and are zero by default. Certain events (like taking damage) increase or reduce these resources. Build-Ups are also slowly reduced over time.

If a build-up reaches a certain threshold, the corresponding status effect is applied and the build-up is set to zero again.

Currently, there are the following effect build-ups:
- Bleeding
- Burning
- Freezing
- Poisoned
- Shocked
- Staggered

Effect Build-ups are displayed in the HUD.

> Almost all aspects of this system are configurable via entity attributes, config files and/or data packs.

# Status Effects

Overhauled Damage adds several status effects that are used by the effect build-ups. They can be configured in the server config.

Some of these effects currently don't have a gameplay effect, since the required third-party APIs are not updated yet.

The Effect Build-Ups can also be configured to use other effects.

The status effects added by Overhauled Damage are:
- "overhauleddamage:bleeding", deals a percentage amount of the entities maximum health as damage. The damage is doubled when the entity is moving.
- "overhauleddamage:burning", deals damage. This effect attempts to recreate the hardcoded "on_fire" entity flag as a status effect.
- "overhauleddamage:chilled", reduces attack and movement speed. 
- "overhauleddamage:frozen", has currently no gameplay effect. Is planned to immobilise the entity and prevent any actions. Currently waiting on third-party mods to update.
- "overhauleddamage:poison", a configurable version of the vanilla status effect. Unlike it's vanilla counter-part, this effect can kill.
- "overhauleddamage:shocked", has currently no gameplay effect. Is planned to increase damage dealt to the entity. Currently waiting on third-party mods to update.
- "overhauleddamage:staggered", has currently no gameplay effect. Is planned to immobilise the entity and prevent any actions. Currently waiting on third-party mods to update.
- "overhauleddamage:hit_stun", reduces attack and movement speed.

# Overhauled Damage Calculation

Every instance of damage that is dealt to an entity in Minecraft has a "damage type". This defines the death message and mechanics like resistances.

Overhauled Damage expands this system.

When an entity is damaged, this no longer just reduces health. Depending on the damage type, there can be a variety of effects. This can still include health reduction, but it can also be effect build-up increases and even mana/stamina can be modified.

What exactly happens, is determined by the damage type or more precisely, to which category (or attack type) the damage type belongs.

## Attack Types

Each "damage type" belongs to one or more categories (called "attack types"). This is defined by a list of float values (called multipliers), one for each attack type.

The attack types are:
- Generic
- Bashing
- Piercing
- Slashing
- Poison
- Fire
- Frost
- Lightning

By default, every damage type has a multiplier of 1.0 for Generic and 0.0 for the rest. Exceptions to this are defined in the server config.


The first step of the new damage calculation is to determine the amount of each attack type, using the damage amount that was dealt to the entity.

> attack_type_amount = damage_amount * attack_type_multiplier

### True Damage

Damage types in the "overhauleddamage:is_true_damage" damage type tag will ignore the custom damage calculation. They are not affected by shield blocking, armor or resistance. They don't apply Effect Build-Ups. They just deal damage to health (and/or mana/stamina, if applicable).

---

## Blocked Damage

This part of the calculation only happens if:
- the attacked entity is currently blocking with a shield
- the damage type can be blocked by the equipped shield (determined by the "minecraft:blocks_attacks" data component)
- the mod "Blocking Overhaul" is installed
- Overhauled Damage's "Blocking Overhaul" version is enabled in the server config
- if "Stamina Attributes" is installed, the entity also has to have at least one stamina (or the "blocking_requires_stamina" server config has to be set to false)

### Parrying

Parrying replaces normal blocking and increases the parry_multiplier if certain conditions are met:
- the entity is in the "blockingoverhaul:can_parry" entity type tag
- the blocking time (the amount of ticks since the blocking was started) is equal or lower than the entity's "blockingoverhaul:parry_window" attribute
- the attack is caused by a LivingEntity
- the item used for blocking has the "blockingoverhaul:parries_attacks" data component

> parry_multiplier = (if parrying) ? ("blockingoverhaul:parry_multiplier" attribute) : 1

### Stamina Cost Application

If Stamina Attributes is installed, the stamina cost for blocking/parrying an attack is applied. If the entity's stamina is lower than zero after that, the attempt to block/parry the attack fails and the damage amount is not reduced.

### Blocked Damage Calculation

The amount of blocked damage is no longer determined by the "minecraft:blocks_attacks" data component, but is now depending on the damage_type (more specifically on its attack_types) of the attack and on several entity attributes. There are 2 different modes, flat damage reduction and percentage based damage reduction.

- Flat Value Mode:
> blocked_attack_type_amount = blocked_attack_type_damage_attribute * parry_multiplier

- Percentage Based Mode:
> blocked_attack_type_amount = attack_type_amount * blocked_attack_type_damage_attribute * parry_multiplier / 100

### Stagger Application

The sum of all attack_type_stagger_amount is then applied to the stagger build-up of the attacked entity. If the entity gets staggered by this, then the block/parry attempt fails and no damage is blocked.

> attack_type_stagger_amount = (attack_type_amount - blocked_attack_type_amount) * attack_type_stagger_multiplier

### Damage Reduction

If the blocking/parrying attempt was successful, then the attack amount is reduced.

> new_attack_type_amount = old_attack_type_amount - blocked_attack_type_amount

### Additional Effects

If the attack had an attacker, then blocking/parrying has additional consequences. If the attack was parried, then the attacker gets staggered instantly.

If the attack was blocked normally, then either the attacking or the defending entity are knocked back by a variable amount.

> additional_attack_type_knockback = old_attack_type_amount * attack_type_negative_block_force_multiplier

> applied_knock_back = ((defender's "blockingoverhaul:block_force" attribute) * parry_multiplier) - (attacker's "minecraft:attack_knockback" attribute) + (sum of all additional_attack_type_knockback) * total_applied_blocking_knockback_multiplier_config_option

If applied_knock_back is greater zero, it is applied to the attacking entity.

If applied_knock_back is lesser zero, its absolute value is applied to the blocking entity.

---

## Damage Reduction by Armor

The remaining attack type amounts are reduced by armor, using the following formula:

> effective_armor = armor_attribute * armor_toughness_attribute (This can be disabled in the server config)

> attack_type_armor_damage = attack_type_amount * effective_armor * attack_type_armor_multiplier / 100;

> attack_type_amount = attack_type_amount - attack_type_armor_damage

The sum of all attack_type_armor_damage is then used to damage the entities armor.

---

## Damage Reduction by Resistances

Each attack type has a corresponding resistance attribute. The remaining attack type amounts are reduced by resistance using the following formula:

> attack_type_amount = attack_type_amount - (attack_type_amount * attack_type_resistance_attribute) / 100;

---

## Effects of the Attack

The remaining attack type amounts apply their effects to the attacked entity.

### Bleeding Build-Up

If the damage type is in the "overhauleddamage:applies_bleeding" damage type tag, the attack can apply bleeding build-up. The amount is calculated by multiplying each attack type amount with a configurable multiplier and adding up the results.

### Burn Build-Up

The applied burn build-up is simply the fire_amount.

### Chilled Effect and Freeze Build-Up

Frost damage applies the Chilled effect. The effect duration is the frost_amount * a configurable multiplier.

The applied freeze build-up is simply the frost_amount.

### Stagger Build-Up

If no attempt was made to block the attack with a shield, then stagger build-up is applied now. The amount is calculated by multiplying each attack type amount with a configurable multiplier and adding up the results.

### Poison Build-Up

The applied poison build-up is simply the poison_amount.

### Shock Build-Up

The applied shock build-up is simply the lightning_amount.

### Damage applied to health/mana/stamina

The amount of "applied damage" is calculated by multiplying each remaining attack type amount with a configurable multiplier and adding up the results.

If Mana Attributes and/or Stamina Attributes is installed, a portion of "applied damage" can be taken from mana/stamina. The remaining amount of "applied damage" is then taken from the entity's health.

# Integration of Overhauled Damage into vanilla Minecraft and mod packs

How exactly Overhauled Damage should be integrated into the gameplay and balance of a mod pack is highly subjective, which is why most features can be configured extensively and/or use data driven methods for configuration.

Most features have default values that are somewhat reasonable and could be used as a baseline for customisations.

If you have any questions or problems when configuring Overhauled Damage, you can contact me on GitHub or Discord.

## API

Overhauled Damage provides a Java API that allows any Mob entity to deal damage with a custom damage type.

Implement the "UsesCustomDamageType" interface and override the "overhauleddamage$getCustomDamageType()" method.

## Built-in Data Packs

Overhauled Damage also comes with several built-in data packs, which further integrate existing or add new content.

### Overhauled Damage Enchantments

This data pack adds several new enchantments:

- Bleeding Protection
- Frost Aspect
- Frost Protection
- Lightning Aspect
- Lightning Protection
- Poison Aspect
- Poison Protection

### Vanilla Enchantments Overhaul

This data pack reworks some vanilla enchantments to use mechanics introduced by Overhauled Damage.

This includes:

- Blast Protection
- Fire Aspect
- Fire Protection
- Projectile Protection
- Protection
