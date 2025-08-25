package com.github.theredbrain.overhauleddamage.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
	public static final GameRules.Key<DoubleRule> NATURAL_ARMOUR_TOUGHNESS =
			GameRuleRegistry.register("naturalArmourToughness", GameRules.Category.MISC, GameRuleFactory.createDoubleRule(1.0, 0.0, 20.0));

	public static void init() {
	}
}
