package com.github.theredbrain.overhauleddamage.mixin.client.gui.hud;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.OverhauledDamageClient;
import com.github.theredbrain.overhauleddamage.config.ClientConfig;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Shadow
	@Final
	private MinecraftClient client;

	@Unique
	private static final Identifier[] BLEEDING_TEXTURES = {
			Identifier.of("textures/gui/sprites/boss_bar/red_background.png"),
			Identifier.of("textures/gui/sprites/boss_bar/red_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_red_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_red_progress.png")
	};

	@Unique
	private static final Identifier[] BURN_TEXTURES = {
			Identifier.of("textures/gui/sprites/boss_bar/red_background.png"),
			Identifier.of("textures/gui/sprites/boss_bar/red_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_red_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_red_progress.png")
	};

	@Unique
	private static final Identifier[] FREEZE_TEXTURES = {
			Identifier.of("textures/gui/sprites/boss_bar/blue_background.png"),
			Identifier.of("textures/gui/sprites/boss_bar/blue_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_blue_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_blue_progress.png")
	};

	@Unique
	private static final Identifier[] POISON_TEXTURES = {
			Identifier.of("textures/gui/sprites/boss_bar/green_background.png"),
			Identifier.of("textures/gui/sprites/boss_bar/green_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_green_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_green_progress.png")
	};

	@Unique
	private static final Identifier[] SHOCK_TEXTURES = {
			Identifier.of("textures/gui/sprites/boss_bar/white_background.png"),
			Identifier.of("textures/gui/sprites/boss_bar/white_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_white_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_white_progress.png")
	};

	@Unique
	private static final Identifier[] STAGGER_TEXTURES = {
			Identifier.of("textures/gui/sprites/boss_bar/yellow_background.png"),
			Identifier.of("textures/gui/sprites/boss_bar/yellow_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_yellow_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/boss_bar/vertical_yellow_progress.png")
	};

	@Unique
	private int[] oldNormalizedBuildUpRatios = {-1, -1, -1, -1, -1, -1};

	@Unique
	private int[] oldMaxBuildUps = {-1, -1, -1, -1, -1, -1};

	@Unique
	private int[] buildUpBarAnimationCounters = {0, 0, 0, 0, 0, 0};

	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void overhauleddamage$renderStatusBars(DrawContext context, CallbackInfo ci) {
		var generalClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().generalClientConfig;
		if (generalClientConfig.show_effect_build_up_elements) {
			PlayerEntity playerEntity = this.getCameraPlayer();
			if (playerEntity != null) {
				boolean use_custom_textures = generalClientConfig.use_custom_textures;

				int dynamic_x_offset = 0;
				int dynamic_y_offset = 0;
				int dynamic_x_offset_increment = generalClientConfig.dynamic_x_offset_increase;
				int dynamic_y_offset_increment = generalClientConfig.dynamic_y_offset_increase;

				//region bleeding build up
				var bleedingClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().bleedingClientConfig;
				int buildUpElementX = context.getScaledWindowWidth() / 2 + bleedingClientConfig.bleeding_build_up_element_x_offset;
				int buildUpElementY = context.getScaledWindowHeight() / 2 + bleedingClientConfig.bleeding_build_up_element_y_offset;
				int currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUp());
				int maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBleedingBuildUp());

				if (currentBuildUp > 0) {
					this.client.getProfiler().push("bleeding_build_up_element");

					if (use_custom_textures) {
						this.drawCustomEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								Identifier.tryParse(bleedingClientConfig.custom_bleeding_element_background_texture_id),
								Identifier.tryParse(bleedingClientConfig.custom_bleeding_element_foreground_texture_id),
								bleedingClientConfig.custom_bleeding_element_texture_width,
								bleedingClientConfig.custom_bleeding_element_texture_height,
								bleedingClientConfig.bleeding_element_fill_direction
						);
					} else {
						this.drawFallbackEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								BLEEDING_TEXTURES,
								bleedingClientConfig.bleeding_build_up_bar_additional_length,
								bleedingClientConfig.bleeding_element_fill_direction,
								0,
								bleedingClientConfig.enable_bleeding_build_up_bar_animation,
								MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUpReduction()),
								bleedingClientConfig.bleeding_build_up_bar_animation_interval
						);
					}

					dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
					dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
					this.client.getProfiler().pop();
				}
				//endregion bleeding build up

				//region burn build up
				var burnClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().burnClientConfig;
				buildUpElementX = context.getScaledWindowWidth() / 2 + burnClientConfig.burn_build_up_element_x_offset;
				buildUpElementY = context.getScaledWindowHeight() / 2 + burnClientConfig.burn_build_up_element_y_offset;
				currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUp());
				maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBurnBuildUp());

				if (currentBuildUp > 0) {
					this.client.getProfiler().push("burn_build_up_element");

					if (use_custom_textures) {
						this.drawCustomEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								Identifier.tryParse(burnClientConfig.custom_burn_element_background_texture_id),
								Identifier.tryParse(burnClientConfig.custom_burn_element_foreground_texture_id),
								burnClientConfig.custom_burn_element_texture_width,
								burnClientConfig.custom_burn_element_texture_height,
								burnClientConfig.burn_element_fill_direction
						);
					} else {
						this.drawFallbackEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								BURN_TEXTURES,
								burnClientConfig.burn_build_up_bar_additional_length,
								burnClientConfig.burn_element_fill_direction,
								1,
								burnClientConfig.enable_burn_build_up_bar_animation,
								MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUpReduction()),
								burnClientConfig.burn_build_up_bar_animation_interval
						);
					}

					dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
					dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
					this.client.getProfiler().pop();
				}
				//endregion burn build up

				//region freeze build up
				var freezeClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().freezeClientConfig;
				buildUpElementX = context.getScaledWindowWidth() / 2 + freezeClientConfig.freeze_build_up_element_x_offset;
				buildUpElementY = context.getScaledWindowHeight() / 2 + freezeClientConfig.freeze_build_up_element_y_offset;
				currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUp());
				maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxFreezeBuildUp());

				if (currentBuildUp > 0) {
					this.client.getProfiler().push("freeze_build_up_element");

					if (use_custom_textures) {
						this.drawCustomEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								Identifier.tryParse(freezeClientConfig.custom_freeze_element_background_texture_id),
								Identifier.tryParse(freezeClientConfig.custom_freeze_element_foreground_texture_id),
								freezeClientConfig.custom_freeze_element_texture_width,
								freezeClientConfig.custom_freeze_element_texture_height,
								freezeClientConfig.freeze_element_fill_direction
						);
					} else {
						this.drawFallbackEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								FREEZE_TEXTURES,
								freezeClientConfig.freeze_build_up_bar_additional_length,
								freezeClientConfig.freeze_element_fill_direction,
								2,
								freezeClientConfig.enable_freeze_build_up_bar_animation,
								MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUpReduction()),
								freezeClientConfig.freeze_build_up_bar_animation_interval
						);
					}

					dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
					dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
					this.client.getProfiler().pop();
				}
				//endregion freeze build up

				//region poison build up
				var poisonClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().poisonClientConfig;
				buildUpElementX = context.getScaledWindowWidth() / 2 + poisonClientConfig.poison_build_up_element_x_offset;
				buildUpElementY = context.getScaledWindowHeight() / 2 + poisonClientConfig.poison_build_up_element_y_offset;
				currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUp());
				maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxPoisonBuildUp());

				if (currentBuildUp > 0) {
					this.client.getProfiler().push("poison_build_up_element");

					if (use_custom_textures) {
						this.drawCustomEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								Identifier.tryParse(poisonClientConfig.custom_poison_element_background_texture_id),
								Identifier.tryParse(poisonClientConfig.custom_poison_element_foreground_texture_id),
								poisonClientConfig.custom_poison_element_texture_width,
								poisonClientConfig.custom_poison_element_texture_height,
								poisonClientConfig.poison_element_fill_direction
						);
					} else {
						this.drawFallbackEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								POISON_TEXTURES,
								poisonClientConfig.poison_build_up_bar_additional_length,
								poisonClientConfig.poison_element_fill_direction,
								3,
								poisonClientConfig.enable_poison_build_up_bar_animation,
								MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUpReduction()),
								poisonClientConfig.poison_build_up_bar_animation_interval
						);
					}

					dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
					dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
					this.client.getProfiler().pop();
				}
				//endregion poison build up

				//region shock build up
				var shockClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().shockClientConfig;
				buildUpElementX = context.getScaledWindowWidth() / 2 + shockClientConfig.shock_build_up_element_x_offset;
				buildUpElementY = context.getScaledWindowHeight() / 2 + shockClientConfig.shock_build_up_element_y_offset;
				currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUp());
				maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxShockBuildUp());

				if (currentBuildUp > 0) {
					this.client.getProfiler().push("shock_build_up_element");

					if (use_custom_textures) {
						this.drawCustomEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								Identifier.tryParse(shockClientConfig.custom_shock_element_background_texture_id),
								Identifier.tryParse(shockClientConfig.custom_shock_element_foreground_texture_id),
								shockClientConfig.custom_shock_element_texture_width,
								shockClientConfig.custom_shock_element_texture_height,
								shockClientConfig.shock_element_fill_direction
						);
					} else {
						this.drawFallbackEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								SHOCK_TEXTURES,
								shockClientConfig.shock_build_up_bar_additional_length,
								shockClientConfig.shock_element_fill_direction,
								4,
								shockClientConfig.enable_shock_build_up_bar_animation,
								MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUpReduction()),
								shockClientConfig.shock_build_up_bar_animation_interval
						);
					}

					dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
					dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
					this.client.getProfiler().pop();
				}
				//endregion shock build up

				//region stagger build up
				var staggerClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().staggerClientConfig;
				buildUpElementX = context.getScaledWindowWidth() / 2 + staggerClientConfig.stagger_build_up_element_x_offset;
				buildUpElementY = context.getScaledWindowHeight() / 2 + staggerClientConfig.stagger_build_up_element_y_offset;
				currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUp());
				maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxStaggerBuildUp());

				if (currentBuildUp > 0) {
					OverhauledDamage.info("stagger bar is filled");
					this.client.getProfiler().push("stagger_build_up_element");

					if (use_custom_textures) {
						this.drawCustomEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								Identifier.tryParse(staggerClientConfig.custom_stagger_element_background_texture_id),
								Identifier.tryParse(staggerClientConfig.custom_stagger_element_foreground_texture_id),
								staggerClientConfig.custom_stagger_element_texture_width,
								staggerClientConfig.custom_stagger_element_texture_height,
								staggerClientConfig.stagger_element_fill_direction
						);
					} else {
						this.drawFallbackEffectBuildUpElement(
								context,
								currentBuildUp,
								maxBuildUp,
								buildUpElementX,
								dynamic_x_offset,
								buildUpElementY,
								dynamic_y_offset,
								STAGGER_TEXTURES,
								staggerClientConfig.stagger_build_up_bar_additional_length,
								staggerClientConfig.stagger_element_fill_direction,
								5,
								staggerClientConfig.enable_stagger_build_up_bar_animation,
								MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUpReduction()),
								staggerClientConfig.stagger_build_up_bar_animation_interval
						);
					}

