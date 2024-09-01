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

	@Shadow
	public abstract TextRenderer getTextRenderer();

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
			int buildUpElementX = (context.getScaledWindowWidth() / 2 + bleedingClientConfig.x_offset) + dynamic_x_offset;
			int buildUpElementY = (context.getScaledWindowHeight() / 2 + bleedingClientConfig.y_offset) + dynamic_y_offset;
			int currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUp());
			int maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBleedingBuildUp());
			String buildUpBarNumberString = String.valueOf(currentBuildUp);
			int buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + bleedingClientConfig.number_x_offset) + dynamic_x_offset;
			int buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + bleedingClientConfig.number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("bleeding_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUpReduction()),
						buildUpElementX,
						buildUpElementY,
						BLEEDING_TEXTURES,
						bleedingClientConfig.fill_direction,
						bleedingClientConfig.background_additional_middle_segment_amount,
						bleedingClientConfig.horizontal_background_left_end_width,
						bleedingClientConfig.horizontal_background_right_end_width,
						bleedingClientConfig.horizontal_background_middle_segment_width,
						bleedingClientConfig.horizontal_background_height,
						bleedingClientConfig.vertical_background_width,
						bleedingClientConfig.vertical_background_top_end_height,
						bleedingClientConfig.vertical_background_bottom_end_height,
						bleedingClientConfig.vertical_background_middle_segment_height,
						bleedingClientConfig.progress_offset_x,
						bleedingClientConfig.progress_offset_y,
						bleedingClientConfig.progress_additional_middle_segment_amount,
						bleedingClientConfig.horizontal_progress_left_end_width,
						bleedingClientConfig.horizontal_progress_right_end_width,
						bleedingClientConfig.horizontal_progress_middle_segment_width,
						bleedingClientConfig.horizontal_progress_height,
						bleedingClientConfig.vertical_progress_width,
						bleedingClientConfig.vertical_progress_top_end_height,
						bleedingClientConfig.vertical_progress_bottom_end_height,
						bleedingClientConfig.vertical_progress_middle_segment_height,
						bleedingClientConfig.show_current_value_overlay,
						bleedingClientConfig.overlay_offset_x,
						bleedingClientConfig.overlay_offset_y,
						bleedingClientConfig.horizontal_overlay_width,
						bleedingClientConfig.horizontal_overlay_height,
						bleedingClientConfig.vertical_overlay_width,
						bleedingClientConfig.vertical_overlay_height,
						bleedingClientConfig.enable_smooth_animation,
						bleedingClientConfig.animation_interval,
						0
				);

				if (bleedingClientConfig.show_number) {
					this.client.getProfiler().swap("bleeding_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							bleedingClientConfig.number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
				dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
				this.client.getProfiler().pop();
			}
			//endregion bleeding build up

			//region burn build up
			var burnClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().burnClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + burnClientConfig.x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + burnClientConfig.y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBurnBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + burnClientConfig.number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + burnClientConfig.number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("burn_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUpReduction()),
						buildUpElementX,
						buildUpElementY,
						BURN_TEXTURES,
						burnClientConfig.fill_direction,
						burnClientConfig.background_additional_middle_segment_amount,
						burnClientConfig.horizontal_background_left_end_width,
						burnClientConfig.horizontal_background_middle_segment_width,
						burnClientConfig.horizontal_background_right_end_width,
						burnClientConfig.horizontal_background_height,
						burnClientConfig.vertical_background_width,
						burnClientConfig.vertical_background_top_end_height,
						burnClientConfig.vertical_background_middle_segment_height,
						burnClientConfig.vertical_background_bottom_end_height,
						burnClientConfig.progress_offset_x,
						burnClientConfig.progress_offset_y,
						burnClientConfig.progress_additional_middle_segment_amount,
						burnClientConfig.horizontal_progress_left_end_width,
						burnClientConfig.horizontal_progress_middle_segment_width,
						burnClientConfig.horizontal_progress_right_end_width,
						burnClientConfig.horizontal_progress_height,
						burnClientConfig.vertical_progress_width,
						burnClientConfig.vertical_progress_top_end_height,
						burnClientConfig.vertical_progress_middle_segment_height,
						burnClientConfig.vertical_progress_bottom_end_height,
						burnClientConfig.show_current_value_overlay,
						burnClientConfig.overlay_offset_x,
						burnClientConfig.overlay_offset_y,
						burnClientConfig.horizontal_overlay_width,
						burnClientConfig.horizontal_overlay_height,
						burnClientConfig.vertical_overlay_width,
						burnClientConfig.vertical_overlay_height,
						burnClientConfig.enable_smooth_animation,
						burnClientConfig.animation_interval,
						1
				);

				if (burnClientConfig.show_number) {
					this.client.getProfiler().swap("burn_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							burnClientConfig.number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
				dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
				this.client.getProfiler().pop();
			}
			//endregion burn build up

			//region freeze build up
			var freezeClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().freezeClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + freezeClientConfig.x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + freezeClientConfig.y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxFreezeBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + freezeClientConfig.number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + freezeClientConfig.number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("freeze_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUpReduction()),
						buildUpElementX,
						buildUpElementY,
						FREEZE_TEXTURES,
						freezeClientConfig.fill_direction,
						freezeClientConfig.background_additional_middle_segment_amount,
						freezeClientConfig.horizontal_background_left_end_width,
						freezeClientConfig.horizontal_background_right_end_width,
						freezeClientConfig.horizontal_background_middle_segment_width,
						freezeClientConfig.horizontal_background_height,
						freezeClientConfig.vertical_background_width,
						freezeClientConfig.vertical_background_top_end_height,
						freezeClientConfig.vertical_background_bottom_end_height,
						freezeClientConfig.vertical_background_middle_segment_height,
						freezeClientConfig.progress_offset_x,
						freezeClientConfig.progress_offset_y,
						freezeClientConfig.progress_additional_middle_segment_amount,
						freezeClientConfig.horizontal_progress_left_end_width,
						freezeClientConfig.horizontal_progress_right_end_width,
						freezeClientConfig.horizontal_progress_middle_segment_width,
						freezeClientConfig.horizontal_progress_height,
						freezeClientConfig.vertical_progress_width,
						freezeClientConfig.vertical_progress_top_end_height,
						freezeClientConfig.vertical_progress_bottom_end_height,
						freezeClientConfig.vertical_progress_middle_segment_height,
						freezeClientConfig.show_current_value_overlay,
						freezeClientConfig.overlay_offset_x,
						freezeClientConfig.overlay_offset_y,
						freezeClientConfig.horizontal_overlay_width,
						freezeClientConfig.horizontal_overlay_height,
						freezeClientConfig.vertical_overlay_width,
						freezeClientConfig.vertical_overlay_height,
						freezeClientConfig.enable_smooth_animation,
						freezeClientConfig.animation_interval,
						2
				);

				if (freezeClientConfig.show_number) {
					this.client.getProfiler().swap("freeze_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							freezeClientConfig.number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
				dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
				this.client.getProfiler().pop();
			}
			//endregion freeze build up

			//region poison build up
			var poisonClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().poisonClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + poisonClientConfig.x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + poisonClientConfig.y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxPoisonBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + poisonClientConfig.number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + poisonClientConfig.number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("poison_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUpReduction()),
						buildUpElementX,
						buildUpElementY,
						POISON_TEXTURES,
						poisonClientConfig.fill_direction,
						poisonClientConfig.background_additional_middle_segment_amount,
						poisonClientConfig.horizontal_background_left_end_width,
						poisonClientConfig.horizontal_background_right_end_width,
						poisonClientConfig.horizontal_background_middle_segment_width,
						poisonClientConfig.horizontal_background_height,
						poisonClientConfig.vertical_background_width,
						poisonClientConfig.vertical_background_top_end_height,
						poisonClientConfig.vertical_background_bottom_end_height,
						poisonClientConfig.vertical_background_middle_segment_height,
						poisonClientConfig.progress_offset_x,
						poisonClientConfig.progress_offset_y,
						poisonClientConfig.progress_additional_middle_segment_amount,
						poisonClientConfig.horizontal_progress_left_end_width,
						poisonClientConfig.horizontal_progress_right_end_width,
						poisonClientConfig.horizontal_progress_middle_segment_width,
						poisonClientConfig.horizontal_progress_height,
						poisonClientConfig.vertical_progress_width,
						poisonClientConfig.vertical_progress_top_end_height,
						poisonClientConfig.vertical_progress_bottom_end_height,
						poisonClientConfig.vertical_progress_middle_segment_height,
						poisonClientConfig.show_current_value_overlay,
						poisonClientConfig.overlay_offset_x,
						poisonClientConfig.overlay_offset_y,
						poisonClientConfig.horizontal_overlay_width,
						poisonClientConfig.horizontal_overlay_height,
						poisonClientConfig.vertical_overlay_width,
						poisonClientConfig.vertical_overlay_height,
						poisonClientConfig.enable_smooth_animation,
						poisonClientConfig.animation_interval,
						3
				);

				if (poisonClientConfig.show_number) {
					this.client.getProfiler().swap("poison_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							poisonClientConfig.number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
				dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
				this.client.getProfiler().pop();
			}
			//endregion poison build up

			//region shock build up
			var shockClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().shockClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + shockClientConfig.x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + shockClientConfig.y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxShockBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + shockClientConfig.number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + shockClientConfig.number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("shock_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUpReduction()),
						buildUpElementX,
						buildUpElementY,
						SHOCK_TEXTURES,
						shockClientConfig.fill_direction,
						shockClientConfig.background_additional_middle_segment_amount,
						shockClientConfig.horizontal_background_left_end_width,
						shockClientConfig.horizontal_background_right_end_width,
						shockClientConfig.horizontal_background_middle_segment_width,
						shockClientConfig.horizontal_background_height,
						shockClientConfig.vertical_background_width,
						shockClientConfig.vertical_background_top_end_height,
						shockClientConfig.vertical_background_bottom_end_height,
						shockClientConfig.vertical_background_middle_segment_height,
						shockClientConfig.progress_offset_x,
						shockClientConfig.progress_offset_y,
						shockClientConfig.progress_additional_middle_segment_amount,
						shockClientConfig.horizontal_progress_left_end_width,
						shockClientConfig.horizontal_progress_right_end_width,
						shockClientConfig.horizontal_progress_middle_segment_width,
						shockClientConfig.horizontal_progress_height,
						shockClientConfig.vertical_progress_width,
						shockClientConfig.vertical_progress_top_end_height,
						shockClientConfig.vertical_progress_bottom_end_height,
						shockClientConfig.vertical_progress_middle_segment_height,
						shockClientConfig.show_current_value_overlay,
						shockClientConfig.overlay_offset_x,
						shockClientConfig.overlay_offset_y,
						shockClientConfig.horizontal_overlay_width,
						shockClientConfig.horizontal_overlay_height,
						shockClientConfig.vertical_overlay_width,
						shockClientConfig.vertical_overlay_height,
						shockClientConfig.enable_smooth_animation,
						shockClientConfig.animation_interval,
						4
				);

				if (shockClientConfig.show_number) {
					this.client.getProfiler().swap("shock_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							shockClientConfig.number_color
					);
				}

				dynamic_x_offset = dynamic_x_offset + generalClientConfig.dynamic_x_offset_increase;
				dynamic_y_offset = dynamic_y_offset + generalClientConfig.dynamic_y_offset_increase;
				this.client.getProfiler().pop();
			}
			//endregion shock build up

			//region stagger build up
			var staggerClientConfig = OverhauledDamageClient.clientConfigHolder.getConfig().staggerClientConfig;
			buildUpElementX = (context.getScaledWindowWidth() / 2 + staggerClientConfig.x_offset) + dynamic_x_offset;
			buildUpElementY = (context.getScaledWindowHeight() / 2 + staggerClientConfig.y_offset) + dynamic_y_offset;
			currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUp());
			maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxStaggerBuildUp());
			buildUpBarNumberString = String.valueOf(currentBuildUp);
			buildUpBarNumberX = ((context.getScaledWindowWidth() - this.getTextRenderer().getWidth(buildUpBarNumberString)) / 2 + staggerClientConfig.number_x_offset) + dynamic_x_offset;
			buildUpBarNumberY = (context.getScaledWindowHeight() / 2 + staggerClientConfig.number_y_offset) + dynamic_y_offset;

			if (currentBuildUp > 0) {
				this.client.getProfiler().push("stagger_build_up_element");

				this.drawEffectBuildUpElement(
						context,
						currentBuildUp,
						maxBuildUp,
						MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUpReduction()),
						buildUpElementX,
						buildUpElementY,
						STAGGER_TEXTURES,
						staggerClientConfig.fill_direction,
						staggerClientConfig.background_additional_middle_segment_amount,
						staggerClientConfig.horizontal_background_left_end_width,
						staggerClientConfig.horizontal_background_right_end_width,
						staggerClientConfig.horizontal_background_middle_segment_width,
						staggerClientConfig.horizontal_background_height,
						staggerClientConfig.vertical_background_width,
						staggerClientConfig.vertical_background_top_end_height,
						staggerClientConfig.vertical_background_bottom_end_height,
						staggerClientConfig.vertical_background_middle_segment_height,
						staggerClientConfig.progress_offset_x,
						staggerClientConfig.progress_offset_y,
						staggerClientConfig.progress_additional_middle_segment_amount,
						staggerClientConfig.horizontal_progress_left_end_width,
						staggerClientConfig.horizontal_progress_right_end_width,
						staggerClientConfig.horizontal_progress_middle_segment_width,
						staggerClientConfig.horizontal_progress_height,
						staggerClientConfig.vertical_progress_width,
						staggerClientConfig.vertical_progress_top_end_height,
						staggerClientConfig.vertical_progress_bottom_end_height,
						staggerClientConfig.vertical_progress_middle_segment_height,
						staggerClientConfig.show_current_value_overlay,
						staggerClientConfig.overlay_offset_x,
						staggerClientConfig.overlay_offset_y,
						staggerClientConfig.horizontal_overlay_width,
						staggerClientConfig.horizontal_overlay_height,
						staggerClientConfig.vertical_overlay_width,
						staggerClientConfig.vertical_overlay_height,
						staggerClientConfig.enable_smooth_animation,
						staggerClientConfig.animation_interval,
						5
				);

				if (staggerClientConfig.show_number) {
					this.client.getProfiler().swap("stagger_build_up_number");
					this.drawEffectBuildUpNumber(
							context,
							buildUpBarNumberString,
							buildUpBarNumberX,
							buildUpBarNumberY,
							staggerClientConfig.number_color
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
			int current_build_up,
			int max_build_up,
			int build_up_reduction,
			int build_up_element_x,
			int build_up_element_y,
			Identifier[] texture_ids,
			ClientConfig.FillDirection fill_direction,
			int background_additional_middle_segment_amount,
			int horizontal_background_left_end_width,
			int horizontal_background_middle_segment_width,
			int horizontal_background_right_end_width,
			int horizontal_background_height,
			int vertical_background_width,
			int vertical_background_top_end_height,
			int vertical_background_middle_segment_height,
			int vertical_background_bottom_end_height,
			int progress_offset_x,
			int progress_offset_y,
			int progress_additional_middle_segment_amount,
			int horizontal_progress_left_end_width,
			int horizontal_progress_middle_segment_width,
			int horizontal_progress_right_end_width,
			int horizontal_progress_height,
			int vertical_progress_width,
			int vertical_progress_top_end_height,
			int vertical_progress_middle_segment_height,
			int vertical_progress_bottom_end_height,
			boolean show_current_value_overlay,
			int overlay_offset_x,
			int overlay_offset_y,
			int horizontal_overlay_width,
			int horizontal_overlay_height,
			int vertical_overlay_width,
			int vertical_overlay_height,
			boolean enable_smooth_animation,
			int animation_interval,
			int effect_id
	) {

//		int backgroundBarLength;
		int progressBarLength;
		int backgroundTextureHeight;
		int backgroundTextureWidth;
		int backgroundMiddleSectionLength;
		int progressTextureHeight;
		int progressTextureWidth;
		int progressMiddleSectionLength;

		// region variable calculation
		if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP || fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
			backgroundTextureHeight = vertical_background_top_end_height + vertical_background_middle_segment_height + vertical_background_bottom_end_height;
			backgroundTextureWidth = vertical_background_width;
			progressTextureHeight = vertical_progress_top_end_height + vertical_progress_middle_segment_height + vertical_progress_bottom_end_height;
			progressTextureWidth = vertical_progress_width;
			backgroundMiddleSectionLength = background_additional_middle_segment_amount * vertical_background_middle_segment_height;
			progressMiddleSectionLength = progress_additional_middle_segment_amount * vertical_progress_middle_segment_height;
//			backgroundBarLength = vertical_background_top_end_height + backgroundMiddleSectionLength + vertical_background_bottom_end_height;
			progressBarLength = vertical_progress_top_end_height + progressMiddleSectionLength + vertical_progress_bottom_end_height;
		} else {
			backgroundTextureHeight = horizontal_background_height;
			backgroundTextureWidth = horizontal_background_left_end_width + horizontal_background_middle_segment_width + horizontal_background_right_end_width;
			progressTextureHeight = horizontal_progress_height;
			progressTextureWidth = horizontal_progress_left_end_width + horizontal_progress_middle_segment_width + horizontal_progress_right_end_width;
			backgroundMiddleSectionLength = background_additional_middle_segment_amount * horizontal_background_middle_segment_width;
			progressMiddleSectionLength = progress_additional_middle_segment_amount * horizontal_progress_middle_segment_width;
//			backgroundBarLength = horizontal_background_left_end_width + backgroundMiddleSectionLength + horizontal_background_right_end_width;
			progressBarLength = horizontal_progress_left_end_width + progressMiddleSectionLength + horizontal_progress_right_end_width;
		}
		// endregion variable calculation

		int normalizedBuildUpRatio = (int) (((double) current_build_up / Math.max(max_build_up, 1)) * (progressBarLength));

		if (this.oldMaxBuildUps[effect_id] != max_build_up) {
			this.oldMaxBuildUps[effect_id] = max_build_up;
			this.oldNormalizedBuildUpRatios[effect_id] = normalizedBuildUpRatio;
		}

		this.buildUpBarAnimationCounters[effect_id] = this.buildUpBarAnimationCounters[effect_id] + Math.max(1, build_up_reduction);

		if (this.oldNormalizedBuildUpRatios[effect_id] != normalizedBuildUpRatio && this.buildUpBarAnimationCounters[effect_id] > Math.max(0, animation_interval)) {
			boolean reduceOldRatio = this.oldNormalizedBuildUpRatios[effect_id] > normalizedBuildUpRatio;
			this.oldNormalizedBuildUpRatios[effect_id] = this.oldNormalizedBuildUpRatios[effect_id] + (reduceOldRatio ? -1 : 1);
			this.buildUpBarAnimationCounters[effect_id] = 0;
		}

		// region background
		// background
		if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP || fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
			context.drawTexture(texture_ids[3], build_up_element_x, build_up_element_y, 0, 0, backgroundTextureWidth, vertical_background_top_end_height, backgroundTextureWidth, backgroundTextureHeight);
			if (background_additional_middle_segment_amount > 0) {
				for (int i = 0; i < background_additional_middle_segment_amount; i++) {
					context.drawTexture(texture_ids[3], build_up_element_x, build_up_element_y + vertical_background_top_end_height + (i * vertical_background_middle_segment_height), 0, vertical_background_top_end_height, backgroundTextureWidth, vertical_background_middle_segment_height, backgroundTextureWidth, backgroundTextureHeight);
				}
			}
			context.drawTexture(texture_ids[3], build_up_element_x, build_up_element_y + vertical_background_top_end_height + backgroundMiddleSectionLength, 0, vertical_background_top_end_height + vertical_background_middle_segment_height, backgroundTextureWidth, vertical_background_bottom_end_height, backgroundTextureWidth, backgroundTextureHeight);
		} else {
			context.drawTexture(texture_ids[0], build_up_element_x, build_up_element_y, 0, 0, horizontal_background_left_end_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
			if (background_additional_middle_segment_amount > 0) {
				for (int i = 0; i < background_additional_middle_segment_amount; i++) {
					context.drawTexture(texture_ids[0], build_up_element_x + horizontal_background_left_end_width + (i * horizontal_background_middle_segment_width), build_up_element_y, horizontal_background_left_end_width, 0, horizontal_background_middle_segment_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
				}
			}
			context.drawTexture(texture_ids[0], build_up_element_x + horizontal_background_left_end_width + backgroundMiddleSectionLength, build_up_element_y, horizontal_background_left_end_width + horizontal_background_middle_segment_width, 0, horizontal_progress_right_end_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
		}
		// endregion background

		// progress
		int displayRatio = enable_smooth_animation ? this.oldNormalizedBuildUpRatios[effect_id] : normalizedBuildUpRatio;
		if (displayRatio > 0) {
			int ratioFirstPart;
			int ratioLastPart;
			int progressElementX = build_up_element_x + progress_offset_x;
			int progressElementY = build_up_element_y + progress_offset_y;

			if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP) {
				// 1: bottom to top

				ratioFirstPart = Math.min(vertical_progress_bottom_end_height, displayRatio);
				ratioLastPart = Math.min(vertical_progress_top_end_height, displayRatio - vertical_progress_bottom_end_height - progressMiddleSectionLength);

				context.drawTexture(texture_ids[4], progressElementX, progressElementY + progressBarLength - ratioFirstPart, 0, progressTextureHeight - ratioFirstPart, progressTextureWidth, ratioFirstPart, progressTextureWidth, progressTextureHeight);
				if (displayRatio > vertical_progress_bottom_end_height && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 1; j <= vertical_progress_middle_segment_height; j++) {
							int currentTextureY = vertical_progress_bottom_end_height + (i * vertical_progress_middle_segment_height) + j;
							if (currentTextureY > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[4], progressElementX, progressElementY + progressBarLength - currentTextureY, 0, vertical_progress_top_end_height + vertical_progress_middle_segment_height - j, progressTextureWidth, 1, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}

				}
				if (displayRatio > (vertical_progress_bottom_end_height + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[4], progressElementX, progressElementY + vertical_progress_top_end_height - ratioLastPart, 0, vertical_progress_top_end_height - ratioLastPart, progressTextureWidth, ratioLastPart, progressTextureWidth, progressTextureHeight);
				}
			}
			else if (fill_direction == ClientConfig.FillDirection.RIGHT_TO_LEFT) {
				// 2: right to left

				ratioFirstPart = Math.min(horizontal_progress_right_end_width, displayRatio);
				ratioLastPart = Math.min(horizontal_progress_left_end_width, displayRatio - horizontal_progress_right_end_width - progressMiddleSectionLength);

				context.drawTexture(texture_ids[1], progressElementX + progressBarLength - ratioFirstPart, progressElementY, progressTextureWidth - ratioFirstPart, 0, ratioFirstPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				if (displayRatio > horizontal_progress_right_end_width && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 1; j <= horizontal_progress_middle_segment_width; j++) {
							int currentTextureX = horizontal_progress_left_end_width + (i * horizontal_progress_middle_segment_width) + j;
							if (currentTextureX > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[1], progressElementX + progressBarLength - currentTextureX, progressElementY, horizontal_progress_left_end_width + horizontal_progress_middle_segment_width - j, 0, 1, progressTextureHeight, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}

				}
				if (displayRatio > (horizontal_progress_right_end_width + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[1], progressElementX + horizontal_progress_left_end_width - ratioLastPart, progressElementY, horizontal_progress_left_end_width - ratioLastPart, 0, ratioLastPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				}
			}
			else if (fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
				// 3: top to bottom

				ratioFirstPart = Math.min(vertical_progress_top_end_height, displayRatio);
				ratioLastPart = Math.min(vertical_progress_bottom_end_height, displayRatio - vertical_progress_top_end_height - progressMiddleSectionLength);

				context.drawTexture(texture_ids[4], progressElementX, progressElementY, 0, 0, progressTextureWidth, ratioFirstPart, progressTextureWidth, progressTextureHeight);
				if (displayRatio > vertical_progress_top_end_height && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 0; j < vertical_progress_middle_segment_height; j++) {
							int currentTextureY = vertical_progress_top_end_height + (i * vertical_progress_middle_segment_height) + j;
							if (currentTextureY > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[4], progressElementX, progressElementY + currentTextureY, 0, vertical_progress_top_end_height + j, progressTextureWidth, 1, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}
				}
				if (displayRatio > (vertical_progress_top_end_height + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[4], progressElementX, progressElementY + vertical_progress_top_end_height + progressMiddleSectionLength, 0, vertical_progress_top_end_height + vertical_progress_middle_segment_height, progressTextureWidth, ratioLastPart, progressTextureWidth, progressTextureHeight);
				}
			}
			else {
				// 0: left to right

				ratioFirstPart = Math.min(horizontal_progress_left_end_width, displayRatio);
				ratioLastPart = Math.min(horizontal_progress_right_end_width, displayRatio - horizontal_progress_left_end_width - progressMiddleSectionLength);

				context.drawTexture(texture_ids[1], progressElementX, progressElementY, 0, 0, ratioFirstPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				if (displayRatio > horizontal_progress_left_end_width && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 0; j < horizontal_progress_middle_segment_width; j++) {
							int currentTextureX = horizontal_progress_left_end_width + (i * horizontal_progress_middle_segment_width) + j;
							if (currentTextureX > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[1], progressElementX + currentTextureX, progressElementY, horizontal_progress_left_end_width + j, 0, 1, progressTextureHeight, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}
				}
				if (displayRatio > (horizontal_progress_left_end_width + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[1], progressElementX + horizontal_progress_left_end_width + progressMiddleSectionLength, progressElementY, horizontal_progress_left_end_width + horizontal_progress_middle_segment_width, 0, ratioLastPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				}
			}
			
			// overlay
			if (show_current_value_overlay) {
				int overlayElementX = progressElementX + overlay_offset_x;
				int overlayElementY = progressElementY + overlay_offset_y;
				if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP) {
					// 1: bottom to top
					if (current_build_up > 0 && current_build_up < max_build_up) {
						context.drawTexture(texture_ids[5], overlayElementX, overlayElementY + progressBarLength - normalizedBuildUpRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
					}
				} else if (fill_direction == ClientConfig.FillDirection.RIGHT_TO_LEFT) {
					// 2: right to left
					if (current_build_up > 0 && current_build_up < max_build_up) {
						context.drawTexture(texture_ids[2], overlayElementX + progressBarLength - normalizedBuildUpRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
					}
				} else if (fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
					// 3: top to bottom
					if (current_build_up > 0 && current_build_up < max_build_up) {
						context.drawTexture(texture_ids[5], overlayElementX, overlayElementY + normalizedBuildUpRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
					}
				} else {
					// 0: left to right
					if (current_build_up > 0 && current_build_up < max_build_up) {
						context.drawTexture(texture_ids[2], overlayElementX + normalizedBuildUpRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
					}
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
