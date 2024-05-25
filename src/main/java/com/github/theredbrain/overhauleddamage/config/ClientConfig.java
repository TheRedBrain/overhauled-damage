package com.github.theredbrain.overhauleddamage.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    @Comment("When an effect build-up is greater 0, a bar shows the progress until the effect is applied.")
    public boolean show_effect_build_up_bars = true;
    public ClientConfig() {

    }
}
