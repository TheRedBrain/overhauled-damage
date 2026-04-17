package com.github.theredbrain.overhauleddamage.entity;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import com.google.common.collect.HashMultimap;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Optional;

public class LivingEntityHelper {

	public static float calculateOverhauledDamage(ServerLevel serverLevel, LivingEntity livingEntity, DamageSource source, float amount) {
		ServerConfig serverConfig = OverhauledDamage.SERVER_CONFIG;
		if (!OverhauledDamage.SERVER_CONFIG.enable_overhauled_damage_calculation.get()) {
			return amount;
		}
		boolean enable_debug_log = serverConfig.damageCalculation.enable_debug_log.get();
		if (enable_debug_log) {
			OverhauledDamage.info("----- start of new damage calculation log -----");
			OverhauledDamage.info("");
			OverhauledDamage.info("entity taken damage : " + livingEntity.getName().getString());
			OverhauledDamage.info("");
			OverhauledDamage.info("damage source : " + source.toString());
			OverhauledDamage.info("");
			OverhauledDamage.info("damage amount : " + amount);
			OverhauledDamage.info("");
		}

		LivingEntity attacker = null;
		if (source.getEntity() instanceof LivingEntity) {
			attacker = (LivingEntity) source.getEntity();
		}
		if (enable_debug_log) {
			if (attacker != null) {
				OverhauledDamage.info("entity dealing damage : " + attacker.getName().getString());
				OverhauledDamage.info("");
			}
		}

		float applied_damage = 0;
		float true_amount = 0;

		if (source.is(Tags.IS_TRUE_DAMAGE)) {
			if (enable_debug_log) {
				OverhauledDamage.info("--- is true damage ---");
				OverhauledDamage.info("");
				OverhauledDamage.info("--- true damage can't be blocked and is not reduced by armor, protection or resistances ---");
				OverhauledDamage.info("");
				OverhauledDamage.info("--- true damage does not apply effect build ups ---");
				OverhauledDamage.info("");
			}
			true_amount = amount;
			amount = 0;
		}

		if (amount > 0) {

			// fallback
			ServerConfig.DamageTypes.DamageTypeMultipliers damage_type_multiplier = null;

			ValidatedMap<String, ServerConfig.DamageTypes.DamageTypeMultipliers> damage_type_multipliers = serverConfig.damageTypes.damage_type_multipliers;

			String damageTypeId = "";
			Optional<ResourceKey<DamageType>> optional = source.typeHolder().unwrapKey();

			if (optional.isPresent()) {
				damageTypeId = optional.get().identifier().toString();
			}
			if (!damageTypeId.isEmpty()) {
				if (enable_debug_log) {
					OverhauledDamage.info("damage type : " + damageTypeId);
					OverhauledDamage.info("");
				}
				damage_type_multiplier = damage_type_multipliers.get(damageTypeId);
			}
			if (damage_type_multiplier == null) {
				if (enable_debug_log) {
					OverhauledDamage.info("using default_damage_type_multipliers");
					OverhauledDamage.info("");
				}
				damage_type_multiplier = serverConfig.damageTypes.default_damage_type_multipliers.get();
			}
			if (enable_debug_log) {
				OverhauledDamage.info("used damage_type_multipliers : " + damage_type_multiplier.toString());
				OverhauledDamage.info("");
			}

			// default values
			float generic_amount = amount * damage_type_multiplier.generic;
			float bashing_amount = amount * damage_type_multiplier.bashing;
			float piercing_amount = amount * damage_type_multiplier.piercing;
			float slashing_amount = amount * damage_type_multiplier.slashing;
			float poison_amount = amount * damage_type_multiplier.poison;
			float fire_amount = amount * damage_type_multiplier.fire;
			float frost_amount = amount * damage_type_multiplier.frost;
			float lightning_amount = amount * damage_type_multiplier.lightning;
			if (enable_debug_log) {
				OverhauledDamage.info("--- initial attack amounts ---");
				OverhauledDamage.info("generic_amount : " + generic_amount);
				OverhauledDamage.info("bashing_amount : " + bashing_amount);
				OverhauledDamage.info("piercing_amount : " + piercing_amount);
				OverhauledDamage.info("slashing_amount : " + slashing_amount);
				OverhauledDamage.info("poison_amount : " + poison_amount);
				OverhauledDamage.info("fire_amount : " + fire_amount);
				OverhauledDamage.info("frost_amount : " + frost_amount);
				OverhauledDamage.info("lightning_amount : " + lightning_amount);
				OverhauledDamage.info("");
			}

			if (attacker != null) {
				bashing_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalBashingDamage() + bashing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedBashingDamage();
				piercing_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalPiercingDamage() + piercing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedPiercingDamage();
				slashing_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalSlashingDamage() + slashing_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedSlashingDamage();
				fire_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalFireDamage() + fire_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedFireDamage();
				frost_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalFrostDamage() + frost_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedFrostDamage();
				lightning_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalLightningDamage() + lightning_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedLightningDamage();
				poison_amount = (((DuckLivingEntityMixin) attacker).overhauleddamage$getAdditionalPoisonDamage() + poison_amount) * ((DuckLivingEntityMixin) attacker).overhauleddamage$getIncreasedPoisonDamage();
			}
			if (enable_debug_log) {
				OverhauledDamage.info("--- attack amounts after additions ---");
				OverhauledDamage.info("generic_amount : " + generic_amount);
				OverhauledDamage.info("bashing_amount : " + bashing_amount);
				OverhauledDamage.info("piercing_amount : " + piercing_amount);
				OverhauledDamage.info("slashing_amount : " + slashing_amount);
				OverhauledDamage.info("poison_amount : " + poison_amount);
				OverhauledDamage.info("fire_amount : " + fire_amount);
				OverhauledDamage.info("frost_amount : " + frost_amount);
				OverhauledDamage.info("lightning_amount : " + lightning_amount);
				OverhauledDamage.info("");
			}

			//
			boolean triedBlocking = false;

			// region shield blocks
			if (OverhauledDamage.isBlockingOverhaulEnabled()) {
				ItemStack shieldItemStack = livingEntity.getUseItem();
				BlocksAttacks blocksAttacks = shieldItemStack.get(DataComponents.BLOCKS_ATTACKS);

				if (blocksAttacks != null && !(Boolean) blocksAttacks.bypassedBy().map(source::is).orElse(false)) {

					if (source.getDirectEntity() instanceof AbstractArrow abstractArrow && abstractArrow.getPierceLevel() > 0) {

						if (enable_debug_log) {
							OverhauledDamage.info("--- piercing projectile bypassed blocking ---");
							OverhauledDamage.info("");
						}
					} else {

						if (OverhauledDamage.currentStaminaAllowsBlocking(livingEntity)) {
							// a parry is tried, if the blocking time < the parry window of the blocking entity, the blocking entity can parry at all and the blocking item is in the 'can_parry' item tag
							boolean tryParry = OverhauledDamage.canParry(livingEntity, source, shieldItemStack);
							double parryBonus = OverhauledDamage.getParryMultiplier(livingEntity, tryParry);

							triedBlocking = true;

							if (enable_debug_log) {
								OverhauledDamage.info("--- blocking/parrying ---");
								OverhauledDamage.info("");
								OverhauledDamage.info("tryParry : " + tryParry);
								OverhauledDamage.info("");
								OverhauledDamage.info("parryBonus : " + parryBonus);
								OverhauledDamage.info("");
							}
							float blockedBashingDamage;
							float blockedPiercingDamage;
							float blockedSlashingDamage;
							float blockedFireDamage;
							float blockedFrostDamage;
							float blockedLightningDamage;
							float blockedPoisonDamage;

							if (serverConfig.damageCalculation.blockingOverhaul.blocked_damage_calculation_works_with_flat_values.get()) {
								if (enable_debug_log) {
									OverhauledDamage.info("blocked damage calculation uses flat values");
									OverhauledDamage.info("");
								}
								blockedBashingDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
								blockedPiercingDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
								blockedSlashingDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
								blockedFireDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFireDamage() * parryBonus);
								blockedFrostDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFrostDamage() * parryBonus);
								blockedLightningDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedLightningDamage() * parryBonus);
								blockedPoisonDamage = (float) (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPoisonDamage() * parryBonus);
							} else {
								if (enable_debug_log) {
									OverhauledDamage.info("blocked damage calculation uses percentage values");
									OverhauledDamage.info("");
								}
								blockedBashingDamage = (float) (bashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
								blockedPiercingDamage = (float) (piercing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
								blockedSlashingDamage = (float) (slashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPhysicalDamage() * parryBonus / 100);
								blockedFireDamage = (float) (fire_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFireDamage() * parryBonus / 100);
								blockedFrostDamage = (float) (frost_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedFrostDamage() * parryBonus / 100);
								blockedLightningDamage = (float) (lightning_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedLightningDamage() * parryBonus / 100);
								blockedPoisonDamage = (float) (poison_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBlockedPoisonDamage() * parryBonus / 100);
							}

							if (enable_debug_log) {
								OverhauledDamage.info("--- blocked damage amounts ---");
								OverhauledDamage.info("blockedBashingDamage : " + blockedBashingDamage);
								OverhauledDamage.info("blockedPiercingDamage : " + blockedPiercingDamage);
								OverhauledDamage.info("blockedSlashingDamage : " + blockedSlashingDamage);
								OverhauledDamage.info("blockedFireDamage : " + blockedFireDamage);
								OverhauledDamage.info("blockedFrostDamage : " + blockedFrostDamage);
								OverhauledDamage.info("blockedLightningDamage : " + blockedLightningDamage);
								OverhauledDamage.info("blockedPoisonDamage : " + blockedPoisonDamage);
								OverhauledDamage.info("");
							}
							// reduce the stamina of the blocking/parrying entity by the block/parry stamina cost
							OverhauledDamage.applyBlockAttackStaminaCost(livingEntity, tryParry);

							// when no stamina is left after blocking/parrying the block/parry was not successful and the damage is not reduced
							if (OverhauledDamage.getCurrentStamina(livingEntity) >= 0 || !OverhauledDamage.isStaminaAttributesLoaded) {

								boolean isStaggered = false;

								// apply stagger based on left over damage
								ServerConfig.DamageCalculation.AttackTypeMultipliers stagger_multipliers = serverConfig.damageCalculation.stagger_multipliers.get();
								if (enable_debug_log) {
									OverhauledDamage.info("--- apply stagger based on left over damage ---");
									OverhauledDamage.info("");
									OverhauledDamage.info("stagger_multipliers : " + stagger_multipliers.toString());
									OverhauledDamage.info("");
								}
								float appliedStagger = (generic_amount * stagger_multipliers.generic) + ((bashing_amount - blockedBashingDamage) * stagger_multipliers.bashing) + ((piercing_amount - blockedPiercingDamage) * stagger_multipliers.piercing) + ((slashing_amount - blockedSlashingDamage) * stagger_multipliers.slashing) + ((poison_amount - blockedPoisonDamage) * stagger_multipliers.poison) + ((fire_amount - blockedFireDamage) * stagger_multipliers.fire) + ((frost_amount - blockedFrostDamage) * stagger_multipliers.frost) + ((lightning_amount - blockedLightningDamage) * stagger_multipliers.lightning);
								if (appliedStagger > 0) {
									if (enable_debug_log) {
										OverhauledDamage.info("appliedStagger : " + appliedStagger);
										OverhauledDamage.info("");
									}
									addStaggerBuildUp(livingEntity, appliedStagger);
									isStaggered = getStaggerBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp();
								}

								// block/parry was successful
								if (!isStaggered) {
									bashing_amount -= blockedBashingDamage;
									piercing_amount -= blockedPiercingDamage;
									slashing_amount -= blockedSlashingDamage;
									fire_amount -= blockedFireDamage;
									frost_amount -= blockedFrostDamage;
									lightning_amount -= blockedLightningDamage;
									poison_amount -= blockedPoisonDamage;

									if (tryParry) {

										if (attacker != null) {
											addStaggerBuildUp(attacker, ((DuckLivingEntityMixin) attacker).overhauleddamage$getMaxStaggerBuildUp());
											if (enable_debug_log) {
												OverhauledDamage.info("--- successful parries stagger the attacker ---");
												OverhauledDamage.info("");
											}
										}
									} else {
										if (attacker != null) {
											ServerConfig.DamageCalculation.AttackTypeMultipliers negative_block_force_multipliers = serverConfig.damageCalculation.blockingOverhaul.negative_block_force_multipliers.get();
											double applied_knock_back = OverhauledDamage.getAppliedBlockingKnockback(livingEntity, attacker, shieldItemStack, tryParry, generic_amount * negative_block_force_multipliers.generic + blockedBashingDamage * negative_block_force_multipliers.bashing + blockedPiercingDamage * negative_block_force_multipliers.piercing + blockedSlashingDamage * negative_block_force_multipliers.slashing + blockedPoisonDamage * negative_block_force_multipliers.poison + blockedFireDamage * negative_block_force_multipliers.fire + blockedFrostDamage * negative_block_force_multipliers.frost + blockedLightningDamage * negative_block_force_multipliers.lightning);

											if (enable_debug_log) {
												OverhauledDamage.info("--- successful blocks apply knockback ---");
												// blocked damage is multiplied based on attack type
												// result is subtracted from defenders block force
												// the total block force is multiplied by a global modifier (supplied by Blocking Overhaul)
												// if end result is positive, the attacker gets knocked back
												// if end result is negative, the defender is knocked back
												OverhauledDamage.info("applied_knockback : " + applied_knock_back);
												OverhauledDamage.info("");
												OverhauledDamage.info("when applied_knock_back is greater zero, it's applied to the attacker");
												OverhauledDamage.info("");
												OverhauledDamage.info("when applied_knock_back is lesser zero, its absolute value is applied to the blocking entity");
												OverhauledDamage.info("");
											}
											if (applied_knock_back > 0.0) {
												attacker.knockback(applied_knock_back, livingEntity.getX() - attacker.getX(), livingEntity.getZ() - attacker.getZ());
											} else if (applied_knock_back < 0.0) {
												livingEntity.knockback(Math.abs(applied_knock_back), attacker.getX() - livingEntity.getX(), attacker.getZ() - livingEntity.getZ());
											}
										}
									}
									float totalBlockedDamage = blockedBashingDamage + blockedPiercingDamage + blockedSlashingDamage + blockedFireDamage + blockedFrostDamage + blockedLightningDamage + blockedPoisonDamage;
									if (livingEntity instanceof ServerPlayer serverPlayerEntity && totalBlockedDamage > 0.0f && totalBlockedDamage < 3.4028235E37f) {
										serverPlayerEntity.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(totalBlockedDamage * 10.0f));
									}

									OverhauledDamage.playBlockingSoundEvent(serverLevel, livingEntity, shieldItemStack, tryParry);

									if (enable_debug_log) {
										OverhauledDamage.info("--- attack amounts after blocking/parrying ---");
										OverhauledDamage.info("generic_amount : " + generic_amount);
										OverhauledDamage.info("bashing_amount : " + bashing_amount);
										OverhauledDamage.info("piercing_amount : " + piercing_amount);
										OverhauledDamage.info("slashing_amount : " + slashing_amount);
										OverhauledDamage.info("poison_amount : " + poison_amount);
										OverhauledDamage.info("fire_amount : " + fire_amount);
										OverhauledDamage.info("frost_amount : " + frost_amount);
										OverhauledDamage.info("lightning_amount : " + lightning_amount);
										OverhauledDamage.info("");
									}
								} else {
									if (enable_debug_log) {
										OverhauledDamage.info("--- blocking/parrying failed because the damage was too high ---");
										OverhauledDamage.info("");
									}
								}
							} else if (enable_debug_log) {
								OverhauledDamage.info("--- blocking/parrying attempt consumed too much stamina ---");
								OverhauledDamage.info("");
							}
						} else if (enable_debug_log) {
							OverhauledDamage.info("--- blocking/parrying failed because stamina was too low ---");
							OverhauledDamage.info("");
						}
					}
				} else if (enable_debug_log) {
					OverhauledDamage.info("--- no blocking/parrying was tried ---");
					OverhauledDamage.info("");
				}
			}
			// endregion shield blocks

