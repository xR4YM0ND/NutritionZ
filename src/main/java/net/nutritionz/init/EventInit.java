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
        // datapacks
        if (FabricLoader.getInstance().isModLoaded("adventurez")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "adventurez_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("bakery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "bakery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("beachparty")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "beachparty_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betterend")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "betterend_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betternether")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "betternether_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("brewery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "brewery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("candlelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "candlelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("meadow")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_x_meadow_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("nethervinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_x_nethervinery_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "dehydration_x_vinery_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(), ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("expandeddelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "expandeddelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "farmersdelight_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("herbalbrews")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "herbalbrews_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("hybrid-aquatic")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "hybrid-aquatic_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("meadow")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "meadow_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("mobcatalog")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "mobcatalog_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("naturalist")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "naturalist_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("nethervinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "nethervinery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("regions_unexplored")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "regions_unexplored_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("supplementaries")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "supplementaries_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("vinery")) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "vinery_nutrition_compat"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }

}
