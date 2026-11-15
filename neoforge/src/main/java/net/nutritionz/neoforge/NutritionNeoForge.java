package net.nutritionz.neoforge;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.nutritionz.NutritionMain;
import net.nutritionz.data.NutritionLoader;
import net.nutritionz.init.CommandInit;
import net.nutritionz.init.EventInit;
import net.nutritionz.network.NutritionServerPacket;
import net.nutritionz.network.packet.NutritionEffectPacket;
import net.nutritionz.network.packet.NutritionItemPacket;
import net.nutritionz.network.packet.NutritionPacket;
import net.nutritionz.network.packet.NutritionSyncPacket;

@Mod(NutritionMain.MOD_ID)
public class NutritionNeoForge {

    public NutritionNeoForge(IEventBus modBus) {
        NutritionMain.init();

        modBus.addListener(RegisterPayloadHandlersEvent.class, NutritionNeoForge::registerPayloads);
        modBus.addListener(AddPackFindersEvent.class, NutritionNeoForge::addPackFinders);

        NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(NutritionLoader.ID, new NutritionLoader()));
        NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                EventInit.onPlayerJoin(serverPlayer);
            }
        });
        NeoForge.EVENT_BUS.addListener(OnDatapackSyncEvent.class, event -> {
            if (event.getPlayer() == null) {
                EventInit.onDataPackReload(event.getPlayerList().getServer());
            }
        });
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> CommandInit.register(event.getDispatcher()));
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.playToClient(NutritionPacket.PACKET_ID, NutritionPacket.PACKET_CODEC);
        registrar.playToClient(NutritionItemPacket.PACKET_ID, NutritionItemPacket.PACKET_CODEC);
        registrar.playToClient(NutritionEffectPacket.PACKET_ID, NutritionEffectPacket.PACKET_CODEC);
        registrar.playToServer(NutritionSyncPacket.PACKET_ID, NutritionSyncPacket.PACKET_CODEC,
                (payload, context) -> NutritionServerPacket.handleSyncPacket((ServerPlayer) context.player()));
    }

    private static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) {
            return;
        }
        for (String pack : EventInit.getBuiltinDataPacks()) {
            event.addPackFinders(Identifier.fromNamespaceAndPath(NutritionMain.MOD_ID, "resourcepacks/" + pack), PackType.SERVER_DATA,
                    Component.translatableWithFallback("pack.nutritionz." + pack, NutritionMain.MOD_ID + "/" + pack), PackSource.BUILT_IN, false, Pack.Position.TOP);
        }
    }

}
