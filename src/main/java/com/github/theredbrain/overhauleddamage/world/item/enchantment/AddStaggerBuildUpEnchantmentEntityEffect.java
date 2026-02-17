package com.github.theredbrain.overhauleddamage.world.item.enchantment;

import com.github.theredbrain.overhauleddamage.entity.LivingEntityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record AddStaggerBuildUpEnchantmentEntityEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
	public static final MapCodec<AddStaggerBuildUpEnchantmentEntityEffect> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(LevelBasedValue.CODEC.fieldOf("amount").forGetter(AddStaggerBuildUpEnchantmentEntityEffect::amount)).apply(instance, AddStaggerBuildUpEnchantmentEntityEffect::new)
	);

	@Override
	public void apply(ServerLevel serverLevel, int i, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
		if (entity instanceof LivingEntity livingEntity) {
			LivingEntityHelper.addStaggerBuildUp(livingEntity, this.amount.calculate(i));
		}
	}

	@Override
	public MapCodec<AddStaggerBuildUpEnchantmentEntityEffect> codec() {
		return CODEC;
	}
}
