package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.OverhauledDamageClient;
import com.github.theredbrain.overhauleddamage.config.ClientConfig;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.entity.LivingEntityHelper;
import com.github.theredbrain.overhauleddamage.gui.hud.DuckGuiMixin;
import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientEventsRegistry {
	private static final String BLEEDING_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":bleeding";
	private static final String BURN_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":burn";
	private static final String FREEZE_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":freeze";
	private static final String POISON_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":poison";
	private static final String SHOCK_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":shock";
	private static final String STAGGER_BAR_IDENTIFIER_STRING = OverhauledDamage.MOD_ID + ":stagger";

	private static final Identifier ICON_BLEEDING_CONTAINER = OverhauledDamage.identifier("hud/icon_bleeding_container");
	private static final Identifier ICON_BLEEDING_FULL = OverhauledDamage.identifier("hud/icon_bleeding_full");
	private static final Identifier ICON_BLEEDING_HALF = OverhauledDamage.identifier("hud/icon_bleeding_half");
	private static final Identifier ICON_BLEEDING_CONTAINER_BLINKING = OverhauledDamage.identifier("hud/icon_bleeding_container_blinking");
	private static final Identifier ICON_BLEEDING_FULL_BLINKING = OverhauledDamage.identifier("hud/icon_bleeding_full_blinking");
	private static final Identifier ICON_BLEEDING_HALF_BLINKING = OverhauledDamage.identifier("hud/icon_bleeding_half_blinking");

	private static final Identifier ICON_BURNING_CONTAINER = OverhauledDamage.identifier("hud/icon_burning_container");
	private static final Identifier ICON_BURNING_FULL = OverhauledDamage.identifier("hud/icon_burning_full");
	private static final Identifier ICON_BURNING_HALF = OverhauledDamage.identifier("hud/icon_burning_half");
	private static final Identifier ICON_BURNING_CONTAINER_BLINKING = OverhauledDamage.identifier("hud/icon_burning_container_blinking");
	private static final Identifier ICON_BURNING_FULL_BLINKING = OverhauledDamage.identifier("hud/icon_burning_full_blinking");
	private static final Identifier ICON_BURNING_HALF_BLINKING = OverhauledDamage.identifier("hud/icon_burning_half_blinking");

	private static final Identifier ICON_FREEZE_CONTAINER = OverhauledDamage.identifier("hud/icon_freeze_container");
	private static final Identifier ICON_FREEZE_FULL = OverhauledDamage.identifier("hud/icon_freeze_full");
	private static final Identifier ICON_FREEZE_HALF = OverhauledDamage.identifier("hud/icon_freeze_half");
	private static final Identifier ICON_FREEZE_CONTAINER_BLINKING = OverhauledDamage.identifier("hud/icon_freeze_container_blinking");
	private static final Identifier ICON_FREEZE_FULL_BLINKING = OverhauledDamage.identifier("hud/icon_freeze_full_blinking");
	private static final Identifier ICON_FREEZE_HALF_BLINKING = OverhauledDamage.identifier("hud/icon_freeze_half_blinking");

	private static final Identifier ICON_POISON_CONTAINER = OverhauledDamage.identifier("hud/icon_poison_container");
	private static final Identifier ICON_POISON_FULL = OverhauledDamage.identifier("hud/icon_poison_full");
	private static final Identifier ICON_POISON_HALF = OverhauledDamage.identifier("hud/icon_poison_half");
	private static final Identifier ICON_POISON_CONTAINER_BLINKING = OverhauledDamage.identifier("hud/icon_poison_container_blinking");
	private static final Identifier ICON_POISON_FULL_BLINKING = OverhauledDamage.identifier("hud/icon_poison_full_blinking");
	private static final Identifier ICON_POISON_HALF_BLINKING = OverhauledDamage.identifier("hud/icon_poison_half_blinking");

	private static final Identifier ICON_SHOCK_CONTAINER = OverhauledDamage.identifier("hud/icon_shock_container");
	private static final Identifier ICON_SHOCK_FULL = OverhauledDamage.identifier("hud/icon_shock_full");
	private static final Identifier ICON_SHOCK_HALF = OverhauledDamage.identifier("hud/icon_shock_half");
	private static final Identifier ICON_SHOCK_CONTAINER_BLINKING = OverhauledDamage.identifier("hud/icon_shock_container_blinking");
	private static final Identifier ICON_SHOCK_FULL_BLINKING = OverhauledDamage.identifier("hud/icon_shock_full_blinking");
	private static final Identifier ICON_SHOCK_HALF_BLINKING = OverhauledDamage.identifier("hud/icon_shock_half_blinking");

	private static final Identifier ICON_STAGGER_CONTAINER = OverhauledDamage.identifier("hud/icon_stagger_container");
	private static final Identifier ICON_STAGGER_FULL = OverhauledDamage.identifier("hud/icon_stagger_full");
	private static final Identifier ICON_STAGGER_HALF = OverhauledDamage.identifier("hud/icon_stagger_half");
	private static final Identifier ICON_STAGGER_CONTAINER_BLINKING = OverhauledDamage.identifier("hud/icon_stagger_container_blinking");
	private static final Identifier ICON_STAGGER_FULL_BLINKING = OverhauledDamage.identifier("hud/icon_stagger_full_blinking");
	private static final Identifier ICON_STAGGER_HALF_BLINKING = OverhauledDamage.identifier("hud/icon_stagger_half_blinking");

	public static void initializeClientEvents() {
		HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR, OverhauledDamage.identifier("effect_build_ups"), ((guiGraphicsExtractor, deltaTracker) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer localPlayer = minecraft.player;
			ClientConfig clientConfig = OverhauledDamageClient.CLIENT_CONFIG;

			if (localPlayer != null && !localPlayer.isCreative() && !minecraft.options.hideGui) {

				int dynamic_offset_x = 0;
				int dynamic_offset_y = 0;

				boolean shouldBlink = false;

				DuckGuiMixin gui = ((DuckGuiMixin) minecraft.gui);

				// region bleeding
				int bleedingBuildUp = Mth.ceil(LivingEntityHelper.getBleedingBuildUp(localPlayer));

				int currentDisplayBleedingBuildUp = bleedingBuildUp;

				if (clientConfig.bleeding_build_up_settings.icon_bar_settings.enable_icon_blinking.get()) {
					shouldBlink = gui.overhauleddamage$getBleedingBuildUpIconBlinkTime() > gui.overhauleddamage$getTickCount() && (gui.overhauleddamage$getBleedingBuildUpIconBlinkTime() - gui.overhauleddamage$getTickCount()) / 3L % 2L == 1L;
					long l = Util.getMillis();
					if (bleedingBuildUp < gui.overhauleddamage$getLastBleedingBuildUp()) {
						gui.overhauleddamage$setLastBleedingBuildUpTime(l);
						gui.overhauleddamage$setBleedingBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 10);
					} else if (bleedingBuildUp > gui.overhauleddamage$getLastBleedingBuildUp()) {
						gui.overhauleddamage$setLastBleedingBuildUpTime(l);
						gui.overhauleddamage$setBleedingBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 5);
					}

					if (l - gui.overhauleddamage$getLastBleedingBuildUpTime() > 100L) {
						gui.overhauleddamage$setDisplayBleedingBuildUp(bleedingBuildUp);
						gui.overhauleddamage$setLastBleedingBuildUpTime(l);
					}

					gui.overhauleddamage$setLastBleedingBuildUp(bleedingBuildUp);
					currentDisplayBleedingBuildUp = gui.overhauleddamage$getDisplayBleedingBuildUp();
				}

				double maxBleedingBuildUp = Math.max(Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getMaxBleedingBuildUp()), Math.max(currentDisplayBleedingBuildUp, bleedingBuildUp));

				if (maxBleedingBuildUp > 0) {
					boolean should_bleeding_bar_be_rendered = bleedingBuildUp > 0 || clientConfig.bleeding_build_up_settings.show_empty_bar;
					boolean should_bleeding_icon_be_rendered = clientConfig.bleeding_build_up_settings.smooth_bar_settings.show_icon && (bleedingBuildUp > 0 || clientConfig.bleeding_build_up_settings.smooth_bar_settings.icon_texture_settings.show_when_bar_empty);
					boolean should_bleeding_number_be_rendered = clientConfig.bleeding_build_up_settings.show_number && (bleedingBuildUp > 0 || clientConfig.bleeding_build_up_settings.number_settings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.bleeding_build_up_settings.origin);

					if (clientConfig.bleeding_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_bleeding_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayBleedingBuildUp,
								maxBleedingBuildUp,
								shouldBlink ? ICON_BLEEDING_CONTAINER_BLINKING : ICON_BLEEDING_CONTAINER,
								shouldBlink ? ICON_BLEEDING_FULL_BLINKING : ICON_BLEEDING_FULL,
								shouldBlink ? ICON_BLEEDING_HALF_BLINKING : ICON_BLEEDING_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.bleeding_build_up_settings.icon_bar_settings.offset_x.get() + dynamic_offset_x,
								clientConfig.bleeding_build_up_settings.icon_bar_settings.offset_y.get() + dynamic_offset_y,
								clientConfig.bleeding_build_up_settings.fill_direction,
								clientConfig.bleeding_build_up_settings.icon_bar_settings.reverse_stack_direction.get(),
								clientConfig.bleeding_build_up_settings.icon_bar_settings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.bleeding_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_bleeding_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphicsExtractor,
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
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_background.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress_decrease_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress_increase_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress_increase_value.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress.png"),
										null,
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_overlay.png"),
										null
								},
								bleedingBuildUp,
								maxBleedingBuildUp,
								Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getBleedingBuildUpReduction()),
								maxBleedingBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.position_settings.offsets_x,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.position_settings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.bleeding_build_up_settings.fill_direction,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_heights,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_widths,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_ids,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_x,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_y,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_heights,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_widths,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_decrease_animation_texture_ids,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_animation_texture_ids,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_value_texture_ids,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.show_current_value_overlay,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_x,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_y,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_heights,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_widths,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_ids,
								should_bleeding_icon_be_rendered,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_x,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_y,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_heights,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_widths,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_ids,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.enable_smooth_animation,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.animation_settings.animation_interval,
								clientConfig.bleeding_build_up_settings.smooth_bar_settings.animation_settings.max_value_change_is_animated
						);
					}
					if (should_bleeding_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphicsExtractor,
								BLEEDING_BAR_IDENTIFIER_STRING,
								bleedingBuildUp,
								maxBleedingBuildUp,
								maxBleedingBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.bleeding_build_up_settings.number_settings.show_max_value,
								clientConfig.bleeding_build_up_settings.number_settings.offset_x,
								clientConfig.bleeding_build_up_settings.number_settings.offset_y,
								clientConfig.bleeding_build_up_settings.number_settings.color.toInt()
						);
					}
					if ((should_bleeding_bar_be_rendered && clientConfig.bleeding_build_up_settings.bar_display != ResourceBarAPI.ResourceBarDisplay.NONE) || should_bleeding_icon_be_rendered || should_bleeding_number_be_rendered) {
						dynamic_offset_x += clientConfig.bleeding_build_up_settings.dynamic_offset_increase_x;
						dynamic_offset_y += clientConfig.bleeding_build_up_settings.dynamic_offset_increase_y;
					}
				}
				// endregion bleeding

				// region burn
				int burnBuildUp = Mth.ceil(LivingEntityHelper.getBurnBuildUp(localPlayer));

				int currentDisplayBurnBuildUp = burnBuildUp;

				if (clientConfig.burn_build_up_settings.icon_bar_settings.enable_icon_blinking.get()) {
					shouldBlink = gui.overhauleddamage$getBurnBuildUpIconBlinkTime() > gui.overhauleddamage$getTickCount() && (gui.overhauleddamage$getBurnBuildUpIconBlinkTime() - gui.overhauleddamage$getTickCount()) / 3L % 2L == 1L;
					long l = Util.getMillis();
					if (burnBuildUp < gui.overhauleddamage$getLastBurnBuildUp()) {
						gui.overhauleddamage$setLastBurnBuildUpTime(l);
						gui.overhauleddamage$setBurnBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 10);
					} else if (burnBuildUp > gui.overhauleddamage$getLastBurnBuildUp()) {
						gui.overhauleddamage$setLastBurnBuildUpTime(l);
						gui.overhauleddamage$setBurnBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 5);
					}

					if (l - gui.overhauleddamage$getLastBurnBuildUpTime() > 100L) {
						gui.overhauleddamage$setDisplayBurnBuildUp(burnBuildUp);
						gui.overhauleddamage$setLastBurnBuildUpTime(l);
					}

					gui.overhauleddamage$setLastBurnBuildUp(burnBuildUp);
					currentDisplayBurnBuildUp = gui.overhauleddamage$getDisplayBurnBuildUp();
				}

				double maxBurnBuildUp = Math.max(Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getMaxBurnBuildUp()), Math.max(currentDisplayBurnBuildUp, burnBuildUp));

				if (maxBurnBuildUp > 0) {
					boolean should_burn_bar_be_rendered = burnBuildUp > 0 || clientConfig.burn_build_up_settings.show_empty_bar;
					boolean should_burn_icon_be_rendered = clientConfig.burn_build_up_settings.smooth_bar_settings.show_icon && (burnBuildUp > 0 || clientConfig.burn_build_up_settings.smooth_bar_settings.icon_texture_settings.show_when_bar_empty);
					boolean should_burn_number_be_rendered = clientConfig.burn_build_up_settings.show_number && (burnBuildUp > 0 || clientConfig.burn_build_up_settings.number_settings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.burn_build_up_settings.origin);

					if (clientConfig.burn_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_burn_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayBurnBuildUp,
								maxBurnBuildUp,
								shouldBlink ? ICON_BURNING_CONTAINER_BLINKING : ICON_BURNING_CONTAINER,
								shouldBlink ? ICON_BURNING_FULL_BLINKING : ICON_BURNING_FULL,
								shouldBlink ? ICON_BURNING_HALF_BLINKING : ICON_BURNING_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.burn_build_up_settings.icon_bar_settings.offset_x.get() + dynamic_offset_x,
								clientConfig.burn_build_up_settings.icon_bar_settings.offset_y.get() + dynamic_offset_y,
								clientConfig.burn_build_up_settings.fill_direction,
								clientConfig.burn_build_up_settings.icon_bar_settings.reverse_stack_direction.get(),
								clientConfig.burn_build_up_settings.icon_bar_settings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.burn_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_burn_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphicsExtractor,
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
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_background.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress_decrease_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress_increase_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress_increase_value.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress.png"),
										null,
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_overlay.png"),
										null
								},
								burnBuildUp,
								maxBurnBuildUp,
								Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getBurnBuildUpReduction()),
								maxBurnBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.burn_build_up_settings.smooth_bar_settings.position_settings.offsets_x,
								clientConfig.burn_build_up_settings.smooth_bar_settings.position_settings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.burn_build_up_settings.fill_direction,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_heights,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_widths,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_ids,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_x,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_y,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_heights,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_widths,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_decrease_animation_texture_ids,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_animation_texture_ids,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_value_texture_ids,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.burn_build_up_settings.smooth_bar_settings.show_current_value_overlay,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_x,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_y,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_heights,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_widths,
								clientConfig.burn_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_ids,
								should_burn_icon_be_rendered,
								clientConfig.burn_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_x,
								clientConfig.burn_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_y,
								clientConfig.burn_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_heights,
								clientConfig.burn_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_widths,
								clientConfig.burn_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_ids,
								clientConfig.burn_build_up_settings.smooth_bar_settings.enable_smooth_animation,
								clientConfig.burn_build_up_settings.smooth_bar_settings.animation_settings.animation_interval,
								clientConfig.burn_build_up_settings.smooth_bar_settings.animation_settings.max_value_change_is_animated
						);
					}
					if (should_burn_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphicsExtractor,
								BURN_BAR_IDENTIFIER_STRING,
								burnBuildUp,
								maxBurnBuildUp,
								maxBurnBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.burn_build_up_settings.number_settings.show_max_value,
								clientConfig.burn_build_up_settings.number_settings.offset_x,
								clientConfig.burn_build_up_settings.number_settings.offset_y,
								clientConfig.burn_build_up_settings.number_settings.color.toInt()
						);
					}
					if (should_burn_bar_be_rendered || should_burn_icon_be_rendered || should_burn_number_be_rendered) {
						dynamic_offset_x += clientConfig.burn_build_up_settings.dynamic_offset_increase_x;
						dynamic_offset_y += clientConfig.burn_build_up_settings.dynamic_offset_increase_y;
					}
				}
				// endregion burn

				// region freeze
				int freezeBuildUp = Mth.ceil(LivingEntityHelper.getFreezeBuildUp(localPlayer));

				int currentDisplayFreezeBuildUp = freezeBuildUp;

				if (clientConfig.freeze_build_up_settings.icon_bar_settings.enable_icon_blinking.get()) {
					shouldBlink = gui.overhauleddamage$getFreezeBuildUpIconBlinkTime() > gui.overhauleddamage$getTickCount() && (gui.overhauleddamage$getFreezeBuildUpIconBlinkTime() - gui.overhauleddamage$getTickCount()) / 3L % 2L == 1L;
					long l = Util.getMillis();
					if (freezeBuildUp < gui.overhauleddamage$getLastFreezeBuildUp()) {
						gui.overhauleddamage$setLastFreezeBuildUpTime(l);
						gui.overhauleddamage$setFreezeBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 10);
					} else if (freezeBuildUp > gui.overhauleddamage$getLastFreezeBuildUp()) {
						gui.overhauleddamage$setLastFreezeBuildUpTime(l);
						gui.overhauleddamage$setFreezeBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 5);
					}

					if (l - gui.overhauleddamage$getLastFreezeBuildUpTime() > 100L) {
						gui.overhauleddamage$setDisplayFreezeBuildUp(freezeBuildUp);
						gui.overhauleddamage$setLastFreezeBuildUpTime(l);
					}

					gui.overhauleddamage$setLastFreezeBuildUp(freezeBuildUp);
					currentDisplayFreezeBuildUp = gui.overhauleddamage$getDisplayFreezeBuildUp();
				}

				double maxFreezeBuildUp = Math.max(Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getMaxFreezeBuildUp()), Math.max(currentDisplayFreezeBuildUp, freezeBuildUp));

				if (maxFreezeBuildUp > 0) {
					boolean should_freeze_bar_be_rendered = freezeBuildUp > 0 || clientConfig.freeze_build_up_settings.show_empty_bar;
					boolean should_freeze_icon_be_rendered = clientConfig.freeze_build_up_settings.smooth_bar_settings.show_icon && (freezeBuildUp > 0 || clientConfig.freeze_build_up_settings.smooth_bar_settings.icon_texture_settings.show_when_bar_empty);
					boolean should_freeze_number_be_rendered = clientConfig.freeze_build_up_settings.show_number && (freezeBuildUp > 0 || clientConfig.freeze_build_up_settings.number_settings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.freeze_build_up_settings.origin);

					if (clientConfig.freeze_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_freeze_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayFreezeBuildUp,
								maxFreezeBuildUp,
								shouldBlink ? ICON_FREEZE_CONTAINER_BLINKING : ICON_FREEZE_CONTAINER,
								shouldBlink ? ICON_FREEZE_FULL_BLINKING : ICON_FREEZE_FULL,
								shouldBlink ? ICON_FREEZE_HALF_BLINKING : ICON_FREEZE_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.freeze_build_up_settings.icon_bar_settings.offset_x.get() + dynamic_offset_x,
								clientConfig.freeze_build_up_settings.icon_bar_settings.offset_y.get() + dynamic_offset_y,
								clientConfig.freeze_build_up_settings.fill_direction,
								clientConfig.freeze_build_up_settings.icon_bar_settings.reverse_stack_direction.get(),
								clientConfig.freeze_build_up_settings.icon_bar_settings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.freeze_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_freeze_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphicsExtractor,
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
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_background.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress_decrease_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress_increase_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress_increase_value.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress.png"),
										null,
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_overlay.png"),
										null
								},
								freezeBuildUp,
								maxFreezeBuildUp,
								Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getFreezeBuildUpReduction()),
								maxFreezeBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.freeze_build_up_settings.smooth_bar_settings.position_settings.offsets_x,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.position_settings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.freeze_build_up_settings.fill_direction,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_heights,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_widths,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_ids,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_x,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_y,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_heights,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_widths,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_decrease_animation_texture_ids,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_animation_texture_ids,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_value_texture_ids,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.freeze_build_up_settings.smooth_bar_settings.show_current_value_overlay,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_x,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_y,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_heights,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_widths,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_ids,
								should_freeze_icon_be_rendered,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_x,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_y,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_heights,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_widths,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_ids,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.enable_smooth_animation,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.animation_settings.animation_interval,
								clientConfig.freeze_build_up_settings.smooth_bar_settings.animation_settings.max_value_change_is_animated
						);
					}
					if (should_freeze_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphicsExtractor,
								FREEZE_BAR_IDENTIFIER_STRING,
								freezeBuildUp,
								maxFreezeBuildUp,
								maxFreezeBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.freeze_build_up_settings.number_settings.show_max_value,
								clientConfig.freeze_build_up_settings.number_settings.offset_x,
								clientConfig.freeze_build_up_settings.number_settings.offset_y,
								clientConfig.freeze_build_up_settings.number_settings.color.toInt()
						);
					}
					if (should_freeze_bar_be_rendered || should_freeze_icon_be_rendered || should_freeze_number_be_rendered) {
						dynamic_offset_x += clientConfig.freeze_build_up_settings.dynamic_offset_increase_x;
						dynamic_offset_y += clientConfig.freeze_build_up_settings.dynamic_offset_increase_y;
					}
				}
				// endregion freeze

				// region poison
				int poisonBuildUp = Mth.ceil(LivingEntityHelper.getPoisonBuildUp(localPlayer));

				int currentDisplayPoisonBuildUp = poisonBuildUp;

				if (clientConfig.poison_build_up_settings.icon_bar_settings.enable_icon_blinking.get()) {
					shouldBlink = gui.overhauleddamage$getPoisonBuildUpIconBlinkTime() > gui.overhauleddamage$getTickCount() && (gui.overhauleddamage$getPoisonBuildUpIconBlinkTime() - gui.overhauleddamage$getTickCount()) / 3L % 2L == 1L;
					long l = Util.getMillis();
					if (poisonBuildUp < gui.overhauleddamage$getLastPoisonBuildUp()) {
						gui.overhauleddamage$setLastPoisonBuildUpTime(l);
						gui.overhauleddamage$setPoisonBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 10);
					} else if (poisonBuildUp > gui.overhauleddamage$getLastPoisonBuildUp()) {
						gui.overhauleddamage$setLastPoisonBuildUpTime(l);
						gui.overhauleddamage$setPoisonBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 5);
					}

					if (l - gui.overhauleddamage$getLastPoisonBuildUpTime() > 100L) {
						gui.overhauleddamage$setDisplayPoisonBuildUp(poisonBuildUp);
						gui.overhauleddamage$setLastPoisonBuildUpTime(l);
					}

					gui.overhauleddamage$setLastPoisonBuildUp(poisonBuildUp);
					currentDisplayPoisonBuildUp = gui.overhauleddamage$getDisplayPoisonBuildUp();
				}

				double maxPoisonBuildUp = Math.max(Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getMaxPoisonBuildUp()), Math.max(currentDisplayPoisonBuildUp, poisonBuildUp));

				if (maxPoisonBuildUp > 0) {
					boolean should_poison_bar_be_rendered = poisonBuildUp > 0 || clientConfig.poison_build_up_settings.show_empty_bar;
					boolean should_poison_icon_be_rendered = clientConfig.poison_build_up_settings.smooth_bar_settings.show_icon && (poisonBuildUp > 0 || clientConfig.poison_build_up_settings.smooth_bar_settings.icon_texture_settings.show_when_bar_empty);
					boolean should_poison_number_be_rendered = clientConfig.poison_build_up_settings.show_number && (poisonBuildUp > 0 || clientConfig.poison_build_up_settings.number_settings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.poison_build_up_settings.origin);

					if (clientConfig.poison_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_poison_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayPoisonBuildUp,
								maxPoisonBuildUp,
								shouldBlink ? ICON_POISON_CONTAINER_BLINKING : ICON_POISON_CONTAINER,
								shouldBlink ? ICON_POISON_FULL_BLINKING : ICON_POISON_FULL,
								shouldBlink ? ICON_POISON_HALF_BLINKING : ICON_POISON_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.poison_build_up_settings.icon_bar_settings.offset_x.get() + dynamic_offset_x,
								clientConfig.poison_build_up_settings.icon_bar_settings.offset_y.get() + dynamic_offset_y,
								clientConfig.poison_build_up_settings.fill_direction,
								clientConfig.poison_build_up_settings.icon_bar_settings.reverse_stack_direction.get(),
								clientConfig.poison_build_up_settings.icon_bar_settings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.poison_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_poison_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphicsExtractor,
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
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_background.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress_decrease_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress_increase_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress_increase_value.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress.png"),
										null,
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_overlay.png"),
										null
								},
								poisonBuildUp,
								maxPoisonBuildUp,
								Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getPoisonBuildUpReduction()),
								maxPoisonBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.poison_build_up_settings.smooth_bar_settings.position_settings.offsets_x,
								clientConfig.poison_build_up_settings.smooth_bar_settings.position_settings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.poison_build_up_settings.fill_direction,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_heights,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_widths,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_ids,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_x,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_y,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_heights,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_widths,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_decrease_animation_texture_ids,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_animation_texture_ids,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_value_texture_ids,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.poison_build_up_settings.smooth_bar_settings.show_current_value_overlay,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_x,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_y,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_heights,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_widths,
								clientConfig.poison_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_ids,
								should_poison_icon_be_rendered,
								clientConfig.poison_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_x,
								clientConfig.poison_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_y,
								clientConfig.poison_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_heights,
								clientConfig.poison_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_widths,
								clientConfig.poison_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_ids,
								clientConfig.poison_build_up_settings.smooth_bar_settings.enable_smooth_animation,
								clientConfig.poison_build_up_settings.smooth_bar_settings.animation_settings.animation_interval,
								clientConfig.poison_build_up_settings.smooth_bar_settings.animation_settings.max_value_change_is_animated
						);
					}
					if (should_poison_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphicsExtractor,
								POISON_BAR_IDENTIFIER_STRING,
								poisonBuildUp,
								maxPoisonBuildUp,
								maxPoisonBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.poison_build_up_settings.number_settings.show_max_value,
								clientConfig.poison_build_up_settings.number_settings.offset_x,
								clientConfig.poison_build_up_settings.number_settings.offset_y,
								clientConfig.poison_build_up_settings.number_settings.color.toInt()
						);
					}
					if (should_poison_bar_be_rendered || should_poison_icon_be_rendered || should_poison_number_be_rendered) {
						dynamic_offset_x += clientConfig.poison_build_up_settings.dynamic_offset_increase_x;
						dynamic_offset_y += clientConfig.poison_build_up_settings.dynamic_offset_increase_y;
					}
				}
				// endregion poison

				// region shock
				int shockBuildUp = Mth.ceil(LivingEntityHelper.getShockBuildUp(localPlayer));

				int currentDisplayShockBuildUp = shockBuildUp;

				if (clientConfig.shock_build_up_settings.icon_bar_settings.enable_icon_blinking.get()) {
					shouldBlink = gui.overhauleddamage$getShockBuildUpIconBlinkTime() > gui.overhauleddamage$getTickCount() && (gui.overhauleddamage$getShockBuildUpIconBlinkTime() - gui.overhauleddamage$getTickCount()) / 3L % 2L == 1L;
					long l = Util.getMillis();
					if (shockBuildUp < gui.overhauleddamage$getLastShockBuildUp()) {
						gui.overhauleddamage$setLastShockBuildUpTime(l);
						gui.overhauleddamage$setShockBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 10);
					} else if (shockBuildUp > gui.overhauleddamage$getLastShockBuildUp()) {
						gui.overhauleddamage$setLastShockBuildUpTime(l);
						gui.overhauleddamage$setShockBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 5);
					}

					if (l - gui.overhauleddamage$getLastShockBuildUpTime() > 100L) {
						gui.overhauleddamage$setDisplayShockBuildUp(shockBuildUp);
						gui.overhauleddamage$setLastShockBuildUpTime(l);
					}

					gui.overhauleddamage$setLastShockBuildUp(shockBuildUp);
					currentDisplayShockBuildUp = gui.overhauleddamage$getDisplayShockBuildUp();
				}

				double maxShockBuildUp = Math.max(Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getMaxShockBuildUp()), Math.max(currentDisplayShockBuildUp, shockBuildUp));

				if (maxShockBuildUp > 0) {
					boolean should_shock_bar_be_rendered = shockBuildUp > 0 || clientConfig.shock_build_up_settings.show_empty_bar;
					boolean should_shock_icon_be_rendered = clientConfig.shock_build_up_settings.smooth_bar_settings.show_icon && (shockBuildUp > 0 || clientConfig.shock_build_up_settings.smooth_bar_settings.icon_texture_settings.show_when_bar_empty);
					boolean should_shock_number_be_rendered = clientConfig.shock_build_up_settings.show_number && (shockBuildUp > 0 || clientConfig.shock_build_up_settings.number_settings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.shock_build_up_settings.origin);

					if (clientConfig.shock_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_shock_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayShockBuildUp,
								maxShockBuildUp,
								shouldBlink ? ICON_SHOCK_CONTAINER_BLINKING : ICON_SHOCK_CONTAINER,
								shouldBlink ? ICON_SHOCK_FULL_BLINKING : ICON_SHOCK_FULL,
								shouldBlink ? ICON_SHOCK_HALF_BLINKING : ICON_SHOCK_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.shock_build_up_settings.icon_bar_settings.offset_x.get() + dynamic_offset_x,
								clientConfig.shock_build_up_settings.icon_bar_settings.offset_y.get() + dynamic_offset_y,
								clientConfig.shock_build_up_settings.fill_direction,
								clientConfig.shock_build_up_settings.icon_bar_settings.reverse_stack_direction.get(),
								clientConfig.shock_build_up_settings.icon_bar_settings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.shock_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_shock_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphicsExtractor,
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
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_background.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress_decrease_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress_increase_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress_increase_value.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress.png"),
										null,
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_overlay.png"),
										null
								},
								shockBuildUp,
								maxShockBuildUp,
								Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getShockBuildUpReduction()),
								maxShockBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.shock_build_up_settings.smooth_bar_settings.position_settings.offsets_x,
								clientConfig.shock_build_up_settings.smooth_bar_settings.position_settings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.shock_build_up_settings.fill_direction,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_heights,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_widths,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_ids,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_x,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_y,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_heights,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_widths,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_decrease_animation_texture_ids,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_animation_texture_ids,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_value_texture_ids,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.shock_build_up_settings.smooth_bar_settings.show_current_value_overlay,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_x,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_y,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_heights,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_widths,
								clientConfig.shock_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_ids,
								should_shock_icon_be_rendered,
								clientConfig.shock_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_x,
								clientConfig.shock_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_y,
								clientConfig.shock_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_heights,
								clientConfig.shock_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_widths,
								clientConfig.shock_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_ids,
								clientConfig.shock_build_up_settings.smooth_bar_settings.enable_smooth_animation,
								clientConfig.shock_build_up_settings.smooth_bar_settings.animation_settings.animation_interval,
								clientConfig.shock_build_up_settings.smooth_bar_settings.animation_settings.max_value_change_is_animated
						);
					}
					if (should_shock_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphicsExtractor,
								SHOCK_BAR_IDENTIFIER_STRING,
								shockBuildUp,
								maxShockBuildUp,
								maxShockBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.shock_build_up_settings.number_settings.show_max_value,
								clientConfig.shock_build_up_settings.number_settings.offset_x,
								clientConfig.shock_build_up_settings.number_settings.offset_y,
								clientConfig.shock_build_up_settings.number_settings.color.toInt()
						);
					}
					if (should_shock_bar_be_rendered || should_shock_icon_be_rendered || should_shock_number_be_rendered) {
						dynamic_offset_x += clientConfig.shock_build_up_settings.dynamic_offset_increase_x;
						dynamic_offset_y += clientConfig.shock_build_up_settings.dynamic_offset_increase_y;
					}
				}
				// endregion shock

				// region stagger
				int staggerBuildUp = Mth.ceil(LivingEntityHelper.getStaggerBuildUp(localPlayer));

				int currentDisplayStaggerBuildUp = staggerBuildUp;

				if (clientConfig.stagger_build_up_settings.icon_bar_settings.enable_icon_blinking.get()) {
					shouldBlink = gui.overhauleddamage$getStaggerBuildUpIconBlinkTime() > gui.overhauleddamage$getTickCount() && (gui.overhauleddamage$getStaggerBuildUpIconBlinkTime() - gui.overhauleddamage$getTickCount()) / 3L % 2L == 1L;
					long l = Util.getMillis();
					if (staggerBuildUp < gui.overhauleddamage$getLastStaggerBuildUp()) {
						gui.overhauleddamage$setLastStaggerBuildUpTime(l);
						gui.overhauleddamage$setStaggerBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 10);
					} else if (staggerBuildUp > gui.overhauleddamage$getLastStaggerBuildUp()) {
						gui.overhauleddamage$setLastStaggerBuildUpTime(l);
						gui.overhauleddamage$setStaggerBuildUpIconBlinkTime(gui.overhauleddamage$getTickCount() + 5);
					}

					if (l - gui.overhauleddamage$getLastStaggerBuildUpTime() > 100L) {
						gui.overhauleddamage$setDisplayStaggerBuildUp(staggerBuildUp);
						gui.overhauleddamage$setLastStaggerBuildUpTime(l);
					}

					gui.overhauleddamage$setLastStaggerBuildUp(staggerBuildUp);
					currentDisplayStaggerBuildUp = gui.overhauleddamage$getDisplayStaggerBuildUp();
				}

				double maxStaggerBuildUp = Math.max(Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getMaxStaggerBuildUp()), Math.max(currentDisplayStaggerBuildUp, staggerBuildUp));

				if (maxStaggerBuildUp > 0) {
					boolean should_stagger_bar_be_rendered = staggerBuildUp > 0 || clientConfig.stagger_build_up_settings.show_empty_bar;
					boolean should_stagger_icon_be_rendered = clientConfig.stagger_build_up_settings.smooth_bar_settings.show_icon && (staggerBuildUp > 0 || clientConfig.stagger_build_up_settings.smooth_bar_settings.icon_texture_settings.show_when_bar_empty);
					boolean should_stagger_number_be_rendered = clientConfig.stagger_build_up_settings.show_number && (staggerBuildUp > 0 || clientConfig.stagger_build_up_settings.number_settings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.stagger_build_up_settings.origin);

					if (clientConfig.stagger_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_stagger_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayStaggerBuildUp,
								maxStaggerBuildUp,
								shouldBlink ? ICON_STAGGER_CONTAINER_BLINKING : ICON_STAGGER_CONTAINER,
								shouldBlink ? ICON_STAGGER_FULL_BLINKING : ICON_STAGGER_FULL,
								shouldBlink ? ICON_STAGGER_HALF_BLINKING : ICON_STAGGER_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.stagger_build_up_settings.icon_bar_settings.offset_x.get() + dynamic_offset_x,
								clientConfig.stagger_build_up_settings.icon_bar_settings.offset_y.get() + dynamic_offset_y,
								clientConfig.stagger_build_up_settings.fill_direction,
								clientConfig.stagger_build_up_settings.icon_bar_settings.reverse_stack_direction.get(),
								clientConfig.stagger_build_up_settings.icon_bar_settings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.stagger_build_up_settings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_stagger_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphicsExtractor,
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
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_background.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress_decrease_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress_increase_animation.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress_increase_value.png"),
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress.png"),
										null,
										OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_overlay.png"),
										null
								},
								staggerBuildUp,
								maxStaggerBuildUp,
								Mth.ceil(((DuckLivingEntityMixin) localPlayer).overhauleddamage$getStaggerBuildUpReduction()),
								maxStaggerBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.stagger_build_up_settings.smooth_bar_settings.position_settings.offsets_x,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.position_settings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.stagger_build_up_settings.fill_direction,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_heights,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_widths,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.background_texture_settings.texture_ids,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_x,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.offset_y,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_heights,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.texture_widths,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_decrease_animation_texture_ids,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_animation_texture_ids,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_increase_value_texture_ids,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.progress_texture_settings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.stagger_build_up_settings.smooth_bar_settings.show_current_value_overlay,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_x,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.offset_y,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_heights,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_widths,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.texture_settings.overlay_texture_settings.texture_ids,
								should_stagger_icon_be_rendered,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_x,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.icon_texture_settings.offset_y,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_heights,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_widths,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.icon_texture_settings.texture_ids,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.enable_smooth_animation,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.animation_settings.animation_interval,
								clientConfig.stagger_build_up_settings.smooth_bar_settings.animation_settings.max_value_change_is_animated
						);
					}
					if (should_stagger_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphicsExtractor,
								STAGGER_BAR_IDENTIFIER_STRING,
								staggerBuildUp,
								maxStaggerBuildUp,
								maxStaggerBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.stagger_build_up_settings.number_settings.show_max_value,
								clientConfig.stagger_build_up_settings.number_settings.offset_x,
								clientConfig.stagger_build_up_settings.number_settings.offset_y,
								clientConfig.stagger_build_up_settings.number_settings.color.toInt()
						);
					}
