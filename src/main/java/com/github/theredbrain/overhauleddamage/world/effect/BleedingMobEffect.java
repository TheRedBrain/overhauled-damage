package com.github.theredbrain.overhauleddamage.world.effect;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.world.damagesource.DuckDamageSourcesMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BleedingMobEffect extends MobEffect {
	public BleedingMobEffect() {
		super(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.bleeding_effect.effect_color.toInt());
	}

	@Override
	public boolean applyEffectTick(final ServerLevel level, final LivingEntity mob, final int amplification) {
		if (!mob.level().isClientSide()) {
			ServerConfig serverConfig = OverhauledDamage.SERVER_CONFIG;
			boolean isMoving = ((DuckLivingEntityMixin) mob).overhauleddamage$isMoving() && serverConfig.status_effects.bleeding_effect.moving_doubles_damage.get();
			float bleedingDamage = Math.max(1.0f, (float) (mob.getAttributeValue(Attributes.MAX_HEALTH) * serverConfig.status_effects.bleeding_effect.max_health_multiplier.get())) * (isMoving ? 2 : 1);
			mob.hurtServer(level, ((DuckDamageSourcesMixin) mob.damageSources()).overhauleddamage$bleeding(mob), bleedingDamage);
		}
		return super.applyEffectTick(level, mob, amplification);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(final int tickCount, final int amplification) {
		return tickCount % OverhauledDamage.SERVER_CONFIG.status_effects.bleeding_effect.tick_update_threshold.get() == 1;
	}
}
