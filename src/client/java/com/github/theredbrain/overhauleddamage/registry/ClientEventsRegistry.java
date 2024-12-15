package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.OverhauledDamageClient;
import com.github.theredbrain.overhauleddamage.config.ClientConfig;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class ClientEventsRegistry {
	private static final String BLEEDING_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":bleeding";
	private static final String BURN_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":burn";
	private static final String FREEZE_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":freeze";
	private static final String POISON_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":poison";
	private static final String SHOCK_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":shock";
	private static final String STAGGER_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":stagger";

	public static void initializeClientEvents() {
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			PlayerEntity playerEntity = minecraftClient.player;
			ClientConfig clientConfig = OverhauledDamageClient.CLIENT_CONFIG;

			if (playerEntity != null) {

				int dynamic_offset_x = 0;
				int dynamic_offset_y = 0;

				// region bleeding
				double bleedingBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUp());
				double maxBleedingBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBleedingBuildUp());

				boolean should_bleeding_bar_be_rendered = clientConfig.bleedingBuildUpSettings.show_bar && maxBleedingBuildUp > 0 && (bleedingBuildUp > 0 || clientConfig.bleedingBuildUpSettings.show_empty_bar);
				boolean should_bleeding_icon_be_rendered = clientConfig.bleedingBuildUpSettings.show_icon && maxBleedingBuildUp > 0 && (bleedingBuildUp > 0 || clientConfig.bleedingBuildUpSettings.iconTextureSettings.show_when_bar_empty);
				boolean should_bleeding_number_be_rendered = clientConfig.bleedingBuildUpSettings.show_number && maxBleedingBuildUp > 0 && (bleedingBuildUp > 0 || clientConfig.bleedingBuildUpSettings.numberSettings.show_when_bar_empty);

				ResourceBarAPIClient.drawResourceBar(
						minecraftClient,
						minecraftClient.textRenderer,
						matrixStack,
						BLEEDING_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_overlay.png"),
								null
						},
						should_bleeding_bar_be_rendered,
						bleedingBuildUp,
						maxBleedingBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUpReduction()),
						0,
						clientConfig.bleedingBuildUpSettings.positionSettings.origin,
						clientConfig.bleedingBuildUpSettings.positionSettings.offsets_x,
						clientConfig.bleedingBuildUpSettings.positionSettings.offsets_y,
						dynamic_offset_x,
						dynamic_offset_y,
						clientConfig.bleedingBuildUpSettings.fill_direction,
						clientConfig.bleedingBuildUpSettings.textureSettings.backgroundTextureSettings.texture_heights,
						clientConfig.bleedingBuildUpSettings.textureSettings.backgroundTextureSettings.texture_widths,
						clientConfig.bleedingBuildUpSettings.textureSettings.backgroundTextureSettings.texture_ids,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.offset_x,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.offset_y,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.texture_heights,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.texture_widths,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
						clientConfig.bleedingBuildUpSettings.textureSettings.progressTextureSettings.progress_texture_ids,
						0,
						0,
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
						clientConfig.bleedingBuildUpSettings.show_current_value_overlay,
						clientConfig.bleedingBuildUpSettings.textureSettings.overlayTextureSettings.offset_x,
						clientConfig.bleedingBuildUpSettings.textureSettings.overlayTextureSettings.offset_y,
						clientConfig.bleedingBuildUpSettings.textureSettings.overlayTextureSettings.texture_heights,
						clientConfig.bleedingBuildUpSettings.textureSettings.overlayTextureSettings.texture_widths,
						clientConfig.bleedingBuildUpSettings.textureSettings.overlayTextureSettings.texture_ids,
						should_bleeding_icon_be_rendered,
						clientConfig.bleedingBuildUpSettings.iconTextureSettings.offset_x,
						clientConfig.bleedingBuildUpSettings.iconTextureSettings.offset_y,
						clientConfig.bleedingBuildUpSettings.iconTextureSettings.texture_heights,
						clientConfig.bleedingBuildUpSettings.iconTextureSettings.texture_widths,
						clientConfig.bleedingBuildUpSettings.iconTextureSettings.texture_ids,
						clientConfig.bleedingBuildUpSettings.enable_smooth_animation,
						clientConfig.bleedingBuildUpSettings.animationSettings.animation_interval,
						clientConfig.bleedingBuildUpSettings.animationSettings.max_value_change_is_animated,
						should_bleeding_number_be_rendered,
						clientConfig.bleedingBuildUpSettings.numberSettings.show_max_value,
						clientConfig.bleedingBuildUpSettings.numberSettings.offset_x + dynamic_offset_x,
						clientConfig.bleedingBuildUpSettings.numberSettings.offset_y + dynamic_offset_y,
						clientConfig.bleedingBuildUpSettings.numberSettings.color.toInt()
				);
				if (should_bleeding_bar_be_rendered || should_bleeding_icon_be_rendered|| should_bleeding_number_be_rendered) {
					dynamic_offset_x = clientConfig.bleedingBuildUpSettings.dynamic_offset_increase_x;
					dynamic_offset_y = clientConfig.bleedingBuildUpSettings.dynamic_offset_increase_y;
				}
				// endregion bleeding

				// region burn
				double burnBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUp());
				double maxBurnBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBurnBuildUp());

				boolean should_burn_bar_be_rendered = clientConfig.burnBuildUpSettings.show_bar && maxBurnBuildUp > 0 && (burnBuildUp > 0 || clientConfig.burnBuildUpSettings.show_empty_bar);
				boolean should_burn_icon_be_rendered = clientConfig.burnBuildUpSettings.show_icon && maxBurnBuildUp > 0 && (burnBuildUp > 0 || clientConfig.burnBuildUpSettings.iconTextureSettings.show_when_bar_empty);
				boolean should_burn_number_be_rendered = clientConfig.burnBuildUpSettings.show_number && maxBurnBuildUp > 0 && (burnBuildUp > 0 || clientConfig.burnBuildUpSettings.numberSettings.show_when_bar_empty);

				ResourceBarAPIClient.drawResourceBar(
						minecraftClient,
						minecraftClient.textRenderer,
						matrixStack,
						BLEEDING_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_overlay.png"),
								null
						},
						should_burn_bar_be_rendered,
						burnBuildUp,
						maxBurnBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUpReduction()),
						0,
						clientConfig.burnBuildUpSettings.positionSettings.origin,
						clientConfig.burnBuildUpSettings.positionSettings.offsets_x,
						clientConfig.burnBuildUpSettings.positionSettings.offsets_y,
						dynamic_offset_x,
						dynamic_offset_y,
						clientConfig.burnBuildUpSettings.fill_direction,
						clientConfig.burnBuildUpSettings.textureSettings.backgroundTextureSettings.texture_heights,
						clientConfig.burnBuildUpSettings.textureSettings.backgroundTextureSettings.texture_widths,
						clientConfig.burnBuildUpSettings.textureSettings.backgroundTextureSettings.texture_ids,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.offset_x,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.offset_y,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.texture_heights,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.texture_widths,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
						clientConfig.burnBuildUpSettings.textureSettings.progressTextureSettings.progress_texture_ids,
						0,
						0,
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
						clientConfig.burnBuildUpSettings.show_current_value_overlay,
						clientConfig.burnBuildUpSettings.textureSettings.overlayTextureSettings.offset_x,
						clientConfig.burnBuildUpSettings.textureSettings.overlayTextureSettings.offset_y,
						clientConfig.burnBuildUpSettings.textureSettings.overlayTextureSettings.texture_heights,
						clientConfig.burnBuildUpSettings.textureSettings.overlayTextureSettings.texture_widths,
						clientConfig.burnBuildUpSettings.textureSettings.overlayTextureSettings.texture_ids,
						should_burn_icon_be_rendered,
						clientConfig.burnBuildUpSettings.iconTextureSettings.offset_x,
						clientConfig.burnBuildUpSettings.iconTextureSettings.offset_y,
						clientConfig.burnBuildUpSettings.iconTextureSettings.texture_heights,
						clientConfig.burnBuildUpSettings.iconTextureSettings.texture_widths,
						clientConfig.burnBuildUpSettings.iconTextureSettings.texture_ids,
						clientConfig.burnBuildUpSettings.enable_smooth_animation,
						clientConfig.burnBuildUpSettings.animationSettings.animation_interval,
						clientConfig.burnBuildUpSettings.animationSettings.max_value_change_is_animated,
						should_burn_number_be_rendered,
						clientConfig.burnBuildUpSettings.numberSettings.show_max_value,
						clientConfig.burnBuildUpSettings.numberSettings.offset_x + dynamic_offset_x,
						clientConfig.burnBuildUpSettings.numberSettings.offset_y + dynamic_offset_y,
						clientConfig.burnBuildUpSettings.numberSettings.color.toInt()
				);
				if (should_burn_bar_be_rendered || should_burn_icon_be_rendered|| should_burn_number_be_rendered) {
					dynamic_offset_x = clientConfig.burnBuildUpSettings.dynamic_offset_increase_x;
					dynamic_offset_y = clientConfig.burnBuildUpSettings.dynamic_offset_increase_y;
				}
				// endregion burn

				// region freeze
				double freezeBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUp());
				double maxFreezeBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxFreezeBuildUp());

				boolean should_freeze_bar_be_rendered = clientConfig.freezeBuildUpSettings.show_bar && maxFreezeBuildUp > 0 && (freezeBuildUp > 0 || clientConfig.freezeBuildUpSettings.show_empty_bar);
				boolean should_freeze_icon_be_rendered = clientConfig.freezeBuildUpSettings.show_icon && maxFreezeBuildUp > 0 && (freezeBuildUp > 0 || clientConfig.freezeBuildUpSettings.iconTextureSettings.show_when_bar_empty);
				boolean should_freeze_number_be_rendered = clientConfig.freezeBuildUpSettings.show_number && maxFreezeBuildUp > 0 && (freezeBuildUp > 0 || clientConfig.freezeBuildUpSettings.numberSettings.show_when_bar_empty);

				ResourceBarAPIClient.drawResourceBar(
						minecraftClient,
						minecraftClient.textRenderer,
						matrixStack,
						BLEEDING_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_overlay.png"),
								null
						},
						should_freeze_bar_be_rendered,
						freezeBuildUp,
						maxFreezeBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUpReduction()),
						0,
						clientConfig.freezeBuildUpSettings.positionSettings.origin,
						clientConfig.freezeBuildUpSettings.positionSettings.offsets_x,
						clientConfig.freezeBuildUpSettings.positionSettings.offsets_y,
						dynamic_offset_x,
						dynamic_offset_y,
						clientConfig.freezeBuildUpSettings.fill_direction,
						clientConfig.freezeBuildUpSettings.textureSettings.backgroundTextureSettings.texture_heights,
						clientConfig.freezeBuildUpSettings.textureSettings.backgroundTextureSettings.texture_widths,
						clientConfig.freezeBuildUpSettings.textureSettings.backgroundTextureSettings.texture_ids,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.offset_x,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.offset_y,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.texture_heights,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.texture_widths,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
						clientConfig.freezeBuildUpSettings.textureSettings.progressTextureSettings.progress_texture_ids,
						0,
						0,
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
						clientConfig.freezeBuildUpSettings.show_current_value_overlay,
						clientConfig.freezeBuildUpSettings.textureSettings.overlayTextureSettings.offset_x,
						clientConfig.freezeBuildUpSettings.textureSettings.overlayTextureSettings.offset_y,
						clientConfig.freezeBuildUpSettings.textureSettings.overlayTextureSettings.texture_heights,
						clientConfig.freezeBuildUpSettings.textureSettings.overlayTextureSettings.texture_widths,
						clientConfig.freezeBuildUpSettings.textureSettings.overlayTextureSettings.texture_ids,
						should_freeze_icon_be_rendered,
						clientConfig.freezeBuildUpSettings.iconTextureSettings.offset_x,
						clientConfig.freezeBuildUpSettings.iconTextureSettings.offset_y,
						clientConfig.freezeBuildUpSettings.iconTextureSettings.texture_heights,
						clientConfig.freezeBuildUpSettings.iconTextureSettings.texture_widths,
						clientConfig.freezeBuildUpSettings.iconTextureSettings.texture_ids,
						clientConfig.freezeBuildUpSettings.enable_smooth_animation,
						clientConfig.freezeBuildUpSettings.animationSettings.animation_interval,
						clientConfig.freezeBuildUpSettings.animationSettings.max_value_change_is_animated,
						should_freeze_number_be_rendered,
						clientConfig.freezeBuildUpSettings.numberSettings.show_max_value,
						clientConfig.freezeBuildUpSettings.numberSettings.offset_x + dynamic_offset_x,
						clientConfig.freezeBuildUpSettings.numberSettings.offset_y + dynamic_offset_y,
						clientConfig.freezeBuildUpSettings.numberSettings.color.toInt()
				);
				if (should_freeze_bar_be_rendered || should_freeze_icon_be_rendered|| should_freeze_number_be_rendered) {
					dynamic_offset_x = clientConfig.freezeBuildUpSettings.dynamic_offset_increase_x;
					dynamic_offset_y = clientConfig.freezeBuildUpSettings.dynamic_offset_increase_y;
				}
				// endregion freeze

				// region poison
				double poisonBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUp());
				double maxPoisonBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxPoisonBuildUp());

				boolean should_poison_bar_be_rendered = clientConfig.poisonBuildUpSettings.show_bar && maxPoisonBuildUp > 0 && (poisonBuildUp > 0 || clientConfig.poisonBuildUpSettings.show_empty_bar);
				boolean should_poison_icon_be_rendered = clientConfig.poisonBuildUpSettings.show_icon && maxPoisonBuildUp > 0 && (poisonBuildUp > 0 || clientConfig.poisonBuildUpSettings.iconTextureSettings.show_when_bar_empty);
				boolean should_poison_number_be_rendered = clientConfig.poisonBuildUpSettings.show_number && maxPoisonBuildUp > 0 && (poisonBuildUp > 0 || clientConfig.poisonBuildUpSettings.numberSettings.show_when_bar_empty);

				ResourceBarAPIClient.drawResourceBar(
						minecraftClient,
						minecraftClient.textRenderer,
						matrixStack,
						BLEEDING_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_overlay.png"),
								null
						},
						should_poison_bar_be_rendered,
						poisonBuildUp,
						maxPoisonBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUpReduction()),
						0,
						clientConfig.poisonBuildUpSettings.positionSettings.origin,
						clientConfig.poisonBuildUpSettings.positionSettings.offsets_x,
						clientConfig.poisonBuildUpSettings.positionSettings.offsets_y,
						dynamic_offset_x,
						dynamic_offset_y,
						clientConfig.poisonBuildUpSettings.fill_direction,
						clientConfig.poisonBuildUpSettings.textureSettings.backgroundTextureSettings.texture_heights,
						clientConfig.poisonBuildUpSettings.textureSettings.backgroundTextureSettings.texture_widths,
						clientConfig.poisonBuildUpSettings.textureSettings.backgroundTextureSettings.texture_ids,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.offset_x,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.offset_y,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.texture_heights,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.texture_widths,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
						clientConfig.poisonBuildUpSettings.textureSettings.progressTextureSettings.progress_texture_ids,
						0,
						0,
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
						clientConfig.poisonBuildUpSettings.show_current_value_overlay,
						clientConfig.poisonBuildUpSettings.textureSettings.overlayTextureSettings.offset_x,
						clientConfig.poisonBuildUpSettings.textureSettings.overlayTextureSettings.offset_y,
						clientConfig.poisonBuildUpSettings.textureSettings.overlayTextureSettings.texture_heights,
						clientConfig.poisonBuildUpSettings.textureSettings.overlayTextureSettings.texture_widths,
						clientConfig.poisonBuildUpSettings.textureSettings.overlayTextureSettings.texture_ids,
						should_poison_icon_be_rendered,
						clientConfig.poisonBuildUpSettings.iconTextureSettings.offset_x,
						clientConfig.poisonBuildUpSettings.iconTextureSettings.offset_y,
						clientConfig.poisonBuildUpSettings.iconTextureSettings.texture_heights,
						clientConfig.poisonBuildUpSettings.iconTextureSettings.texture_widths,
						clientConfig.poisonBuildUpSettings.iconTextureSettings.texture_ids,
						clientConfig.poisonBuildUpSettings.enable_smooth_animation,
						clientConfig.poisonBuildUpSettings.animationSettings.animation_interval,
						clientConfig.poisonBuildUpSettings.animationSettings.max_value_change_is_animated,
						should_poison_number_be_rendered,
						clientConfig.poisonBuildUpSettings.numberSettings.show_max_value,
						clientConfig.poisonBuildUpSettings.numberSettings.offset_x + dynamic_offset_x,
						clientConfig.poisonBuildUpSettings.numberSettings.offset_y + dynamic_offset_y,
						clientConfig.poisonBuildUpSettings.numberSettings.color.toInt()
				);
				if (should_poison_bar_be_rendered || should_poison_icon_be_rendered|| should_poison_number_be_rendered) {
					dynamic_offset_x = clientConfig.poisonBuildUpSettings.dynamic_offset_increase_x;
					dynamic_offset_y = clientConfig.poisonBuildUpSettings.dynamic_offset_increase_y;
				}
				// endregion poison

				// region shock
				double shockBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUp());
				double maxShockBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxShockBuildUp());

				boolean should_shock_bar_be_rendered = clientConfig.shockBuildUpSettings.show_bar && maxShockBuildUp > 0 && (shockBuildUp > 0 || clientConfig.shockBuildUpSettings.show_empty_bar);
				boolean should_shock_icon_be_rendered = clientConfig.shockBuildUpSettings.show_icon && maxShockBuildUp > 0 && (shockBuildUp > 0 || clientConfig.shockBuildUpSettings.iconTextureSettings.show_when_bar_empty);
				boolean should_shock_number_be_rendered = clientConfig.shockBuildUpSettings.show_number && maxShockBuildUp > 0 && (shockBuildUp > 0 || clientConfig.shockBuildUpSettings.numberSettings.show_when_bar_empty);

				ResourceBarAPIClient.drawResourceBar(
						minecraftClient,
						minecraftClient.textRenderer,
						matrixStack,
						BLEEDING_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_overlay.png"),
								null
						},
						should_shock_bar_be_rendered,
						shockBuildUp,
						maxShockBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUpReduction()),
						0,
						clientConfig.shockBuildUpSettings.positionSettings.origin,
						clientConfig.shockBuildUpSettings.positionSettings.offsets_x,
						clientConfig.shockBuildUpSettings.positionSettings.offsets_y,
						dynamic_offset_x,
						dynamic_offset_y,
						clientConfig.shockBuildUpSettings.fill_direction,
						clientConfig.shockBuildUpSettings.textureSettings.backgroundTextureSettings.texture_heights,
						clientConfig.shockBuildUpSettings.textureSettings.backgroundTextureSettings.texture_widths,
						clientConfig.shockBuildUpSettings.textureSettings.backgroundTextureSettings.texture_ids,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.offset_x,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.offset_y,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.texture_heights,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.texture_widths,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
						clientConfig.shockBuildUpSettings.textureSettings.progressTextureSettings.progress_texture_ids,
						0,
						0,
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
						clientConfig.shockBuildUpSettings.show_current_value_overlay,
						clientConfig.shockBuildUpSettings.textureSettings.overlayTextureSettings.offset_x,
						clientConfig.shockBuildUpSettings.textureSettings.overlayTextureSettings.offset_y,
						clientConfig.shockBuildUpSettings.textureSettings.overlayTextureSettings.texture_heights,
						clientConfig.shockBuildUpSettings.textureSettings.overlayTextureSettings.texture_widths,
						clientConfig.shockBuildUpSettings.textureSettings.overlayTextureSettings.texture_ids,
						should_shock_icon_be_rendered,
						clientConfig.shockBuildUpSettings.iconTextureSettings.offset_x,
						clientConfig.shockBuildUpSettings.iconTextureSettings.offset_y,
						clientConfig.shockBuildUpSettings.iconTextureSettings.texture_heights,
						clientConfig.shockBuildUpSettings.iconTextureSettings.texture_widths,
						clientConfig.shockBuildUpSettings.iconTextureSettings.texture_ids,
						clientConfig.shockBuildUpSettings.enable_smooth_animation,
						clientConfig.shockBuildUpSettings.animationSettings.animation_interval,
						clientConfig.shockBuildUpSettings.animationSettings.max_value_change_is_animated,
						should_shock_number_be_rendered,
						clientConfig.shockBuildUpSettings.numberSettings.show_max_value,
						clientConfig.shockBuildUpSettings.numberSettings.offset_x + dynamic_offset_x,
						clientConfig.shockBuildUpSettings.numberSettings.offset_y + dynamic_offset_y,
						clientConfig.shockBuildUpSettings.numberSettings.color.toInt()
				);
				if (should_shock_bar_be_rendered || should_shock_icon_be_rendered|| should_shock_number_be_rendered) {
					dynamic_offset_x = clientConfig.shockBuildUpSettings.dynamic_offset_increase_x;
					dynamic_offset_y = clientConfig.shockBuildUpSettings.dynamic_offset_increase_y;
				}
				// endregion shock

				// region stagger
				double staggerBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUp());
				double maxStaggerBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxStaggerBuildUp());

				boolean should_stagger_bar_be_rendered = clientConfig.staggerBuildUpSettings.show_bar && maxStaggerBuildUp > 0 && (staggerBuildUp > 0 || clientConfig.staggerBuildUpSettings.show_empty_bar);
				boolean should_stagger_icon_be_rendered = clientConfig.staggerBuildUpSettings.show_icon && maxStaggerBuildUp > 0 && (staggerBuildUp > 0 || clientConfig.staggerBuildUpSettings.iconTextureSettings.show_when_bar_empty);
				boolean should_stagger_number_be_rendered = clientConfig.staggerBuildUpSettings.show_number && maxStaggerBuildUp > 0 && (staggerBuildUp > 0 || clientConfig.staggerBuildUpSettings.numberSettings.show_when_bar_empty);

				ResourceBarAPIClient.drawResourceBar(
						minecraftClient,
						minecraftClient.textRenderer,
						matrixStack,
						BLEEDING_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_overlay.png"),
								null
						},
						should_stagger_bar_be_rendered,
						staggerBuildUp,
						maxStaggerBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUpReduction()),
						0,
						clientConfig.staggerBuildUpSettings.positionSettings.origin,
						clientConfig.staggerBuildUpSettings.positionSettings.offsets_x,
						clientConfig.staggerBuildUpSettings.positionSettings.offsets_y,
						dynamic_offset_x,
						dynamic_offset_y,
						clientConfig.staggerBuildUpSettings.fill_direction,
						clientConfig.staggerBuildUpSettings.textureSettings.backgroundTextureSettings.texture_heights,
						clientConfig.staggerBuildUpSettings.textureSettings.backgroundTextureSettings.texture_widths,
						clientConfig.staggerBuildUpSettings.textureSettings.backgroundTextureSettings.texture_ids,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.offset_x,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.offset_y,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.texture_heights,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.texture_widths,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
						clientConfig.staggerBuildUpSettings.textureSettings.progressTextureSettings.progress_texture_ids,
						0,
						0,
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
						new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
						clientConfig.staggerBuildUpSettings.show_current_value_overlay,
						clientConfig.staggerBuildUpSettings.textureSettings.overlayTextureSettings.offset_x,
						clientConfig.staggerBuildUpSettings.textureSettings.overlayTextureSettings.offset_y,
						clientConfig.staggerBuildUpSettings.textureSettings.overlayTextureSettings.texture_heights,
						clientConfig.staggerBuildUpSettings.textureSettings.overlayTextureSettings.texture_widths,
						clientConfig.staggerBuildUpSettings.textureSettings.overlayTextureSettings.texture_ids,
						should_stagger_icon_be_rendered,
						clientConfig.staggerBuildUpSettings.iconTextureSettings.offset_x,
						clientConfig.staggerBuildUpSettings.iconTextureSettings.offset_y,
						clientConfig.staggerBuildUpSettings.iconTextureSettings.texture_heights,
						clientConfig.staggerBuildUpSettings.iconTextureSettings.texture_widths,
						clientConfig.staggerBuildUpSettings.iconTextureSettings.texture_ids,
						clientConfig.staggerBuildUpSettings.enable_smooth_animation,
						clientConfig.staggerBuildUpSettings.animationSettings.animation_interval,
						clientConfig.staggerBuildUpSettings.animationSettings.max_value_change_is_animated,
						should_stagger_number_be_rendered,
						clientConfig.staggerBuildUpSettings.numberSettings.show_max_value,
						clientConfig.staggerBuildUpSettings.numberSettings.offset_x + dynamic_offset_x,
						clientConfig.staggerBuildUpSettings.numberSettings.offset_y + dynamic_offset_y,
						clientConfig.staggerBuildUpSettings.numberSettings.color.toInt()
				);
