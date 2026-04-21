package com.github.theredbrain.overhauleddamage.config;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.Identifier;

import java.util.HashMap;

@ConvertFrom(fileName = "client.json5", folder = "overhauleddamage")
public class ClientConfig extends Config {

	public ClientConfig() {
		super(OverhauledDamage.identifier("client"));
	}

	public BleedingBuildUpSettings bleeding_build_up_settings = new BleedingBuildUpSettings();

	@Translation(prefix = "overhauleddamage.client.resource_bar")
	public static class BleedingBuildUpSettings extends ConfigSection {

		public boolean show_empty_bar = false;

		public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE;
		public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

		// TODO prepend text explaining dynamic offsets

		public int dynamic_offset_increase_x = 0;
		public int dynamic_offset_increase_y = 10;

		public ResourceBarAPI.ResourceBarDisplay bar_display = ResourceBarAPI.ResourceBarDisplay.ICON;

		public IconBarSettings icon_bar_settings = new IconBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class IconBarSettings extends ConfigSection {
			public ValidatedInt offset_x = new ValidatedInt(-41);
			public ValidatedInt offset_y = new ValidatedInt(5);
			public ValidatedInt max_icon_amount_per_bar = new ValidatedInt(10);
			public ValidatedBoolean reverse_stack_direction = new ValidatedBoolean(true);
			public ValidatedBoolean enable_icon_blinking = new ValidatedBoolean(true);
		}

