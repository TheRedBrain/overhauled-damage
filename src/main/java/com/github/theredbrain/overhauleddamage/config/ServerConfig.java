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
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean disable_jump_crit_mechanic = true;
    @Comment("When set to 'true', blocking requires the player to have at least 1 stamina.")
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
            Damage types in this map use the corresponding multipliers when calculating the different elemental and physical damage amounts inflicted by that damage type.
            
            The array must contain exactly seven (7) values.
            They correspond to the physical/elemental damages like so:
            {bashing, piercing, slashing, poison, fire, frost, lightning}
            """)
    public LinkedHashMap<String, Float[]> damage_type_multipliers = new LinkedHashMap<>() {{
        put("minecraft:arrow", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:bad_respawn_point", new Float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:cactus", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:explosion", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:falling_anvil", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:falling_block", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:falling_stalactite", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:fireball", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F});
        put("minecraft:fireworks", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:fly_into_wall", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:hot_floor", new Float[]{0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F});
        put("minecraft:in_fire", new Float[]{0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F});
        put("minecraft:lightning_bolt", new Float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F});
        put("minecraft:mob_attack", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:mob_attack_no_aggro", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:mob_projectile", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:player_attack", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:player_explosion", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:spit", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:stalagmite", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:sting", new Float[]{0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:sweet_berry_bush", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:thorns", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:thrown", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:trident", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:unattributed_fireball", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("minecraft:wither_skull", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("overhauleddamage:mob_bashing_damage_type", new Float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("overhauleddamage:mob_piercing_damage_type", new Float[]{0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
        put("overhauleddamage:mob_slashing_damage_type", new Float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F});
    }};
    public ServerConfig() {

    }
}
