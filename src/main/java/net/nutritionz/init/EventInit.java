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
            NutritionServerPacket.writeS2CItemNutritionPacket(handler);
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
        // DATAPACKS
        //
        // [Lets Do Series]
        if (FabricLoader.getInstance().isModLoaded("bakery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "bakery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("beachparty")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "beachparty_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("brewery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "brewery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("candlelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "candlelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("meadow")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "dehydration_x_meadow_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("nethervinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "dehydration_x_nethervinery_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "dehydration_x_vinery_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("herbalbrews")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "herbalbrews_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("meadow")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "meadow_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("nethervinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "nethervinery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "vinery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        // Farmers Delight Series
        if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "brewinandchewin_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("culturaldelights")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "culturaldelights_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("ends_delight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "endsdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("expandeddelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "expandeddelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "farmersdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("festive_delight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "festivedelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("moredelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "moredelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("natures_delight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "natures_delight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("nethersdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "nethersdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("oceansdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "oceansdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("ubesdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "ubesdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        // Create Series
        if (FabricLoader.getInstance().isModLoaded("create")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "create_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("createaddition")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "createaddition_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        // Others
        if (FabricLoader.getInstance().isModLoaded("ad_astra")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "adastra_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("adventurez")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "adventurez_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("aquamirae")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "aquamirae_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betterend")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "betterend_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betternether")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "betternether_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("biomemakeover")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "biomemakeover_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("bountifulfares")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "bountifulfares_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("culinaire")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "culinaire_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "dehydration_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("exlinefishing")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "exlinefishing_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("fishofthieves")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "fishofthieves_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("hybrid-aquatic")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "hybrid-aquatic_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("immersive_weathering")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "immersiveweathering_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("livingthings")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "livingthings_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("mobcatalog")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "mobcatalog_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("naturalist")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "naturalist_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("natures_spirit")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "natures_spirit_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("promenade")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "promenade_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("regions_unexplored")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "regions_unexplored_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("spawn")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "spawn_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("spelunkery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "spelunkery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("supplementaries")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "supplementaries_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("the_bumblezone")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "thebumblezone_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("nutritionz", "wilderwild_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }

}
