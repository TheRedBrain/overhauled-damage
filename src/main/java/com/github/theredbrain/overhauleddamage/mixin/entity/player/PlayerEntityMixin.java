package com.github.theredbrain.overhauleddamage.mixin.entity.player;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckLivingEntityMixin {


	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"), argsOnly = true)
	private float overhauleddamage$applyDamage(float old, DamageSource source) {
		return this.overhauleddamage$calculateOverhauledDamage(source, old);
	}

	// effectively disables the vanilla jump crit mechanic
	@WrapOperation(
			method = "attack",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;hasVehicle()Z")
	)
	public boolean overhauleddamage$wrap_hasVehicle(PlayerEntity instance, Operation<Boolean> original) {
		return !OverhauledDamage.SERVER_CONFIG.disable_jump_crit_mechanic.get() && original.call(instance);
	}

	@Override
	public boolean overhauleddamage$canParry() {
		return true;
	}
}
