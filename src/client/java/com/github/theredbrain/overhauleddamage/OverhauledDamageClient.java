package com.github.theredbrain.overhauleddamage;

import com.github.theredbrain.overhauleddamage.config.ClientConfig;
import com.github.theredbrain.overhauleddamage.config.ClientConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class OverhauledDamageClient implements ClientModInitializer {
	public static ClientConfig clientConfig;
	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfig = ((ClientConfigWrapper)AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

		ClientPlayNetworking.registerGlobalReceiver(OverhauledDamage.ServerConfigSync.ID, (client, handler, buf, responseSender) -> {
			OverhauledDamage.serverConfig = OverhauledDamage.ServerConfigSync.read(buf);
		});
	}
}