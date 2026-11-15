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
        
        // Lets Do Compat
        if (Services.PLATFORM.isModLoaded("alpinewhispers") && ConfigInit.CONFIG.letsdo.letsdoAlpineWhispersDefaultCompat) {
            packs.add("letsdo_alpinewhispers_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("bakery") && ConfigInit.CONFIG.letsdo.letsdoBakeryDefaultCompat) {
            packs.add("letsdo_bakery_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("beachparty") && ConfigInit.CONFIG.letsdo.letsdoBeachpartyDefaultCompat) {
            packs.add("letsdo_beachparty_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("brewery") && ConfigInit.CONFIG.letsdo.letsdoBreweryDefaultCompat) {
            packs.add("letsdo_brewery_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("candlelight") && ConfigInit.CONFIG.letsdo.letsdoCandlelightDefaultCompat) {
            packs.add("letsdo_candlelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("cornexpansion") && ConfigInit.CONFIG.letsdo.letsdoCornexpansionDefaultCompat) {
            packs.add("letsdo_cornexpansion_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("farm_and_charm") && ConfigInit.CONFIG.letsdo.letsdoFarmcharmDefaultCompat) {
            packs.add("letsdo_farmcharm_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("herbalbrews") && ConfigInit.CONFIG.letsdo.letsdoHerbalbrewsDefaultCompat) {
            packs.add("letsdo_herbalbrews_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("lilis_lucky_lures") && ConfigInit.CONFIG.letsdo.letsdoLllDefaultCompat) {
            packs.add("letsdo_lll_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("meadow") && ConfigInit.CONFIG.letsdo.letsdoMeadowDefaultCompat) {
            packs.add("letsdo_meadow_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("vinery") && ConfigInit.CONFIG.letsdo.letsdoVineryDefaultCompat) {
            packs.add("letsdo_vinery_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("wildernature") && ConfigInit.CONFIG.letsdo.letsdoWildernatureDefaultCompat) {
            packs.add("letsdo_wildernature_nutritionz");
        }

        // Farmers Delight Compat
        if (Services.PLATFORM.isModLoaded("farmersdelight") && ConfigInit.CONFIG.fd.fdFarmersdelightDefaultCompat) {
            packs.add("fd_farmersdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("moredelight") && ConfigInit.CONFIG.fd.fdMoredelightDefaultCompat) {
            packs.add("fd_moredelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("natures_delight") && Services.PLATFORM.isModLoaded("natures_spirit") && ConfigInit.CONFIG.fd.fdNaturesdelightDefaultCompat) {
            packs.add("fd_ns_naturesdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("oceansdelight") && ConfigInit.CONFIG.fd.fdOceansdelightDefaultCompat) {
            packs.add("fd_oceansdelight_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("ubesdelight") && ConfigInit.CONFIG.fd.fdUbesdelightDefaultCompat) {
            packs.add("fd_ubesdelight_nutritionz");
        }

        // Dehydration Compat
        if (Services.PLATFORM.isModLoaded("dehydration") && ConfigInit.CONFIG.misc.dehydrationDefaultCompat) {
            packs.add("dehydration_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("dehydration") && Services.PLATFORM.isModLoaded("meadow") && ConfigInit.CONFIG.misc.dehydrationmeadowDefaultCompat) {
            packs.add("dehydration_meadow_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("dehydration") && Services.PLATFORM.isModLoaded("vinery") && ConfigInit.CONFIG.misc.dehydrationvineryDefaultCompat) {
            packs.add("dehydration_vinery_nutritionz");
        }

        // MISC Compat
        if (Services.PLATFORM.isModLoaded("adventurez") && ConfigInit.CONFIG.misc.adventurezDefaultCompat) {
            packs.add("adventurez_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("betterend") && ConfigInit.CONFIG.misc.betterendDefaultCompat) {
            packs.add("betterend_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("betternether") && ConfigInit.CONFIG.misc.betternetherDefaultCompat) {
            packs.add("betternether_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("croptopia") && ConfigInit.CONFIG.misc.croptopiaDefaultCompat) {
            packs.add("croptopia_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("farmz") && ConfigInit.CONFIG.misc.farmzDefaultCompat) {
            packs.add("farmz_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("farmz") && Services.PLATFORM.isModLoaded("farm_and_charm") && ConfigInit.CONFIG.misc.farmzfarmcharmDefaultCompat) {
            packs.add("farmz_farmcharm_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("fishofthieves") && ConfigInit.CONFIG.misc.fishofthievesDefaultCompat) {
            packs.add("fishofthieves_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("livingthings") && ConfigInit.CONFIG.misc.livingthingsDefaultCompat) {
            packs.add("livingthings_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("natures_spirit") && ConfigInit.CONFIG.misc.naturesspiritDefaultCompat) {
            packs.add("naturesspirit_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("rosegoldequipment") && ConfigInit.CONFIG.misc.rosegoldequipmentDefaultCompat) {
            packs.add("rosegoldequipment_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("the_bumblezone") && ConfigInit.CONFIG.misc.thebumblezoneDefaultCompat) {
            packs.add("thebumblezone_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("tide") && ConfigInit.CONFIG.misc.tideDefaultCompat) {
            packs.add("tide_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("untamed") && ConfigInit.CONFIG.misc.untamedDefaultCompat) {
            packs.add("untamed_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("wilderwild") && ConfigInit.CONFIG.misc.wilderwildDefaultCompat) {
            packs.add("wilderwild_nutritionz");
        }
        if (Services.PLATFORM.isModLoaded("yungscavebiomes") && ConfigInit.CONFIG.misc.yungsCaveBiomesDefaultCompat) {
            packs.add("yungscavebiomes_nutritionz");
        }
        return packs;
    }
}
