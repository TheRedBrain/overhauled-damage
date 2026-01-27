package com.github.theredbrain.overhauleddamage.mixin.entity.player;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.entity.LivingEntityHelper;
import com.github.theredbrain.overhauleddamage.entity.PlayerHelper;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckLivingEntityMixin {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "createAttributes", at = @At("RETURN"))
	private static void overhauleddamage$createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue()
				.add(OverhauledDamage.ADDITIONAL_BASHING_DAMAGE, 0.0)
				.add(OverhauledDamage.INCREASED_BASHING_DAMAGE, 0.0)
				.add(OverhauledDamage.BASHING_RESISTANCE, 0.0)

				.add(OverhauledDamage.ADDITIONAL_PIERCING_DAMAGE, 0.0)
				.add(OverhauledDamage.INCREASED_PIERCING_DAMAGE, 0.0)
				.add(OverhauledDamage.PIERCING_RESISTANCE, 0.0)

				.add(OverhauledDamage.ADDITIONAL_SLASHING_DAMAGE, 0.0)
				.add(OverhauledDamage.INCREASED_SLASHING_DAMAGE, 0.0)
				.add(OverhauledDamage.SLASHING_RESISTANCE, 0.0)

				.add(OverhauledDamage.BLOCKED_PHYSICAL_DAMAGE, 0.0)

				.add(OverhauledDamage.MAX_BLEEDING_BUILD_UP, 0.0)
				.add(OverhauledDamage.BLEEDING_DURATION, 0.0)
				.add(OverhauledDamage.BLEEDING_TICK_THRESHOLD, 0.0)
				.add(OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION, 0.0)
				.add(OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION_DELAY_THRESHOLD, 0.0)

				.add(OverhauledDamage.ADDITIONAL_FROST_DAMAGE, 0.0)
				.add(OverhauledDamage.INCREASED_FROST_DAMAGE, 0.0)
				.add(OverhauledDamage.BLOCKED_FROST_DAMAGE, 0.0)
				.add(OverhauledDamage.FROST_RESISTANCE, 0.0)
				.add(OverhauledDamage.MAX_FREEZE_BUILD_UP, 0.0)
				.add(OverhauledDamage.FREEZE_DURATION, 0.0)
				.add(OverhauledDamage.FREEZE_TICK_THRESHOLD, 0.0)
				.add(OverhauledDamage.FREEZE_BUILD_UP_REDUCTION, 0.0)
				.add(OverhauledDamage.FREEZE_BUILD_UP_REDUCTION_DELAY_THRESHOLD, 0.0)

				.add(OverhauledDamage.ADDITIONAL_FIRE_DAMAGE, 0.0)
				.add(OverhauledDamage.INCREASED_FIRE_DAMAGE, 0.0)
				.add(OverhauledDamage.BLOCKED_FIRE_DAMAGE, 0.0)
				.add(OverhauledDamage.FIRE_RESISTANCE, 0.0)
				.add(OverhauledDamage.MAX_BURN_BUILD_UP, 0.0)
				.add(OverhauledDamage.BURN_DURATION, 0.0)
				.add(OverhauledDamage.BURN_TICK_THRESHOLD, 0.0)
				.add(OverhauledDamage.BURN_BUILD_UP_REDUCTION, 0.0)
				.add(OverhauledDamage.BURN_BUILD_UP_REDUCTION_DELAY_THRESHOLD, 0.0)

				.add(OverhauledDamage.ADDITIONAL_LIGHTNING_DAMAGE, 0.0)
				.add(OverhauledDamage.INCREASED_LIGHTNING_DAMAGE, 0.0)
				.add(OverhauledDamage.BLOCKED_LIGHTNING_DAMAGE, 0.0)
				.add(OverhauledDamage.LIGHTNING_RESISTANCE, 0.0)
				.add(OverhauledDamage.MAX_SHOCK_BUILD_UP, 0.0)
				.add(OverhauledDamage.SHOCK_DURATION, 0.0)
				.add(OverhauledDamage.SHOCK_TICK_THRESHOLD, 0.0)
				.add(OverhauledDamage.SHOCK_BUILD_UP_REDUCTION, 0.0)
				.add(OverhauledDamage.SHOCK_BUILD_UP_REDUCTION_DELAY_THRESHOLD, 0.0)

				.add(OverhauledDamage.ADDITIONAL_POISON_DAMAGE, 0.0)
				.add(OverhauledDamage.INCREASED_POISON_DAMAGE, 0.0)
				.add(OverhauledDamage.BLOCKED_POISON_DAMAGE, 0.0)
				.add(OverhauledDamage.POISON_RESISTANCE, 0.0)
				.add(OverhauledDamage.MAX_POISON_BUILD_UP, 0.0)
				.add(OverhauledDamage.POISON_DURATION, 0.0)
				.add(OverhauledDamage.POISON_TICK_THRESHOLD, 0.0)
				.add(OverhauledDamage.POISON_BUILD_UP_REDUCTION, 0.0)
				.add(OverhauledDamage.POISON_BUILD_UP_REDUCTION_DELAY_THRESHOLD, 0.0)

				.add(OverhauledDamage.MAX_STAGGER_BUILD_UP, 0.0)
				.add(OverhauledDamage.STAGGER_DURATION, 0.0)
				.add(OverhauledDamage.STAGGER_TICK_THRESHOLD, 0.0)
				.add(OverhauledDamage.STAGGER_BUILD_UP_REDUCTION, 0.0)
				.add(OverhauledDamage.STAGGER_BUILD_UP_REDUCTION_DELAY_THRESHOLD, 0.0)

				.add(OverhauledDamage.DAMAGE_TAKEN_FROM_MANA_MULTIPLIER, 0.0)
				.add(OverhauledDamage.DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER, 0.0)
		;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void overhauleddamage$tick(CallbackInfo ci) {
		PlayerHelper.tick(((Player) (Object) this));
	}

	@Definition(id = "modifyAppliedDamage", method = "Lnet/minecraft/world/entity/player/Player;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F")
	@Expression("? = ?.modifyAppliedDamage(?, ?)")
	@ModifyVariable(method = "actuallyHurt", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER), argsOnly = true)
	private float overhauleddamage$modify_applyDamage(float value, @Local(argsOnly = true) ServerLevel serverLevel, @Local(argsOnly = true) DamageSource source) {
		return LivingEntityHelper.calculateOverhauledDamage(serverLevel, ((LivingEntity) (Object) this), source, value);
	}

	// disable the vanilla jump crit mechanic
	@WrapMethod(
			method = "canCriticalAttack"
	)
	public boolean overhauleddamage$wrap_isCriticalHit(Entity target, Operation<Boolean> original) {
		return !OverhauledDamage.SERVER_CONFIG.disable_jump_crit_mechanic.get() && original.call(target);
	}
}
