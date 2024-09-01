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
		public int dynamic_x_offset_increase = 0;
		public int dynamic_y_offset_increase = 6;

		public GeneralClientConfig() {

		}

	}

	@Config(
			name = "bleedingClientConfig"
	)
	public static class BleedingClientConfig implements ConfigData {
		public int x_offset = -31;
		public int y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection fill_direction = FillDirection.LEFT_TO_RIGHT;

		@ConfigEntry.Gui.PrefixText
		public int background_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_background_left_end_width = 5;
		public int horizontal_background_middle_segment_width = 1;
		public int horizontal_background_right_end_width = 5;
		public int horizontal_background_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_background_width = 5;
		@ConfigEntry.Gui.PrefixText
		public int vertical_background_top_end_height = 5;
		public int vertical_background_middle_segment_height = 1;
		public int vertical_background_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int progress_offset_x = 0;
		public int progress_offset_y = 0;
		public int progress_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_progress_left_end_width = 5;
		public int horizontal_progress_middle_segment_width = 1;
		public int horizontal_progress_right_end_width = 5;
		public int horizontal_progress_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_width = 5;
		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_top_end_height = 5;
		public int vertical_progress_middle_segment_height = 1;
		public int vertical_progress_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean show_current_value_overlay = false;

		public int overlay_offset_x = -2;
		public int overlay_offset_y = 0;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_overlay_width = 5;
		public int horizontal_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_overlay_width = 5;
		public int vertical_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_smooth_animation = true;
		public int animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public boolean show_number = false;

		public int number_x_offset = 0;
		public int number_y_offset = 16;

		public int number_color = -6250336;

		public BleedingClientConfig() {

		}
	}

	@Config(
			name = "burnClientConfig"
	)
	public static class BurnClientConfig implements ConfigData {
		public int x_offset = -31;
		public int y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection fill_direction = FillDirection.LEFT_TO_RIGHT;

		@ConfigEntry.Gui.PrefixText
		public int background_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_background_left_end_width = 5;
		public int horizontal_background_middle_segment_width = 1;
		public int horizontal_background_right_end_width = 5;
		public int horizontal_background_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_background_width = 5;
		public int vertical_background_top_end_height = 5;
		public int vertical_background_middle_segment_height = 1;
		public int vertical_background_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int progress_offset_x = 0;
		public int progress_offset_y = 0;
		public int progress_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_progress_left_end_width = 5;
		public int horizontal_progress_middle_segment_width = 1;
		public int horizontal_progress_right_end_width = 5;
		public int horizontal_progress_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_width = 5;
		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_top_end_height = 5;
		public int vertical_progress_middle_segment_height = 1;
		public int vertical_progress_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean show_current_value_overlay = false;

		public int overlay_offset_x = -2;
		public int overlay_offset_y = 0;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_overlay_width = 5;
		public int horizontal_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_overlay_width = 5;
		public int vertical_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_smooth_animation = true;
		public int animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public boolean show_number = false;

		public int number_x_offset = 0;
		public int number_y_offset = 16;

		public int number_color = -6250336;

		public BurnClientConfig() {

		}
	}

	@Config(
			name = "freezeClientConfig"
	)
	public static class FreezeClientConfig implements ConfigData {
		public int x_offset = -31;
		public int y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection fill_direction = FillDirection.LEFT_TO_RIGHT;

		@ConfigEntry.Gui.PrefixText
		public int background_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_background_left_end_width = 5;
		public int horizontal_background_middle_segment_width = 1;
		public int horizontal_background_right_end_width = 5;
		public int horizontal_background_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_background_width = 5;
		public int vertical_background_top_end_height = 5;
		public int vertical_background_middle_segment_height = 1;
		public int vertical_background_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int progress_offset_x = 0;
		public int progress_offset_y = 0;
		public int progress_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_progress_left_end_width = 5;
		public int horizontal_progress_middle_segment_width = 1;
		public int horizontal_progress_right_end_width = 5;
		public int horizontal_progress_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_width = 5;
		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_top_end_height = 5;
		public int vertical_progress_middle_segment_height = 1;
		public int vertical_progress_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean show_current_value_overlay = false;

		public int overlay_offset_x = -2;
		public int overlay_offset_y = 0;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_overlay_width = 5;
		public int horizontal_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_overlay_width = 5;
		public int vertical_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_smooth_animation = true;
		public int animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public boolean show_number = false;

		public int number_x_offset = 0;
		public int number_y_offset = 16;

		public int number_color = -6250336;

		public FreezeClientConfig() {

		}
	}

	@Config(
			name = "poisonClientConfig"
	)
	public static class PoisonClientConfig implements ConfigData {
		public int x_offset = -31;
		public int y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection fill_direction = FillDirection.LEFT_TO_RIGHT;

		@ConfigEntry.Gui.PrefixText
		public int background_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_background_left_end_width = 5;
		public int horizontal_background_middle_segment_width = 1;
		public int horizontal_background_right_end_width = 5;
		public int horizontal_background_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_background_width = 5;
		public int vertical_background_top_end_height = 5;
		public int vertical_background_middle_segment_height = 1;
		public int vertical_background_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int progress_offset_x = 0;
		public int progress_offset_y = 0;
		public int progress_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_progress_left_end_width = 5;
		public int horizontal_progress_middle_segment_width = 1;
		public int horizontal_progress_right_end_width = 5;
		public int horizontal_progress_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_width = 5;
		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_top_end_height = 5;
		public int vertical_progress_middle_segment_height = 1;
		public int vertical_progress_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean show_current_value_overlay = false;

		public int overlay_offset_x = -2;
		public int overlay_offset_y = 0;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_overlay_width = 5;
		public int horizontal_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_overlay_width = 5;
		public int vertical_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_smooth_animation = true;
		public int animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public boolean show_number = false;

		public int number_x_offset = 0;
		public int number_y_offset = 16;

		public int number_color = -6250336;

		public PoisonClientConfig() {

		}
	}

	@Config(
			name = "shockClientConfig"
	)
	public static class ShockClientConfig implements ConfigData {
		public int x_offset = -31;
		public int y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection fill_direction = FillDirection.LEFT_TO_RIGHT;

		@ConfigEntry.Gui.PrefixText
		public int background_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_background_left_end_width = 5;
		public int horizontal_background_middle_segment_width = 1;
		public int horizontal_background_right_end_width = 5;
		public int horizontal_background_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_background_width = 5;
		public int vertical_background_top_end_height = 5;
		public int vertical_background_middle_segment_height = 1;
		public int vertical_background_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int progress_offset_x = 0;
		public int progress_offset_y = 0;
		public int progress_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_progress_left_end_width = 5;
		public int horizontal_progress_middle_segment_width = 1;
		public int horizontal_progress_right_end_width = 5;
		public int horizontal_progress_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_width = 5;
		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_top_end_height = 5;
		public int vertical_progress_middle_segment_height = 1;
		public int vertical_progress_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean show_current_value_overlay = false;

		public int overlay_offset_x = -2;
		public int overlay_offset_y = 0;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_overlay_width = 5;
		public int horizontal_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_overlay_width = 5;
		public int vertical_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_smooth_animation = true;
		public int animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public boolean show_number = false;

		public int number_x_offset = 0;
		public int number_y_offset = 16;

		public int number_color = -6250336;

		public ShockClientConfig() {

		}
	}

	@Config(
			name = "staggerClientConfig"
	)
	public static class StaggerClientConfig implements ConfigData {
		public int x_offset = -31;
		public int y_offset = 18;
		@EnumHandler(
				option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON
		)
		public FillDirection fill_direction = FillDirection.LEFT_TO_RIGHT;

		@ConfigEntry.Gui.PrefixText
		public int background_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_background_left_end_width = 5;
		public int horizontal_background_middle_segment_width = 1;
		public int horizontal_background_right_end_width = 5;
		public int horizontal_background_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_background_width = 5;
		public int vertical_background_top_end_height = 5;
		public int vertical_background_middle_segment_height = 1;
		public int vertical_background_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int progress_offset_x = 0;
		public int progress_offset_y = 0;
		public int progress_middle_segment_amount = 52;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_progress_left_end_width = 5;
		public int horizontal_progress_middle_segment_width = 1;
		public int horizontal_progress_right_end_width = 5;
		public int horizontal_progress_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_width = 5;
		@ConfigEntry.Gui.PrefixText
		public int vertical_progress_top_end_height = 5;
		public int vertical_progress_middle_segment_height = 1;
		public int vertical_progress_bottom_end_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean show_current_value_overlay = false;

		public int overlay_offset_x = -2;
		public int overlay_offset_y = 0;

		@ConfigEntry.Gui.PrefixText
		public int horizontal_overlay_width = 5;
		public int horizontal_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public int vertical_overlay_width = 5;
		public int vertical_overlay_height = 5;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_smooth_animation = true;
		public int animation_interval = 1;

		@ConfigEntry.Gui.PrefixText
		public boolean show_number = false;

		public int number_x_offset = 0;
		public int number_y_offset = 16;

		public int number_color = -6250336;

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
