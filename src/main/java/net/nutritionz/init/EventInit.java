package net.nutritionz.init;

import net.dehydration.api.DrinkEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.network.NutritionServerPacket;

public class EventInit {

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            NutritionServerPacket.writeS2CItemNutritionPacket(handler.getPlayer());
        });
        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
            DrinkEvent.EVENT.register((ItemStack stack, PlayerEntity player) -> {
                if (!player.getWorld().isClient()) {
                    Item item = stack.getItem();
                    if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(item)) {
                        for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(item).size(); i++) {
                            if (NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i) > 0) {
                                ((HungerManagerAccess) player.getHungerManager()).addNutritionLevel(i, NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i));
                            }
                        }
                    }
                }
            });
        }

        // Main Compat
        if (ConfigInit.CONFIG.minecraftDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "minecraft_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        
        // Lets Do Compat
        if (FabricLoader.getInstance().isModLoaded("bakery") && ConfigInit.CONFIG.letsdo.letsdoBakeryDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_bakery_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("beachparty") && ConfigInit.CONFIG.letsdo.letsdoBeachpartyDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_beachparty_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("brewery") && ConfigInit.CONFIG.letsdo.letsdoBreweryDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_brewery_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("candlelight") && ConfigInit.CONFIG.letsdo.letsdoCandlelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_candlelight_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("cornexpansion") && ConfigInit.CONFIG.letsdo.letsdoCornexpansionDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_cornexpansion_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farm_and_charm") && ConfigInit.CONFIG.letsdo.letsdoFarmcharmDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_farmcharm_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("herbalbrews") && ConfigInit.CONFIG.letsdo.letsdoHerbalbrewsDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_herbalbrews_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("lilis_lucky_lures") && ConfigInit.CONFIG.letsdo.letsdoLllDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_lll_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("meadow") && ConfigInit.CONFIG.letsdo.letsdoMeadowDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_meadow_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("vinery") && ConfigInit.CONFIG.letsdo.letsdoVineryDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_vinery_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("wildernature") && ConfigInit.CONFIG.letsdo.letsdoWildernatureDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "letsdo_wildernature_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }

        // Farmers Delight Compat
        if (FabricLoader.getInstance().isModLoaded("farmersdelight") && ConfigInit.CONFIG.fd.fdFarmersdelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "fd_farmersdelight_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("moredelight") && ConfigInit.CONFIG.fd.fdMoredelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "fd_moredelight_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("natures_delight") && FabricLoader.getInstance().isModLoaded("natures_spirit") && ConfigInit.CONFIG.fd.fdNaturesdelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "fd_ns_naturesdelight_nutritionz"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("oceansdelight") && ConfigInit.CONFIG.fd.fdOceansdelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "fd_oceansdelight_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("ubesdelight") && ConfigInit.CONFIG.fd.fdUbesdelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "fd_ubesdelight_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }

        // Dehydration Compat
        if (FabricLoader.getInstance().isModLoaded("dehydration") && ConfigInit.CONFIG.misc.dehydrationDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("meadow") && ConfigInit.CONFIG.misc.dehydrationmeadowDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_meadow_nutritionz"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("vinery") && ConfigInit.CONFIG.misc.dehydrationvineryDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_vinery_nutritionz"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }

        // MISC Compat
        if (FabricLoader.getInstance().isModLoaded("adventurez") && ConfigInit.CONFIG.misc.adventurezDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "adventurez_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betterend") && ConfigInit.CONFIG.misc.betterendDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "betterend_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betternether") && ConfigInit.CONFIG.misc.betternetherDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "betternether_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("croptopia") && ConfigInit.CONFIG.misc.croptopiaDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "croptopia_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farmz") && ConfigInit.CONFIG.misc.farmzDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "farmz_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("fishofthieves") && ConfigInit.CONFIG.misc.fishofthievesDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "fishofthieves_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("livingthings") && ConfigInit.CONFIG.misc.livingthingsDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "livingthings_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("natures_spirit") && ConfigInit.CONFIG.misc.naturesspiritDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "naturesspirit_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("rosegoldequipment") && ConfigInit.CONFIG.misc.rosegoldequipmentDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "rosegoldequipment_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("the_bumblezone") && ConfigInit.CONFIG.misc.thebumblezoneDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "thebumblezone_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("tide") && ConfigInit.CONFIG.misc.tideDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "tide_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("wilderwild") && ConfigInit.CONFIG.misc.wilderwildDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "wilderwild_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }
}
