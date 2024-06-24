package com.github.theredbrain.overhauleddamage;

import com.github.theredbrain.overhauleddamage.config.ServerConfig;
import com.github.theredbrain.overhauleddamage.config.ServerConfigWrapper;
import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverhauledDamage implements ModInitializer {
    public static final String MOD_ID = "overhauleddamage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ServerConfig serverConfig;
    private static PacketByteBuf serverConfigSerialized = PacketByteBufs.create();

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

    public static EntityAttribute ADDITIONAL_FROST_DAMAGE;
    public static EntityAttribute INCREASED_FROST_DAMAGE;
    public static EntityAttribute BLOCKED_FROST_DAMAGE;
    public static EntityAttribute FROST_RESISTANCE;
    public static EntityAttribute MAX_FREEZE_BUILD_UP;
    public static EntityAttribute FREEZE_DURATION;
    public static EntityAttribute FREEZE_TICK_THRESHOLD;
    public static EntityAttribute FREEZE_BUILD_UP_REDUCTION;

    public static EntityAttribute ADDITIONAL_FIRE_DAMAGE;
    public static EntityAttribute INCREASED_FIRE_DAMAGE;
    public static EntityAttribute BLOCKED_FIRE_DAMAGE;
    public static EntityAttribute FIRE_RESISTANCE;
    public static EntityAttribute MAX_BURN_BUILD_UP;
    public static EntityAttribute BURN_DURATION;
    public static EntityAttribute BURN_TICK_THRESHOLD;
    public static EntityAttribute BURN_BUILD_UP_REDUCTION;

    public static EntityAttribute ADDITIONAL_LIGHTNING_DAMAGE;
    public static EntityAttribute INCREASED_LIGHTNING_DAMAGE;
    public static EntityAttribute BLOCKED_LIGHTNING_DAMAGE;
    public static EntityAttribute LIGHTNING_RESISTANCE;
    public static EntityAttribute MAX_SHOCK_BUILD_UP;
    public static EntityAttribute SHOCK_DURATION;
    public static EntityAttribute SHOCK_TICK_THRESHOLD;
    public static EntityAttribute SHOCK_BUILD_UP_REDUCTION;

    public static EntityAttribute ADDITIONAL_POISON_DAMAGE;
    public static EntityAttribute INCREASED_POISON_DAMAGE;
    public static EntityAttribute BLOCKED_POISON_DAMAGE;
    public static EntityAttribute POISON_RESISTANCE;
    public static EntityAttribute MAX_POISON_BUILD_UP;
    public static EntityAttribute POISON_DURATION;
    public static EntityAttribute POISON_TICK_THRESHOLD;
    public static EntityAttribute POISON_BUILD_UP_REDUCTION;

    public static EntityAttribute MAX_STAGGER_BUILD_UP;
    public static EntityAttribute STAGGER_DURATION;
    public static EntityAttribute STAGGER_TICK_THRESHOLD;
    public static EntityAttribute STAGGER_BUILD_UP_REDUCTION;

    public static EntityAttribute BLOCK_FORCE;
    public static EntityAttribute PARRY_BONUS;
    public static EntityAttribute PARRY_WINDOW;

    public static EntityAttribute BLOCK_STAMINA_COST;
    public static EntityAttribute PARRY_STAMINA_COST;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");

        // Config
        AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        serverConfig = ((ServerConfigWrapper) AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

        // TODO 1.20.6
////		PayloadTypeRegistry.playS2C().register(ConfigSyncPacket.PACKET_ID, ConfigSyncPacket.PACKET_CODEC); // TODO might be necessary, works for now
//		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//			sender.sendPacket(new ConfigSyncPacket(serverConfig));
//		});

        // Events
        serverConfigSerialized = ServerConfigSync.write(serverConfig);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            sender.sendPacket(ServerConfigSync.ID, serverConfigSerialized);
        });

    }

    public static class ServerConfigSync { // TODO 1.20.6 port to packet
        public static Identifier ID = identifier("server_config_sync");

        public static PacketByteBuf write(ServerConfig serverConfig) {
            var gson = new Gson();
            var json = gson.toJson(serverConfig);
            var buffer = PacketByteBufs.create();
            buffer.writeString(json);
            return buffer;
        }

        public static ServerConfig read(PacketByteBuf buffer) {
            var gson = new Gson();
            var json = buffer.readString();
            return gson.fromJson(json, ServerConfig.class);
        }
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static void info(String message) {
        LOGGER.info("[" + MOD_ID + "] [info]: {}", message);
    }

}