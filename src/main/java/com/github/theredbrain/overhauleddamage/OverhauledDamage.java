package com.github.theredbrain.overhauleddamage;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverhauledDamage implements ModInitializer {
	public static final String MOD_ID = "overhauleddamage";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static EntityAttribute ADDITIONAL_BASHING_DAMAGE;
	public static EntityAttribute INCREASED_BASHING_DAMAGE;
	public static EntityAttribute BASHING_RESISTANCE;

	public static EntityAttribute ADDITIONAL_PIERCING_DAMAGE;
	public static EntityAttribute INCREASED_PIERCING_DAMAGE;
	public static EntityAttribute PIERCING_RESISTANCE;

	public static EntityAttribute ADDITIONAL_SLASHING_DAMAGE;
	public static EntityAttribute INCREASED_SLASHING_DAMAGE;
	public static EntityAttribute SLASHING_RESISTANCE;

	public static EntityAttribute BLOCKED_PHYSICAL_DAMAGE;

	public static EntityAttribute MAX_BLEEDING_BUILD_UP;
	public static EntityAttribute BLEEDING_DURATION;
	public static EntityAttribute BLEEDING_TICK_THRESHOLD;
	public static EntityAttribute BLEEDING_BUILD_UP_REDUCTION;
	public static EntityAttribute BLEEDING_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static EntityAttribute ADDITIONAL_FROST_DAMAGE;
	public static EntityAttribute INCREASED_FROST_DAMAGE;
	public static EntityAttribute BLOCKED_FROST_DAMAGE;
	public static EntityAttribute FROST_RESISTANCE;
	public static EntityAttribute MAX_FREEZE_BUILD_UP;
	public static EntityAttribute FREEZE_DURATION;
	public static EntityAttribute FREEZE_TICK_THRESHOLD;
	public static EntityAttribute FREEZE_BUILD_UP_REDUCTION;
	public static EntityAttribute FREEZE_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static EntityAttribute ADDITIONAL_FIRE_DAMAGE;
	public static EntityAttribute INCREASED_FIRE_DAMAGE;
	public static EntityAttribute BLOCKED_FIRE_DAMAGE;
	public static EntityAttribute FIRE_RESISTANCE;
	public static EntityAttribute MAX_BURN_BUILD_UP;
	public static EntityAttribute BURN_DURATION;
	public static EntityAttribute BURN_TICK_THRESHOLD;
	public static EntityAttribute BURN_BUILD_UP_REDUCTION;
	public static EntityAttribute BURN_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static EntityAttribute ADDITIONAL_LIGHTNING_DAMAGE;
	public static EntityAttribute INCREASED_LIGHTNING_DAMAGE;
	public static EntityAttribute BLOCKED_LIGHTNING_DAMAGE;
	public static EntityAttribute LIGHTNING_RESISTANCE;
	public static EntityAttribute MAX_SHOCK_BUILD_UP;
	public static EntityAttribute SHOCK_DURATION;
	public static EntityAttribute SHOCK_TICK_THRESHOLD;
	public static EntityAttribute SHOCK_BUILD_UP_REDUCTION;
	public static EntityAttribute SHOCK_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static EntityAttribute ADDITIONAL_POISON_DAMAGE;
	public static EntityAttribute INCREASED_POISON_DAMAGE;
	public static EntityAttribute BLOCKED_POISON_DAMAGE;
	public static EntityAttribute POISON_RESISTANCE;
	public static EntityAttribute MAX_POISON_BUILD_UP;
	public static EntityAttribute POISON_DURATION;
	public static EntityAttribute POISON_TICK_THRESHOLD;
	public static EntityAttribute POISON_BUILD_UP_REDUCTION;
	public static EntityAttribute POISON_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static EntityAttribute MAX_STAGGER_BUILD_UP;
	public static EntityAttribute STAGGER_DURATION;
	public static EntityAttribute STAGGER_TICK_THRESHOLD;
	public static EntityAttribute STAGGER_BUILD_UP_REDUCTION;
	public static EntityAttribute STAGGER_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static EntityAttribute BLOCK_FORCE;
	public static EntityAttribute PARRY_BONUS;
	public static EntityAttribute PARRY_WINDOW;

	public static EntityAttribute BLOCK_STAMINA_COST;
	public static EntityAttribute PARRY_STAMINA_COST;

	public static EntityAttribute DAMAGE_TAKEN_MULTIPLIER;
	public static EntityAttribute DAMAGE_TAKEN_FROM_MANA_MULTIPLIER;
	public static EntityAttribute DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER;

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
		LOGGER.info("Dealing overhauled damage!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: {}", message);
	}

}