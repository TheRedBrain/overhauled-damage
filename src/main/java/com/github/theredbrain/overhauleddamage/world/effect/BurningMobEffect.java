package com.github.theredbrain.overhauleddamage.world.effect;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.world.damagesource.DuckDamageSourcesMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BurningMobEffect extends MobEffect {
	public BurningMobEffect() {
		super(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.burning_effect.effect_color.toInt());
	}

	@Override
	public boolean applyEffectTick(final ServerLevel level, final LivingEntity mob, final int amplification) {
		mob.hurtServer(level, ((DuckDamageSourcesMixin) mob.damageSources()).overhauleddamage$burning(mob), OverhauledDamage.SERVER_CONFIG.status_effects.burning_effect.damage_per_tick.get());
		return super.applyEffectTick(level, mob, amplification);
	}
	@Override
	public boolean shouldApplyEffectTickThisTick(final int tickCount, final int amplification) {
		return tickCount % OverhauledDamage.SERVER_CONFIG.status_effects.burning_effect.tick_update_threshold.get() == 1;
	}
}
