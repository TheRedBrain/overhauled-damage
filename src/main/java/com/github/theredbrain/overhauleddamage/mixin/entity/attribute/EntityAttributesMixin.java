package com.github.theredbrain.overhauleddamage.mixin.entity.attribute;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Shadow
    private static EntityAttribute register(String id, EntityAttribute attribute) {
        throw new AssertionError();
    }

    static {
        OverhauledDamage.ADDITIONAL_BASHING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.additional_bashing_damage", new ClampedEntityAttribute("attribute.name.generic.additional_bashing_damage", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.INCREASED_BASHING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.increased_bashing_damage", new ClampedEntityAttribute("attribute.name.generic.increased_bashing_damage", 1.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.BASHING_RESISTANCE = register(OverhauledDamage.MOD_ID + ":generic.bashing_resistance", new ClampedEntityAttribute("attribute.name.generic.bashing_resistance", 0.0, -1024.0, 1024.0).setTracked(true));

        OverhauledDamage.ADDITIONAL_PIERCING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.additional_piercing_damage", new ClampedEntityAttribute("attribute.name.generic.additional_piercing_damage", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.INCREASED_PIERCING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.increased_piercing_damage", new ClampedEntityAttribute("attribute.name.generic.increased_piercing_damage", 1.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.PIERCING_RESISTANCE = register(OverhauledDamage.MOD_ID + ":generic.piercing_resistance", new ClampedEntityAttribute("attribute.name.generic.piercing_resistance", 0.0, -1024.0, 1024.0).setTracked(true));

        OverhauledDamage.ADDITIONAL_SLASHING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.additional_slashing_damage", new ClampedEntityAttribute("attribute.name.generic.additional_slashing_damage", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.INCREASED_SLASHING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.increased_slashing_damage", new ClampedEntityAttribute("attribute.name.generic.increased_slashing_damage", 1.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.SLASHING_RESISTANCE = register(OverhauledDamage.MOD_ID + ":generic.slashing_resistance", new ClampedEntityAttribute("attribute.name.generic.slashing_resistance", 0.0, -1024.0, 1024.0).setTracked(true));

        OverhauledDamage.BLOCKED_PHYSICAL_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.blocked_physical_damage", new ClampedEntityAttribute("attribute.name.generic.blocked_physical_damage", 0.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.MAX_BLEEDING_BUILD_UP = register(OverhauledDamage.MOD_ID + ":generic.max_bleeding_build_up", new ClampedEntityAttribute("attribute.name.generic.max_bleeding_build_up", 20.0, -1.0, 1024.0).setTracked(true));
        OverhauledDamage.BLEEDING_DURATION = register(OverhauledDamage.MOD_ID + ":generic.bleeding_duration", new ClampedEntityAttribute("attribute.name.generic.bleeding_duration", 201.0, 1.0, 1000000.0).setTracked(true));
        OverhauledDamage.BLEEDING_TICK_THRESHOLD = register(OverhauledDamage.MOD_ID + ":generic.bleeding_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.bleeding_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.BLEEDING_BUILD_UP_REDUCTION = register(OverhauledDamage.MOD_ID + ":generic.bleeding_build_up_reduction", new ClampedEntityAttribute("attribute.name.generic.bleeding_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.ADDITIONAL_FROST_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.additional_frost_damage", new ClampedEntityAttribute("attribute.name.generic.additional_frost_damage", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.INCREASED_FROST_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.increased_frost_damage", new ClampedEntityAttribute("attribute.name.generic.increased_frost_damage", 1.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.BLOCKED_FROST_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.blocked_frost_damage", new ClampedEntityAttribute("attribute.name.generic.blocked_frost_damage", 0.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.FROST_RESISTANCE = register(OverhauledDamage.MOD_ID + ":generic.frost_resistance", new ClampedEntityAttribute("attribute.name.generic.frost_resistance", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.MAX_FREEZE_BUILD_UP = register(OverhauledDamage.MOD_ID + ":generic.max_freeze_build_up", new ClampedEntityAttribute("attribute.name.generic.max_freeze_build_up", 20.0, -1.0, 1024.0).setTracked(true));
        OverhauledDamage.FREEZE_DURATION = register(OverhauledDamage.MOD_ID + ":generic.freeze_duration", new ClampedEntityAttribute("attribute.name.generic.freeze_duration", 200.0, 1.0, 1000000.0).setTracked(true));
        OverhauledDamage.FREEZE_TICK_THRESHOLD = register(OverhauledDamage.MOD_ID + ":generic.freeze_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.freeze_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.FREEZE_BUILD_UP_REDUCTION = register(OverhauledDamage.MOD_ID + ":generic.freeze_build_up_reduction", new ClampedEntityAttribute("attribute.name.generic.freeze_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.ADDITIONAL_FIRE_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.additional_fire_damage", new ClampedEntityAttribute("attribute.name.generic.additional_fire_damage", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.INCREASED_FIRE_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.increased_fire_damage", new ClampedEntityAttribute("attribute.name.generic.increased_fire_damage", 1.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.BLOCKED_FIRE_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.blocked_fire_damage", new ClampedEntityAttribute("attribute.name.generic.blocked_fire_damage", 0.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.FIRE_RESISTANCE = register(OverhauledDamage.MOD_ID + ":generic.fire_resistance", new ClampedEntityAttribute("attribute.name.generic.fire_resistance", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.MAX_BURN_BUILD_UP = register(OverhauledDamage.MOD_ID + ":generic.max_burn_build_up", new ClampedEntityAttribute("attribute.name.generic.max_burn_build_up", 20.0, -1.0, 1024.0).setTracked(true));
        OverhauledDamage.BURN_DURATION = register(OverhauledDamage.MOD_ID + ":generic.burn_duration", new ClampedEntityAttribute("attribute.name.generic.burn_duration", 351.0, 1.0, 1000000.0).setTracked(true));
        OverhauledDamage.BURN_TICK_THRESHOLD = register(OverhauledDamage.MOD_ID + ":generic.burn_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.burn_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.BURN_BUILD_UP_REDUCTION = register(OverhauledDamage.MOD_ID + ":generic.burn_build_up_reduction", new ClampedEntityAttribute("attribute.name.generic.burn_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.ADDITIONAL_LIGHTNING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.additional_lightning_damage", new ClampedEntityAttribute("attribute.name.generic.additional_lightning_damage", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.INCREASED_LIGHTNING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.increased_lightning_damage", new ClampedEntityAttribute("attribute.name.generic.increased_lightning_damage", 1.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.BLOCKED_LIGHTNING_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.blocked_lightning_damage", new ClampedEntityAttribute("attribute.name.generic.blocked_lightning_damage", 0.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.LIGHTNING_RESISTANCE = register(OverhauledDamage.MOD_ID + ":generic.lightning_resistance", new ClampedEntityAttribute("attribute.name.generic.lightning_resistance", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.MAX_SHOCK_BUILD_UP = register(OverhauledDamage.MOD_ID + ":generic.max_shock_build_up", new ClampedEntityAttribute("attribute.name.generic.max_shock_build_up", 20.0, -1.0, 1024.0).setTracked(true));
        OverhauledDamage.SHOCK_DURATION = register(OverhauledDamage.MOD_ID + ":generic.shock_duration", new ClampedEntityAttribute("attribute.name.generic.shock_duration", 1.0, 1.0, 1000000.0).setTracked(true));
        OverhauledDamage.SHOCK_TICK_THRESHOLD = register(OverhauledDamage.MOD_ID + ":generic.shock_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.shock_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.SHOCK_BUILD_UP_REDUCTION = register(OverhauledDamage.MOD_ID + ":generic.shock_build_up_reduction", new ClampedEntityAttribute("attribute.name.generic.shock_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.ADDITIONAL_POISON_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.additional_poison_damage", new ClampedEntityAttribute("attribute.name.generic.additional_poison_damage", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.INCREASED_POISON_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.increased_poison_damage", new ClampedEntityAttribute("attribute.name.generic.increased_poison_damage", 1.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.BLOCKED_POISON_DAMAGE = register(OverhauledDamage.MOD_ID + ":generic.blocked_poison_damage", new ClampedEntityAttribute("attribute.name.generic.blocked_poison_damage", 0.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.POISON_RESISTANCE = register(OverhauledDamage.MOD_ID + ":generic.poison_resistance", new ClampedEntityAttribute("attribute.name.generic.poison_resistance", 0.0, -1024.0, 1024.0).setTracked(true));
        OverhauledDamage.MAX_POISON_BUILD_UP = register(OverhauledDamage.MOD_ID + ":generic.max_poison_build_up", new ClampedEntityAttribute("attribute.name.generic.max_poison_build_up", 20.0, -1.0, 1024.0).setTracked(true));
        OverhauledDamage.POISON_DURATION = register(OverhauledDamage.MOD_ID + ":generic.poison_duration", new ClampedEntityAttribute("attribute.name.generic.poison_duration", 201.0, 1.0, 1000000.0).setTracked(true));
        OverhauledDamage.POISON_TICK_THRESHOLD = register(OverhauledDamage.MOD_ID + ":generic.poison_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.poison_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.POISON_BUILD_UP_REDUCTION = register(OverhauledDamage.MOD_ID + ":generic.poison_build_up_reduction", new ClampedEntityAttribute("attribute.name.generic.poison_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.MAX_STAGGER_BUILD_UP = register(OverhauledDamage.MOD_ID + ":generic.max_stagger_build_up", new ClampedEntityAttribute("attribute.name.generic.max_stagger_build_up", 20.0, -1.0, 1024.0).setTracked(true));
        OverhauledDamage.STAGGER_DURATION = register(OverhauledDamage.MOD_ID + ":generic.stagger_duration", new ClampedEntityAttribute("attribute.name.generic.stagger_duration", 200.0, 1.0, 1000000.0).setTracked(true));
        OverhauledDamage.STAGGER_TICK_THRESHOLD = register(OverhauledDamage.MOD_ID + ":generic.stagger_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.stagger_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.STAGGER_BUILD_UP_REDUCTION = register(OverhauledDamage.MOD_ID + ":generic.stagger_build_up_reduction", new ClampedEntityAttribute("attribute.name.generic.stagger_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.BLOCK_FORCE = register(OverhauledDamage.MOD_ID + ":generic.block_force", new ClampedEntityAttribute("attribute.name.generic.block_force", 0.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.PARRY_BONUS = register(OverhauledDamage.MOD_ID + ":generic.parry_bonus", new ClampedEntityAttribute("attribute.name.generic.parry_bonus", 1.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.PARRY_WINDOW = register(OverhauledDamage.MOD_ID + ":generic.parry_window", new ClampedEntityAttribute("attribute.name.generic.parry_window", 0.0, 0.0, 1024.0).setTracked(true));

        OverhauledDamage.BLOCK_STAMINA_COST = register(OverhauledDamage.MOD_ID + ":generic.block_stamina_cost", new ClampedEntityAttribute("attribute.name.generic.block_stamina_cost", 0.0, 0.0, 1024.0).setTracked(true));
        OverhauledDamage.PARRY_STAMINA_COST = register(OverhauledDamage.MOD_ID + ":generic.parry_stamina_cost", new ClampedEntityAttribute("attribute.name.generic.parry_stamina_cost", 0.0, 0.0, 1024.0).setTracked(true));
    }
}
