package net.nutritionz.network;

import java.util.*;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.api.NutritionType;
import net.nutritionz.data.NutritionLoader;
import net.nutritionz.network.packet.NutritionEffectPacket;
import net.nutritionz.network.packet.NutritionItemPacket;
import net.nutritionz.network.packet.NutritionPacket;
import net.nutritionz.network.packet.NutritionSyncPacket;

@Environment(EnvType.CLIENT)
public class NutritionClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(NutritionPacket.PACKET_ID, (payload, context) -> {
            int carbohydrateLevel = payload.carbohydrateLevel();
            int proteinLevel = payload.proteinLevel();
            int fatLevel = payload.fatLevel();
            int vitaminLevel = payload.vitaminLevel();
            int mineralLevel = payload.mineralLevel();

            context.client().execute(() -> {
                ((HungerManagerAccess) context.player().getFoodData()).setNutritionLevel(0, carbohydrateLevel);
                ((HungerManagerAccess) context.player().getFoodData()).setNutritionLevel(1, proteinLevel);
                ((HungerManagerAccess) context.player().getFoodData()).setNutritionLevel(2, fatLevel);
                ((HungerManagerAccess) context.player().getFoodData()).setNutritionLevel(3, vitaminLevel);
                ((HungerManagerAccess) context.player().getFoodData()).setNutritionLevel(4, mineralLevel);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NutritionItemPacket.PACKET_ID, (payload, context) -> {
            List<Integer> itemIds = payload.itemIds();
            List<Integer> nutritionValues = payload.nutritionValues();

            context.client().execute(() -> {
                if (context.client().hasSingleplayerServer()) {
                    return;
                }
                NutritionMain.NUTRITION_ITEM_MAP.clear();
                for (int i = 0; i < itemIds.size(); i++) {
                    List<Integer> nutritionList = new ArrayList<Integer>();
                    nutritionList.add(nutritionValues.get(i * 5));
                    nutritionList.add(nutritionValues.get(i * 5 + 1));
                    nutritionList.add(nutritionValues.get(i * 5 + 2));
                    nutritionList.add(nutritionValues.get(i * 5 + 3));
                    nutritionList.add(nutritionValues.get(i * 5 + 4));
                    NutritionMain.NUTRITION_ITEM_MAP.put(BuiltInRegistries.ITEM.byId(itemIds.get(i)), nutritionList);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NutritionEffectPacket.PACKET_ID, (payload, context) -> {

            List<Integer> positiveEffectCount = payload.positiveEffectCount();
            List<ResourceLocation> positiveEffectIds = payload.positiveEffectIds();
            List<Integer> positiveEffectDurations = payload.positiveEffectDurations();
            List<Integer> positiveEffectAmplifiers = payload.positiveEffectAmplifiers();
            List<ResourceLocation> positiveAttributeIds = payload.positiveAttributeIds();
            List<Float> positiveAttributeValues = payload.positiveAttributeValues();
            List<String> positiveAttributeOperations = payload.positiveAttributeOperations();

            List<Integer> negativeEffectCount = payload.negativeEffectCount();
            List<ResourceLocation> negativeEffectIds = payload.negativeEffectIds();
            List<Integer> negativeEffectDurations = payload.negativeEffectDurations();
            List<Integer> negativeEffectAmplifiers = payload.negativeEffectAmplifiers();
            List<ResourceLocation> negativeAttributeIds = payload.negativeAttributeIds();
            List<Float> negativeAttributeValues = payload.negativeAttributeValues();
            List<String> negativeAttributeOperations = payload.negativeAttributeOperations();

            HashMap<Integer, List<Object>> positiveEffectMap = new HashMap<>();

            int effectCount = 0;
            int attributeCount = 0;
            for (int i = 0; i < positiveEffectCount.size() / 2; i++) {
                List<Object> list = new ArrayList<>();

                for (int u = 0; u < positiveEffectCount.get(i * 2); u++) {
                    if (BuiltInRegistries.MOB_EFFECT.get(positiveEffectIds.get(effectCount)) != null) {
                        list.add(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.getHolder(positiveEffectIds.get(effectCount)).get(), positiveEffectDurations.get(effectCount), positiveEffectAmplifiers.get(effectCount), false, false, true));
                    }
                    effectCount++;
                }

                for (int u = 0; u < positiveEffectCount.get(i * 2 + 1); u++) {
                    if (BuiltInRegistries.ATTRIBUTE.get(positiveAttributeIds.get(attributeCount)) != null) {
                        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                        attributeModifiers.put(BuiltInRegistries.ATTRIBUTE.getHolder(positiveAttributeIds.get(attributeCount)).get(),
                                new AttributeModifier(NutritionLoader.getModifierId(NutritionType.byIndex(i), true, positiveAttributeIds.get(attributeCount)), positiveAttributeValues.get(attributeCount), Operation.valueOf(positiveAttributeOperations.get(attributeCount).toUpperCase())));
                        list.add(attributeModifiers);
                    }
                    attributeCount++;
                }

                positiveEffectMap.put(i, list);
            }

            HashMap<Integer, List<Object>> negativeEffectMap = new HashMap<>();

            effectCount = 0;
            attributeCount = 0;
            for (int i = 0; i < negativeEffectCount.size() / 2; i++) {
                List<Object> list = new ArrayList<>();

                for (int u = 0; u < negativeEffectCount.get(i * 2); u++) {
                    if (BuiltInRegistries.MOB_EFFECT.get(negativeEffectIds.get(effectCount)) != null) {
                        list.add(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.getHolder(negativeEffectIds.get(effectCount)).get(), negativeEffectDurations.get(effectCount), negativeEffectAmplifiers.get(effectCount), false, false, true));
                    }
                    effectCount++;
                }

                for (int u = 0; u < negativeEffectCount.get(i * 2 + 1); u++) {
                    if (BuiltInRegistries.ATTRIBUTE.get(negativeAttributeIds.get(attributeCount)) != null) {
                        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                        attributeModifiers.put(BuiltInRegistries.ATTRIBUTE.getHolder(negativeAttributeIds.get(attributeCount)).get(),
                                new AttributeModifier(NutritionLoader.getModifierId(NutritionType.byIndex(i), false, negativeAttributeIds.get(attributeCount)), negativeAttributeValues.get(attributeCount), AttributeModifier.Operation.valueOf(negativeAttributeOperations.get(attributeCount).toUpperCase())));
                        list.add(attributeModifiers);
                    }
                    attributeCount++;
                }

                negativeEffectMap.put(i, list);
            }

            context.client().execute(() -> {
                if (context.client().hasSingleplayerServer()) {
                    return;
                }
                NutritionMain.NUTRITION_POSITIVE_EFFECTS.clear();
                NutritionMain.NUTRITION_NEGATIVE_EFFECTS.clear();
                positiveEffectMap.forEach(NutritionMain.NUTRITION_POSITIVE_EFFECTS::put);
                negativeEffectMap.forEach(NutritionMain.NUTRITION_NEGATIVE_EFFECTS::put);
            });
        });
    }

    public static void writeC2SNutritionPacket() {
        ClientPlayNetworking.send(new NutritionSyncPacket());
    }
}
