package com.github.theredbrain.overhauleddamage.mixin.entity.damage;

import com.github.theredbrain.overhauleddamage.registry.DamageTypesRegistry;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin {

    @Shadow
    protected abstract DamageSource create(RegistryKey<DamageType> key, @Nullable Entity attacker);

    @Inject(method = "mobAttack", at = @At("HEAD"), cancellable = true)
    public void overhauleddamage$mobAttack(LivingEntity attacker, CallbackInfoReturnable<DamageSource> cir) {
        if (attacker.getType().isIn(Tags.ATTACKS_WITH_BASHING)) {
            cir.setReturnValue(this.create(DamageTypesRegistry.MOB_BASHING_DAMAGE_TYPE, attacker));
            cir.cancel();
        } else if (attacker.getType().isIn(Tags.ATTACKS_WITH_PIERCING)) {
            cir.setReturnValue(this.create(DamageTypesRegistry.MOB_PIERCING_DAMAGE_TYPE, attacker));
            cir.cancel();
        } else if (attacker.getType().isIn(Tags.ATTACKS_WITH_SLASHING)) {
            cir.setReturnValue(this.create(DamageTypesRegistry.MOB_SLASHING_DAMAGE_TYPE, attacker));
            cir.cancel();
        }
    }
}
