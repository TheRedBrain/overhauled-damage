package com.github.theredbrain.overhauleddamage.mixin.enchantment;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {

	@Shadow @Final public ProtectionEnchantment.Type protectionType;

	@Inject(method = "getProtectionAmount", at = @At("HEAD"), cancellable = true) // TODO use better mixin type
	public void getProtectionAmount(int level, DamageSource source, CallbackInfoReturnable<Integer> cir) {
		if (this.protectionType != ProtectionEnchantment.Type.FALL && OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_protection_enchantment_override) {
			cir.setReturnValue(0);
			cir.cancel();
		}
	}
}