		public SmoothBarSettings smooth_bar_settings = new SmoothBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class SmoothBarSettings extends ConfigSection {

			public PositionSettings position_settings = new PositionSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class PositionSettings extends ConfigSection {
				public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
					put(0, -31);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
					put(0, 18);
				}}, new ValidatedInt(), new ValidatedInt());
			}

			public boolean show_current_value_overlay = false;

			public TextureSettings texture_settings = new TextureSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class TextureSettings extends ConfigSection {
				public BackgroundTextureSettings background_texture_settings = new BackgroundTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class BackgroundTextureSettings extends ConfigSection {

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_background.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public ProgressTextureSettings progress_texture_settings = new ProgressTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class ProgressTextureSettings extends ConfigSection {
					public int offset_x = 0;
					public int offset_y = 0;

					// TODO prepend text explaining that the following textures need to be of the same size

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					// TODO prepend text explaining what the different textures are used for

					public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_decrease_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_increase_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_increase_value.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public OverlayTextureSettings overlay_texture_settings = new OverlayTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class OverlayTextureSettings extends ConfigSection {
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_x = -2;
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_y = 0;

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_overlay.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}
			}

			public boolean enable_smooth_animation = true;

			public AnimationsSettings animation_settings = new AnimationsSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class AnimationsSettings extends ConfigSection {
				public int animation_interval = 1;
				public boolean max_value_change_is_animated = false;
			}

			public boolean show_icon = false;

			public IconTextureSettings icon_texture_settings = new IconTextureSettings();

			@Translation(prefix = "overhauleddamage.client.texture_layer")
			public static class IconTextureSettings extends ConfigSection {

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_x = 0;
				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_y = 0;

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public boolean show_when_bar_empty = false;

				public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());

				public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {
				}, new ValidatedInt(), new ValidatedIdentifier());

			}
		}

		public boolean show_number = false;

		public NumberSettings number_settings = new NumberSettings();

		@Translation(prefix = "overhauleddamage.client.resource_number")
		public static class NumberSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 17;
			public boolean show_max_value = false;
			public boolean show_when_bar_empty = false;
			public ValidatedColor color = new ValidatedColor(150, 150, 150);
		}
	}

	public BurnBuildUpSettings burn_build_up_settings = new BurnBuildUpSettings();

	@Translation(prefix = "overhauleddamage.client.resource_bar")
	public static class BurnBuildUpSettings extends ConfigSection {

		public boolean show_empty_bar = false;

		public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE;
		public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

		// TODO prepend text explaining dynamic offsets

		public int dynamic_offset_increase_x = 0;
		public int dynamic_offset_increase_y = 10;

		public ResourceBarAPI.ResourceBarDisplay bar_display = ResourceBarAPI.ResourceBarDisplay.ICON;

		public IconBarSettings icon_bar_settings = new IconBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class IconBarSettings extends ConfigSection {
			public ValidatedInt offset_x = new ValidatedInt(-41);
			public ValidatedInt offset_y = new ValidatedInt(5);
			public ValidatedInt max_icon_amount_per_bar = new ValidatedInt(10);
			public ValidatedBoolean reverse_stack_direction = new ValidatedBoolean(true);
			public ValidatedBoolean enable_icon_blinking = new ValidatedBoolean(true);
		}

		public SmoothBarSettings smooth_bar_settings = new SmoothBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class SmoothBarSettings extends ConfigSection {

			public PositionSettings position_settings = new PositionSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class PositionSettings extends ConfigSection {
				public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
					put(0, -31);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
					put(0, 18);
				}}, new ValidatedInt(), new ValidatedInt());
			}

			public boolean show_current_value_overlay = false;

			public TextureSettings texture_settings = new TextureSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class TextureSettings extends ConfigSection {
				public BackgroundTextureSettings background_texture_settings = new BackgroundTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class BackgroundTextureSettings extends ConfigSection {

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_background.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public ProgressTextureSettings progress_texture_settings = new ProgressTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class ProgressTextureSettings extends ConfigSection {
					public int offset_x = 0;
					public int offset_y = 0;

					// TODO prepend text explaining that the following textures need to be of the same size

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					// TODO prepend text explaining what the different textures are used for

					public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_decrease_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_increase_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_increase_value.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public OverlayTextureSettings overlay_texture_settings = new OverlayTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class OverlayTextureSettings extends ConfigSection {
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_x = -2;
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_y = 0;

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_overlay.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}
			}

			public boolean enable_smooth_animation = true;

			public AnimationsSettings animation_settings = new AnimationsSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class AnimationsSettings extends ConfigSection {
				public int animation_interval = 1;
				public boolean max_value_change_is_animated = false;
			}

			public boolean show_icon = false;

			public IconTextureSettings icon_texture_settings = new IconTextureSettings();

			@Translation(prefix = "overhauleddamage.client.texture_layer")
			public static class IconTextureSettings extends ConfigSection {

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_x = 0;
				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_y = 0;

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public boolean show_when_bar_empty = false;

				public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());

				public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {
				}, new ValidatedInt(), new ValidatedIdentifier());

			}
		}

		public boolean show_number = false;

		public NumberSettings number_settings = new NumberSettings();

		@Translation(prefix = "overhauleddamage.client.resource_number")
		public static class NumberSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 17;
			public boolean show_max_value = false;
			public boolean show_when_bar_empty = false;
			public ValidatedColor color = new ValidatedColor(150, 150, 150);
		}
	}

	public FreezeBuildUpSettings freeze_build_up_settings = new FreezeBuildUpSettings();

	@Translation(prefix = "overhauleddamage.client.resource_bar")
	public static class FreezeBuildUpSettings extends ConfigSection {

		public boolean show_empty_bar = false;

		public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE;
		public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

		// TODO prepend text explaining dynamic offsets

		public int dynamic_offset_increase_x = 0;
		public int dynamic_offset_increase_y = 10;

		public ResourceBarAPI.ResourceBarDisplay bar_display = ResourceBarAPI.ResourceBarDisplay.ICON;

		public IconBarSettings icon_bar_settings = new IconBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class IconBarSettings extends ConfigSection {
			public ValidatedInt offset_x = new ValidatedInt(-41);
			public ValidatedInt offset_y = new ValidatedInt(5);
			public ValidatedInt max_icon_amount_per_bar = new ValidatedInt(10);
			public ValidatedBoolean reverse_stack_direction = new ValidatedBoolean(true);
			public ValidatedBoolean enable_icon_blinking = new ValidatedBoolean(true);
		}

		public SmoothBarSettings smooth_bar_settings = new SmoothBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class SmoothBarSettings extends ConfigSection {

			public PositionSettings position_settings = new PositionSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class PositionSettings extends ConfigSection {
				public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
					put(0, -31);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
					put(0, 18);
				}}, new ValidatedInt(), new ValidatedInt());
			}

			public boolean show_current_value_overlay = false;

			public TextureSettings texture_settings = new TextureSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class TextureSettings extends ConfigSection {
				public BackgroundTextureSettings background_texture_settings = new BackgroundTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class BackgroundTextureSettings extends ConfigSection {

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_background.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public ProgressTextureSettings progress_texture_settings = new ProgressTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class ProgressTextureSettings extends ConfigSection {
					public int offset_x = 0;
					public int offset_y = 0;

					// TODO prepend text explaining that the following textures need to be of the same size

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					// TODO prepend text explaining what the different textures are used for

					public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_decrease_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_increase_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_increase_value.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public OverlayTextureSettings overlay_texture_settings = new OverlayTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class OverlayTextureSettings extends ConfigSection {
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_x = -2;
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_y = 0;

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_overlay.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}
			}

			public boolean enable_smooth_animation = true;

			public AnimationsSettings animation_settings = new AnimationsSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class AnimationsSettings extends ConfigSection {
				public int animation_interval = 1;
				public boolean max_value_change_is_animated = false;
			}

			public boolean show_icon = false;

			public IconTextureSettings icon_texture_settings = new IconTextureSettings();

			@Translation(prefix = "overhauleddamage.client.texture_layer")
			public static class IconTextureSettings extends ConfigSection {

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_x = 0;
				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_y = 0;

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public boolean show_when_bar_empty = false;

				public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());

				public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {
				}, new ValidatedInt(), new ValidatedIdentifier());

			}
		}

		public boolean show_number = false;

		public NumberSettings number_settings = new NumberSettings();

		@Translation(prefix = "overhauleddamage.client.resource_number")
		public static class NumberSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 17;
			public boolean show_max_value = false;
			public boolean show_when_bar_empty = false;
			public ValidatedColor color = new ValidatedColor(150, 150, 150);
		}
	}

	public PoisonBuildUpSettings poison_build_up_settings = new PoisonBuildUpSettings();

	@Translation(prefix = "overhauleddamage.client.resource_bar")
	public static class PoisonBuildUpSettings extends ConfigSection {

		public boolean show_empty_bar = false;

		public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE;
		public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

		// TODO prepend text explaining dynamic offsets

		public int dynamic_offset_increase_x = 0;
		public int dynamic_offset_increase_y = 10;

		public ResourceBarAPI.ResourceBarDisplay bar_display = ResourceBarAPI.ResourceBarDisplay.ICON;

		public IconBarSettings icon_bar_settings = new IconBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class IconBarSettings extends ConfigSection {
			public ValidatedInt offset_x = new ValidatedInt(-41);
			public ValidatedInt offset_y = new ValidatedInt(5);
			public ValidatedInt max_icon_amount_per_bar = new ValidatedInt(10);
			public ValidatedBoolean reverse_stack_direction = new ValidatedBoolean(true);
			public ValidatedBoolean enable_icon_blinking = new ValidatedBoolean(true);
		}

		public SmoothBarSettings smooth_bar_settings = new SmoothBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class SmoothBarSettings extends ConfigSection {

			public PositionSettings position_settings = new PositionSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class PositionSettings extends ConfigSection {
				public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
					put(0, -31);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
					put(0, 18);
				}}, new ValidatedInt(), new ValidatedInt());
			}

			public boolean show_current_value_overlay = false;

			public TextureSettings texture_settings = new TextureSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class TextureSettings extends ConfigSection {
				public BackgroundTextureSettings background_texture_settings = new BackgroundTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class BackgroundTextureSettings extends ConfigSection {

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_background.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public ProgressTextureSettings progress_texture_settings = new ProgressTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class ProgressTextureSettings extends ConfigSection {
					public int offset_x = 0;
					public int offset_y = 0;

					// TODO prepend text explaining that the following textures need to be of the same size

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					// TODO prepend text explaining what the different textures are used for

					public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_decrease_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_increase_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_increase_value.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public OverlayTextureSettings overlay_texture_settings = new OverlayTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class OverlayTextureSettings extends ConfigSection {
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_x = -2;
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_y = 0;

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_overlay.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}
			}

			public boolean enable_smooth_animation = true;

			public AnimationsSettings animation_settings = new AnimationsSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class AnimationsSettings extends ConfigSection {
				public int animation_interval = 1;
				public boolean max_value_change_is_animated = false;
			}

			public boolean show_icon = false;

			public IconTextureSettings icon_texture_settings = new IconTextureSettings();

			@Translation(prefix = "overhauleddamage.client.texture_layer")
			public static class IconTextureSettings extends ConfigSection {

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_x = 0;
				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_y = 0;

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public boolean show_when_bar_empty = false;

				public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());

				public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {
				}, new ValidatedInt(), new ValidatedIdentifier());

			}
		}

		public boolean show_number = false;

		public NumberSettings number_settings = new NumberSettings();

		@Translation(prefix = "overhauleddamage.client.resource_number")
		public static class NumberSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 17;
			public boolean show_max_value = false;
			public boolean show_when_bar_empty = false;
			public ValidatedColor color = new ValidatedColor(150, 150, 150);
		}
	}

	public ShockBuildUpSettings shock_build_up_settings = new ShockBuildUpSettings();

	@Translation(prefix = "overhauleddamage.client.resource_bar")
	public static class ShockBuildUpSettings extends ConfigSection {

		public boolean show_empty_bar = false;

		public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE;
		public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

		// TODO prepend text explaining dynamic offsets

		public int dynamic_offset_increase_x = 0;
		public int dynamic_offset_increase_y = 10;

		public ResourceBarAPI.ResourceBarDisplay bar_display = ResourceBarAPI.ResourceBarDisplay.ICON;

		public IconBarSettings icon_bar_settings = new IconBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class IconBarSettings extends ConfigSection {
			public ValidatedInt offset_x = new ValidatedInt(-41);
			public ValidatedInt offset_y = new ValidatedInt(5);
			public ValidatedInt max_icon_amount_per_bar = new ValidatedInt(10);
			public ValidatedBoolean reverse_stack_direction = new ValidatedBoolean(true);
			public ValidatedBoolean enable_icon_blinking = new ValidatedBoolean(true);
		}

		public SmoothBarSettings smooth_bar_settings = new SmoothBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class SmoothBarSettings extends ConfigSection {

			public PositionSettings position_settings = new PositionSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class PositionSettings extends ConfigSection {
				public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
					put(0, -31);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
					put(0, 18);
				}}, new ValidatedInt(), new ValidatedInt());
			}

			public boolean show_current_value_overlay = false;

			public TextureSettings texture_settings = new TextureSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class TextureSettings extends ConfigSection {
				public BackgroundTextureSettings background_texture_settings = new BackgroundTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class BackgroundTextureSettings extends ConfigSection {

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_background.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public ProgressTextureSettings progress_texture_settings = new ProgressTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class ProgressTextureSettings extends ConfigSection {
					public int offset_x = 0;
					public int offset_y = 0;

					// TODO prepend text explaining that the following textures need to be of the same size

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					// TODO prepend text explaining what the different textures are used for

					public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_decrease_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_increase_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_increase_value.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public OverlayTextureSettings overlay_texture_settings = new OverlayTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class OverlayTextureSettings extends ConfigSection {
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_x = -2;
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_y = 0;

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_overlay.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}
			}

			public boolean enable_smooth_animation = true;

			public AnimationsSettings animation_settings = new AnimationsSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class AnimationsSettings extends ConfigSection {
				public int animation_interval = 1;
				public boolean max_value_change_is_animated = false;
			}

			public boolean show_icon = false;

			public IconTextureSettings icon_texture_settings = new IconTextureSettings();

			@Translation(prefix = "overhauleddamage.client.texture_layer")
			public static class IconTextureSettings extends ConfigSection {

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_x = 0;
				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_y = 0;

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public boolean show_when_bar_empty = false;

				public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());

				public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {
				}, new ValidatedInt(), new ValidatedIdentifier());

			}
		}

		public boolean show_number = false;

		public NumberSettings number_settings = new NumberSettings();

		@Translation(prefix = "overhauleddamage.client.resource_number")
		public static class NumberSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 17;
			public boolean show_max_value = false;
			public boolean show_when_bar_empty = false;
			public ValidatedColor color = new ValidatedColor(150, 150, 150);
		}
	}

	public StaggerBuildUpSettings stagger_build_up_settings = new StaggerBuildUpSettings();

	@Translation(prefix = "overhauleddamage.client.resource_bar")
	public static class StaggerBuildUpSettings extends ConfigSection {

		public boolean show_empty_bar = false;

		public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE;
		public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

		// TODO prepend text explaining dynamic offsets

//		public int dynamic_offset_increase_x = 0;
//		public int dynamic_offset_increase_y = 10;

		public ResourceBarAPI.ResourceBarDisplay bar_display = ResourceBarAPI.ResourceBarDisplay.ICON;

		public IconBarSettings icon_bar_settings = new IconBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class IconBarSettings extends ConfigSection {
			public ValidatedInt offset_x = new ValidatedInt(-41);
			public ValidatedInt offset_y = new ValidatedInt(5);
			public ValidatedInt max_icon_amount_per_bar = new ValidatedInt(10);
			public ValidatedBoolean reverse_stack_direction = new ValidatedBoolean(true);
			public ValidatedBoolean enable_icon_blinking = new ValidatedBoolean(true);
		}

		public SmoothBarSettings smooth_bar_settings = new SmoothBarSettings();

		@Translation(prefix = "overhauleddamage.client.resource_bar")
		public static class SmoothBarSettings extends ConfigSection {

			public PositionSettings position_settings = new PositionSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class PositionSettings extends ConfigSection {
				public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
					put(0, -31);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
					put(0, 18);
				}}, new ValidatedInt(), new ValidatedInt());
			}

			public boolean show_current_value_overlay = false;

			public TextureSettings texture_settings = new TextureSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class TextureSettings extends ConfigSection {
				public BackgroundTextureSettings background_texture_settings = new BackgroundTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class BackgroundTextureSettings extends ConfigSection {

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_background.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public ProgressTextureSettings progress_texture_settings = new ProgressTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class ProgressTextureSettings extends ConfigSection {
					public int offset_x = 0;
					public int offset_y = 0;

					// TODO prepend text explaining that the following textures need to be of the same size

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 11);
					}}, new ValidatedInt(), new ValidatedInt());

					// TODO prepend text explaining what the different textures are used for

					public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_decrease_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_increase_animation.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_increase_value.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

					public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}

				public OverlayTextureSettings overlay_texture_settings = new OverlayTextureSettings();

				@Translation(prefix = "overhauleddamage.client.texture_layer")
				public static class OverlayTextureSettings extends ConfigSection {
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_x = -2;
					@Translation(prefix = "overhauleddamage.client.overlay_texture_layer")
					public int offset_y = 0;

					public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());
					public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
						put(0, 5);
					}}, new ValidatedInt(), new ValidatedInt());

					public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
						put(0, Identifier.fromNamespaceAndPath("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_overlay.png"));
					}}, new ValidatedInt(), new ValidatedIdentifier());

				}
			}

			public boolean enable_smooth_animation = true;

			public AnimationsSettings animation_settings = new AnimationsSettings();

			@Translation(prefix = "overhauleddamage.client.resource_bar")
			public static class AnimationsSettings extends ConfigSection {
				public int animation_interval = 1;
				public boolean max_value_change_is_animated = false;
			}

			public boolean show_icon = false;

			public IconTextureSettings icon_texture_settings = new IconTextureSettings();

			@Translation(prefix = "overhauleddamage.client.texture_layer")
			public static class IconTextureSettings extends ConfigSection {

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_x = 0;
				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public int offset_y = 0;

				@Translation(prefix = "overhauleddamage.client.resource_icon")
				public boolean show_when_bar_empty = false;

				public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());
				public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
					put(0, 0);
				}}, new ValidatedInt(), new ValidatedInt());

				public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {
				}, new ValidatedInt(), new ValidatedIdentifier());

			}
		}

		public boolean show_number = false;

		public NumberSettings number_settings = new NumberSettings();

		@Translation(prefix = "overhauleddamage.client.resource_number")
		public static class NumberSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 17;
			public boolean show_max_value = false;
			public boolean show_when_bar_empty = false;
			public ValidatedColor color = new ValidatedColor(150, 150, 150);
		}
	}
}