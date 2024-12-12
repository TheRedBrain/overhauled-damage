package com.github.theredbrain.overhauleddamage.mixin.enchantment;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {

	@Shadow @Final public ProtectionEnchantment.Type protectionType;

	@ModifyReturnValue(
			method = "getProtectionAmount",
			at = @At("RETURN")
	)
	public int overhauleddamage$modify_getProtectionAmount(int original) {
		if (this.protectionType != ProtectionEnchantment.Type.FALL && OverhauledDamage.SERVER_CONFIG.damageCalculation.enable_protection_overhaul) {
			return 0;
		}
		return original;
	}
}