//				if (should_stagger_bar_be_rendered || should_stagger_icon_be_rendered|| should_stagger_number_be_rendered) {
//					dynamic_offset_x += clientConfig.staggerBuildUpSettings.dynamic_offset_increase_x;
//					dynamic_offset_y += clientConfig.staggerBuildUpSettings.dynamic_offset_increase_y;
//				}
				}
				// endregion stagger
			}
		}));
		ConfigApi.event().onUpdateClient((identifier, config) -> {
			if (identifier.equals(OverhauledDamage.identifier("client"))) {
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
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_background.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress_decrease_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress_increase_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress_increase_value.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress.png"),
								null,
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_overlay.png"),
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
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_background.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress_decrease_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress_increase_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress_increase_value.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress.png"),
								null,
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_overlay.png"),
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
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_background.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress_decrease_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress_increase_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress_increase_value.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress.png"),
								null,
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_overlay.png"),
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
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_background.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress_decrease_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress_increase_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress_increase_value.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress.png"),
								null,
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_overlay.png"),
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
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_background.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress_decrease_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress_increase_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress_increase_value.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress.png"),
								null,
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_overlay.png"),
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
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_background.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress_decrease_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress_increase_animation.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress_increase_value.png"),
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress.png"),
								null,
								OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_overlay.png"),
								null
						}
				);
			}
		});
	}
}