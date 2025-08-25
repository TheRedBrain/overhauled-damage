package com.github.theredbrain.overhauleddamage;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.registry.GameRulesRegistry;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverhauledDamage implements ModInitializer {
	public static final String MOD_ID = "overhauleddamage";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_BASHING_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_BASHING_DAMAGE;
	public static RegistryEntry<EntityAttribute> BASHING_RESISTANCE;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_PIERCING_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_PIERCING_DAMAGE;
	public static RegistryEntry<EntityAttribute> PIERCING_RESISTANCE;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_SLASHING_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_SLASHING_DAMAGE;
	public static RegistryEntry<EntityAttribute> SLASHING_RESISTANCE;

	public static RegistryEntry<EntityAttribute> BLOCKED_PHYSICAL_DAMAGE;

	public static RegistryEntry<EntityAttribute> MAX_BLEEDING_BUILD_UP;
	public static RegistryEntry<EntityAttribute> BLEEDING_DURATION;
	public static RegistryEntry<EntityAttribute> BLEEDING_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> BLEEDING_BUILD_UP_REDUCTION;
	public static RegistryEntry<EntityAttribute> BLEEDING_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_FROST_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_FROST_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_FROST_DAMAGE;
	public static RegistryEntry<EntityAttribute> FROST_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_FREEZE_BUILD_UP;
	public static RegistryEntry<EntityAttribute> FREEZE_DURATION;
	public static RegistryEntry<EntityAttribute> FREEZE_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> FREEZE_BUILD_UP_REDUCTION;
	public static RegistryEntry<EntityAttribute> FREEZE_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_FIRE_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_FIRE_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_FIRE_DAMAGE;
	public static RegistryEntry<EntityAttribute> FIRE_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_BURN_BUILD_UP;
	public static RegistryEntry<EntityAttribute> BURN_DURATION;
	public static RegistryEntry<EntityAttribute> BURN_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> BURN_BUILD_UP_REDUCTION;
	public static RegistryEntry<EntityAttribute> BURN_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_LIGHTNING_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_LIGHTNING_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_LIGHTNING_DAMAGE;
	public static RegistryEntry<EntityAttribute> LIGHTNING_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_SHOCK_BUILD_UP;
	public static RegistryEntry<EntityAttribute> SHOCK_DURATION;
	public static RegistryEntry<EntityAttribute> SHOCK_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> SHOCK_BUILD_UP_REDUCTION;
	public static RegistryEntry<EntityAttribute> SHOCK_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_POISON_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_POISON_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_POISON_DAMAGE;
	public static RegistryEntry<EntityAttribute> POISON_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_POISON_BUILD_UP;
	public static RegistryEntry<EntityAttribute> POISON_DURATION;
	public static RegistryEntry<EntityAttribute> POISON_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> POISON_BUILD_UP_REDUCTION;
	public static RegistryEntry<EntityAttribute> POISON_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static RegistryEntry<EntityAttribute> MAX_STAGGER_BUILD_UP;
	public static RegistryEntry<EntityAttribute> STAGGER_DURATION;
	public static RegistryEntry<EntityAttribute> STAGGER_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> STAGGER_BUILD_UP_REDUCTION;
	public static RegistryEntry<EntityAttribute> STAGGER_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static RegistryEntry<EntityAttribute> BLOCK_FORCE;
	public static RegistryEntry<EntityAttribute> PARRY_BONUS;
	public static RegistryEntry<EntityAttribute> PARRY_WINDOW;

	public static RegistryEntry<EntityAttribute> BLOCK_STAMINA_COST;
	public static RegistryEntry<EntityAttribute> PARRY_STAMINA_COST;

	public static RegistryEntry<EntityAttribute> DAMAGE_TAKEN_FROM_MANA_MULTIPLIER;
	public static RegistryEntry<EntityAttribute> DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER;

	public static final boolean isManaAttributesLoaded = FabricLoader.getInstance().isModLoaded("manaattributes");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");

	public static float getCurrentMana(LivingEntity livingEntity) {
		float currentMana = 0.0F;
		if (isManaAttributesLoaded) {
			currentMana = ((ManaUsingEntity) livingEntity).manaattributes$getMana();
		}
		return currentMana;
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
		if (isManaAttributesLoaded) {
			((ManaUsingEntity) livingEntity).manaattributes$addMana(amount);
		}
	}

	public static float getCurrentStamina(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
		}
		return currentStamina;
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		if (isStaminaAttributesLoaded) {
			((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
		}
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Now dealing overhauled damage!");

		// Config
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

		GameRulesRegistry.init();
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: {}", message);
	}

}