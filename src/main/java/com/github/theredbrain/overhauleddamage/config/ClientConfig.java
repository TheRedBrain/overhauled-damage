package com.github.theredbrain.overhauleddamage.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    @Comment("""
            Changes HUD elements like the health bar, disables some like the armor bar
            and adds new ones like a stamina bar.
            """)
    public boolean show_effect_build_up_bars = false;
    public ClientConfig() {

    }
}
