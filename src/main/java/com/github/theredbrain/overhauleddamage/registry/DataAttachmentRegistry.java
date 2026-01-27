package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class DataAttachmentRegistry {
	public static AttachmentType<Double> BLEEDING_BUILD_UP;
	public static AttachmentType<Double> BURN_BUILD_UP;
	public static AttachmentType<Double> FREEZE_BUILD_UP;
	public static AttachmentType<Double> POISON_BUILD_UP;
	public static AttachmentType<Double> STAGGER_BUILD_UP;
	public static AttachmentType<Double> SHOCK_BUILD_UP;

	static {
		BLEEDING_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("bleeding_build_up"));
		BURN_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("burn_build_up"));
		FREEZE_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("freeze_build_up"));
		POISON_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("poison_build_up"));
		STAGGER_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("stagger_build_up"));
		SHOCK_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("shock_build_up"));
	}
}
