package com.github.theredbrain.overhauleddamage.mixin.entity.mob;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public class MobEntityMixin {

	// effectively disables the vanilla knockback on attack
	@WrapOperation(
			method = "tryAttack",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getKnockbackAgainst(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)F")
	)
	public float overhauleddamage$wrap_getKnockbackAgainst(MobEntity instance, Entity entity, DamageSource damageSource, Operation<Float> original) {
		return OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_knockback_overhaul.get() ? 0.0F : original.call(instance, entity, damageSource);
	}

}
