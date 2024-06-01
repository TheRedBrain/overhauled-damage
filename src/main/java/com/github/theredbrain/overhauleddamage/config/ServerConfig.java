package com.github.theredbrain.overhauleddamage.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

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
    public ServerConfig() {

    }
}
