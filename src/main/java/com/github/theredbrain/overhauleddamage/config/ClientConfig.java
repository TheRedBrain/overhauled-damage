package com.github.theredbrain.overhauleddamage.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(
		name = "overhauleddamage"
)
public class ClientConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("generalClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public GeneralClientConfig generalClientConfig = new GeneralClientConfig();
	@ConfigEntry.Category("bleedingClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public BleedingClientConfig bleedingClientConfig = new BleedingClientConfig();
	@ConfigEntry.Category("burnClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public BurnClientConfig burnClientConfig = new BurnClientConfig();
	@ConfigEntry.Category("freezeClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public FreezeClientConfig freezeClientConfig = new FreezeClientConfig();
	@ConfigEntry.Category("poisonClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public PoisonClientConfig poisonClientConfig = new PoisonClientConfig();
	@ConfigEntry.Category("shockClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public ShockClientConfig shockClientConfig = new ShockClientConfig();
	@ConfigEntry.Category("staggerClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public StaggerClientConfig staggerClientConfig = new StaggerClientConfig();

	public ClientConfig() {
	}

	@Config(
			name = "generalClientConfig"
	)
	public static class GeneralClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public boolean show_effect_build_up_elements = true;

		@ConfigEntry.Gui.PrefixText()
		public boolean use_custom_textures = false;

		@ConfigEntry.Gui.PrefixText()
		public int dynamic_x_offset_increase = 0;
		public int dynamic_y_offset_increase = 6;

		public GeneralClientConfig() {

		}

	}

	@Config(
			name = "bleedingClientConfig"
	)
	public static class BleedingClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public int bleeding_build_up_element_x_offset = -31;
		public int bleeding_build_up_element_y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection bleeding_element_fill_direction = FillDirection.LEFT_TO_RIGHT;
		public boolean enable_bleeding_build_up_bar_animation = true;
		public int bleeding_build_up_bar_animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public int bleeding_build_up_bar_additional_length = 52;

		@ConfigEntry.Gui.PrefixText
		public String custom_bleeding_element_background_texture_id = "";
		public String custom_bleeding_element_foreground_texture_id = "";
		public int custom_bleeding_element_texture_width = 0;
		public int custom_bleeding_element_texture_height = 0;

		public BleedingClientConfig() {

		}
	}

	@Config(
			name = "burnClientConfig"
	)
	public static class BurnClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public int burn_build_up_element_x_offset = -31;
		public int burn_build_up_element_y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection burn_element_fill_direction = FillDirection.LEFT_TO_RIGHT;
		public boolean enable_burn_build_up_bar_animation = true;
		public int burn_build_up_bar_animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public int burn_build_up_bar_additional_length = 52;

		@ConfigEntry.Gui.PrefixText
		public String custom_burn_element_background_texture_id = "";
		public String custom_burn_element_foreground_texture_id = "";
		public int custom_burn_element_texture_width = 0;
		public int custom_burn_element_texture_height = 0;

		public BurnClientConfig() {

		}
	}

	@Config(
			name = "freezeClientConfig"
	)
	public static class FreezeClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public int freeze_build_up_element_x_offset = -31;
		public int freeze_build_up_element_y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection freeze_element_fill_direction = FillDirection.LEFT_TO_RIGHT;
		public boolean enable_freeze_build_up_bar_animation = true;
		public int freeze_build_up_bar_animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public int freeze_build_up_bar_additional_length = 52;

		@ConfigEntry.Gui.PrefixText
		public String custom_freeze_element_background_texture_id = "";
		public String custom_freeze_element_foreground_texture_id = "";
		public int custom_freeze_element_texture_width = 0;
		public int custom_freeze_element_texture_height = 0;

		public FreezeClientConfig() {

		}
	}

	@Config(
			name = "poisonClientConfig"
	)
	public static class PoisonClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public int poison_build_up_element_x_offset = -31;
		public int poison_build_up_element_y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection poison_element_fill_direction = FillDirection.LEFT_TO_RIGHT;
		public boolean enable_poison_build_up_bar_animation = true;
		public int poison_build_up_bar_animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public int poison_build_up_bar_additional_length = 52;

		@ConfigEntry.Gui.PrefixText
		public String custom_poison_element_background_texture_id = "";
		public String custom_poison_element_foreground_texture_id = "";
		public int custom_poison_element_texture_width = 0;
		public int custom_poison_element_texture_height = 0;

		public PoisonClientConfig() {

		}
	}

	@Config(
			name = "shockClientConfig"
	)
	public static class ShockClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public int shock_build_up_element_x_offset = -31;
		public int shock_build_up_element_y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection shock_element_fill_direction = FillDirection.LEFT_TO_RIGHT;
		public boolean enable_shock_build_up_bar_animation = true;
		public int shock_build_up_bar_animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public int shock_build_up_bar_additional_length = 52;

		@ConfigEntry.Gui.PrefixText
		public String custom_shock_element_background_texture_id = "";
		public String custom_shock_element_foreground_texture_id = "";
		public int custom_shock_element_texture_width = 0;
		public int custom_shock_element_texture_height = 0;

		public ShockClientConfig() {

		}
	}

	@Config(
			name = "staggerClientConfig"
	)
	public static class StaggerClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public int stagger_build_up_element_x_offset = -31;
		public int stagger_build_up_element_y_offset = 18;
		@EnumHandler(
				option = EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection stagger_element_fill_direction = FillDirection.LEFT_TO_RIGHT;
		public boolean enable_stagger_build_up_bar_animation = true;
		public int stagger_build_up_bar_animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public int stagger_build_up_bar_additional_length = 52;

		@ConfigEntry.Gui.PrefixText
		public String custom_stagger_element_background_texture_id = "";
		public String custom_stagger_element_foreground_texture_id = "";
		public int custom_stagger_element_texture_width = 0;
		public int custom_stagger_element_texture_height = 0;

		public StaggerClientConfig() {

		}
	}

	public enum FillDirection {
		LEFT_TO_RIGHT,
		BOTTOM_TO_TOP,
		RIGHT_TO_LEFT,
		TOP_TO_BOTTOM;

		FillDirection() {
		}
	}
}
