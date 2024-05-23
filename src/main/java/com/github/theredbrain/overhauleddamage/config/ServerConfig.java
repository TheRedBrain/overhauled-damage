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
    @Comment("""
            When set to 'true', blocking requires the player to have at least 1 stamina.
            """)
    public boolean blocking_requires_stamina = true;
    public ServerConfig() {

    }
}
