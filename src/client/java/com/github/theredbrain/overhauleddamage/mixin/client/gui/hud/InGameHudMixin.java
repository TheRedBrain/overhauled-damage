package com.github.theredbrain.overhauleddamage.mixin.client.gui.hud;

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

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private static final Identifier BARS_TEXTURE = new Identifier("textures/gui/bars.png");

    @Inject(method = "renderStatusBars", at = @At("HEAD"))
    private void overhauleddamage$renderStatusBars(DrawContext context, CallbackInfo ci) {
        var clientConfig = OverhauledDamageClient.clientConfig;
        if (clientConfig.show_effect_build_up_elements) {
            PlayerEntity playerEntity = this.getCameraPlayer();
            if (playerEntity != null) {
                boolean use_custom_textures = clientConfig.use_custom_textures;

                int dynamic_x_offset = 0;
                int dynamic_y_offset = 0;
                int dynamic_x_offset_increment = clientConfig.dynamic_x_offset_increase;
                int dynamic_y_offset_increment = clientConfig.dynamic_y_offset_increase;

                //region bleeding build up
                int buildUpElementX = this.scaledWidth / 2 + clientConfig.bleeding_build_up_element_x_offset;
                int buildUpElementY = this.scaledHeight / 2 + clientConfig.bleeding_build_up_element_y_offset;
                int currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBleedingBuildUp());
                int maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBleedingBuildUp());

                if (currentBuildUp > 0) {
                    this.client.getProfiler().push("bleeding_build_up_element");

                    if (use_custom_textures) {
                        this.drawCustomEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset,
                                Identifier.tryParse(clientConfig.custom_bleeding_element_background_texture_id),
                                Identifier.tryParse(clientConfig.custom_bleeding_element_foreground_texture_id),
                                clientConfig.custom_bleeding_element_texture_width,
                                clientConfig.custom_bleeding_element_texture_height,
                                clientConfig.custom_bleeding_element_fill_direction
                        );
                    } else {
                        this.drawFallbackEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset, BARS_TEXTURE,
                                clientConfig.bleeding_build_up_bar_additional_length,
                                20
                        );
                    }

                    dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
                    dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
                    this.client.getProfiler().pop();
                }
                //endregion bleeding build up

                //region burn build up
                buildUpElementX = this.scaledWidth / 2 + clientConfig.burn_build_up_element_x_offset;
                buildUpElementY = this.scaledHeight / 2 + clientConfig.burn_build_up_element_y_offset;
                currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getBurnBuildUp());
                maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxBurnBuildUp());

                if (currentBuildUp > 0) {
                    this.client.getProfiler().push("burn_build_up_element");

                    if (use_custom_textures) {
                        this.drawCustomEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset,
                                Identifier.tryParse(clientConfig.custom_burn_element_background_texture_id),
                                Identifier.tryParse(clientConfig.custom_burn_element_foreground_texture_id),
                                clientConfig.custom_burn_element_texture_width,
                                clientConfig.custom_burn_element_texture_height,
                                clientConfig.custom_burn_element_fill_direction
                        );
                    } else {
                        this.drawFallbackEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset, BARS_TEXTURE,
                                clientConfig.burn_build_up_bar_additional_length,
                                20
                        );
                    }

                    dynamic_x_offset = dynamic_x_offset + dynamic_x_offset_increment;
                    dynamic_y_offset = dynamic_y_offset + dynamic_y_offset_increment;
                    this.client.getProfiler().pop();
                }
                //endregion burn build up

                //region freeze build up
                buildUpElementX = this.scaledWidth / 2 + clientConfig.freeze_build_up_element_x_offset;
                buildUpElementY = this.scaledHeight / 2 + clientConfig.freeze_build_up_element_y_offset;
                currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getFreezeBuildUp());
                maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxFreezeBuildUp());

                if (currentBuildUp > 0) {
                    this.client.getProfiler().push("freeze_build_up_element");

                    if (use_custom_textures) {
                        this.drawCustomEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset,
                                Identifier.tryParse(clientConfig.custom_freeze_element_background_texture_id),
                                Identifier.tryParse(clientConfig.custom_freeze_element_foreground_texture_id),
                                clientConfig.custom_freeze_element_texture_width,
                                clientConfig.custom_freeze_element_texture_height,
                                clientConfig.custom_freeze_element_fill_direction
                        );
                    } else {
                        this.drawFallbackEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset, BARS_TEXTURE,
                                clientConfig.freeze_build_up_bar_additional_length,
                                10
                        );
                    }

                    dynamic_x_offset = dynamic_x_offset + clientConfig.dynamic_x_offset_increase;
                    dynamic_y_offset = dynamic_y_offset + clientConfig.dynamic_y_offset_increase;
                    this.client.getProfiler().pop();
                }
                //endregion freeze build up

                //region poison build up
                buildUpElementX = this.scaledWidth / 2 + clientConfig.poison_build_up_element_x_offset;
                buildUpElementY = this.scaledHeight / 2 + clientConfig.poison_build_up_element_y_offset;
                currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getPoisonBuildUp());
                maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxPoisonBuildUp());

                if (currentBuildUp > 0) {
                    this.client.getProfiler().push("poison_build_up_element");

                    if (use_custom_textures) {
                        this.drawCustomEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset,
                                Identifier.tryParse(clientConfig.custom_poison_element_background_texture_id),
                                Identifier.tryParse(clientConfig.custom_poison_element_foreground_texture_id),
                                clientConfig.custom_poison_element_texture_width,
                                clientConfig.custom_poison_element_texture_height,
                                clientConfig.custom_poison_element_fill_direction
                        );
                    } else {
                        this.drawFallbackEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset, BARS_TEXTURE,
                                clientConfig.poison_build_up_bar_additional_length,
                                30
                        );
                    }

                    dynamic_x_offset = dynamic_x_offset + clientConfig.dynamic_x_offset_increase;
                    dynamic_y_offset = dynamic_y_offset + clientConfig.dynamic_y_offset_increase;
                    this.client.getProfiler().pop();
                }
                //endregion poison build up

                //region shock build up
                buildUpElementX = this.scaledWidth / 2 + clientConfig.shock_build_up_element_x_offset;
                buildUpElementY = this.scaledHeight / 2 + clientConfig.shock_build_up_element_y_offset;
                currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getShockBuildUp());
                maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxShockBuildUp());

                if (currentBuildUp > 0) {
                    this.client.getProfiler().push("shock_build_up_element");

                    if (use_custom_textures) {
                        this.drawCustomEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset,
                                Identifier.tryParse(clientConfig.custom_shock_element_background_texture_id),
                                Identifier.tryParse(clientConfig.custom_shock_element_foreground_texture_id),
                                clientConfig.custom_shock_element_texture_width,
                                clientConfig.custom_shock_element_texture_height,
                                clientConfig.custom_shock_element_fill_direction
                        );
                    } else {
                        this.drawFallbackEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset, BARS_TEXTURE,
                                clientConfig.shock_build_up_bar_additional_length,
                                60
                        );
                    }

                    dynamic_x_offset = dynamic_x_offset + clientConfig.dynamic_x_offset_increase;
                    dynamic_y_offset = dynamic_y_offset + clientConfig.dynamic_y_offset_increase;
                    this.client.getProfiler().pop();
                }
                //endregion shock build up

                //region stagger build up
                buildUpElementX = this.scaledWidth / 2 + clientConfig.stagger_build_up_element_x_offset;
                buildUpElementY = this.scaledHeight / 2 + clientConfig.stagger_build_up_element_y_offset;
                currentBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getStaggerBuildUp());
                maxBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).overhauleddamage$getMaxStaggerBuildUp());

                if (currentBuildUp > 0) {
                    this.client.getProfiler().push("stagger_build_up_element");

                    if (use_custom_textures) {
                        this.drawCustomEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset,
                                Identifier.tryParse(clientConfig.custom_stagger_element_background_texture_id),
                                Identifier.tryParse(clientConfig.custom_stagger_element_foreground_texture_id),
                                clientConfig.custom_stagger_element_texture_width,
                                clientConfig.custom_stagger_element_texture_height,
                                clientConfig.custom_stagger_element_fill_direction
                        );
                    } else {
                        this.drawFallbackEffectBuildUpElement(context, currentBuildUp, maxBuildUp, buildUpElementX, dynamic_x_offset, buildUpElementY, dynamic_y_offset, BARS_TEXTURE,
                                clientConfig.stagger_build_up_bar_additional_length,
                                40
                        );
                    }

                    dynamic_x_offset = dynamic_x_offset + clientConfig.dynamic_x_offset_increase;
                    dynamic_y_offset = dynamic_y_offset + clientConfig.dynamic_y_offset_increase;
                    this.client.getProfiler().pop();
                }
                //endregion stagger build up
            }
        }
    }

    @Unique
    private void drawCustomEffectBuildUpElement(DrawContext context, int currentBuildUp, int maxBuildUp, int buildUpElementX, int dynamic_x_offset, int buildUpElementY, int dynamic_y_offset, Identifier background_texture_id, Identifier foreground_texture_id, int texture_width, int texture_height, int fill_direction) {

        int normalizedBuildUpRatio = (int) (((double) currentBuildUp / Math.max(maxBuildUp, 1)) * ((fill_direction == 1 || fill_direction == 3) ? texture_height : texture_width));

//                context.drawGuiTexture(BURNING_BUILD_UP_BAR_BACKGROUND_TEXTURE, buildUpElementX, buildUpElementY, 62, 5);
        context.drawTexture(background_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, texture_width, texture_height, texture_width, texture_height);
        if (normalizedBuildUpRatio > 0) {
            // 0: left to right, 1: bottom to top, 2: right to left, 3: top to bottom
            int offset;
            switch (fill_direction) {
                case 1:
                    offset = texture_height - normalizedBuildUpRatio;
                    context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset + offset, 0, offset, texture_width, texture_height, texture_width, texture_height);
                    break;
                case 2:
                    offset = texture_height - normalizedBuildUpRatio;
                    context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset + offset, buildUpElementY + dynamic_y_offset, offset, 0, texture_width, texture_height, texture_width, texture_height);
                    break;
                case 3:
                    context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, texture_width, normalizedBuildUpRatio, texture_width, texture_height);
                    break;
                default:
