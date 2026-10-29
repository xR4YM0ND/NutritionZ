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
            NutritionServerPacket.writeS2CEffectNutritionPacket(handler.getPlayer());
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
                    NutritionServerPacket.writeS2CItemNutritionPacket(serverPlayerEntity);
                    NutritionServerPacket.writeS2CEffectNutritionPacket(serverPlayerEntity);
                }
            }
        });
        // datapacks
        if (ConfigInit.CONFIG.vanillaMinecraftDefaultDatapack) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "vanilla_minecraft_nutrition_datapack"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
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
        if (FabricLoader.getInstance().isModLoaded("untamed") && ConfigInit.CONFIG.misc.untamedDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "untamed_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("wilderwild") && ConfigInit.CONFIG.misc.wilderwildDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "wilderwild_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
        if (FabricLoader.getInstance().isModLoaded("yungscavebiomes") && ConfigInit.CONFIG.misc.yungsCaveBiomesDefaultCompat) {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("nutritionz", "yungscavebiomes_nutritionz"), FabricLoader.getInstance().getModContainer("nutritionz").orElseThrow(),
                    ResourcePackActivationType.DEFAULT_ENABLED);
        }
    }
}
