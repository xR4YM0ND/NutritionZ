package net.nutritionz.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.nutritionz.network.NutritionServerPacket;
import net.nutritionz.platform.Services;

public class EventInit {

    public static void onPlayerJoin(ServerPlayer serverPlayerEntity) {
        NutritionServerPacket.writeS2CItemNutritionPacket(serverPlayerEntity);
        NutritionServerPacket.writeS2CEffectNutritionPacket(serverPlayerEntity);
    }

    public static void onDataPackReload(MinecraftServer server) {
        for (ServerPlayer serverPlayerEntity : server.getPlayerList().getPlayers()) {
            NutritionServerPacket.writeS2CItemNutritionPacket(serverPlayerEntity);
            NutritionServerPacket.writeS2CEffectNutritionPacket(serverPlayerEntity);
        }
    }

    public static List<String> getBuiltinDataPacks() {
        List<String> packs = new ArrayList<>();
        // datapacks
        if (ConfigInit.CONFIG.minecraftDefaultCompat) {
            packs.add("minecraft_nutritionz");
        }

        // Farmers Delight Compat
        if (Services.PLATFORM.isModLoaded("farmersdelight") && ConfigInit.CONFIG.fd.fdFarmersdelightDefaultCompat) {
            packs.add("fd_farmersdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("culturaldelights") && ConfigInit.CONFIG.fd.fdCulturaldelightsDefaultCompat) {
            packs.add("fd_culturaldelights_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("eastersdelight") && ConfigInit.CONFIG.fd.fdEastersdelightDefaultCompat) {
            packs.add("fd_eastersdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("moredelight") && ConfigInit.CONFIG.fd.fdMoredelightDefaultCompat) {
            packs.add("fd_moredelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("mynethersdelight") && ConfigInit.CONFIG.fd.fdMynethersdelightDefaultCompat) {
            packs.add("fd_mynethersdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("rusticdelight") && ConfigInit.CONFIG.fd.fdRusticdelightDefaultCompat) {
            packs.add("fd_rusticdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("spanishdelight") && ConfigInit.CONFIG.fd.fdSpanishdelightDefaultCompat) {
            packs.add("fd_spanishdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("ubesdelight") && ConfigInit.CONFIG.fd.fdUbesdelightDefaultCompat) {
            packs.add("fd_ubesdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("wilderdelights") && ConfigInit.CONFIG.fd.fdWilderdelightsDefaultCompat) {
            packs.add("fd_wilderdelights_nutritionz");
        }

        // Dehydration Compat
        if (Services.PLATFORM.isModLoaded("dehydration") && ConfigInit.CONFIG.misc.dehydrationDefaultCompat) {
            packs.add("dehydration_nutritionz");
        }

        // MISC Compat
        if (Services.PLATFORM.isModLoaded("adventurez") && ConfigInit.CONFIG.misc.adventurezDefaultCompat) {
            packs.add("adventurez_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("aquaculture") && ConfigInit.CONFIG.misc.aquacultureDefaultCompat) {
            packs.add("aquaculture_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("betterend") && ConfigInit.CONFIG.misc.betterendDefaultCompat) {
            packs.add("betterend_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("betternether") && ConfigInit.CONFIG.misc.betternetherDefaultCompat) {
            packs.add("betternether_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("create") && ConfigInit.CONFIG.misc.createDefaultCompat) {
            packs.add("create_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("ecologics") && ConfigInit.CONFIG.misc.ecologicsDefaultCompat) {
            packs.add("ecologics_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("exlinefishing") && ConfigInit.CONFIG.misc.exlinefishingDefaultCompat) {
            packs.add("exlinefishing_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("farmz") && ConfigInit.CONFIG.misc.farmzDefaultCompat) {
            packs.add("farmz_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("fishingparadise") && ConfigInit.CONFIG.misc.fishingparadiseDefaultCompat) {
            packs.add("fishingparadise_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("fishofthieves") && ConfigInit.CONFIG.misc.fishofthievesDefaultCompat) {
            packs.add("fishofthieves_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("kafvalentine") && ConfigInit.CONFIG.misc.kafvalentineDefaultCompat) {
            packs.add("kafvalentine_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("livingthings") && ConfigInit.CONFIG.misc.livingthingsDefaultCompat) {
            packs.add("livingthings_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("moreberries") && ConfigInit.CONFIG.misc.moreberriesDefaultCompat) {
            packs.add("moreberries_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("naturalist") && ConfigInit.CONFIG.misc.naturalistDefaultCompat) {
            packs.add("naturalist_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("netherandend") && ConfigInit.CONFIG.misc.netherandendDefaultCompat) {
            packs.add("netherandend_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("promenade") && ConfigInit.CONFIG.misc.promenadeDefaultCompat) {
            packs.add("promenade_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("rosegoldequipment") && ConfigInit.CONFIG.misc.rosegoldequipmentDefaultCompat) {
            packs.add("rosegoldequipment_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("rusticpancakes") && ConfigInit.CONFIG.misc.rusticpancakesDefaultCompat) {
            packs.add("rusticpancakes_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("starcatcher") && ConfigInit.CONFIG.misc.starcatcherDefaultCompat) {
            packs.add("starcatcher_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("tastyvanilla") && ConfigInit.CONFIG.misc.tastyvanillaDefaultCompat) {
            packs.add("tastyvanilla_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("tofucraft") && ConfigInit.CONFIG.misc.tofucraftDefaultCompat) {
            packs.add("tofucraft_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("untamed") && ConfigInit.CONFIG.misc.untamedDefaultCompat) {
            packs.add("untamed_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("usefulfood") && ConfigInit.CONFIG.misc.usefulfoodDefaultCompat) {
            packs.add("usefulfood_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("vanillarice") && ConfigInit.CONFIG.misc.vanillariceDefaultCompat) {
            packs.add("vanillarice_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("wilderwild") && ConfigInit.CONFIG.misc.wilderwildDefaultCompat) {
            packs.add("wilderwild_nutritionz");
        }
        return packs;
    }
}
