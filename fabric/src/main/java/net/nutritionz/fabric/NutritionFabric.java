package net.nutritionz.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.nutritionz.NutritionMain;
import net.nutritionz.data.NutritionLoader;
import net.nutritionz.init.CommandInit;
import net.nutritionz.init.EventInit;
import net.nutritionz.network.NutritionServerPacket;
import net.nutritionz.network.packet.NutritionEffectPacket;
import net.nutritionz.network.packet.NutritionItemPacket;
import net.nutritionz.network.packet.NutritionPacket;
import net.nutritionz.network.packet.NutritionSyncPacket;

public class NutritionFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        NutritionMain.init();

        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(NutritionLoader.ID, new NutritionLoader());

        PayloadTypeRegistry.clientboundPlay().register(NutritionPacket.PACKET_ID, NutritionPacket.PACKET_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(NutritionItemPacket.PACKET_ID, NutritionItemPacket.PACKET_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(NutritionEffectPacket.PACKET_ID, NutritionEffectPacket.PACKET_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(NutritionSyncPacket.PACKET_ID, NutritionSyncPacket.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(NutritionSyncPacket.PACKET_ID,
                (payload, context) -> context.server().execute(() -> NutritionServerPacket.handleSyncPacket(context.player())));

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> EventInit.onPlayerJoin(handler.getPlayer()));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                EventInit.onDataPackReload(server);
            }
        });

        ModContainer modContainer = FabricLoader.getInstance().getModContainer(NutritionMain.MOD_ID).orElseThrow();
        for (String pack : EventInit.getBuiltinDataPacks()) {
            ResourceLoader.registerBuiltinPack(Identifier.fromNamespaceAndPath(NutritionMain.MOD_ID, pack), modContainer, PackActivationType.DEFAULT_ENABLED);
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandInit.register(dispatcher));
    }

}
