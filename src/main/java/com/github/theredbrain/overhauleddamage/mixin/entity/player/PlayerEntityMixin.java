package com.github.theredbrain.overhauleddamage.mixin.entity.player;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckLivingEntityMixin {


    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"), argsOnly = true)
    private float overhauleddamage$applyDamage(float old, DamageSource source) {
        return this.overhauleddamage$calculateOverhauledDamage(source, old);
    }

    // effectively disables the vanilla jump crit mechanic
    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;hasVehicle()Z"
            )
    )
    public boolean overhauleddamage$redirect_hasVehicle(PlayerEntity instance) {
        return OverhauledDamage.serverConfig.disable_jump_crit_mechanic;
    }

    @Override
    public boolean overhauleddamage$canParry() {
        return true;
    }
}
