package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.effect.*;
import com.github.theredbrain.overhauleddamage.spell_engine.ExtendedEntityActionsAllowedSemanticType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;

public class StatusEffectsRegistry {

    public static final StatusEffect FALL_IMMUNE = register("fall_immune", new BeneficialStatusEffect());
    public static final StatusEffect CALAMITY = register("calamity", new HarmfulStatusEffect());
    public static final StatusEffect STAGGERED = register("staggered", new HarmfulStatusEffect());
    public static final StatusEffect BLEEDING = register("bleeding", new BleedingStatusEffect());
    public static final StatusEffect BURNING = register("burning", new BurningStatusEffect());
    public static final StatusEffect CHILLED = register("chilled", new HarmfulStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "47112e1e-823b-434f-85f4-dba55bf335e8", 0.75F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "47112e1e-823b-434f-85f4-dba55bf335e8", 0.75F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
    );
    public static final StatusEffect FROZEN = register("frozen", new HarmfulStatusEffect());
    public static final StatusEffect POISON = register("poison", new CustomPoisonStatusEffect());
    public static final StatusEffect SHOCKED = register("shocked", new ShockedStatusEffect());

    private static StatusEffect register(String id, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, OverhauledDamage.identifier(id), statusEffect);
    }

    public static void init() {
        ActionImpairing.configure(FROZEN, new EntityActionsAllowed(false, false, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(false), ExtendedEntityActionsAllowedSemanticType.FROZEN));
        ActionImpairing.configure(STAGGERED, new EntityActionsAllowed(false, false, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(false), ExtendedEntityActionsAllowedSemanticType.STAGGERED));
        Synchronized.configure(STAGGERED, true);
        Synchronized.configure(BURNING, true);
        Synchronized.configure(CHILLED, true);
        Synchronized.configure(FROZEN, true);
    }
}
