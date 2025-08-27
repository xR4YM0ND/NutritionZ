package net.nutritionz.init;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.nutritionz.network.NutritionServerPacket;

public class EventInit {

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            NutritionServerPacket.writeS2CItemNutritionPacket(handler.getPlayer());
        });
        if (FabricLoader.getInstance().isModLoaded("dehydration")) {
            DehydrationEventInit.init();
        }
        // datapacks
        if (ConfigInit.CONFIG.vanillaMinecraftDefaultDatapack) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "vanilla_minecraft_nutrition_datapack"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("adventurez") && ConfigInit.CONFIG.adventurezDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "adventurez_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betterend") && ConfigInit.CONFIG.betterendDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "betterend_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("betternether") && ConfigInit.CONFIG.betternetherDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "betternether_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("cornexpansion") && ConfigInit.CONFIG.cornexpansionDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "cornexpansion_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("croptopia") && ConfigInit.CONFIG.croptopiaDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "croptopia_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && ConfigInit.CONFIG.dehydrationDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "dehydration_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("dehydration") && FabricLoader.getInstance().isModLoaded("vinery")
                && ConfigInit.CONFIG.dehydrationDefaultCompat && ConfigInit.CONFIG.vineryDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "dehydration_x_vinery_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farm_and_charm") && ConfigInit.CONFIG.farmandcharmDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "farm_and_charm_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("farmersdelight") && ConfigInit.CONFIG.farmersdelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "farmersdelight_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("fishofthieves") && ConfigInit.CONFIG.fishofthievesDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "fishofthieves_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("livingthings") && ConfigInit.CONFIG.livingthingsDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "livingthings_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("moredelight") && ConfigInit.CONFIG.moredelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "moredelight_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("natures_spirit") && ConfigInit.CONFIG.naturesspiritDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "natures_spirit_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("oceansdelight") && ConfigInit.CONFIG.oceansdelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "oceansdelight_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("the_bumblezone") && ConfigInit.CONFIG.thebumblezoneDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "the_bumblezone_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("ubesdelight") && ConfigInit.CONFIG.ubesdelightDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "ubesdelight_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("vinery") && ConfigInit.CONFIG.vineryDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "vinery_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("wilderwild") && ConfigInit.CONFIG.wilderwildDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of("nutritionz", "wilderwild_nutrition_compat"),
                    FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }
}