			// region apply protection
			if (!source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS) && serverConfig.damageCalculation.enable_protection_overhaul.get()) {

				// the protection enchantments reduce damage, with a default value of 2 percent reduction per enchantment level
				float protection = 0.0F;
				if (livingEntity.level() instanceof ServerLevel serverWorld) {
					protection = (float) (EnchantmentHelper.getDamageProtection(serverWorld, livingEntity, source) * serverConfig.damageCalculation.protectionOverhaul.protection_damage_reduction_per_level);
				}

				// band-aid solution to prevent fall damage being reduced a second time by Feather Falling
				if (source.is(DamageTypeTags.IS_FALL)) {
					protection = 0.0F;
				}

				if (enable_debug_log) {
					OverhauledDamage.info("protection : " + protection);
					OverhauledDamage.info("");
				}

				// the different attack types also have a protection_multiplier
				ServerConfig.DamageCalculation.ProtectionOverhaul.ProtectionMultipliers protection_multipliers = serverConfig.damageCalculation.protectionOverhaul.protection_multipliers.get();

				if (enable_debug_log) {
					OverhauledDamage.info("protection_multipliers : " + protection_multipliers.toString());
					OverhauledDamage.info("");
				}

				generic_amount = generic_amount - (generic_amount * protection * protection_multipliers.generic / 100);

				bashing_amount = bashing_amount - (bashing_amount * protection * protection_multipliers.bashing / 100);

				piercing_amount = piercing_amount - (piercing_amount * protection * protection_multipliers.piercing / 100);

				slashing_amount = slashing_amount - (slashing_amount * protection * protection_multipliers.slashing / 100);

				poison_amount = poison_amount - (poison_amount * protection * protection_multipliers.poison / 100);

				fire_amount = fire_amount - (fire_amount * protection * protection_multipliers.fire / 100);

				frost_amount = frost_amount - (frost_amount * protection * protection_multipliers.frost / 100);

				lightning_amount = lightning_amount - (lightning_amount * protection * protection_multipliers.lightning / 100);

				if (enable_debug_log) {
					OverhauledDamage.info("--- attack amounts after protection ---");
					OverhauledDamage.info("generic_amount : " + generic_amount);
					OverhauledDamage.info("bashing_amount : " + bashing_amount);
					OverhauledDamage.info("piercing_amount : " + piercing_amount);
					OverhauledDamage.info("slashing_amount : " + slashing_amount);
					OverhauledDamage.info("poison_amount : " + poison_amount);
					OverhauledDamage.info("fire_amount : " + fire_amount);
					OverhauledDamage.info("frost_amount : " + frost_amount);
					OverhauledDamage.info("lightning_amount : " + lightning_amount);
					OverhauledDamage.info("");
				}
			} else if (enable_debug_log) {
				if (!serverConfig.damageCalculation.enable_protection_overhaul.get()) {
					OverhauledDamage.info("protection overhaul not active");
				}
				if (source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
					OverhauledDamage.info("damage bypasses protection");
				}
			}
			// endregion apply protection

			// region apply armor
			if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && serverConfig.damageCalculation.enable_armor_overhaul.get()) {
				float armorDamage = 0.0F;
				if (serverConfig.damageCalculation.armorOverhaul.armor_calculation_works_with_flat_values.get()) {
					// TODO this calculation needs a serious overhaul
					// armorToughness now directly determines how effective armor is
					// effective armor reduces damage by its amount
					// armor is more or less effective against different attack types
					float effectiveArmor = livingEntity.getArmorValue();

					if (serverConfig.damageCalculation.armorOverhaul.enable_armor_toughness_attribute.get()) {
						if (enable_debug_log) {
							OverhauledDamage.info("armor toughness is enabled");
							OverhauledDamage.info("");
						}
						effectiveArmor *= (float) livingEntity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
					} else if (enable_debug_log) {
						OverhauledDamage.info("armor toughness is disabled");
						OverhauledDamage.info("");
					}

					if (enable_debug_log) {
						OverhauledDamage.info("armor calculation uses flat values");
						OverhauledDamage.info("effective_armor : " + effectiveArmor);
						OverhauledDamage.info("");
					}

					if (piercing_amount * 1.25 <= effectiveArmor) {
						effectiveArmor -= (float) (piercing_amount * 1.25);
						piercing_amount = 0;
					} else {
						piercing_amount -= (float) (effectiveArmor * 0.75);
						effectiveArmor = 0;
					}

					if (bashing_amount <= effectiveArmor) {
						effectiveArmor -= bashing_amount;
						bashing_amount = 0;
					} else {
						bashing_amount -= effectiveArmor;
						effectiveArmor = 0;
					}

					if (fire_amount <= effectiveArmor) {
						effectiveArmor -= fire_amount;
						fire_amount = 0;
					} else {
						fire_amount -= effectiveArmor;
						effectiveArmor = 0;
					}

					if (slashing_amount <= effectiveArmor) {
						effectiveArmor -= slashing_amount;
						slashing_amount = 0;
					} else {
						slashing_amount -= effectiveArmor;
						slashing_amount = (float) (slashing_amount * 1.25); // slashing damage not blocked by armor deals more damage
						effectiveArmor = 0;
					}
					armorDamage = livingEntity.getArmorValue() - effectiveArmor;
				} else {
					// this is the alternative armor calculation
					// armor reduces damage on a percentage base
					// 1 armor point = 1 percent reduction
					// armor toughness is a multiplier to this
					float effective_armor = livingEntity.getArmorValue();

					if (serverConfig.damageCalculation.armorOverhaul.enable_armor_toughness_attribute.get()) {
						if (enable_debug_log) {
							OverhauledDamage.info("armor toughness is enabled");
							OverhauledDamage.info("");
						}
						effective_armor *= (float) livingEntity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
					} else if (enable_debug_log) {
						OverhauledDamage.info("armor toughness is disabled");
						OverhauledDamage.info("");
					}

					if (enable_debug_log) {
						OverhauledDamage.info("armor calculation uses percentage values");
						OverhauledDamage.info("effective_armor : " + effective_armor);
						OverhauledDamage.info("");
					}

					// notable difference to the first method:
					// armor is not reduced when reducing the damage amount of one attack_type

					// the different attack types have an armor_multiplier on their own
					ServerConfig.DamageCalculation.ArmorOverhaul.ArmorMultipliers armor_multipliers = serverConfig.damageCalculation.armorOverhaul.armor_multipliers.get();

					if (enable_debug_log) {
						OverhauledDamage.info("armor_multipliers: " + armor_multipliers.toString());
						OverhauledDamage.info("");
					}
					float generic_armor_damage = generic_amount * effective_armor * armor_multipliers.generic / 100;
					generic_amount = generic_amount - generic_armor_damage;

					float bashing_armor_damage = bashing_amount * effective_armor * armor_multipliers.bashing / 100;
					bashing_amount = bashing_amount - bashing_armor_damage;

					float piercing_armor_damage = piercing_amount * effective_armor * armor_multipliers.piercing / 100;
					piercing_amount = piercing_amount - piercing_armor_damage;

					float slashing_armor_damage = slashing_amount * effective_armor * armor_multipliers.slashing / 100;
					slashing_amount = slashing_amount - slashing_armor_damage;

					float poison_armor_damage = poison_amount * effective_armor * armor_multipliers.poison / 100;
					poison_amount = poison_amount - poison_armor_damage;

					float fire_armor_damage = fire_amount * effective_armor * armor_multipliers.fire / 100;
					fire_amount = fire_amount - fire_armor_damage;

					float frost_armor_damage = frost_amount * effective_armor * armor_multipliers.frost / 100;
					frost_amount = frost_amount - frost_armor_damage;

					float lightning_armor_damage = (lightning_amount * effective_armor * armor_multipliers.lightning / 100);
					lightning_amount = lightning_amount - lightning_armor_damage;

					armorDamage = generic_armor_damage + bashing_armor_damage + piercing_armor_damage + slashing_armor_damage + poison_armor_damage + fire_armor_damage + frost_armor_damage + lightning_armor_damage;

				}
				livingEntity.hurtArmor(source, armorDamage);
				if (enable_debug_log) {
					OverhauledDamage.info("damage applied to equipped armor: " + armorDamage);
					OverhauledDamage.info("");
					OverhauledDamage.info("--- attack amounts after armor ---");
					OverhauledDamage.info("generic_amount : " + generic_amount);
					OverhauledDamage.info("bashing_amount : " + bashing_amount);
					OverhauledDamage.info("piercing_amount : " + piercing_amount);
					OverhauledDamage.info("slashing_amount : " + slashing_amount);
					OverhauledDamage.info("poison_amount : " + poison_amount);
					OverhauledDamage.info("fire_amount : " + fire_amount);
					OverhauledDamage.info("frost_amount : " + frost_amount);
					OverhauledDamage.info("lightning_amount : " + lightning_amount);
					OverhauledDamage.info("");
				}
			} else if (enable_debug_log) {
				if (serverConfig.damageCalculation.enable_armor_overhaul.get()) {
					OverhauledDamage.info("armor overhaul not active");
				} else {
					OverhauledDamage.info("damage bypasses armor");
				}
			}
			// endregion apply armor

			// region apply resistances
			bashing_amount = bashing_amount - (bashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBashingResistance()) / 100;

			piercing_amount = piercing_amount - (piercing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPiercingResistance()) / 100;

			slashing_amount = slashing_amount - (slashing_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getSlashingResistance()) / 100;

			poison_amount = poison_amount - (poison_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonResistance()) / 100;

			fire_amount = fire_amount - (fire_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFireResistance()) / 100;

			frost_amount = frost_amount - (frost_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFrostResistance()) / 100;

			lightning_amount = lightning_amount - (lightning_amount * ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getLightningResistance()) / 100;
			// endregion apply resistances

			if (enable_debug_log) {
				OverhauledDamage.info("--- attack amounts after resistances ---");
				OverhauledDamage.info("generic_amount : " + generic_amount);
				OverhauledDamage.info("bashing_amount : " + bashing_amount);
				OverhauledDamage.info("piercing_amount : " + piercing_amount);
				OverhauledDamage.info("slashing_amount : " + slashing_amount);
				OverhauledDamage.info("poison_amount : " + poison_amount);
				OverhauledDamage.info("fire_amount : " + fire_amount);
				OverhauledDamage.info("frost_amount : " + frost_amount);
				OverhauledDamage.info("lightning_amount : " + lightning_amount);
				OverhauledDamage.info("");
			}

			ServerConfig.DamageCalculation.AttackTypeMultipliers applied_damage_multipliers = serverConfig.damageCalculation.applied_damage_multipliers.get();
			applied_damage = (generic_amount * applied_damage_multipliers.generic) + (bashing_amount * applied_damage_multipliers.bashing) + (piercing_amount * applied_damage_multipliers.piercing) + (slashing_amount * applied_damage_multipliers.slashing) + (poison_amount * applied_damage_multipliers.poison) + (fire_amount * applied_damage_multipliers.fire) + (frost_amount * applied_damage_multipliers.frost) + (lightning_amount * applied_damage_multipliers.lightning);

			if (enable_debug_log) {
				OverhauledDamage.info("--- damage applied to resources like health is multiplied ---");
				OverhauledDamage.info("applied_damage_multipliers : " + applied_damage_multipliers);
				OverhauledDamage.info("");
			}

			// taking damage interrupts eating food, drinking potions, etc
			if (applied_damage > 0.0f && !livingEntity.isBlocking() && serverConfig.damage_interrupts_item_usage.get()) {
				if (enable_debug_log) {
					OverhauledDamage.info("item usage was stopped");
					OverhauledDamage.info("");
				}
				livingEntity.releaseUsingItem();
			}

			if (enable_debug_log) {
				OverhauledDamage.info("--- apply damage by increasing effect build ups ---");
				OverhauledDamage.info("");
			}

			// apply hit stun
			if (serverConfig.enable_hit_stun_mechanic.get()) {
				Optional<Holder.Reference<Attribute>> attribute = BuiltInRegistries.ATTRIBUTE.get(serverConfig.hitStun.attribute.get());
				if (attribute.isPresent()) {
					if (livingEntity.getAttributes().hasAttribute(attribute.get())) {
						double attributeValue = livingEntity.getAttributeValue(attribute.get());
						ServerConfig.HitStun.HitStunSettings hitStunSettings = serverConfig.hitStun.hit_stun_settings.get(damageTypeId);
						if (hitStunSettings == null) {
							hitStunSettings = serverConfig.hitStun.default_hit_stun_settings.get();
						}

						if (attributeValue <= hitStunSettings.required_attribute_threshold) {
							// apply hit stun
							Optional<Holder.Reference<MobEffect>> hit_stun_status_effect = BuiltInRegistries.MOB_EFFECT.get(serverConfig.hitStun.hit_stun_status_effect_identifier.get());
							if (hit_stun_status_effect.isPresent()) {
								livingEntity.addEffect(new MobEffectInstance(hit_stun_status_effect.get(), hitStunSettings.duration, 0, false, false, true));
							}
						}
					}
				}
			} else if (enable_debug_log) {
				OverhauledDamage.info("no hit stun was applied");
				OverhauledDamage.info("");
			}

			// apply bleeding
			ServerConfig.DamageCalculation.AttackTypeMultipliers bleeding_multipliers = serverConfig.damageCalculation.bleeding_multipliers.get();
			if (source.is(Tags.APPLIES_BLEEDING)) {
				float applied_bleeding = (generic_amount * bleeding_multipliers.generic) + (bashing_amount * bleeding_multipliers.bashing) + (piercing_amount * bleeding_multipliers.piercing) + (slashing_amount * bleeding_multipliers.slashing) + (poison_amount * bleeding_multipliers.poison) + (fire_amount * bleeding_multipliers.fire) + (frost_amount * bleeding_multipliers.frost) + (lightning_amount * bleeding_multipliers.lightning);

				if (enable_debug_log) {
					OverhauledDamage.info("--- apply bleeding build up ---");
					OverhauledDamage.info("bleeding_multipliers : " + bleeding_multipliers);
				}

				if (applied_bleeding > 0) {
					if (enable_debug_log) {
						OverhauledDamage.info("applied bleeding build up : " + applied_bleeding);
						OverhauledDamage.info("");
					}
					addBleedingBuildUp(livingEntity, applied_bleeding);
				} else if (enable_debug_log) {
					OverhauledDamage.info("no bleeding build up was applied");
					OverhauledDamage.info("");
				}
			}

			if (enable_debug_log) {
				OverhauledDamage.info("--- apply burn build up ---");
			}
			// apply burn
			if (fire_amount > 0) {
				if (enable_debug_log) {
					OverhauledDamage.info("applied burn build up : " + fire_amount);
					OverhauledDamage.info("");
				}
				addBurnBuildUp(livingEntity, fire_amount);
			} else if (enable_debug_log) {
				OverhauledDamage.info("no burn build up was applied");
				OverhauledDamage.info("");
			}

			// apply chilled effect and freeze build up
			if (enable_debug_log) {
				OverhauledDamage.info("--- apply chilled effect and freeze build up ---");
			}
			if (frost_amount > 0) {
				Optional<Holder.Reference<MobEffect>> chilled_status_effect = BuiltInRegistries.MOB_EFFECT.get(serverConfig.buildUpEffects.chilled_status_effect_identifier.get());
				if (chilled_status_effect.isPresent()) {
					int chilledDuration = (int) Math.ceil(frost_amount * serverConfig.buildUpEffects.chilled_duration_multiplier);
					int existingChilledDuration = 0;
					int chilledAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(chilled_status_effect.get());
					if (statusEffectInstance != null) {
						chilledDuration = chilledDuration + statusEffectInstance.getDuration();
						if (serverConfig.buildUpEffects.should_chilled_duration_be_additive.get()) {
							existingChilledDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.buildUpEffects.should_chilled_amplifier_be_additive.get()) {
							chilledAmplifier = statusEffectInstance.getAmplifier();
						}
					}
					if (enable_debug_log) {
						OverhauledDamage.info("applied chilled effect with duration of : " + chilledDuration + existingChilledDuration + " and amplifier of : " + chilledAmplifier);
						OverhauledDamage.info("");
					}
					livingEntity.addEffect(new MobEffectInstance(chilled_status_effect.get(), chilledDuration + existingChilledDuration, chilledAmplifier, false, false, true));
				} else if (enable_debug_log) {
					OverhauledDamage.info("no chilled effect was applied");
					OverhauledDamage.info("");
				}
				if (enable_debug_log) {
					OverhauledDamage.info("applied freeze build up : " + frost_amount);
					OverhauledDamage.info("");
				}
				addFreezeBuildUp(livingEntity, frost_amount);
			} else if (enable_debug_log) {
				OverhauledDamage.info("no chilled effect was applied");
				OverhauledDamage.info("");
				OverhauledDamage.info("no freeze build up was applied");
				OverhauledDamage.info("");
			}

			if (!triedBlocking) {
				// apply stagger
				ServerConfig.DamageCalculation.AttackTypeMultipliers stagger_multipliers = serverConfig.damageCalculation.stagger_multipliers.get();
				if (enable_debug_log) {
					OverhauledDamage.info("--- apply stagger when no blocking was tried ---");
					OverhauledDamage.info("stagger_multipliers : " + stagger_multipliers.toString());
				}
				float appliedStagger = (generic_amount * stagger_multipliers.generic) + (bashing_amount * stagger_multipliers.bashing) + (piercing_amount * stagger_multipliers.piercing) + (slashing_amount * stagger_multipliers.slashing) + (poison_amount * stagger_multipliers.poison) + (fire_amount * stagger_multipliers.fire) + (frost_amount * stagger_multipliers.frost) + (lightning_amount * stagger_multipliers.lightning);
				if (appliedStagger > 0) {
					if (enable_debug_log) {
						OverhauledDamage.info("appliedStagger : " + appliedStagger);
						OverhauledDamage.info("");
					}
					addStaggerBuildUp(livingEntity, appliedStagger);
				} else if (enable_debug_log) {
					OverhauledDamage.info("no stagger was applied");
					OverhauledDamage.info("");
				}
			}

			// apply poison build up
			if (enable_debug_log) {
				OverhauledDamage.info("--- apply poison build up ---");
			}
			if (poison_amount > 0) {
				addPoisonBuildUp(livingEntity, poison_amount);
			}
			if (poison_amount > 0) {
				if (enable_debug_log) {
					OverhauledDamage.info("applied poison build up : " + poison_amount);
					OverhauledDamage.info("");
				}
				addPoisonBuildUp(livingEntity, poison_amount);
			} else if (enable_debug_log) {
				OverhauledDamage.info("no poison build up was applied");
				OverhauledDamage.info("");
			}

			// apply shock build up
			if (enable_debug_log) {
				OverhauledDamage.info("--- apply shock build up ---");
			}
			if (lightning_amount > 0) {
				if (enable_debug_log) {
					OverhauledDamage.info("applied shock build up : " + lightning_amount);
					OverhauledDamage.info("");
				}
				addShockBuildUp(livingEntity, lightning_amount);
			} else if (enable_debug_log) {
				OverhauledDamage.info("no shock build up was applied");
				OverhauledDamage.info("");
			}
		}

		float health_damage = applied_damage + true_amount;

		// TODO do these attributes need a rework? maybe clamp them between 0 and 100
		float damageTakenFromMana = ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getDamageTakenFromManaMultiplier();
		float damageTakenFromStamina = ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getDamageTakenFromStaminaMultiplier();
		float mana_damage = 0.0F;
		float stamina_damage = 0.0F;
		if (damageTakenFromMana > 0 && OverhauledDamage.isManaAttributesLoaded) {
			mana_damage = health_damage * damageTakenFromMana;
			OverhauledDamage.addMana(livingEntity, -mana_damage);
		}
		if (damageTakenFromStamina > 0 && OverhauledDamage.isStaminaAttributesLoaded) {
			stamina_damage = health_damage * damageTakenFromStamina;
			OverhauledDamage.addStamina(livingEntity, -stamina_damage);
		}
		health_damage = health_damage - mana_damage - stamina_damage;
		if (enable_debug_log) {
			OverhauledDamage.info("--- apply damage by reducing health / mana / stamina ---");
			OverhauledDamage.info("health_damage : " + health_damage);
			OverhauledDamage.info("this is further reduced by absorption");
			OverhauledDamage.info("");
			OverhauledDamage.info("mana_damage : " + mana_damage);
			OverhauledDamage.info("");
			OverhauledDamage.info("stamina_damage : " + stamina_damage);
			OverhauledDamage.info("");
		}

		return health_damage;
	}

	public static void tick(LivingEntity livingEntity) {

		if (!livingEntity.level().isClientSide()) {

			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$delayEffectBuildUpTick()) {
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setDelayEffectBuildUpTick(false);
				return;
			}

			((DuckLivingEntityMixin) livingEntity).overhauleddamage$setIsMoving(!livingEntity.oldPosition().equals(livingEntity.position()));;

			ServerConfig serverConfig = OverhauledDamage.SERVER_CONFIG;

			// bleeding
			if (getBleedingBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp()) {
				Optional<Holder.Reference<MobEffect>> bleeding_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.bleeding_status_effect_identifier.get());
				if (bleeding_status_effect.isPresent()) {
					int existingBleedingDuration = 0;
					int bleedingAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(bleeding_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.buildUpEffects.should_bleeding_duration_be_additive.get()) {
							existingBleedingDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.buildUpEffects.should_bleeding_amplifier_be_additive.get()) {
							bleedingAmplifier = statusEffectInstance.getAmplifier();
						}
					}
					livingEntity.addEffect(new MobEffectInstance(bleeding_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingDuration() + existingBleedingDuration, bleedingAmplifier, false, false, true));
				}
				setBleedingBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReductionDelayThreshold());
			}
			if (getBleedingBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReductionDelayThreshold()) {
					addBleedingBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				}
			}

			// burn
			if (getBurnBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp()) {
				Optional<Holder.Reference<MobEffect>> burn_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.burn_status_effect_identifier.get());
				if (burn_status_effect.isPresent()) {
					int existingBurnDuration = 0;
					int burnAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(burn_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.buildUpEffects.should_burn_duration_be_additive.get()) {
							existingBurnDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.buildUpEffects.should_burn_amplifier_be_additive.get()) {
							burnAmplifier = statusEffectInstance.getAmplifier() + 1;
						}
					}
					livingEntity.addEffect(new MobEffectInstance(burn_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnDuration() + existingBurnDuration, burnAmplifier, false, false, true));
				}
				setBurnBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReductionDelayThreshold());
			}
			if (getBurnBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReductionDelayThreshold()) {
					addBurnBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				}
			}

			// freeze
			if (getFreezeBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp()) {
				Optional<Holder.Reference<MobEffect>> freeze_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.freeze_status_effect_identifier.get());
				if (freeze_status_effect.isPresent()) {
					int existingFreezeDuration = 0;
					int freezeAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(freeze_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.buildUpEffects.should_freeze_duration_be_additive.get()) {
							existingFreezeDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.buildUpEffects.should_freeze_amplifier_be_additive.get()) {
							freezeAmplifier = statusEffectInstance.getAmplifier();
						}
					}
					livingEntity.addEffect(new MobEffectInstance(freeze_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeDuration() + existingFreezeDuration, freezeAmplifier, false, false, true));
				}
				setFreezeBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReductionDelayThreshold());
			}
			if (getFreezeBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReductionDelayThreshold()) {
					addFreezeBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				}
			}

			// stagger
			if (getStaggerBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp()) {
				Optional<Holder.Reference<MobEffect>> staggered_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.stagger_status_effect_identifier.get());
				if (staggered_status_effect.isPresent()) {
					int existingStaggerDuration = 0;
					int staggerAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(staggered_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.buildUpEffects.should_stagger_duration_be_additive.get()) {
							existingStaggerDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.buildUpEffects.should_stagger_amplifier_be_additive.get()) {
							staggerAmplifier = statusEffectInstance.getAmplifier();
						}
					}
					livingEntity.addEffect(new MobEffectInstance(staggered_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerDuration() + existingStaggerDuration, staggerAmplifier, false, false, true));
				}
				setStaggerBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReductionDelayThreshold());
			}
			if (getStaggerBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReductionDelayThreshold()) {
					addStaggerBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				}
			}

			// poison
			if (getPoisonBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp()) {
				Optional<Holder.Reference<MobEffect>> poison_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.poison_status_effect_identifier.get());
				if (poison_status_effect.isPresent()) {
					int existingPoisonDuration = 0;
					int poisonAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(poison_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.buildUpEffects.should_poison_duration_be_additive.get()) {
							existingPoisonDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.buildUpEffects.should_poison_amplifier_be_additive.get()) {
							poisonAmplifier = statusEffectInstance.getAmplifier() + 1;
						}
					}
					livingEntity.addEffect(new MobEffectInstance(poison_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonDuration() + existingPoisonDuration, poisonAmplifier, false, false, true));
				}
				setPoisonBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReductionDelayThreshold());
			}
			if (getPoisonBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReductionDelayThreshold()) {
					addPoisonBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				}
			}

			// shock
			if (getShockBuildUp(livingEntity) >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp()) {
				Optional<Holder.Reference<MobEffect>> shocked_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.shock_status_effect_identifier.get());
				if (shocked_status_effect.isPresent()) {
					int existingShockDuration = 0;
					int shockAmplifier = 0;
					MobEffectInstance statusEffectInstance = livingEntity.getEffect(shocked_status_effect.get());
					if (statusEffectInstance != null) {
						if (serverConfig.buildUpEffects.should_shock_duration_be_additive.get()) {
							existingShockDuration = statusEffectInstance.getDuration();
						}
						if (serverConfig.buildUpEffects.should_shock_amplifier_be_additive.get()) {
							shockAmplifier = statusEffectInstance.getAmplifier() + 1;
						}
					}
					livingEntity.addEffect(new MobEffectInstance(shocked_status_effect.get(), ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockDuration() + existingShockDuration, shockAmplifier, false, false, false));
				}
				setShockBuildUp(livingEntity, 0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReductionDelayThreshold());
			}
			if (getShockBuildUp(livingEntity) > 0) {
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockReductionDelayTimer() < ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReductionDelayThreshold()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockReductionDelayTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockReductionDelayTimer() + 1);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				} else {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickTimer() + 1);
				}
				if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickThreshold() && ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockReductionDelayTimer() >= ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReductionDelayThreshold()) {
					addShockBuildUp(livingEntity, -((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockBuildUpReduction());
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				}
			}
		}
	}

	public static float getBleedingBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getBleedingBuildUp(livingEntity);
	}

	public static void setBleedingBuildUp(LivingEntity livingEntity, float bleedingBuildUp) {
		DataAttachmentHelper.setBleedingBuildUp(livingEntity, bleedingBuildUp);
	}

	public static void addBleedingBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> bleeding_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.bleeding_status_effect_identifier.get());
		if (bleeding_status_effect.isEmpty()) {
			if (getBleedingBuildUp(livingEntity) > 0) {
				setBleedingBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp() != -1.0f && !livingEntity.hasEffect(bleeding_status_effect.get())) {
				double f = getBleedingBuildUp(livingEntity);
				setBleedingBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp()));
				if (getBleedingBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBleedingBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBleedingTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBleedingTickTimer(0);
				}
			}
		}
	}

	public static float getBurnBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getBurnBuildUp(livingEntity);
	}

	public static void setBurnBuildUp(LivingEntity livingEntity, float burnBuildUp) {
		DataAttachmentHelper.setBurnBuildUp(livingEntity, burnBuildUp);
	}

	public static void addBurnBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> burn_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.burn_status_effect_identifier.get());
		if (burn_status_effect.isEmpty()) {
			if (getBurnBuildUp(livingEntity) > 0) {
				setBurnBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp() != -1.0f) {
				setBurnBuildUp(livingEntity, (float) Mth.clamp(getBurnBuildUp(livingEntity) + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp()));
				if (getBurnBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxBurnBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getBurnTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setBurnTickTimer(0);
				}
			}
		}
	}

	public static float getFreezeBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getFreezeBuildUp(livingEntity);
	}

	public static void setFreezeBuildUp(LivingEntity livingEntity, float freezeBuildUp) {
		DataAttachmentHelper.setFreezeBuildUp(livingEntity, freezeBuildUp);
	}

	public static void addFreezeBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> freeze_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.freeze_status_effect_identifier.get());
		if (freeze_status_effect.isEmpty()) {
			if (getFreezeBuildUp(livingEntity) > 0) {
				setFreezeBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp() != -1.0f && !livingEntity.hasEffect(freeze_status_effect.get())) {
				double f = getFreezeBuildUp(livingEntity);
				setFreezeBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp()));
				if (getFreezeBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxFreezeBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getFreezeTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setFreezeTickTimer(0);
				}
			}
		}
	}

	public static float getStaggerBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getStaggerBuildUp(livingEntity);
	}

	public static void setStaggerBuildUp(LivingEntity livingEntity, float staggerBuildUp) {
		DataAttachmentHelper.setStaggerBuildUp(livingEntity, staggerBuildUp);
	}

	public static void addStaggerBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> staggered_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.stagger_status_effect_identifier.get());
		if (staggered_status_effect.isEmpty()) {
			if (getStaggerBuildUp(livingEntity) > 0) {
				setStaggerBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp() != -1.0f && !livingEntity.hasEffect(staggered_status_effect.get())) {
				double f = getStaggerBuildUp(livingEntity);
				setStaggerBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp()));
				if (getStaggerBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxStaggerBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getStaggerTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setStaggerTickTimer(0);
				}
			}
		}
	}

	public static float getPoisonBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getPoisonBuildUp(livingEntity);
	}

	public static void setPoisonBuildUp(LivingEntity livingEntity, float poisonBuildUp) {
		DataAttachmentHelper.setPoisonBuildUp(livingEntity, poisonBuildUp);
	}

	public static void addPoisonBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> poison_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.poison_status_effect_identifier.get());
		if (poison_status_effect.isEmpty()) {
			if (getPoisonBuildUp(livingEntity) > 0) {
				setPoisonBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp() != -1.0f) {
				double f = getPoisonBuildUp(livingEntity);
				setPoisonBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp()));
				if (getPoisonBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxPoisonBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getPoisonTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setPoisonTickTimer(0);
				}
			}
		}
	}

	public static float getShockBuildUp(LivingEntity livingEntity) {
		return DataAttachmentHelper.getShockBuildUp(livingEntity);
	}

	public static void setShockBuildUp(LivingEntity livingEntity, float shockBuildUp) {
		DataAttachmentHelper.setShockBuildUp(livingEntity, shockBuildUp);
	}

	public static void addShockBuildUp(LivingEntity livingEntity, float amount) {
		Optional<Holder.Reference<MobEffect>> shock_status_effect = BuiltInRegistries.MOB_EFFECT.get(OverhauledDamage.SERVER_CONFIG.buildUpEffects.shock_status_effect_identifier.get());
		if (shock_status_effect.isEmpty()) {
			if (getShockBuildUp(livingEntity) > 0) {
				setShockBuildUp(livingEntity, 0);
			}
		} else {
			if (((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp() != -1.0f) {
				double f = getShockBuildUp(livingEntity);
				setShockBuildUp(livingEntity, (float) Mth.clamp(f + amount, 0.0, ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp()));
				if (getShockBuildUp(livingEntity) > ((DuckLivingEntityMixin) livingEntity).overhauleddamage$getMaxShockBuildUp()) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(((DuckLivingEntityMixin) livingEntity).overhauleddamage$getShockTickThreshold());
				} else if (amount > 0) {
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockReductionDelayTimer(0);
					((DuckLivingEntityMixin) livingEntity).overhauleddamage$setShockTickTimer(0);
				}
			}
		}
	}

}
