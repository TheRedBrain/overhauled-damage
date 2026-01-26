package com.github.theredbrain.overhauleddamage;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import com.github.theredbrain.overhauleddamage.compatibility.BlockingOverhaulIntegration;
import com.github.theredbrain.overhauleddamage.compatibility.ManaAttributesIntegration;
import com.github.theredbrain.overhauleddamage.compatibility.StaminaAttributesIntegration;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverhauledDamage implements ModInitializer {
	public static final String MOD_ID = "overhauleddamage";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static Holder<Attribute> ADDITIONAL_BASHING_DAMAGE;
	public static Holder<Attribute> INCREASED_BASHING_DAMAGE;
	public static Holder<Attribute> BASHING_RESISTANCE;

	public static Holder<Attribute> ADDITIONAL_PIERCING_DAMAGE;
	public static Holder<Attribute> INCREASED_PIERCING_DAMAGE;
	public static Holder<Attribute> PIERCING_RESISTANCE;

	public static Holder<Attribute> ADDITIONAL_SLASHING_DAMAGE;
	public static Holder<Attribute> INCREASED_SLASHING_DAMAGE;
	public static Holder<Attribute> SLASHING_RESISTANCE;

	public static Holder<Attribute> BLOCKED_PHYSICAL_DAMAGE;

	public static Holder<Attribute> MAX_BLEEDING_BUILD_UP;
	public static Holder<Attribute> BLEEDING_DURATION;
	public static Holder<Attribute> BLEEDING_TICK_THRESHOLD;
	public static Holder<Attribute> BLEEDING_BUILD_UP_REDUCTION;
	public static Holder<Attribute> BLEEDING_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static Holder<Attribute> ADDITIONAL_FROST_DAMAGE;
	public static Holder<Attribute> INCREASED_FROST_DAMAGE;
	public static Holder<Attribute> BLOCKED_FROST_DAMAGE;
	public static Holder<Attribute> FROST_RESISTANCE;
	public static Holder<Attribute> MAX_FREEZE_BUILD_UP;
	public static Holder<Attribute> FREEZE_DURATION;
	public static Holder<Attribute> FREEZE_TICK_THRESHOLD;
	public static Holder<Attribute> FREEZE_BUILD_UP_REDUCTION;
	public static Holder<Attribute> FREEZE_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static Holder<Attribute> ADDITIONAL_FIRE_DAMAGE;
	public static Holder<Attribute> INCREASED_FIRE_DAMAGE;
	public static Holder<Attribute> BLOCKED_FIRE_DAMAGE;
	public static Holder<Attribute> FIRE_RESISTANCE;
	public static Holder<Attribute> MAX_BURN_BUILD_UP;
	public static Holder<Attribute> BURN_DURATION;
	public static Holder<Attribute> BURN_TICK_THRESHOLD;
	public static Holder<Attribute> BURN_BUILD_UP_REDUCTION;
	public static Holder<Attribute> BURN_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static Holder<Attribute> ADDITIONAL_LIGHTNING_DAMAGE;
	public static Holder<Attribute> INCREASED_LIGHTNING_DAMAGE;
	public static Holder<Attribute> BLOCKED_LIGHTNING_DAMAGE;
	public static Holder<Attribute> LIGHTNING_RESISTANCE;
	public static Holder<Attribute> MAX_SHOCK_BUILD_UP;
	public static Holder<Attribute> SHOCK_DURATION;
	public static Holder<Attribute> SHOCK_TICK_THRESHOLD;
	public static Holder<Attribute> SHOCK_BUILD_UP_REDUCTION;
	public static Holder<Attribute> SHOCK_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static Holder<Attribute> ADDITIONAL_POISON_DAMAGE;
	public static Holder<Attribute> INCREASED_POISON_DAMAGE;
	public static Holder<Attribute> BLOCKED_POISON_DAMAGE;
	public static Holder<Attribute> POISON_RESISTANCE;
	public static Holder<Attribute> MAX_POISON_BUILD_UP;
	public static Holder<Attribute> POISON_DURATION;
	public static Holder<Attribute> POISON_TICK_THRESHOLD;
	public static Holder<Attribute> POISON_BUILD_UP_REDUCTION;
	public static Holder<Attribute> POISON_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static Holder<Attribute> MAX_STAGGER_BUILD_UP;
	public static Holder<Attribute> STAGGER_DURATION;
	public static Holder<Attribute> STAGGER_TICK_THRESHOLD;
	public static Holder<Attribute> STAGGER_BUILD_UP_REDUCTION;
	public static Holder<Attribute> STAGGER_BUILD_UP_REDUCTION_DELAY_THRESHOLD;

	public static Holder<Attribute> DAMAGE_TAKEN_FROM_MANA_MULTIPLIER;
	public static Holder<Attribute> DAMAGE_TAKEN_FROM_STAMINA_MULTIPLIER;

	public static final boolean isManaAttributesLoaded = FabricLoader.getInstance().isModLoaded("manaattributes");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");
	public static final boolean isBlockingOverhaulLoaded = FabricLoader.getInstance().isModLoaded("blockingoverhaul");

	public static float getCurrentMana(LivingEntity livingEntity) {
		float currentMana = 0.0F;
		if (isManaAttributesLoaded) {
			currentMana = ManaAttributesIntegration.getCurrentMana(livingEntity);
		}
		return currentMana;
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
		if (isManaAttributesLoaded) {
			ManaAttributesIntegration.addMana(livingEntity, amount);
		}
	}

	public static float getCurrentStamina(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = StaminaAttributesIntegration.getCurrentStamina(livingEntity);
		}
		return currentStamina;
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		if (isStaminaAttributesLoaded) {
			StaminaAttributesIntegration.addStamina(livingEntity, amount);
		}
	}

	public static void applyBlockAttackStaminaCost(LivingEntity livingEntity, boolean parried) {
		if (isBlockingOverhaulLoaded) {
			BlockingOverhaulIntegration.applyBlockAttackStaminaCost(livingEntity, parried);
		}
	}

	public static boolean canParry(LivingEntity livingEntity, DamageSource damageSource, ItemStack shieldItemStack) {
		boolean canParry = true;
		if (isBlockingOverhaulLoaded) {
			canParry = BlockingOverhaulIntegration.canParry(livingEntity, damageSource, shieldItemStack);
		}
		return canParry;
	}

	public static double getParryMultiplier(LivingEntity livingEntity, boolean parried) {
		double parryMultiplier = 1.0;
		if (isBlockingOverhaulLoaded) {
			parryMultiplier = BlockingOverhaulIntegration.getParryMultiplier(livingEntity, parried);
		}
		return parryMultiplier;
	}

	public static double getAppliedBlockingKnockback(LivingEntity defender, LivingEntity attacker, ItemStack blockingItemStack, boolean parried, double additionalAttackKnockback) {
		double appliedBlockingKnockback = 0.0;
		if (isBlockingOverhaulLoaded) {
			appliedBlockingKnockback = BlockingOverhaulIntegration.getAppliedBlockingKnockback(defender, attacker, blockingItemStack, parried, additionalAttackKnockback);
		}
		return appliedBlockingKnockback;
	}

	public static boolean currentStaminaAllowsBlocking(LivingEntity livingEntity) {
		boolean currentStaminaAllowsBlocking = true;
		if (isStaminaAttributesLoaded) {
			currentStaminaAllowsBlocking = BlockingOverhaulIntegration.currentStaminaAllowsBlocking(livingEntity);
		}
		return currentStaminaAllowsBlocking;
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Now dealing overhauled damage!");

		// Config
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

	}

	public static Identifier identifier(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: {}", message);
	}

}