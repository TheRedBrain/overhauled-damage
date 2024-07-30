package com.github.theredbrain.overhauleddamage;

import com.github.theredbrain.overhauleddamage.config.ClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class OverhauledDamageClient implements ClientModInitializer {
	public static ConfigHolder<ClientConfig> clientConfigHolder;

	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfigHolder = AutoConfig.getConfigHolder(ClientConfig.class);

		// Packets
		ClientPlayNetworking.registerGlobalReceiver(OverhauledDamage.ServerConfigSyncPacket.PACKET_ID, (payload, context) -> {
			OverhauledDamage.serverConfig = payload.serverConfig();
		});
	}
}