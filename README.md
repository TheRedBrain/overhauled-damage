# Overhauled Damage

A complete overhaul to several mechanics revolving around damage. It is inspired by games like Dark Souls and Valheim and aims to deepen Minecraft's combat.
It is however not a content mod, more of an API for other mods.

## New Damage Calculation
Damage is split into physical and elemental damage which are split further into bashing, piercing and slashing (physical) and poison, fire, frost and lightning (elemental).

Physical damage is reduced by armor and directly reduces health.\
Armor and Armor Toughness work different to vanilla Minecraft. Armor is multiplied with Armor Toughness and the result is subtracted from the incoming damage.

Elemental damage is reduced by resistances, applies status effect build-ups but doesn't directly reduce health.\
Piercing and slashing damage can apply bleeding build-up.\
Taking physical or lightning damage adds to the stagger build-up.

Damage can be 'true damage', which means it is not reduced by armor or other resistances.

### Wait, what are "Effect build-ups"?
Build-ups are a value just like health, which is normally 0. Attacks can apply build-ups to the player (or other entities).
When a build-up reaches a threshold, the corresponding status effect is applied and the build-up is set to 0. Build-ups are also lowered over time.

The effects applied when a build-up reaches the threshold are defined in the server config.
When no valid status effect is defined, the build-up will not be applied by attacks.

Build-ups are displayed in the HUD, which can be disabled in the client config.

## Blocking and Parrying
Shields are completely overhauled. They no longer provide complete damage immunity.

Only a limited amount of damage is blocked, blocking costs [stamina](https://modrinth.com/mod/stamina-attributes) and when the shield is raised just before a hit, the attack is parried.

Parrying increases stagger build-up and when the build-up reaches the threshold, the parry fails and the parrying entity is staggered. In that case, no damage is blocked.
A successful parry multiplies the blocked damage and staggers the attacker.

A normal blocked attack (when the shield was raised for a longer time before the attack hit), can knockback the attacker.

## Additional features
3 entity type tags which allow for easy customization of mob attack types. (include vanilla melee mobs by default, eg zombies deal bashing damage)

Optional features enabled in the server config:
- disable "jump crit mechanic"
- taking damage cancels using items (doesn't apply when blocking or when damage is "true")
- blocking requires at least 1 stamina

## Customization
The HUD elements which display the different effect build-ups can be customized via the client config or in game, if Mod Menu is installed.\
The default elements are simple bars in different colors, similar to the experience bar.

There is an option to use custom textures, which have to be provided by a resource pack.

## How does it work?

Overhauled Damage uses damage type tags, entity attributes and its server config file for its damage calculation.

Damage in Minecraft consists of an amount and a "damage source". The source includes the attacking entity, its position and a "damage type".
The damage type determines the death message and is also used to check several things. This includes checking for immunities, damage reductions based on enchantments and damaging armor items.
These checks don't look for each individual damage type, but for "tags", which are collections of damage types defined via a data pack.
Overhauled Damage uses tags to determine if a damage_type can apply bleeding build-up and if it applies 'true damage'

"Entity attributes" control things like maximum health, armor and attack strength. Status effects, potions and commands can manipulate these attributes.
Most aspects of Overhauled Damage are controlled by attributes, like the thresholds for effect build-ups, blocked damage, elemental resistances, etc.

In the server config damage types can be associated with an array of values, which determine the multipliers used when calculating the different elemental and physical damage amounts.
## Examples

We look at an attack of the damage type "mod_id:test_damage_type" with an amount of 4.

### Example 1

"mod_id:test_damage_type" has the following damage_type_multipliers: {**1.0F**, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F}

Our attack deals 4 points of bashing damage.

### Example 2

"mod_id:test_damage_type" has the following damage_type_multipliers: {**1.0F**, **1.0F**, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F}

Our attack deals 4 points of bashing and 4 points of piercing damage, so 8 points in total.

### Example 3

"mod_id:test_damage_type" has the following damage_type_multipliers: {**0.5F**, **0.5F**, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F}

Our attack deals 2 points of bashing and 2 points of piercing damage, so 4 points in total.

### Example 4

"mod_id:test_damage_type" has the following damage_type_multipliers: {**1.5F**, **0.5F**, 0.0F, 0.0F, 0.0F, 0.0F, **1.0F**}

Our attack deals 6 points of bashing and 2 points of piercing damage, so 8 points in total.
It also applies 4 points of shock build-up.