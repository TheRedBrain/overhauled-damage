package com.github.theredbrain.overhauleddamage.mixin.client.gui.hud;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.OverhauledDamageClient;
import com.github.theredbrain.overhauleddamage.config.ClientConfig;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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

	@Shadow public abstract TextRenderer getTextRenderer();

	@Unique
	private static final Identifier[] BLEEDING_TEXTURES = {
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_bleeding_overlay.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_bleeding_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_bleeding_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_bleeding_overlay.png")
	};

	@Unique
	private static final Identifier[] BURN_TEXTURES = {
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_burn_overlay.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_burn_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_burn_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_burn_overlay.png")
	};

	@Unique
	private static final Identifier[] FREEZE_TEXTURES = {
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_freeze_overlay.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_freeze_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_freeze_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_freeze_overlay.png")
	};

	@Unique
	private static final Identifier[] POISON_TEXTURES = {
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_poison_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_overlay.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_poison_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_poison_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_poison_overlay.png")
	};

	@Unique
	private static final Identifier[] SHOCK_TEXTURES = {
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_shock_overlay.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_shock_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_shock_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_shock_overlay.png")
	};

	@Unique
	private static final Identifier[] STAGGER_TEXTURES = {
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/horizontal_stagger_overlay.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_stagger_background.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_stagger_progress.png"),
			OverhauledDamage.identifier("textures/gui/sprites/hud/vertical_stagger_overlay.png")
	};

	@Unique
	private int[] oldNormalizedBuildUpRatios = {-1, -1, -1, -1, -1, -1};

	@Unique
	private int[] oldMaxBuildUps = {-1, -1, -1, -1, -1, -1};

	@Unique
	private int[] buildUpBarAnimationCounters = {0, 0, 0, 0, 0, 0};

	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void overhauleddamage$renderStatusBars(DrawContext context, CallbackInfo ci) {
		if (OverhauledDamageClient.clientConfigHolder.getConfig().generalClientConfig.show_effect_build_up_elements) {
			renderEffectBuildUpElements(context);
		}
	}

	@Unique
	private void renderEffectBuildUpElements(DrawContext context) {
		var generalClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().generalClientConfig;
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {

			int dynamic_x_offset = 0;
			int dynamic_y_offset = 0;
			int dynamic_x_offset_increment = generalClientConfig.dynamic_x_offset_increase;
			int dynamic_y_offset_increment = generalClientConfig.dynamic_y_offset_increase;

			//region bleeding build up
			var bleedingClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().bleedingClientConfig;
			int buildUpElementX = (context.getScaledWindowWidth() / 2 + bleedingClientConfig.bleeding_build_up_element_x_offset) + dynamic_x_offset;
			int buildUpElementY = (context.getScaledWindowHeight() / 2 + bleedingClientConfig.bleeding_build_up_element_y_offset) + dynamic_y_offset;
			int currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUp());
			int maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBleedingBuildUp());
			String buildUpBarNumberString = String.valueOf(currentBuildUp);
			int buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + bleedingClientConfig.bleeding_build_up_bar_number_x_offset) + dynamic_x_offset;
			int buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + bleedingClientConfig.bleeding_build_up_bar_number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("bleeding_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						buildUpElementX,
						buildUpElementY,
						BLEEDING_TEXTURES,
						bleedingClientConfig.bleeding_build_up_bar_additional_length,
						bleedingClientConfig.horizontal_bleeding_build_up_bar_texture_width,
						bleedingClientConfig.horizontal_bleeding_build_up_bar_texture_height,
						bleedingClientConfig.vertical_bleeding_build_up_bar_texture_width,
						bleedingClientConfig.vertical_bleeding_build_up_bar_texture_height,
						bleedingClientConfig.horizontal_bleeding_overlay_texture_width,
						bleedingClientConfig.horizontal_bleeding_overlay_texture_height,
						bleedingClientConfig.vertical_bleeding_overlay_texture_width,
						bleedingClientConfig.vertical_bleeding_overlay_texture_height,
						bleedingClientConfig.bleeding_element_fill_direction,
						0,
						bleedingClientConfig.enable_bleeding_build_up_bar_animation,
						bleedingClientConfig.show_current_bleeding_value_overlay,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUpReduction()),
						bleedingClientConfig.bleeding_build_up_bar_animation_interval
				);

				if (bleedingClientConfig.show_bleeding_build_up_bar_number) {
					this.client.getProfiler().swap("bleeding_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							bleedingClientConfig.bleeding_build_up_bar_number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
				dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
				this.client.getProfiler().pop();
			}
			//endregion bleeding build up

			//region burn build up
			var burnClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().burnClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + burnClientConfig.burn_build_up_element_x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + burnClientConfig.burn_build_up_element_y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBurnBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + burnClientConfig.burn_build_up_bar_number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + burnClientConfig.burn_build_up_bar_number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("burn_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						buildUpElementX,
						buildUpElementY,
						BURN_TEXTURES,
						burnClientConfig.burn_build_up_bar_additional_length,
						burnClientConfig.horizontal_burn_build_up_bar_texture_width,
						burnClientConfig.horizontal_burn_build_up_bar_texture_height,
						burnClientConfig.vertical_burn_build_up_bar_texture_width,
						burnClientConfig.vertical_burn_build_up_bar_texture_height,
						burnClientConfig.horizontal_burn_overlay_texture_width,
						burnClientConfig.horizontal_burn_overlay_texture_height,
						burnClientConfig.vertical_burn_overlay_texture_width,
						burnClientConfig.vertical_burn_overlay_texture_height,
						burnClientConfig.burn_element_fill_direction,
						1,
						burnClientConfig.enable_burn_build_up_bar_animation,
						burnClientConfig.show_current_burn_value_overlay,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUpReduction()),
						burnClientConfig.burn_build_up_bar_animation_interval
				);

				if (burnClientConfig.show_burn_build_up_bar_number) {
					this.client.getProfiler().swap("burn_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							burnClientConfig.burn_build_up_bar_number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
				dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
				this.client.getProfiler().pop();
			}
			//endregion burn build up

			//region freeze build up
			var freezeClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().freezeClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + freezeClientConfig.freeze_build_up_element_x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + freezeClientConfig.freeze_build_up_element_y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxFreezeBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + freezeClientConfig.freeze_build_up_bar_number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + freezeClientConfig.freeze_build_up_bar_number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("freeze_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						buildUpElementX,
						buildUpElementY,
						FREEZE_TEXTURES,
						freezeClientConfig.freeze_build_up_bar_additional_length,
						freezeClientConfig.horizontal_freeze_build_up_bar_texture_width,
						freezeClientConfig.horizontal_freeze_build_up_bar_texture_height,
						freezeClientConfig.vertical_freeze_build_up_bar_texture_width,
						freezeClientConfig.vertical_freeze_build_up_bar_texture_height,
						freezeClientConfig.horizontal_freeze_overlay_texture_width,
						freezeClientConfig.horizontal_freeze_overlay_texture_height,
						freezeClientConfig.vertical_freeze_overlay_texture_width,
						freezeClientConfig.vertical_freeze_overlay_texture_height,
						freezeClientConfig.freeze_element_fill_direction,
						2,
						freezeClientConfig.enable_freeze_build_up_bar_animation,
						freezeClientConfig.show_current_freeze_value_overlay,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUpReduction()),
						freezeClientConfig.freeze_build_up_bar_animation_interval
				);

				if (freezeClientConfig.show_freeze_build_up_bar_number) {
					this.client.getProfiler().swap("freeze_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							freezeClientConfig.freeze_build_up_bar_number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
				dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
				this.client.getProfiler().pop();
			}
			//endregion freeze build up

			//region poison build up
			var poisonClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().poisonClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + poisonClientConfig.poison_build_up_element_x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + poisonClientConfig.poison_build_up_element_y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxPoisonBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + poisonClientConfig.poison_build_up_bar_number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + poisonClientConfig.poison_build_up_bar_number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("poison_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						buildUpElementX,
						buildUpElementY,
						POISON_TEXTURES,
						poisonClientConfig.poison_build_up_bar_additional_length,
						poisonClientConfig.horizontal_poison_build_up_bar_texture_width,
						poisonClientConfig.horizontal_poison_build_up_bar_texture_height,
						poisonClientConfig.vertical_poison_build_up_bar_texture_width,
						poisonClientConfig.vertical_poison_build_up_bar_texture_height,
						poisonClientConfig.horizontal_poison_overlay_texture_width,
						poisonClientConfig.horizontal_poison_overlay_texture_height,
						poisonClientConfig.vertical_poison_overlay_texture_width,
						poisonClientConfig.vertical_poison_overlay_texture_height,
						poisonClientConfig.poison_element_fill_direction,
						3,
						poisonClientConfig.enable_poison_build_up_bar_animation,
						poisonClientConfig.show_current_poison_value_overlay,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUpReduction()),
						poisonClientConfig.poison_build_up_bar_animation_interval
				);

				if (poisonClientConfig.show_poison_build_up_bar_number) {
					this.client.getProfiler().swap("poison_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							poisonClientConfig.poison_build_up_bar_number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
				dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
				this.client.getProfiler().pop();
			}
			//endregion poison build up

			//region shock build up
			var shockClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().shockClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + shockClientConfig.shock_build_up_element_x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + shockClientConfig.shock_build_up_element_y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxShockBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + shockClientConfig.shock_build_up_bar_number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + shockClientConfig.shock_build_up_bar_number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("shock_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						buildUpElementX,
						buildUpElementY,
						SHOCK_TEXTURES,
						shockClientConfig.shock_build_up_bar_additional_length,
						shockClientConfig.horizontal_shock_build_up_bar_texture_width,
						shockClientConfig.horizontal_shock_build_up_bar_texture_height,
						shockClientConfig.vertical_shock_build_up_bar_texture_width,
						shockClientConfig.vertical_shock_build_up_bar_texture_height,
						shockClientConfig.horizontal_shock_overlay_texture_width,
						shockClientConfig.horizontal_shock_overlay_texture_height,
						shockClientConfig.vertical_shock_overlay_texture_width,
						shockClientConfig.vertical_shock_overlay_texture_height,
						shockClientConfig.shock_element_fill_direction,
						4,
						shockClientConfig.enable_shock_build_up_bar_animation,
						shockClientConfig.show_current_shock_value_overlay,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUpReduction()),
						shockClientConfig.shock_build_up_bar_animation_interval
				);

				if (shockClientConfig.show_shock_build_up_bar_number) {
					this.client.getProfiler().swap("shock_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							shockClientConfig.shock_build_up_bar_number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
				dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
				this.client.getProfiler().pop();
			}
			//endregion shock build up

			//region stagger build up
			var staggerClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().staggerClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + staggerClientConfig.stagger_build_up_element_x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + staggerClientConfig.stagger_build_up_element_y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxStaggerBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + staggerClientConfig.stagger_build_up_bar_number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + staggerClientConfig.stagger_build_up_bar_number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("stagger_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						buildUpElementX,
						buildUpElementY,
						STAGGER_TEXTURES,
						staggerClientConfig.stagger_build_up_bar_additional_length,
						staggerClientConfig.horizontal_stagger_build_up_bar_texture_width,
						staggerClientConfig.horizontal_stagger_build_up_bar_texture_height,
						staggerClientConfig.vertical_stagger_build_up_bar_texture_width,
						staggerClientConfig.vertical_stagger_build_up_bar_texture_height,
						staggerClientConfig.horizontal_stagger_overlay_texture_width,
						staggerClientConfig.horizontal_stagger_overlay_texture_height,
						staggerClientConfig.vertical_stagger_overlay_texture_width,
						staggerClientConfig.vertical_stagger_overlay_texture_height,
						staggerClientConfig.stagger_element_fill_direction,
						5,
						staggerClientConfig.enable_stagger_build_up_bar_animation,
						staggerClientConfig.show_current_stagger_value_overlay,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUpReduction()),
						staggerClientConfig.stagger_build_up_bar_animation_interval
				);

				if (staggerClientConfig.show_stagger_build_up_bar_number) {
					this.client.getProfiler().swap("stagger_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							staggerClientConfig.stagger_build_up_bar_number_color
					);
				}

