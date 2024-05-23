package com.github.theredbrain.overhauleddamage.mixin.entity.player;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckLivingEntityMixin {


    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author TheRedBrain
     * @reason complete overhaul
     */
    @Overwrite
    public void applyDamage(DamageSource source, float amount) {
        super.applyDamage(source, amount);
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
