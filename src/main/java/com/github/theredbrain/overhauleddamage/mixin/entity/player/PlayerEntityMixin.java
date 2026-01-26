package com.github.theredbrain.overhauleddamage.mixin.entity.player;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.entity.LivingEntityHelper;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckLivingEntityMixin {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Definition(id = "modifyAppliedDamage", method = "Lnet/minecraft/entity/player/PlayerEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F")
	@Expression("? = ?.modifyAppliedDamage(?, ?)")
	@ModifyVariable(method = "applyDamage", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER), argsOnly = true)
	private float overhauleddamage$modify_applyDamage(float value, @Local(argsOnly = true) DamageSource source) {
		return LivingEntityHelper.calculateOverhauledDamage(((LivingEntity) (Object) this), source, value);
	}

	// disable the vanilla jump crit mechanic
	@WrapMethod(
			method = "isCriticalHit"
	)
	public boolean overhauleddamage$wrap_isCriticalHit(Entity target, Operation<Boolean> original) {
		return !OverhauledDamage.SERVER_CONFIG.disable_jump_crit_mechanic.get() && original.call(target);
	}

	@Override
	public boolean overhauleddamage$canParry() {
		return true;
	}
}
