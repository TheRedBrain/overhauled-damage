package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.OverhauledDamageClient;
import com.github.theredbrain.overhauleddamage.config.ClientConfig;
import com.github.theredbrain.overhauleddamage.entity.DataAttachmentHelper;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
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
		HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR, OverhauledDamage.identifier("effect_build_ups"), ((guiGraphics, delta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer localPlayer = minecraft.player;
			ClientConfig clientConfig = OverhauledDamageClient.CLIENT_CONFIG;

			if (localPlayer != null && !localPlayer.isCreative() && !minecraft.options.hideGui) {

				int dynamic_offset_x = 0;
				int dynamic_offset_y = 0;

				boolean shouldBlink = false;

				DuckGuiMixin gui = ((DuckGuiMixin) minecraft.gui);

				// region bleeding
				int bleedingBuildUp = Mth.ceil(DataAttachmentHelper.getBleedingBuildUp(localPlayer));

				int currentDisplayBleedingBuildUp = bleedingBuildUp;

				if (clientConfig.bleedingBuildUpSettings.iconBarSettings.enable_icon_blinking.get()) {
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
					boolean should_bleeding_bar_be_rendered = bleedingBuildUp > 0 || clientConfig.bleedingBuildUpSettings.show_empty_bar;
					boolean should_bleeding_icon_be_rendered = clientConfig.bleedingBuildUpSettings.show_icon && (bleedingBuildUp > 0 || clientConfig.bleedingBuildUpSettings.iconTextureSettings.show_when_bar_empty);
					boolean should_bleeding_number_be_rendered = clientConfig.bleedingBuildUpSettings.show_number && (bleedingBuildUp > 0 || clientConfig.bleedingBuildUpSettings.numberSettings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphics, clientConfig.bleedingBuildUpSettings.origin);

					if (clientConfig.bleedingBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_bleeding_bar_be_rendered) {

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
								guiGraphics,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.bleedingBuildUpSettings.iconBarSettings.offset_x.get(),
								clientConfig.bleedingBuildUpSettings.iconBarSettings.offset_y.get(),
								clientConfig.bleedingBuildUpSettings.fill_direction,
								clientConfig.bleedingBuildUpSettings.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.bleedingBuildUpSettings.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.bleedingBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_bleeding_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphics,
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
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.positionSettings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.bleedingBuildUpSettings.fill_direction,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.show_current_value_overlay,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								should_bleeding_icon_be_rendered,
								clientConfig.bleedingBuildUpSettings.iconTextureSettings.offset_x,
								clientConfig.bleedingBuildUpSettings.iconTextureSettings.offset_y,
								clientConfig.bleedingBuildUpSettings.iconTextureSettings.texture_heights,
								clientConfig.bleedingBuildUpSettings.iconTextureSettings.texture_widths,
								clientConfig.bleedingBuildUpSettings.iconTextureSettings.texture_ids,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.enable_smooth_animation,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.bleedingBuildUpSettings.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (should_bleeding_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphics,
								BLEEDING_BAR_IDENTIFIER_STRING,
								bleedingBuildUp,
								maxBleedingBuildUp,
								maxBleedingBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.bleedingBuildUpSettings.numberSettings.show_max_value,
								clientConfig.bleedingBuildUpSettings.numberSettings.offset_x,
								clientConfig.bleedingBuildUpSettings.numberSettings.offset_y,
								clientConfig.bleedingBuildUpSettings.numberSettings.color.toInt()
						);
					}
					if ((should_bleeding_bar_be_rendered && clientConfig.bleedingBuildUpSettings.bar_display != ResourceBarAPI.ResourceBarDisplay.NONE) || should_bleeding_icon_be_rendered || should_bleeding_number_be_rendered) {
						dynamic_offset_x = clientConfig.bleedingBuildUpSettings.dynamic_offset_increase_x;
						dynamic_offset_y = clientConfig.bleedingBuildUpSettings.dynamic_offset_increase_y;
					}
				}
				// endregion bleeding

				// region burn
				int burnBuildUp = Mth.ceil(DataAttachmentHelper.getBurnBuildUp(localPlayer));

				int currentDisplayBurnBuildUp = burnBuildUp;

				if (clientConfig.burnBuildUpSettings.iconBarSettings.enable_icon_blinking.get()) {
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
					boolean should_burn_bar_be_rendered = burnBuildUp > 0 || clientConfig.burnBuildUpSettings.show_empty_bar;
					boolean should_burn_icon_be_rendered = clientConfig.burnBuildUpSettings.show_icon && (burnBuildUp > 0 || clientConfig.burnBuildUpSettings.iconTextureSettings.show_when_bar_empty);
					boolean should_burn_number_be_rendered = clientConfig.burnBuildUpSettings.show_number && (burnBuildUp > 0 || clientConfig.burnBuildUpSettings.numberSettings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphics, clientConfig.burnBuildUpSettings.origin);

					if (clientConfig.burnBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_burn_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayBleedingBuildUp,
								maxBleedingBuildUp,
								shouldBlink ? ICON_BURNING_CONTAINER_BLINKING : ICON_BURNING_CONTAINER,
								shouldBlink ? ICON_BURNING_FULL_BLINKING : ICON_BURNING_FULL,
								shouldBlink ? ICON_BURNING_HALF_BLINKING : ICON_BURNING_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphics,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.burnBuildUpSettings.iconBarSettings.offset_x.get(),
								clientConfig.burnBuildUpSettings.iconBarSettings.offset_y.get(),
								clientConfig.burnBuildUpSettings.fill_direction,
								clientConfig.burnBuildUpSettings.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.burnBuildUpSettings.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.burnBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_burn_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphics,
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
								clientConfig.burnBuildUpSettings.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.burnBuildUpSettings.smoothBarSettings.positionSettings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.burnBuildUpSettings.fill_direction,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.burnBuildUpSettings.smoothBarSettings.show_current_value_overlay,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.burnBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								should_burn_icon_be_rendered,
								clientConfig.burnBuildUpSettings.iconTextureSettings.offset_x,
								clientConfig.burnBuildUpSettings.iconTextureSettings.offset_y,
								clientConfig.burnBuildUpSettings.iconTextureSettings.texture_heights,
								clientConfig.burnBuildUpSettings.iconTextureSettings.texture_widths,
								clientConfig.burnBuildUpSettings.iconTextureSettings.texture_ids,
								clientConfig.burnBuildUpSettings.smoothBarSettings.enable_smooth_animation,
								clientConfig.burnBuildUpSettings.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.burnBuildUpSettings.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (should_burn_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphics,
								BURN_BAR_IDENTIFIER_STRING,
								burnBuildUp,
								maxBurnBuildUp,
								maxBurnBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.burnBuildUpSettings.numberSettings.show_max_value,
								clientConfig.burnBuildUpSettings.numberSettings.offset_x,
								clientConfig.burnBuildUpSettings.numberSettings.offset_y,
								clientConfig.burnBuildUpSettings.numberSettings.color.toInt()
						);
					}
					if (should_burn_bar_be_rendered || should_burn_icon_be_rendered || should_burn_number_be_rendered) {
						dynamic_offset_x = clientConfig.burnBuildUpSettings.dynamic_offset_increase_x;
						dynamic_offset_y = clientConfig.burnBuildUpSettings.dynamic_offset_increase_y;
					}
				}
				// endregion burn

				// region freeze
				int freezeBuildUp = Mth.ceil(DataAttachmentHelper.getFreezeBuildUp(localPlayer));

				int currentDisplayFreezeBuildUp = freezeBuildUp;

				if (clientConfig.freezeBuildUpSettings.iconBarSettings.enable_icon_blinking.get()) {
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
					boolean should_freeze_bar_be_rendered = freezeBuildUp > 0 || clientConfig.freezeBuildUpSettings.show_empty_bar;
					boolean should_freeze_icon_be_rendered = clientConfig.freezeBuildUpSettings.show_icon && (freezeBuildUp > 0 || clientConfig.freezeBuildUpSettings.iconTextureSettings.show_when_bar_empty);
					boolean should_freeze_number_be_rendered = clientConfig.freezeBuildUpSettings.show_number && (freezeBuildUp > 0 || clientConfig.freezeBuildUpSettings.numberSettings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphics, clientConfig.freezeBuildUpSettings.origin);

					if (clientConfig.freezeBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_freeze_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayBleedingBuildUp,
								maxBleedingBuildUp,
								shouldBlink ? ICON_FREEZE_CONTAINER_BLINKING : ICON_FREEZE_CONTAINER,
								shouldBlink ? ICON_FREEZE_FULL_BLINKING : ICON_FREEZE_FULL,
								shouldBlink ? ICON_FREEZE_HALF_BLINKING : ICON_FREEZE_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphics,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.freezeBuildUpSettings.iconBarSettings.offset_x.get(),
								clientConfig.freezeBuildUpSettings.iconBarSettings.offset_y.get(),
								clientConfig.freezeBuildUpSettings.fill_direction,
								clientConfig.freezeBuildUpSettings.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.freezeBuildUpSettings.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.freezeBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_freeze_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphics,
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
								clientConfig.freezeBuildUpSettings.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.positionSettings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.freezeBuildUpSettings.fill_direction,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.freezeBuildUpSettings.smoothBarSettings.show_current_value_overlay,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								should_freeze_icon_be_rendered,
								clientConfig.freezeBuildUpSettings.iconTextureSettings.offset_x,
								clientConfig.freezeBuildUpSettings.iconTextureSettings.offset_y,
								clientConfig.freezeBuildUpSettings.iconTextureSettings.texture_heights,
								clientConfig.freezeBuildUpSettings.iconTextureSettings.texture_widths,
								clientConfig.freezeBuildUpSettings.iconTextureSettings.texture_ids,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.enable_smooth_animation,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.freezeBuildUpSettings.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (should_freeze_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphics,
								FREEZE_BAR_IDENTIFIER_STRING,
								freezeBuildUp,
								maxFreezeBuildUp,
								maxFreezeBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.freezeBuildUpSettings.numberSettings.show_max_value,
								clientConfig.freezeBuildUpSettings.numberSettings.offset_x,
								clientConfig.freezeBuildUpSettings.numberSettings.offset_y,
								clientConfig.freezeBuildUpSettings.numberSettings.color.toInt()
						);
					}
					if (should_freeze_bar_be_rendered || should_freeze_icon_be_rendered || should_freeze_number_be_rendered) {
						dynamic_offset_x = clientConfig.freezeBuildUpSettings.dynamic_offset_increase_x;
						dynamic_offset_y = clientConfig.freezeBuildUpSettings.dynamic_offset_increase_y;
					}
				}
				// endregion freeze

				// region poison
				int poisonBuildUp = Mth.ceil(DataAttachmentHelper.getPoisonBuildUp(localPlayer));

				int currentDisplayPoisonBuildUp = poisonBuildUp;

				if (clientConfig.poisonBuildUpSettings.iconBarSettings.enable_icon_blinking.get()) {
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
					boolean should_poison_bar_be_rendered = poisonBuildUp > 0 || clientConfig.poisonBuildUpSettings.show_empty_bar;
					boolean should_poison_icon_be_rendered = clientConfig.poisonBuildUpSettings.show_icon && (poisonBuildUp > 0 || clientConfig.poisonBuildUpSettings.iconTextureSettings.show_when_bar_empty);
					boolean should_poison_number_be_rendered = clientConfig.poisonBuildUpSettings.show_number && (poisonBuildUp > 0 || clientConfig.poisonBuildUpSettings.numberSettings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphics, clientConfig.poisonBuildUpSettings.origin);

					if (clientConfig.poisonBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_poison_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayBleedingBuildUp,
								maxBleedingBuildUp,
								shouldBlink ? ICON_POISON_CONTAINER_BLINKING : ICON_POISON_CONTAINER,
								shouldBlink ? ICON_POISON_FULL_BLINKING : ICON_POISON_FULL,
								shouldBlink ? ICON_POISON_HALF_BLINKING : ICON_POISON_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphics,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.poisonBuildUpSettings.iconBarSettings.offset_x.get(),
								clientConfig.poisonBuildUpSettings.iconBarSettings.offset_y.get(),
								clientConfig.poisonBuildUpSettings.fill_direction,
								clientConfig.poisonBuildUpSettings.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.poisonBuildUpSettings.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.poisonBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_poison_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphics,
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
								clientConfig.poisonBuildUpSettings.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.positionSettings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.poisonBuildUpSettings.fill_direction,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.poisonBuildUpSettings.smoothBarSettings.show_current_value_overlay,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								should_poison_icon_be_rendered,
								clientConfig.poisonBuildUpSettings.iconTextureSettings.offset_x,
								clientConfig.poisonBuildUpSettings.iconTextureSettings.offset_y,
								clientConfig.poisonBuildUpSettings.iconTextureSettings.texture_heights,
								clientConfig.poisonBuildUpSettings.iconTextureSettings.texture_widths,
								clientConfig.poisonBuildUpSettings.iconTextureSettings.texture_ids,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.enable_smooth_animation,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.poisonBuildUpSettings.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (should_poison_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphics,
								POISON_BAR_IDENTIFIER_STRING,
								poisonBuildUp,
								maxPoisonBuildUp,
								maxPoisonBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.poisonBuildUpSettings.numberSettings.show_max_value,
								clientConfig.poisonBuildUpSettings.numberSettings.offset_x,
								clientConfig.poisonBuildUpSettings.numberSettings.offset_y,
								clientConfig.poisonBuildUpSettings.numberSettings.color.toInt()
						);
					}
					if (should_poison_bar_be_rendered || should_poison_icon_be_rendered || should_poison_number_be_rendered) {
						dynamic_offset_x = clientConfig.poisonBuildUpSettings.dynamic_offset_increase_x;
						dynamic_offset_y = clientConfig.poisonBuildUpSettings.dynamic_offset_increase_y;
					}
				}
				// endregion poison

				// region shock
				int shockBuildUp = Mth.ceil(DataAttachmentHelper.getShockBuildUp(localPlayer));

				int currentDisplayShockBuildUp = shockBuildUp;

				if (clientConfig.shockBuildUpSettings.iconBarSettings.enable_icon_blinking.get()) {
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
					boolean should_shock_bar_be_rendered = shockBuildUp > 0 || clientConfig.shockBuildUpSettings.show_empty_bar;
					boolean should_shock_icon_be_rendered = clientConfig.shockBuildUpSettings.show_icon && (shockBuildUp > 0 || clientConfig.shockBuildUpSettings.iconTextureSettings.show_when_bar_empty);
					boolean should_shock_number_be_rendered = clientConfig.shockBuildUpSettings.show_number && (shockBuildUp > 0 || clientConfig.shockBuildUpSettings.numberSettings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphics, clientConfig.shockBuildUpSettings.origin);

					if (clientConfig.shockBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_shock_bar_be_rendered) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayBleedingBuildUp,
								maxBleedingBuildUp,
								shouldBlink ? ICON_SHOCK_CONTAINER_BLINKING : ICON_SHOCK_CONTAINER,
								shouldBlink ? ICON_SHOCK_FULL_BLINKING : ICON_SHOCK_FULL,
								shouldBlink ? ICON_SHOCK_HALF_BLINKING : ICON_SHOCK_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphics,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.shockBuildUpSettings.iconBarSettings.offset_x.get(),
								clientConfig.shockBuildUpSettings.iconBarSettings.offset_y.get(),
								clientConfig.shockBuildUpSettings.fill_direction,
								clientConfig.shockBuildUpSettings.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.shockBuildUpSettings.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.shockBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_shock_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphics,
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
								clientConfig.shockBuildUpSettings.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.shockBuildUpSettings.smoothBarSettings.positionSettings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.shockBuildUpSettings.fill_direction,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.shockBuildUpSettings.smoothBarSettings.show_current_value_overlay,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.shockBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								should_shock_icon_be_rendered,
								clientConfig.shockBuildUpSettings.iconTextureSettings.offset_x,
								clientConfig.shockBuildUpSettings.iconTextureSettings.offset_y,
								clientConfig.shockBuildUpSettings.iconTextureSettings.texture_heights,
								clientConfig.shockBuildUpSettings.iconTextureSettings.texture_widths,
								clientConfig.shockBuildUpSettings.iconTextureSettings.texture_ids,
								clientConfig.shockBuildUpSettings.smoothBarSettings.enable_smooth_animation,
								clientConfig.shockBuildUpSettings.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.shockBuildUpSettings.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (should_shock_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphics,
								SHOCK_BAR_IDENTIFIER_STRING,
								shockBuildUp,
								maxShockBuildUp,
								maxShockBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.shockBuildUpSettings.numberSettings.show_max_value,
								clientConfig.shockBuildUpSettings.numberSettings.offset_x,
								clientConfig.shockBuildUpSettings.numberSettings.offset_y,
								clientConfig.shockBuildUpSettings.numberSettings.color.toInt()
						);
					}
					if (should_shock_bar_be_rendered || should_shock_icon_be_rendered || should_shock_number_be_rendered) {
						dynamic_offset_x = clientConfig.shockBuildUpSettings.dynamic_offset_increase_x;
						dynamic_offset_y = clientConfig.shockBuildUpSettings.dynamic_offset_increase_y;
					}
				}
				// endregion shock

				// region stagger
				int staggerBuildUp = Mth.ceil(DataAttachmentHelper.getStaggerBuildUp(localPlayer));

				int currentDisplayStaggerBuildUp = staggerBuildUp;

				if (clientConfig.staggerBuildUpSettings.iconBarSettings.enable_icon_blinking.get()) {
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
					boolean should_stagger_bar_be_rendered = staggerBuildUp > 0 || clientConfig.staggerBuildUpSettings.show_empty_bar;
					boolean should_stagger_icon_be_rendered = clientConfig.staggerBuildUpSettings.show_icon && (staggerBuildUp > 0 || clientConfig.staggerBuildUpSettings.iconTextureSettings.show_when_bar_empty);
					boolean should_stagger_number_be_rendered = clientConfig.staggerBuildUpSettings.show_number && (staggerBuildUp > 0 || clientConfig.staggerBuildUpSettings.numberSettings.show_when_bar_empty);

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphics, clientConfig.staggerBuildUpSettings.origin);

					if (clientConfig.staggerBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && should_stagger_bar_be_rendered) {

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
								guiGraphics,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.staggerBuildUpSettings.iconBarSettings.offset_x.get(),
								clientConfig.staggerBuildUpSettings.iconBarSettings.offset_y.get(),
								clientConfig.staggerBuildUpSettings.fill_direction,
								clientConfig.staggerBuildUpSettings.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.staggerBuildUpSettings.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.staggerBuildUpSettings.bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && should_stagger_bar_be_rendered) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphics,
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
								clientConfig.staggerBuildUpSettings.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.positionSettings.offsets_y,
								dynamic_offset_x,
								dynamic_offset_y,
								clientConfig.staggerBuildUpSettings.fill_direction,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								0,
								0,
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedInt()),
								new ValidatedMap<>(new HashMap<>(), new ValidatedInt(), new ValidatedIdentifier()),
								clientConfig.staggerBuildUpSettings.smoothBarSettings.show_current_value_overlay,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								should_stagger_icon_be_rendered,
								clientConfig.staggerBuildUpSettings.iconTextureSettings.offset_x,
								clientConfig.staggerBuildUpSettings.iconTextureSettings.offset_y,
								clientConfig.staggerBuildUpSettings.iconTextureSettings.texture_heights,
								clientConfig.staggerBuildUpSettings.iconTextureSettings.texture_widths,
								clientConfig.staggerBuildUpSettings.iconTextureSettings.texture_ids,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.enable_smooth_animation,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.staggerBuildUpSettings.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (should_stagger_number_be_rendered) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphics,
								STAGGER_BAR_IDENTIFIER_STRING,
								staggerBuildUp,
								maxStaggerBuildUp,
								maxStaggerBuildUp,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.staggerBuildUpSettings.numberSettings.show_max_value,
								clientConfig.staggerBuildUpSettings.numberSettings.offset_x,
								clientConfig.staggerBuildUpSettings.numberSettings.offset_y,
								clientConfig.staggerBuildUpSettings.numberSettings.color.toInt()
						);
					}
//				if (should_stagger_bar_be_rendered || should_stagger_icon_be_rendered|| should_stagger_number_be_rendered) {
//					dynamic_offset_x = clientConfig.staggerBuildUpSettings.dynamic_offset_increase_x;
//					dynamic_offset_y = clientConfig.staggerBuildUpSettings.dynamic_offset_increase_y;
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