//					dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
//					dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
					this.client.getProfiler().pop();
				}
				//endregion stagger build up
			}
		}
	}

	@Unique
	private void drawCustomEffectBuildUpElement(DrawContext context, int currentBuildUp, int maxBuildUp, int buildUpElementX, int dynamic_x_offset, int buildUpElementY, int dynamic_y_offset, Identifier background_texture_id, Identifier foreground_texture_id, int texture_width, int texture_height, ClientConfig.FillDirection fill_direction) {

		int normalizedBuildUpRatio = (int) (((double) currentBuildUp / Math.max(maxBuildUp, 1)) * ((fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP || fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) ? texture_height : texture_width));

		context.drawTexture(background_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, texture_width, texture_height, texture_width, texture_height);
		if (normalizedBuildUpRatio > 0) {
			// 0: left to right, 1: bottom to top, 2: right to left, 3: top to bottom
			int offset;
			switch (fill_direction) {
				case BOTTOM_TO_TOP:
					offset = texture_height - normalizedBuildUpRatio;
					context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset + offset, 0, offset, texture_width, texture_height, texture_width, texture_height);
					break;
				case RIGHT_TO_LEFT:
					offset = texture_width - normalizedBuildUpRatio;
					context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset + offset, buildUpElementY + dynamic_y_offset, offset, 0, texture_width, texture_height, texture_width, texture_height);
					break;
				case TOP_TO_BOTTOM:
					context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, texture_width, normalizedBuildUpRatio, texture_width, texture_height);
					break;
				default:
					context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, normalizedBuildUpRatio, texture_height, texture_width, texture_height);
					break;
			}
		}
	}

	@Unique
	private void drawFallbackEffectBuildUpElement(
			DrawContext context,
			int currentBuildUp,
			int maxBuildUp,
			int buildUpElementX,
			int dynamic_x_offset,
			int buildUpElementY,
			int dynamic_y_offset,
			Identifier[] fallback_texture_ids,
			int fallback_additional_bar_length,
			ClientConfig.FillDirection fill_direction,
			int effect_id,
			boolean enable_smooth_animation,
			int build_up_reduction,
			int build_up_bar_animation_interval
	) {

		int normalizedBuildUpRatio = (int) (((double) currentBuildUp / Math.max(maxBuildUp, 1)) * (5 + fallback_additional_bar_length + 5));

		if (this.oldMaxBuildUps[effect_id] != maxBuildUp) {
			this.oldMaxBuildUps[effect_id] = maxBuildUp;
			this.oldNormalizedBuildUpRatios[effect_id] = normalizedBuildUpRatio;
		}

		this.buildUpBarAnimationCounters[effect_id] = this.buildUpBarAnimationCounters[effect_id] + Math.max(1, build_up_reduction);

		if (this.oldNormalizedBuildUpRatios[effect_id] != normalizedBuildUpRatio && this.buildUpBarAnimationCounters[effect_id] > Math.max(0, build_up_bar_animation_interval)) {
			boolean reduceOldRatio = this.oldNormalizedBuildUpRatios[effect_id] > normalizedBuildUpRatio;
			this.oldNormalizedBuildUpRatios[effect_id] = this.oldNormalizedBuildUpRatios[effect_id] + (reduceOldRatio ? -1 : 1);
			this.buildUpBarAnimationCounters[effect_id] = 0;
		}

		// background
		if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP || fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
			context.drawTexture(fallback_texture_ids[2], buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, 5, 5, 5, 11);
			if (fallback_additional_bar_length > 0) {
				for (int i = 0; i < fallback_additional_bar_length; i++) {
					context.drawTexture(fallback_texture_ids[2], buildUpElementX + dynamic_x_offset, buildUpElementY + 5 + i + dynamic_y_offset, 0, 5, 5, 1, 5, 11);
				}
			}
			context.drawTexture(fallback_texture_ids[2], buildUpElementX + dynamic_x_offset, buildUpElementY + 5 + fallback_additional_bar_length + dynamic_y_offset, 0, 6, 5, 5, 5, 11);
		} else {
			context.drawTexture(fallback_texture_ids[0], buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, 5, 5, 182, 5);
			if (fallback_additional_bar_length > 0) {
				for (int i = 0; i < fallback_additional_bar_length; i++) {
					context.drawTexture(fallback_texture_ids[0], buildUpElementX + 5 + i + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 5, 0, 1, 5, 182, 5);
				}
			}
			context.drawTexture(fallback_texture_ids[0], buildUpElementX + 5 + fallback_additional_bar_length + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 177, 0, 5, 5, 182, 5);
		}

		// foreground
		int displayRatio = enable_smooth_animation ? this.oldNormalizedBuildUpRatios[effect_id] : normalizedBuildUpRatio;
		if (displayRatio > 0) {
			int ratioFirstPart = Math.min(5, displayRatio);
			int ratioLastPart = Math.min(5, displayRatio - 5 - fallback_additional_bar_length);
			if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP) {
				// 1: bottom to top
				context.drawTexture(fallback_texture_ids[3], buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset + 5 + fallback_additional_bar_length + (5 - ratioFirstPart), 0, 6 + (5 - ratioFirstPart),5,  ratioFirstPart, 5, 11);
				if (displayRatio > 5 && fallback_additional_bar_length > 0) {
					for (int i = 5; i <= Math.min(5 + fallback_additional_bar_length, displayRatio); i++) {
						context.drawTexture(fallback_texture_ids[3], buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset + 5 + fallback_additional_bar_length + 5 - i, 0, 5, 5, 1, 5, 11);
					}
				}
				if (displayRatio > (5 + fallback_additional_bar_length)) {
					context.drawTexture(fallback_texture_ids[3], buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset + 5 - ratioLastPart, 0, 5 - ratioLastPart, 5, ratioLastPart, 5, 11);
				}
			} else if (fill_direction == ClientConfig.FillDirection.RIGHT_TO_LEFT) {
				// 2: right to left
				context.drawTexture(fallback_texture_ids[1], buildUpElementX + dynamic_x_offset + 5 + fallback_additional_bar_length + (5 - ratioFirstPart), buildUpElementY + dynamic_y_offset, 177 + (5 - ratioFirstPart), 0, Math.min(5, displayRatio), 5, 182, 5);
				if (displayRatio > 5 && fallback_additional_bar_length > 0) {
					for (int i = 5; i <= Math.min(5 + fallback_additional_bar_length, displayRatio); i++) {
						context.drawTexture(fallback_texture_ids[1], buildUpElementX + dynamic_x_offset + 5 + fallback_additional_bar_length + 5 - i, buildUpElementY + dynamic_y_offset, 5, 0, 1, 5, 182, 5);
					}
				}
				if (displayRatio > (5 + fallback_additional_bar_length)) {
					context.drawTexture(fallback_texture_ids[1], buildUpElementX + dynamic_x_offset + 5 - ratioLastPart, buildUpElementY + dynamic_y_offset, 5 - ratioLastPart, 0, ratioLastPart, 5, 182, 5);
				}
			} else if (fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
				// 3: top to bottom
				context.drawTexture(fallback_texture_ids[3], buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, 5, ratioFirstPart, 5, 11);
				if (displayRatio > 5 && fallback_additional_bar_length > 0) {
					for (int i = 5; i < Math.min(5 + fallback_additional_bar_length, displayRatio); i++) {
						context.drawTexture(fallback_texture_ids[3], buildUpElementX + dynamic_x_offset, buildUpElementY + i + dynamic_y_offset, 0, 5, 5, 1, 5, 11);
					}
				}
				if (displayRatio > (5 + fallback_additional_bar_length)) {
					context.drawTexture(fallback_texture_ids[3], buildUpElementX + dynamic_x_offset, buildUpElementY + 5 + fallback_additional_bar_length + dynamic_y_offset, 0, 6, 5, ratioLastPart, 5, 11);
				}
			} else {
				// 0: left to right
				context.drawTexture(fallback_texture_ids[1], buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, ratioFirstPart, 5, 182, 5);
				if (displayRatio > 5 && fallback_additional_bar_length > 0) {
					for (int i = 5; i < Math.min(5 + fallback_additional_bar_length, displayRatio); i++) {
						context.drawTexture(fallback_texture_ids[1], buildUpElementX + i + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 5, 0, 1, 5, 182, 5);
					}
				}
				if (displayRatio > (5 + fallback_additional_bar_length)) {
					context.drawTexture(fallback_texture_ids[1], buildUpElementX + 5 + fallback_additional_bar_length + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 177, 0, ratioLastPart, 5, 182, 5);
				}
			}
		}

		// TODO current value indicator
	}
}
