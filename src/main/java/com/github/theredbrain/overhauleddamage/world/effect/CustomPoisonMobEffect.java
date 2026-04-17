package com.github.theredbrain.overhauleddamage.world.effect;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.world.damagesource.DuckDamageSourcesMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CustomPoisonMobEffect extends MobEffect {
	public CustomPoisonMobEffect() {
		super(MobEffectCategory.HARMFUL, OverhauledDamage.SERVER_CONFIG.status_effects.poison_effect.effect_color.toInt());
	}

	@Override
	public boolean applyEffectTick(final ServerLevel level, final LivingEntity mob, final int amplification) {
		float poisonDamage = amplification + 1;
		mob.hurtServer(level, ((DuckDamageSourcesMixin) mob.damageSources()).overhauleddamage$poison(mob), poisonDamage * OverhauledDamage.SERVER_CONFIG.status_effects.poison_effect.amplifier_multiplier.get());
		return super.applyEffectTick(level, mob, amplification);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(final int tickCount, final int amplification) {
		return tickCount % OverhauledDamage.SERVER_CONFIG.status_effects.poison_effect.tick_update_threshold.get() == 1;
	}
}
