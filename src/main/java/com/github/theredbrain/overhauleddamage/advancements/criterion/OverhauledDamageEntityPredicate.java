package com.github.theredbrain.overhauleddamage.advancements.criterion;

import com.github.theredbrain.overhauleddamage.entity.LivingEntityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record OverhauledDamageEntityPredicate(
		MinMaxBounds.Ints bleeding_buildup_amount,
		MinMaxBounds.Ints burn_buildup_amount,
		MinMaxBounds.Ints freeze_buildup_amount,
		MinMaxBounds.Ints stagger_buildup_amount,
		MinMaxBounds.Ints poison_buildup_amount,
		MinMaxBounds.Ints shock_buildup_amount
) implements EntitySubPredicate {
	public static final MapCodec<OverhauledDamageEntityPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					MinMaxBounds.Ints.CODEC.optionalFieldOf("bleeding_buildup_amount", MinMaxBounds.Ints.ANY).forGetter(OverhauledDamageEntityPredicate::bleeding_buildup_amount),
					MinMaxBounds.Ints.CODEC.optionalFieldOf("burn_buildup_amount", MinMaxBounds.Ints.ANY).forGetter(OverhauledDamageEntityPredicate::burn_buildup_amount),
					MinMaxBounds.Ints.CODEC.optionalFieldOf("freeze_buildup_amount", MinMaxBounds.Ints.ANY).forGetter(OverhauledDamageEntityPredicate::freeze_buildup_amount),
					MinMaxBounds.Ints.CODEC.optionalFieldOf("stagger_buildup_amount", MinMaxBounds.Ints.ANY).forGetter(OverhauledDamageEntityPredicate::stagger_buildup_amount),
					MinMaxBounds.Ints.CODEC.optionalFieldOf("poison_buildup_amount", MinMaxBounds.Ints.ANY).forGetter(OverhauledDamageEntityPredicate::poison_buildup_amount),
					MinMaxBounds.Ints.CODEC.optionalFieldOf("shock_buildup_amount", MinMaxBounds.Ints.ANY).forGetter(OverhauledDamageEntityPredicate::shock_buildup_amount)
			).apply(instance, OverhauledDamageEntityPredicate::new)
	);

	@Override
	public MapCodec<? extends EntitySubPredicate> codec() {
		return CODEC;
	}

	@Override
	public boolean matches(Entity entity, ServerLevel serverLevel, @Nullable Vec3 vec3) {
		if (entity instanceof LivingEntity livingEntity) {
			return (this.bleeding_buildup_amount == MinMaxBounds.Ints.ANY || this.bleeding_buildup_amount.matches(Mth.ceil(LivingEntityHelper.getBleedingBuildUp(livingEntity)))) &&
					(this.burn_buildup_amount == MinMaxBounds.Ints.ANY || this.burn_buildup_amount.matches(Mth.ceil(LivingEntityHelper.getBurnBuildUp(livingEntity)))) &&
					(this.freeze_buildup_amount == MinMaxBounds.Ints.ANY || this.freeze_buildup_amount.matches(Mth.ceil(LivingEntityHelper.getFreezeBuildUp(livingEntity)))) &&
					(this.stagger_buildup_amount == MinMaxBounds.Ints.ANY || this.stagger_buildup_amount.matches(Mth.ceil(LivingEntityHelper.getStaggerBuildUp(livingEntity)))) &&
					(this.poison_buildup_amount == MinMaxBounds.Ints.ANY || this.poison_buildup_amount.matches(Mth.ceil(LivingEntityHelper.getPoisonBuildUp(livingEntity)))) &&
					(this.shock_buildup_amount == MinMaxBounds.Ints.ANY || this.shock_buildup_amount.matches(Mth.ceil(LivingEntityHelper.getShockBuildUp(livingEntity))));
		}
		return false;
	}
}