//					dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
//					dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
				this.client.getProfiler().pop();
			}
			//endregion stagger build up
		}

	}

	@Unique
	private void drawEffectBuildUpElement(
			DrawContext context,
			int currentBuildUp,
			int maxBuildUp,
			int buildUpElementX,
			int buildUpElementY,
			Identifier[] texture_ids,
			int additional_bar_length,
			int horizontal_texture_width,
			int horizontal_texture_height,
			int vertical_texture_width,
			int vertical_texture_height,
			int horizontal_overlay_width,
			int horizontal_overlay_height,
			int vertical_overlay_width,
			int vertical_overlay_height,
			ClientConfig.FillDirection fill_direction,
			int effect_id,
			boolean enable_smooth_animation,
			boolean show_current_value_overlay,
			int build_up_reduction,
			int build_up_bar_animation_interval
	) {

		int barEndsLength; // TODO find better name
		if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP || fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
			barEndsLength = (vertical_texture_height - 1) / 2;
		} else {
			barEndsLength = (horizontal_texture_width - 1) / 2;
		}
		int barLength = barEndsLength + additional_bar_length + barEndsLength;

		int normalizedBuildUpRatio = (int) (((double) currentBuildUp / Math.max(maxBuildUp, 1)) * (barLength));

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
			context.drawTexture(texture_ids[3], buildUpElementX, buildUpElementY, 0, 0, vertical_texture_width, barEndsLength, vertical_texture_width, vertical_texture_height);
			if (additional_bar_length > 0) {
				for (int i = 0; i < additional_bar_length; i++) {
					context.drawTexture(texture_ids[3], buildUpElementX, buildUpElementY + barEndsLength + i, 0, barEndsLength, vertical_texture_width, 1, vertical_texture_width, vertical_texture_height);
				}
			}
			context.drawTexture(texture_ids[3], buildUpElementX, buildUpElementY + barEndsLength + additional_bar_length, 0, barEndsLength + 1, vertical_texture_width, barEndsLength, vertical_texture_width, vertical_texture_height);
		} else {
			context.drawTexture(texture_ids[0], buildUpElementX, buildUpElementY, 0, 0, barEndsLength, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
			if (additional_bar_length > 0) {
				for (int i = 0; i < additional_bar_length; i++) {
					context.drawTexture(texture_ids[0], buildUpElementX + barEndsLength + i, buildUpElementY, barEndsLength, 0, 1, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
				}
			}
			context.drawTexture(texture_ids[0], buildUpElementX + barEndsLength + additional_bar_length, buildUpElementY, barEndsLength + 1, 0, barEndsLength, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
		}

		// foreground
		int displayRatio = enable_smooth_animation ? this.oldNormalizedBuildUpRatios[effect_id] : normalizedBuildUpRatio;
		if (displayRatio > 0) {
			int ratioFirstPart = Math.min(barEndsLength, displayRatio);
			int ratioLastPart = Math.min(barEndsLength, displayRatio - barEndsLength - additional_bar_length);
			if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP) {
				// 1: bottom to top
				context.drawTexture(texture_ids[4], buildUpElementX, buildUpElementY + barEndsLength + additional_bar_length + (barEndsLength - ratioFirstPart), 0, barEndsLength + 1 + (barEndsLength - ratioFirstPart), vertical_texture_width, ratioFirstPart, vertical_texture_width, vertical_texture_height);
				if (displayRatio > barEndsLength && additional_bar_length > 0) {
					for (int i = barEndsLength; i <= Math.min(barEndsLength + additional_bar_length, displayRatio); i++) {
						context.drawTexture(texture_ids[4], buildUpElementX, buildUpElementY + barLength - i, 0, barEndsLength, vertical_texture_width, 1, vertical_texture_width, vertical_texture_height);
					}
				}
				if (displayRatio > (barEndsLength + additional_bar_length)) {
					context.drawTexture(texture_ids[4], buildUpElementX, buildUpElementY + barEndsLength - ratioLastPart, 0, barEndsLength - ratioLastPart, vertical_texture_width, ratioLastPart, vertical_texture_width, vertical_texture_height);
				}
			} else if (fill_direction == ClientConfig.FillDirection.RIGHT_TO_LEFT) {
				// 2: right to left
				context.drawTexture(texture_ids[1], buildUpElementX + barEndsLength + additional_bar_length + (barEndsLength - ratioFirstPart), buildUpElementY, barEndsLength + 1 + (barEndsLength - ratioFirstPart), 0, Math.min(barEndsLength, displayRatio), horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
				if (displayRatio > barEndsLength && additional_bar_length > 0) {
					for (int i = barEndsLength; i <= Math.min(barEndsLength + additional_bar_length, displayRatio); i++) {
						context.drawTexture(texture_ids[1], buildUpElementX + barLength - i, buildUpElementY, barEndsLength, 0, 1, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
					}
				}
				if (displayRatio > (barEndsLength + additional_bar_length)) {
					context.drawTexture(texture_ids[1], buildUpElementX + barEndsLength - ratioLastPart, buildUpElementY, barEndsLength - ratioLastPart, 0, ratioLastPart, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
				}
			} else if (fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
				// 3: top to bottom
				context.drawTexture(texture_ids[4], buildUpElementX, buildUpElementY, 0, 0, vertical_texture_width, ratioFirstPart, vertical_texture_width, vertical_texture_height);
				if (displayRatio > barEndsLength && additional_bar_length > 0) {
					for (int i = barEndsLength; i < Math.min(barEndsLength + additional_bar_length, displayRatio); i++) {
						context.drawTexture(texture_ids[4], buildUpElementX, buildUpElementY + i, 0, barEndsLength, vertical_texture_width, 1, vertical_texture_width, vertical_texture_height);
					}
				}
				if (displayRatio > (barEndsLength + additional_bar_length)) {
					context.drawTexture(texture_ids[4], buildUpElementX, buildUpElementY + barEndsLength + additional_bar_length, 0, barEndsLength + 1, vertical_texture_width, ratioLastPart, vertical_texture_width, vertical_texture_height);
				}
			} else {
				// 0: left to right
				context.drawTexture(texture_ids[1], buildUpElementX, buildUpElementY, 0, 0, ratioFirstPart, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
				if (displayRatio > barEndsLength && additional_bar_length > 0) {
					for (int i = barEndsLength; i < Math.min(barEndsLength + additional_bar_length, displayRatio); i++) {
						context.drawTexture(texture_ids[1], buildUpElementX + i, buildUpElementY, barEndsLength, 0, 1, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
					}
				}
				if (displayRatio > (barEndsLength + additional_bar_length)) {
					context.drawTexture(texture_ids[1], buildUpElementX + barEndsLength + additional_bar_length, buildUpElementY, barEndsLength + 1, 0, ratioLastPart, horizontal_texture_height, horizontal_texture_width, horizontal_texture_height);
				}
			}
		}

		// overlay
		if (show_current_value_overlay) {
			if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP) {
				// 1: bottom to top
				if (currentBuildUp > 0 && currentBuildUp < maxBuildUp) {
					context.drawTexture(texture_ids[2], buildUpElementX, buildUpElementY + barLength - normalizedBuildUpRatio - ((int) Math.floor(vertical_overlay_height / 2.0)), 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
				}
			} else if (fill_direction == ClientConfig.FillDirection.RIGHT_TO_LEFT) {
				// 2: right to left
				if (currentBuildUp > 0 && currentBuildUp < maxBuildUp) {
					context.drawTexture(texture_ids[2], buildUpElementX + barLength - normalizedBuildUpRatio - ((int) Math.floor(horizontal_overlay_width / 2.0)), buildUpElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
				}
			} else if (fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
				// 3: top to bottom
				if (currentBuildUp > 0 && currentBuildUp < maxBuildUp) {
					context.drawTexture(texture_ids[2], buildUpElementX, buildUpElementY + normalizedBuildUpRatio - ((int) Math.floor(vertical_overlay_height / 2.0)), 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
				}
			} else {
				// 0: left to right
				if (currentBuildUp > 0 && currentBuildUp < maxBuildUp) {
					context.drawTexture(texture_ids[2], buildUpElementX + normalizedBuildUpRatio - ((int) Math.floor(horizontal_overlay_width / 2.0)), buildUpElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
				}
			}
		}
	}
	
	@Unique
	private void drawEffectBuildUpNumber(
			DrawContext context,
			String buildUpBarNumberString,
			int build_up_bar_number_x,
			int build_up_bar_number_y,
			int build_up_bar_number_color
	) {
			context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x + 1, build_up_bar_number_y, 0, false);
			context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x - 1, build_up_bar_number_y, 0, false);
			context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x, build_up_bar_number_y + 1, 0, false);
			context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x, build_up_bar_number_y - 1, 0, false);
			context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x, build_up_bar_number_y, build_up_bar_number_color, false);
	}
}
