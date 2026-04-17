package com.github.theredbrain.overhauleddamage.entity;

public interface DuckLivingEntityMixin {

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

	float overhauleddamage$getMaxBleedingBuildUp();

	int overhauleddamage$getBleedingDuration();

	int overhauleddamage$getBleedingTickThreshold();

	int overhauleddamage$getBleedingBuildUpReduction();

	int overhauleddamage$getBleedingBuildUpReductionDelayThreshold();

	int overhauleddamage$getBleedingTickTimer();

	void overhauleddamage$setBleedingTickTimer(int bleedingTickTimer);

	int overhauleddamage$getBleedingReductionDelayTimer();

	void overhauleddamage$setBleedingReductionDelayTimer(int bleedingReductionDelayTimer);

	float overhauleddamage$getAdditionalFireDamage();

	float overhauleddamage$getIncreasedFireDamage();

	float overhauleddamage$getBlockedFireDamage();

	float overhauleddamage$getFireResistance();

	float overhauleddamage$getMaxBurnBuildUp();

	int overhauleddamage$getBurnDuration();

	int overhauleddamage$getBurnTickThreshold();

	int overhauleddamage$getBurnBuildUpReduction();

	int overhauleddamage$getBurnBuildUpReductionDelayThreshold();

	int overhauleddamage$getBurnTickTimer();

	void overhauleddamage$setBurnTickTimer(int burnTickTimer);

	int overhauleddamage$getBurnReductionDelayTimer();

	void overhauleddamage$setBurnReductionDelayTimer(int burnReductionDelayTimer);

	float overhauleddamage$getAdditionalFrostDamage();

	float overhauleddamage$getIncreasedFrostDamage();

	float overhauleddamage$getBlockedFrostDamage();

	float overhauleddamage$getFrostResistance();

	float overhauleddamage$getMaxFreezeBuildUp();

	int overhauleddamage$getFreezeDuration();

	int overhauleddamage$getFreezeTickThreshold();

	int overhauleddamage$getFreezeBuildUpReduction();

	int overhauleddamage$getFreezeBuildUpReductionDelayThreshold();

	int overhauleddamage$getFreezeTickTimer();

	void overhauleddamage$setFreezeTickTimer(int freezeTickTimer);

	int overhauleddamage$getFreezeReductionDelayTimer();

	void overhauleddamage$setFreezeReductionDelayTimer(int freezeReductionDelayTimer);

	float overhauleddamage$getMaxStaggerBuildUp();

	int overhauleddamage$getStaggerDuration();

	int overhauleddamage$getStaggerTickThreshold();

	int overhauleddamage$getStaggerBuildUpReduction();

	int overhauleddamage$getStaggerBuildUpReductionDelayThreshold();

	int overhauleddamage$getStaggerTickTimer();

	void overhauleddamage$setStaggerTickTimer(int staggerTickTimer);

	int overhauleddamage$getStaggerReductionDelayTimer();

	void overhauleddamage$setStaggerReductionDelayTimer(int staggerReductionDelayTimer);

	float overhauleddamage$getAdditionalPoisonDamage();

	float overhauleddamage$getIncreasedPoisonDamage();

	float overhauleddamage$getBlockedPoisonDamage();

	float overhauleddamage$getPoisonResistance();

	float overhauleddamage$getMaxPoisonBuildUp();

	int overhauleddamage$getPoisonDuration();

	int overhauleddamage$getPoisonTickThreshold();

	int overhauleddamage$getPoisonBuildUpReduction();

	int overhauleddamage$getPoisonBuildUpReductionDelayThreshold();

	int overhauleddamage$getPoisonTickTimer();

	void overhauleddamage$setPoisonTickTimer(int poisonTickTimer);

	int overhauleddamage$getPoisonReductionDelayTimer();

	void overhauleddamage$setPoisonReductionDelayTimer(int poisonReductionDelayTimer);

	float overhauleddamage$getAdditionalLightningDamage();

	float overhauleddamage$getIncreasedLightningDamage();

	float overhauleddamage$getBlockedLightningDamage();

	float overhauleddamage$getLightningResistance();

	float overhauleddamage$getMaxShockBuildUp();

	int overhauleddamage$getShockDuration();

	int overhauleddamage$getShockTickThreshold();

	int overhauleddamage$getShockBuildUpReduction();

	int overhauleddamage$getShockBuildUpReductionDelayThreshold();

	int overhauleddamage$getShockTickTimer();

	void overhauleddamage$setShockTickTimer(int shockTickTimer);

	int overhauleddamage$getShockReductionDelayTimer();

	void overhauleddamage$setShockReductionDelayTimer(int shockReductionDelayTimer);

	float overhauleddamage$getDamageTakenFromManaMultiplier();

	float overhauleddamage$getDamageTakenFromStaminaMultiplier();

	boolean overhauleddamage$isMoving();

	void overhauleddamage$setIsMoving(boolean isMoving);

}
