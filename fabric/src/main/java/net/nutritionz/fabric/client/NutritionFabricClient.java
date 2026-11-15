package net.nutritionz.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.nutritionz.init.KeyInit;
import net.nutritionz.init.RenderInit;
import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.network.packet.NutritionEffectPacket;
import net.nutritionz.network.packet.NutritionItemPacket;
import net.nutritionz.network.packet.NutritionPacket;

@Environment(EnvType.CLIENT)
public class NutritionFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyMappingHelper.registerKeyMapping(KeyInit.createScreenKey(KeyMapping.Category.register(KeyInit.CATEGORY_ID)));
        ClientTickEvents.END_CLIENT_TICK.register(KeyInit::onClientTick);
        RenderInit.init();

        ClientPlayNetworking.registerGlobalReceiver(NutritionPacket.PACKET_ID,
                (payload, context) -> context.client().execute(() -> NutritionClientPacket.handleNutritionPacket(payload)));
        ClientPlayNetworking.registerGlobalReceiver(NutritionItemPacket.PACKET_ID,
                (payload, context) -> context.client().execute(() -> NutritionClientPacket.handleItemPacket(payload)));
        ClientPlayNetworking.registerGlobalReceiver(NutritionEffectPacket.PACKET_ID,
                (payload, context) -> context.client().execute(() -> NutritionClientPacket.handleEffectPacket(payload)));
    }
}
