package com.github.theredbrain.overhauleddamage.mixin.entity.player;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import com.github.theredbrain.overhauleddamage.registry.Tags;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.LinkedHashMap;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckLivingEntityMixin {


    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "applyDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;setAbsorptionAmount(F)V"
            ),
            locals= LocalCapture.CAPTURE_FAILSOFT)
    private void overhauleddamage$customDamageCalculation(DamageSource source, float amount, CallbackInfo ci, float var7) {

        StatusEffect fall_immune_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.serverConfig.fall_immune_status_effect_identifier));
        if (source.isIn(DamageTypeTags.IS_FALL) && fall_immune_status_effect != null && this.hasStatusEffect(fall_immune_status_effect)) {
            return;
        }

        StatusEffect staggered_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.serverConfig.staggered_status_effect_identifier));
        if (staggered_status_effect != null && this.hasStatusEffect(staggered_status_effect)) {
            amount = amount * 2;
        }

        StatusEffect calamity_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.serverConfig.calamity_status_effect_identifier));
        if (calamity_status_effect != null) {
            StatusEffectInstance calamityEffectInstance = this.getStatusEffect(calamity_status_effect);
            if (calamityEffectInstance != null) {
                amount = amount * 2 + calamityEffectInstance.getAmplifier();
            }
        }

        float f = amount;
        amount = Math.max(amount - this.getAbsorptionAmount(), 0.0F);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - amount));
        float g = f - amount;
        if (g > 0.0F && g < 3.4028235E37F) {
            Entity var6 = source.getAttacker();
            if (var6 instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.increaseStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(g * 10.0F));
            }
        }

        if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
            this.damageArmor(source, amount);
        }

        float applied_damage = 0;
        float true_amount = 0;

        // true damage is
        if (source.isIn(Tags.IS_TRUE_DAMAGE)) {
            true_amount = amount;
            amount = 0;
        }

        if (amount > 0) {
            LinkedHashMap<String, Float[]> damage_type_multipliers = OverhauledDamage.serverConfig.damage_type_multipliers;

            String damageTypeId = "";
            Float[] damage_type_multiplier = null;
            Optional<RegistryKey<DamageType>> optional = source.getTypeRegistryEntry().getKey();

            if (optional.isPresent()) {
                damageTypeId = optional.get().getValue().toString();
            }
            if (!damageTypeId.isEmpty()) {
                damage_type_multiplier = damage_type_multipliers.get(damageTypeId);
            }

            float bashing_amount = 0.0F;
            float piercing_amount = 0.0F;
            float slashing_amount = 0.0F;
            float poison_amount = 0.0F;
            float fire_amount = 0.0F;
            float frost_amount = 0.0F;
            float lightning_amount = 0.0F;

            if (damage_type_multiplier != null && damage_type_multiplier.length == 7) {
                bashing_amount = amount * damage_type_multiplier[0];
                piercing_amount = amount * damage_type_multiplier[1];
                slashing_amount = amount * damage_type_multiplier[2];
                poison_amount = amount * damage_type_multiplier[3];
                fire_amount = amount * damage_type_multiplier[4];
                frost_amount = amount * damage_type_multiplier[5];
                lightning_amount = amount * damage_type_multiplier[6];
            }

            LivingEntity attacker = null;
            if (source.getAttacker() instanceof LivingEntity) {
                attacker = (LivingEntity) source.getAttacker();
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

            // region shield blocks
            ItemStack shieldItemStack = this.getOffHandStack();
            if (this.isBlocking() && this.blockedByShield(source) && (((StaminaUsingEntity) this).staminaattributes$getStamina() > 0 || OverhauledDamage.serverConfig.blocking_requires_stamina)) {
                // try to parry the attack
                boolean tryParry = this.overhauleddamage$canParry() && ((DuckLivingEntityMixin) (Object)this).overhauleddamage$getBlockingTime() <= ((DuckLivingEntityMixin) this).overhauleddamage$getParryWindow() && source.getAttacker() != null && source.getAttacker() instanceof LivingEntity && shieldItemStack.isIn(Tags.CAN_PARRY);
                double parryBonus = tryParry ? ((DuckLivingEntityMixin) this).overhauleddamage$getParryBonus() : 1;
                float blockedBashingDamage = (float) (bashing_amount - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
                float blockedPiercingDamage = (float) (piercing_amount - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
                float blockedSlashingDamage = (float) (slashing_amount - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPhysicalDamage() * parryBonus);
                float blockedFireDamage = (float) (fire_amount - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFireDamage() * parryBonus);
                float blockedFrostDamage = (float) (frost_amount - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedFrostDamage() * parryBonus);
                float blockedLightningDamage = (float) (lightning_amount - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedLightningDamage() * parryBonus);
                float blockedPoisonDamage = (float) (poison_amount - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockedPoisonDamage() * parryBonus);

                ((StaminaUsingEntity) this).staminaattributes$addStamina(tryParry ? - ((DuckLivingEntityMixin) this).overhauleddamage$getParryStaminaCost() : - ((DuckLivingEntityMixin) this).overhauleddamage$getBlockStaminaCost());

                if (((StaminaUsingEntity) this).staminaattributes$getStamina() >= 0) {

                    boolean isStaggered = false;
                    // apply stagger based on left over damage
                    float appliedStagger = (float) Math.max(((bashing_amount - blockedBashingDamage) * 0.75 + (piercing_amount - blockedPiercingDamage) * 0.5 + (slashing_amount - blockedSlashingDamage) * 0.5 + (lightning_amount - blockedLightningDamage) * 0.5), 0);
                    if (appliedStagger > 0) {
                        this.overhauleddamage$addStaggerBuildUp(appliedStagger);
                        isStaggered = this.overhauleddamage$getStaggerBuildUp() >= this.overhauleddamage$getMaxStaggerBuildUp();

                    }

                    // parry was successful
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
                                // attacker is staggered
                                ((DuckLivingEntityMixin) attacker).overhauleddamage$addStaggerBuildUp(((DuckLivingEntityMixin) attacker).overhauleddamage$getMaxStaggerBuildUp());
                            }
                        } else {
                            // normal blocking applies knockback to the attacker
                            if (attacker != null) {
                                attacker.takeKnockback(((DuckLivingEntityMixin) this).overhauleddamage$getBlockForce(), attacker.getX() - this.getX(), attacker.getZ() - this.getZ());
                            }
                        }
                        float totalBlockedDamage = blockedBashingDamage + blockedPiercingDamage + blockedSlashingDamage + blockedFireDamage + blockedFrostDamage + blockedLightningDamage + blockedPoisonDamage;
                        if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity && totalBlockedDamage > 0.0f && totalBlockedDamage < 3.4028235E37f) {
                            serverPlayerEntity.increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(totalBlockedDamage * 10.0f));
                        }

                        this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
                    } else {
                        this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                    }
                }
            }
            // endregion shield blocks

            // region apply resistances/armor
            // armorToughness now directly determines how effective armor is
            // effective armor reduces damage by its amount
            float effectiveArmor = this.getArmor() * (float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);

            // TODO think about this more
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

            poison_amount = poison_amount - (poison_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getPoisonResistance()) / 100;

            fire_amount = fire_amount - (fire_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getFireResistance()) / 100;

            frost_amount = frost_amount - (frost_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getFrostResistance()) / 100;

            lightning_amount = lightning_amount - (lightning_amount * ((DuckLivingEntityMixin) this).overhauleddamage$getLightningResistance()) / 100;
            // endregion apply resistances/armor

            applied_damage = piercing_amount + bashing_amount + slashing_amount;

            // taking damage interrupts eating food, drinking potions, etc
            if (applied_damage > 0.0f && !this.isBlocking() && OverhauledDamage.serverConfig.damage_interrupts_item_usage) {
                this.stopUsingItem();
            }

            // apply bleeding
            float appliedBleeding = (float) ((piercing_amount * 0.5) + (slashing_amount * 0.5));
            if (appliedBleeding > 0 && source.isIn(Tags.APPLIES_BLEEDING)) {
                this.overhauleddamage$addBleedingBuildUp(appliedBleeding);
            }

            // apply burning
            if (fire_amount > 0) {
                this.overhauleddamage$addBurnBuildUp(fire_amount);
            }

            // apply chilled and frozen
            if (frost_amount > 0) {
                StatusEffect chilled_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(OverhauledDamage.serverConfig.chilled_status_effect_identifier));
                if (chilled_status_effect != null) {
                    int chilledDuration = (int) Math.ceil(frost_amount);
                    StatusEffectInstance statusEffectInstance = this.getStatusEffect(chilled_status_effect);
                    if (statusEffectInstance != null) {
                        chilledDuration = chilledDuration + statusEffectInstance.getDuration();
                    }
                    this.addStatusEffect(new StatusEffectInstance(chilled_status_effect, chilledDuration, 0, false, false, true));
                }
                this.overhauleddamage$addFreezeBuildUp(frost_amount);
            }

            // apply stagger
            float appliedStagger = (float) ((bashing_amount * 0.75) + (piercing_amount * 0.5) + (slashing_amount * 0.5) + (lightning_amount * 0.5));
            if (appliedStagger > 0) {
                this.overhauleddamage$addStaggerBuildUp(appliedStagger);
            }

            // apply poison
            if (poison_amount > 0) {
                this.overhauleddamage$addPoisonBuildUp(poison_amount);
            }

            // apply shocked
            if (lightning_amount > 0) {
                this.overhauleddamage$addShockBuildUp(lightning_amount);
            }
        }

        var7 = applied_damage + true_amount;
    }

    // effectively disables the vanilla jump crit mechanic
    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;hasVehicle()Z"
            )
    )
    public boolean overhauleddamage$redirect_hasVehicle(PlayerEntity instance) {
        return OverhauledDamage.serverConfig.disable_jump_crit_mechanic;
    }

    @Override
    public boolean overhauleddamage$canParry() {
        return true;
    }
}
