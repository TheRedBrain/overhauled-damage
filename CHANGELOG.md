# 2.0.0

Update to 1.21

# 1.5.0

- default attack type multipliers of damage types not present in the server config are now 1.0 for the bashing attack type and 0.0 for all other attack types (this should result in damage types not present in the config behaving like normal)
- internal changes to how the custom damage calculation is applied, which should result in better maintainable code
- fixed error in calculation of blocked damage

# 1.4.0

Physical and elemental damage amount is no longer calculated using damage type tags. The idea was good on paper, but it proved to be quite challenging to keep track of all the different files. The new system uses the server config. This makes adding, removing and editing the values much easier, gives better control over the exact value and future proofs the system for eventual expansions.

# 1.3.0

- changed default config to use status effects provided by 'Various Status Effects'
- fixed compatibility issues caused by using mixin overwrites

# 1.2.0

- added customization for the GUI elements
- fixed an issue with adding effect build-ups

# 1.1.0

No longer adds status effects, in the server config the ids of effects implemented by other mods can be specified instead. This mod is an API, so I felt that adding content like status effects is not in scope for this project. This gives mod (pack) authors more control and reduces dependencies and jar size. Also, no more assets are missing (or needed), so the mod is no longer in beta.

- no longer depends on Spell Engine, because it's ActionImpairing API is no longer needed
- added Mod Menu support
- fixed an issue where the folder containing the config files was not named correctly

# 1.0.1

- fixed an issue with the attribute registry
- fixed an issue where a damage type added in a newer minecraft version was present in a tag
- updated dependencies

# 1.0.0

Beta release! A few assets are still missing

#