//                    context.drawGuiTexture(foreground_texture_id, buildUpElementX, buildUpElementY, normalizedBurnBuildUpRatio, 5);
                    context.drawTexture(foreground_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, 0, normalizedBuildUpRatio, texture_height, texture_width, texture_height);
                    break;
            }
        }
    }

    @Unique
    private void drawFallbackEffectBuildUpElement(DrawContext context, int currentBuildUp, int maxBuildUp, int buildUpElementX, int dynamic_x_offset, int buildUpElementY, int dynamic_y_offset, Identifier fallback_texture_id, int fallback_additional_bar_length, int fallback_background_texture_v) {

        int normalizedBuildUpRatio = (int) (((double) currentBuildUp / Math.max(maxBuildUp, 1)) * (5 + fallback_additional_bar_length + 5));

        // background
        context.drawTexture(fallback_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, fallback_background_texture_v, 5, 5, 256, 256);
        if (fallback_additional_bar_length > 0) {
            for (int i = 0; i < fallback_additional_bar_length; i++) {
                context.drawTexture(fallback_texture_id, buildUpElementX + 5 + i + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 5, fallback_background_texture_v, 1, 5, 256, 256);
            }
        }
        context.drawTexture(fallback_texture_id, buildUpElementX + 5 + fallback_additional_bar_length + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 177, fallback_background_texture_v, 5, 5, 256, 256);

        // foreground
        if (normalizedBuildUpRatio > 0) {
            context.drawTexture(fallback_texture_id, buildUpElementX + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 0, fallback_background_texture_v + 5, Math.min(5, normalizedBuildUpRatio), 5, 256, 256);
            if (normalizedBuildUpRatio > 5) {
                if (fallback_additional_bar_length > 0) {
                    for (int i = 5; i < Math.min(5 + fallback_additional_bar_length, normalizedBuildUpRatio); i++) {
                        context.drawTexture(fallback_texture_id, buildUpElementX + i + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 5, fallback_background_texture_v + 5, 1, 5, 256, 256);
                    }
                }
            }
            if (normalizedBuildUpRatio > (5 + fallback_additional_bar_length)) {
                context.drawTexture(fallback_texture_id, buildUpElementX + 5 + fallback_additional_bar_length + dynamic_x_offset, buildUpElementY + dynamic_y_offset, 177, fallback_background_texture_v + 5, Math.min(5, normalizedBuildUpRatio - 5 - fallback_additional_bar_length), 5, 256, 256);
            }
        }
    }
}
