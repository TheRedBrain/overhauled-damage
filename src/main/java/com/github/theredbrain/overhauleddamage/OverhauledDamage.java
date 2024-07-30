package com.github.theredbrain.overhauleddamage;

import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.config.ServerConfigWrapper;
import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverhauledDamage implements ModInitializer {
	public static final String MOD_ID = "overhauleddamage";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

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

	public static RegistryEntry<EntityAttribute> ADDITIONAL_FROST_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_FROST_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_FROST_DAMAGE;
	public static RegistryEntry<EntityAttribute> FROST_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_FREEZE_BUILD_UP;
	public static RegistryEntry<EntityAttribute> FREEZE_DURATION;
	public static RegistryEntry<EntityAttribute> FREEZE_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> FREEZE_BUILD_UP_REDUCTION;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_FIRE_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_FIRE_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_FIRE_DAMAGE;
	public static RegistryEntry<EntityAttribute> FIRE_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_BURN_BUILD_UP;
	public static RegistryEntry<EntityAttribute> BURN_DURATION;
	public static RegistryEntry<EntityAttribute> BURN_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> BURN_BUILD_UP_REDUCTION;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_LIGHTNING_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_LIGHTNING_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_LIGHTNING_DAMAGE;
	public static RegistryEntry<EntityAttribute> LIGHTNING_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_SHOCK_BUILD_UP;
	public static RegistryEntry<EntityAttribute> SHOCK_DURATION;
	public static RegistryEntry<EntityAttribute> SHOCK_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> SHOCK_BUILD_UP_REDUCTION;

	public static RegistryEntry<EntityAttribute> ADDITIONAL_POISON_DAMAGE;
	public static RegistryEntry<EntityAttribute> INCREASED_POISON_DAMAGE;
	public static RegistryEntry<EntityAttribute> BLOCKED_POISON_DAMAGE;
	public static RegistryEntry<EntityAttribute> POISON_RESISTANCE;
	public static RegistryEntry<EntityAttribute> MAX_POISON_BUILD_UP;
	public static RegistryEntry<EntityAttribute> POISON_DURATION;
	public static RegistryEntry<EntityAttribute> POISON_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> POISON_BUILD_UP_REDUCTION;

	public static RegistryEntry<EntityAttribute> MAX_STAGGER_BUILD_UP;
	public static RegistryEntry<EntityAttribute> STAGGER_DURATION;
	public static RegistryEntry<EntityAttribute> STAGGER_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> STAGGER_BUILD_UP_REDUCTION;

	public static RegistryEntry<EntityAttribute> BLOCK_FORCE;
	public static RegistryEntry<EntityAttribute> PARRY_BONUS;
	public static RegistryEntry<EntityAttribute> PARRY_WINDOW;

	public static RegistryEntry<EntityAttribute> BLOCK_STAMINA_COST;
	public static RegistryEntry<EntityAttribute> PARRY_STAMINA_COST;

	@Override
	public void onInitialize() {
		LOGGER.info("Now dealing overhauled damage!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper) AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		PayloadTypeRegistry.playS2C().register(ServerConfigSyncPacket.PACKET_ID, ServerConfigSyncPacket.PACKET_CODEC);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, new ServerConfigSyncPacket(serverConfig));
		});

	}

	public record ServerConfigSyncPacket(ServerConfig serverConfig) implements CustomPayload {
		public static final CustomPayload.Id<ServerConfigSyncPacket> PACKET_ID = new CustomPayload.Id<>(identifier("server_config_sync"));
		public static final PacketCodec<RegistryByteBuf, ServerConfigSyncPacket> PACKET_CODEC = PacketCodec.of(ServerConfigSyncPacket::write, ServerConfigSyncPacket::new);

		public ServerConfigSyncPacket(RegistryByteBuf registryByteBuf) {
			this(new Gson().fromJson(registryByteBuf.readString(), ServerConfig.class));
		}

		private void write(RegistryByteBuf registryByteBuf) {
			registryByteBuf.writeString(new Gson().toJson(serverConfig));
		}

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return PACKET_ID;
		}
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: {}", message);
	}

}