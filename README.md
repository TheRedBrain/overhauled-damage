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

### Wait, what are "Effect build-ups"?
Build ups are a value just like health, which is normally 0. Attacks can apply build-ups to the player (or other entities).
When a build-up reaches a threshold, the corresponding status effect is applied and the build-up is set to 0. Build-ups are also lowered over time.

Build-ups are displayed in the HUD, which can be disabled in the client config.

## Blocking, Parrying and Stagger
Shields are completely overhauled. They no longer provide complete damage immunity.

Only a limited amount of damage is blocked, blocking costs [stamina](https://modrinth.com/mod/stamina-attributes) and when the shield is raised just before a hit, the attack is parried.

Parrying increases stagger build-up and when the build-up reaches the threshold, the parry fails and the parrying entity is staggered. In that case, no damage is blocked.
A successful parry multiplies the blocked damage and staggers the attacker.

A normal blocked attack (when the shield was raised for a longer time before the attack hit), can knockback the attacker.

## Status Effects
Overhauled Damage adds several new status effects.

Bleeding - build-up is added by piercing and slashing damage, but the damage_type must be in a special tag. It deals damage based on the entities maximum health and is doubled when the entity is moving.\
Burning - build-up is added by fire damage. It deals damage over time.\
Chilled - directly applied by frost damage, each point of damage results in a tick of duration. When the entity already is chilled, the duration is added.\
Frozen - build-up is added by frost damage. Frozen entities can't move, attack or use items.\
Poison - build-up is added by poison damage. This is not the vanilla status effect, but a custom variant.\
Shock - build-up is added by lightning damage. Shock instantly deals 10 damage.\
Stagger - build-up is added by physical and lightning damage. Frozen entities can't move, attack or use items. Stagger also doubles incoming damage.\
Fall Immunity - damage in tag 'is_fall' is ignored (mod implements no way of getting this effect in survival gameplay, it's meant for other content mods)\
Calamity - multiplies damage taken based on amplifier (mod implements no way of getting this effect in survival gameplay, it's meant for other content mods)\

Damage dealt by status effects is "true damage", which means it is not reduced by armor or other resistance, and it reduces health.

## Additional features
3 entity type tags which allow for easy customization of mob attack types. (include vanilla melee mobs by default, eg zombies deal bashing damage)

Optional features enabled in the server config:\
- disable "jump crit mechanic"
- taking damage cancels using items (doesn't apply when blocking or when damage is "true")
- blocking requires at least 1 stamina

## How does it work?

Overhauled Damage uses both damage type tags and entity attributes.

Damage in Minecraft consists of an amount and a "damage source". The source includes the attacking entity, its position and a "damage type".
The damage type determines the death message and is also used to check several things. This includes checking for immunities, damage reductions based on enchantments and damaging armor items.
These checks don't look for each individual damage type, but for "tags", which are collections of damage types defined via a data pack.

"Entity attributes" control things like maximum health, armor and attack strength. Status effects, potions and commands can manipulate these attributes.
Most aspects of Overhauled Damage are controlled by attributes, like the thresholds for effect build-ups, blocked damage, elemental resistances, etc.

## Examples

We look at an attack of the damage type "mod_id:test_damage_type" with an amount of 4.

### Example 1

"mod_id:test_damage_type" is in the "has_bashing_division_of_1" damage type tag.

Our attack deals 4 points of bashing damage.

### Example 2

"mod_id:test_damage_type" is in the "has_bashing_division_of_1" AND in the "has_piercing_division_of_1" damage type tag.

Our attack deals 4 points of bashing and 4 points of piercing damage, so 8 points in total.

### Example 3

"mod_id:test_damage_type" is in the "has_bashing_division_of_0_5" AND in the "has_piercing_division_of_0_5" damage type tag.
Notice the change from "division_of_1" to "division_of_0_5".

Our attack deals 2 points of bashing and 2 points of piercing damage, so 4 points in total.