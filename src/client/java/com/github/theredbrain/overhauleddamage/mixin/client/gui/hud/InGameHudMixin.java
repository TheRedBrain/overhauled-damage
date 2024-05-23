package com.github.theredbrain.overhauleddamage.mixin.client.gui.hud;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.OverhauledDamageClient;
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

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow @Final private MinecraftClient client;

    @Unique
    private static final Identifier BLEEDING_BUILD_UP_BAR_BACKGROUND_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_red_background.png");
    @Unique
    private static final Identifier BLEEDING_BUILD_UP_BAR_PROGRESS_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_red_progress.png");
    @Unique
    private static final Identifier BURNING_BUILD_UP_BAR_BACKGROUND_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_red_background.png");
    @Unique
    private static final Identifier BURNING_BUILD_UP_BAR_PROGRESS_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_red_progress.png");
    @Unique
    private static final Identifier FREEZE_BUILD_UP_BAR_BACKGROUND_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_blue_background.png");
    @Unique
    private static final Identifier FREEZE_BUILD_UP_BAR_PROGRESS_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_blue_progress.png");
    @Unique
    private static final Identifier POISON_BUILD_UP_BAR_BACKGROUND_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_green_background.png");
    @Unique
    private static final Identifier POISON_BUILD_UP_BAR_PROGRESS_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_green_progress.png");
    @Unique
    private static final Identifier SHOCK_BUILD_UP_BAR_BACKGROUND_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_white_background.png");
    @Unique
    private static final Identifier SHOCK_BUILD_UP_BAR_PROGRESS_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_white_progress.png");
    @Unique
    private static final Identifier STAGGER_BAR_BACKGROUND_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_yellow_background.png");
    @Unique
    private static final Identifier STAGGER_BAR_PROGRESS_TEXTURE = OverhauledDamage.identifier("textures/gui/sprites/boss_bar/short_yellow_progress.png");

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void overhauleddamage$renderStatusBars(DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null && OverhauledDamageClient.clientConfig.show_effect_build_up_bars) {
            int bleedingBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUp());
            int maxBleedingBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBleedingBuildUp());
            int burnBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUp());
            int maxBurnBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBurnBuildUp());
            int freezeBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUp());
            int maxFreezeBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxFreezeBuildUp());
            int staggerBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUp());
            int maxStaggerBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxStaggerBuildUp());
            int poisonBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUp());
            int maxPoisonBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxPoisonBuildUp());
            int shockBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUp());
            int maxShockBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxShockBuildUp());

            int attributeBarX = this.scaledWidth / 2 - 91 + 60;
            int attributeBarY = this.scaledHeight / 2 - 7 + 25;
            int normalizedBleedingBuildUpRatio = (int) (((double) bleedingBuildUp / Math.max(maxBleedingBuildUp, 1)) * 62);
            int normalizedBurnBuildUpRatio = (int) (((double) burnBuildUp / Math.max(maxBurnBuildUp, 1)) * 62);
            int normalizedFreezeBuildUpRatio = (int) (((double) freezeBuildUp / Math.max(maxFreezeBuildUp, 1)) * 62);
            int normalizedStaggerBuildUpRatio = (int) (((double) staggerBuildUp / Math.max(maxStaggerBuildUp, 1)) * 62);
            int normalizedPoisonBuildUpRatio = (int) (((double) poisonBuildUp / Math.max(maxPoisonBuildUp, 1)) * 62);
            int normalizedShockBuildUpRatio = (int) (((double) shockBuildUp / Math.max(maxShockBuildUp, 1)) * 62);

            if (staggerBuildUp > 0) {
                this.client.getProfiler().swap("stagger_bar");
//                context.drawGuiTexture(STAGGER_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                context.drawTexture(STAGGER_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 62, 5, 62, 5);
                if (normalizedStaggerBuildUpRatio > 0) {
//                    context.drawGuiTexture(STAGGER_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedStaggerBuildUpRatio, 5);
                    context.drawTexture(STAGGER_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 0, normalizedStaggerBuildUpRatio, 5, 62, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // bleeding build up
            if (bleedingBuildUp > 0) {
                this.client.getProfiler().swap("bleeding_build_up_bar");
//                context.drawGuiTexture(BLEEDING_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                context.drawTexture(BLEEDING_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 62, 5, 62, 5);
                if (normalizedBleedingBuildUpRatio > 0) {
//                    context.drawGuiTexture(BLEEDING_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedBleedingBuildUpRatio, 5);
                    context.drawTexture(BLEEDING_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 0, normalizedBleedingBuildUpRatio, 5, 62, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // burn build up
            if (burnBuildUp > 0) {
                this.client.getProfiler().swap("burn_build_up_bar");
//                context.drawGuiTexture(BURNING_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                context.drawTexture(BURNING_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 62, 5, 62, 5);
                if (normalizedBurnBuildUpRatio > 0) {
//                    context.drawGuiTexture(BURNING_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedBurnBuildUpRatio, 5);
                    context.drawTexture(BURNING_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 0, normalizedBurnBuildUpRatio, 5, 62, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // freeze build up
            if (freezeBuildUp > 0) {
                this.client.getProfiler().swap("freeze_build_up_bar");
//                context.drawGuiTexture(FREEZE_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                context.drawTexture(FREEZE_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 62, 5, 62, 5);
                if (normalizedFreezeBuildUpRatio > 0) {
//                    context.drawGuiTexture(FREEZE_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedFreezeBuildUpRatio, 5);
                    context.drawTexture(FREEZE_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 0, normalizedFreezeBuildUpRatio, 5, 62, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // poison build up
            if (poisonBuildUp > 0) {
                this.client.getProfiler().swap("poison_build_up_bar");
//                context.drawGuiTexture(POISON_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                context.drawTexture(POISON_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 62, 5, 62, 5);
                if (normalizedPoisonBuildUpRatio > 0) {
//                    context.drawGuiTexture(POISON_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedPoisonBuildUpRatio, 5);
                    context.drawTexture(POISON_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 0, normalizedPoisonBuildUpRatio, 5, 62, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // shock build up
            if (shockBuildUp > 0) {
                this.client.getProfiler().swap("shock_build_up_bar");
//                context.drawGuiTexture(SHOCK_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                context.drawTexture(SHOCK_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 62, 5, 62, 5);
                if (normalizedShockBuildUpRatio > 0) {
//                    context.drawGuiTexture(SHOCK_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedShockBuildUpRatio, 5);
                    context.drawTexture(SHOCK_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 0, normalizedShockBuildUpRatio, 5, 62, 5);
                }
            }
            this.client.getProfiler().pop();
            ci.cancel();
        }
    }
}
