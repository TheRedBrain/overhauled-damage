package com.github.theredbrain.overhauleddamage.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
		name = "client"
)
public class ClientConfig implements ConfigData {
	@Comment("When an effect build-up is greater 0, a HUD element shows the progress until the effect is applied.")
	public boolean show_effect_build_up_elements = true;

	@Comment("""
			The textures have to be provided by a resource pack.
			It is recommended to use textures of equal dimensions.
			When the dynamic offsets are used as well, they should be at least equal to the texture dimensions
			""")
	public boolean use_custom_textures = false;

	@Comment("""
			When multiple effect build-ups are displayed at the same time, these values are added to every elements position offset, multiplied by the amount off elements already on screen.
			The order is: bleeding, burn, freeze, poison, shock, stagger
			            
			Example: The burn, poison and stagger elements are currently displayed
			The burn element is at its normal position
			The poison element is offset by "dynamic_x_offset_increase" on the x axis and by "dynamic_y_offset_increase" on the y axis
			The stagger element is offset by "dynamic_x_offset_increase" times 2 on the x axis and by "dynamic_y_offset_increase" times 2 on the y axis
			""")
	public int dynamic_x_offset_increase = 0;
	public int dynamic_y_offset_increase = 6;

	@Comment("""
			Each HUD element can be individually customized
			The "element offset" values offset the elements upper left corner from the middle of the screen
			            
			When using the default textures, the "additional length" value increases the width of the bar.
			            
			The remaining values only have an effect when using custom textures.
			"background texture id" and "foreground texture id" are the paths to the custom textures.
			"texture width" and "texture height" should be the dimensions of the custom textures.
			"fill direction" indicates in which direction the foreground texture is drawn.
			0: left to right, 1: bottom to top, 2: right to left, 3: top to bottom
			""")
	public int bleeding_build_up_element_x_offset = -31;
	public int bleeding_build_up_element_y_offset = 18;

	public int bleeding_build_up_bar_additional_length = 52;

	public String custom_bleeding_element_background_texture_id = "";
	public String custom_bleeding_element_foreground_texture_id = "";
	public int custom_bleeding_element_texture_width = 0;
	public int custom_bleeding_element_texture_height = 0;
	public int custom_bleeding_element_fill_direction = 0;

	public int burn_build_up_element_x_offset = -31;
	public int burn_build_up_element_y_offset = 18;

	public int burn_build_up_bar_additional_length = 52;

	public String custom_burn_element_background_texture_id = "";
	public String custom_burn_element_foreground_texture_id = "";
	public int custom_burn_element_texture_width = 0;
	public int custom_burn_element_texture_height = 0;
	public int custom_burn_element_fill_direction = 0;

	public int freeze_build_up_element_x_offset = -31;
	public int freeze_build_up_element_y_offset = 18;

	public int freeze_build_up_bar_additional_length = 52;

	public String custom_freeze_element_background_texture_id = "";
	public String custom_freeze_element_foreground_texture_id = "";
	public int custom_freeze_element_texture_width = 0;
	public int custom_freeze_element_texture_height = 0;
	public int custom_freeze_element_fill_direction = 0;

	public int poison_build_up_element_x_offset = -31;
	public int poison_build_up_element_y_offset = 18;

	public int poison_build_up_bar_additional_length = 52;

	public String custom_poison_element_background_texture_id = "";
	public String custom_poison_element_foreground_texture_id = "";
	public int custom_poison_element_texture_width = 0;
	public int custom_poison_element_texture_height = 0;
	public int custom_poison_element_fill_direction = 0;

	public int shock_build_up_element_x_offset = -31;
	public int shock_build_up_element_y_offset = 18;

	public int shock_build_up_bar_additional_length = 52;

	public String custom_shock_element_background_texture_id = "";
	public String custom_shock_element_foreground_texture_id = "";
	public int custom_shock_element_texture_width = 0;
	public int custom_shock_element_texture_height = 0;
	public int custom_shock_element_fill_direction = 0;

	public int stagger_build_up_element_x_offset = -31;
	public int stagger_build_up_element_y_offset = 18;

	public int stagger_build_up_bar_additional_length = 52;

	public String custom_stagger_element_background_texture_id = "";
	public String custom_stagger_element_foreground_texture_id = "";
	public int custom_stagger_element_texture_width = 0;
	public int custom_stagger_element_texture_height = 0;
	public int custom_stagger_element_fill_direction = 0;

	public ClientConfig() {

	}
}