//				if (should_stagger_bar_be_rendered || should_stagger_icon_be_rendered|| should_stagger_number_be_rendered) {
//					dynamic_offset_x = clientConfig.staggerBuildUpSettings.dynamic_offset_increase_x;
//					dynamic_offset_y = clientConfig.staggerBuildUpSettings.dynamic_offset_increase_y;
//				}
				// endregion stagger
			}
		});
		ConfigApi.event().onUpdateClient((identifier, config) -> {
			if (identifier.equals(Identifier.of(OverhauledDamage.MOD_ID, "client"))) {
				ResourceBarAPIClient.clearCache(
						BLEEDING_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_bleeding_overlay.png"),
								null
						}
				);
				ResourceBarAPIClient.clearCache(
						BURN_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_burn_overlay.png"),
								null
						}
				);
				ResourceBarAPIClient.clearCache(
						FREEZE_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_freeze_overlay.png"),
								null
						}
				);
				ResourceBarAPIClient.clearCache(
						POISON_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_poison_overlay.png"),
								null
						}
				);
				ResourceBarAPIClient.clearCache(
						SHOCK_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_shock_overlay.png"),
								null
						}
				);
				ResourceBarAPIClient.clearCache(
						STAGGER_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-31,
								18,
								5,
								62,
								5,
								62,
								0,
								0,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_background.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_decrease_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_increase_animation.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress_increase_value.png"),
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_progress.png"),
								null,
								Identifier.of("overhauleddamage", "textures/gui/sprites/hud/horizontal_stagger_overlay.png"),
								null
						}
				);
			}
		});
	}
}