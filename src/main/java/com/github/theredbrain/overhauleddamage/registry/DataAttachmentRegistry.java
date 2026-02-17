package com.github.theredbrain.overhauleddamage.registry;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;

public class DataAttachmentRegistry {
	public static AttachmentType<Float> BLEEDING_BUILD_UP;
	public static AttachmentType<Float> BURN_BUILD_UP;
	public static AttachmentType<Float> FREEZE_BUILD_UP;
	public static AttachmentType<Float> POISON_BUILD_UP;
	public static AttachmentType<Float> STAGGER_BUILD_UP;
	public static AttachmentType<Float> SHOCK_BUILD_UP;

	public static void init() {
	}

	static {
		BLEEDING_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("bleeding_build_up"), builder -> builder
				.persistent(Codec.FLOAT)
				.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
		);
		BURN_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("burn_build_up"), builder -> builder
				.persistent(Codec.FLOAT)
				.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
		);
		FREEZE_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("freeze_build_up"), builder -> builder
				.persistent(Codec.FLOAT)
				.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
		);
		POISON_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("poison_build_up"), builder -> builder
				.persistent(Codec.FLOAT)
				.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
		);
		STAGGER_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("stagger_build_up"), builder -> builder
				.persistent(Codec.FLOAT)
				.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
		);
		SHOCK_BUILD_UP = AttachmentRegistry.create(OverhauledDamage.identifier("shock_build_up"), builder -> builder
				.persistent(Codec.FLOAT)
				.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
		);
	}
}
