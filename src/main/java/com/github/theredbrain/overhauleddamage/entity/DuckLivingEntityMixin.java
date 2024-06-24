package com.github.theredbrain.overhauleddamage.entity;

import net.minecraft.entity.damage.DamageSource;

public interface DuckLivingEntityMixin {

    float overhauleddamage$calculateOverhauledDamage(DamageSource damageSource, float amount);
    float overhauleddamage$getAdditionalBashingDamage();
    float overhauleddamage$getIncreasedBashingDamage();
    float overhauleddamage$getBashingResistance();

    float overhauleddamage$getAdditionalPiercingDamage();
    float overhauleddamage$getIncreasedPiercingDamage();
    float overhauleddamage$getPiercingResistance();

    float overhauleddamage$getAdditionalSlashingDamage();
    float overhauleddamage$getIncreasedSlashingDamage();
    float overhauleddamage$getSlashingResistance();

    float overhauleddamage$getBlockedPhysicalDamage();

    void overhauleddamage$addBleedingBuildUp(float amount);
    float overhauleddamage$getBleedingBuildUp();
    void overhauleddamage$setBleedingBuildUp(float bleedingBuildUp);
    float overhauleddamage$getMaxBleedingBuildUp();
    int overhauleddamage$getBleedingDuration();
    int overhauleddamage$getBleedingTickThreshold();
    int overhauleddamage$getBleedingBuildUpReduction();

    float overhauleddamage$getAdditionalFireDamage();
    float overhauleddamage$getIncreasedFireDamage();
    float overhauleddamage$getBlockedFireDamage();
    float overhauleddamage$getFireResistance();
    void overhauleddamage$addBurnBuildUp(float amount);
    float overhauleddamage$getBurnBuildUp();
    void overhauleddamage$setBurnBuildUp(float burnBuildUp);
    float overhauleddamage$getMaxBurnBuildUp();
    int overhauleddamage$getBurnDuration();
    int overhauleddamage$getBurnTickThreshold();
    int overhauleddamage$getBurnBuildUpReduction();

    float overhauleddamage$getAdditionalFrostDamage();
    float overhauleddamage$getIncreasedFrostDamage();
    float overhauleddamage$getBlockedFrostDamage();
    float overhauleddamage$getFrostResistance();
    void overhauleddamage$addFreezeBuildUp(float amount);
    float overhauleddamage$getFreezeBuildUp();
    void overhauleddamage$setFreezeBuildUp(float freezeBuildUp);
    float overhauleddamage$getMaxFreezeBuildUp();
    int overhauleddamage$getFreezeDuration();
    int overhauleddamage$getFreezeTickThreshold();
    int overhauleddamage$getFreezeBuildUpReduction();

    void overhauleddamage$addStaggerBuildUp(float amount);
    float overhauleddamage$getStaggerBuildUp();
    void overhauleddamage$setStaggerBuildUp(float poise);
    float overhauleddamage$getMaxStaggerBuildUp();
    int overhauleddamage$getStaggerDuration();
    int overhauleddamage$getStaggerTickThreshold();
    int overhauleddamage$getStaggerBuildUpReduction();

    float overhauleddamage$getAdditionalPoisonDamage();
    float overhauleddamage$getIncreasedPoisonDamage();
    float overhauleddamage$getBlockedPoisonDamage();
    float overhauleddamage$getPoisonResistance();
    void overhauleddamage$addPoisonBuildUp(float amount);
    float overhauleddamage$getPoisonBuildUp();
    void overhauleddamage$setPoisonBuildUp(float poisonBuildUp);
    float overhauleddamage$getMaxPoisonBuildUp();
    int overhauleddamage$getPoisonDuration();
    int overhauleddamage$getPoisonTickThreshold();
    int overhauleddamage$getPoisonBuildUpReduction();

    float overhauleddamage$getAdditionalLightningDamage();
    float overhauleddamage$getIncreasedLightningDamage();
    float overhauleddamage$getBlockedLightningDamage();
    float overhauleddamage$getLightningResistance();
    void overhauleddamage$addShockBuildUp(float amount);
    float overhauleddamage$getShockBuildUp();
    void overhauleddamage$setShockBuildUp(float shockBuildUp);
    float overhauleddamage$getMaxShockBuildUp();
    int overhauleddamage$getShockDuration();
    int overhauleddamage$getShockTickThreshold();
    int overhauleddamage$getShockBuildUpReduction();

    float overhauleddamage$getBlockForce();
    float overhauleddamage$getParryBonus();
    float overhauleddamage$getParryWindow();

    float overhauleddamage$getBlockStaminaCost();
    float overhauleddamage$getParryStaminaCost();

    boolean overhauleddamage$canParry();
    int overhauleddamage$getBlockingTime();
}
