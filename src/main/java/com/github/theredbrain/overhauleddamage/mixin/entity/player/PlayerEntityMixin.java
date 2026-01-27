package com.github.theredbrain.overhauleddamage.mixin.entity.player;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.entity.LivingEntityHelper;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckLivingEntityMixin {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Definition(id = "modifyAppliedDamage", method = "Lnet/minecraft/world/entity/player/Player;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F")
	@Expression("? = ?.modifyAppliedDamage(?, ?)")
	@ModifyVariable(method = "actuallyHurt", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER), argsOnly = true)
	private float overhauleddamage$modify_applyDamage(float value, @Local(argsOnly = true) DamageSource source) {
		return LivingEntityHelper.calculateOverhauledDamage(((LivingEntity) (Object) this), source, value);
	}

	// disable the vanilla jump crit mechanic
	@WrapMethod(
			method = "canCriticalAttack"
	)
	public boolean overhauleddamage$wrap_isCriticalHit(Entity target, Operation<Boolean> original) {
		return !OverhauledDamage.SERVER_CONFIG.disable_jump_crit_mechanic.get() && original.call(target);
	}
}
