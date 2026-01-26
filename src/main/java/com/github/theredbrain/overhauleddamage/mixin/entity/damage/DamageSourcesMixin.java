package com.github.theredbrain.overhauleddamage.mixin.entity.damage;

import com.github.theredbrain.overhauleddamage.registry.DamageTypesRegistry;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin {

	@Shadow
	public abstract DamageSource source(ResourceKey<DamageType> key, @Nullable Entity attacker);

	@Inject(method = "mobAttack", at = @At("HEAD"), cancellable = true)
	public void overhauleddamage$mobAttack(LivingEntity attacker, CallbackInfoReturnable<DamageSource> cir) {
		if (attacker.getType().is(Tags.ATTACKS_WITH_BASHING)) {
			cir.setReturnValue(this.source(DamageTypesRegistry.MOB_BASHING_DAMAGE_TYPE, attacker));
			cir.cancel();
		} else if (attacker.getType().is(Tags.ATTACKS_WITH_PIERCING)) {
			cir.setReturnValue(this.source(DamageTypesRegistry.MOB_PIERCING_DAMAGE_TYPE, attacker));
			cir.cancel();
		} else if (attacker.getType().is(Tags.ATTACKS_WITH_SLASHING)) {
			cir.setReturnValue(this.source(DamageTypesRegistry.MOB_SLASHING_DAMAGE_TYPE, attacker));
			cir.cancel();
		}
	}
}
