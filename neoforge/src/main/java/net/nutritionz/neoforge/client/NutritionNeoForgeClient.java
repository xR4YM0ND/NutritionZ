package net.nutritionz.neoforge.client;

import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.nutritionz.NutritionMain;
import net.nutritionz.config.NutritionzConfig;
import net.nutritionz.init.KeyInit;
import net.nutritionz.init.RenderInit;
import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.network.packet.NutritionEffectPacket;
import net.nutritionz.network.packet.NutritionItemPacket;
import net.nutritionz.network.packet.NutritionPacket;

@Mod(value = NutritionMain.MOD_ID, dist = Dist.CLIENT)
public class NutritionNeoForgeClient {

    public NutritionNeoForgeClient(IEventBus modBus, ModContainer container) {
        modBus.addListener(RegisterKeyMappingsEvent.class, NutritionNeoForgeClient::registerKeyMappings);
        modBus.addListener(RegisterClientPayloadHandlersEvent.class, event -> {
            event.register(NutritionPacket.PACKET_ID, (payload, context) -> NutritionClientPacket.handleNutritionPacket(payload));
            event.register(NutritionItemPacket.PACKET_ID, (payload, context) -> NutritionClientPacket.handleItemPacket(payload));
            event.register(NutritionEffectPacket.PACKET_ID, (payload, context) -> NutritionClientPacket.handleEffectPacket(payload));
        });
        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, event -> KeyInit.onClientTick(Minecraft.getInstance()));
        RenderInit.init();

        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> AutoConfigClient.getConfigScreen(NutritionzConfig.class, parent).get());
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        KeyMapping.Category category = new KeyMapping.Category(KeyInit.CATEGORY_ID);
        event.registerCategory(category);
        event.register(KeyInit.createScreenKey(category));
    }

}
