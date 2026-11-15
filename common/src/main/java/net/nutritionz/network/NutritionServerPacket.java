package net.nutritionz.network;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.api.NutritionType;
import net.nutritionz.network.packet.NutritionEffectPacket;
import net.nutritionz.network.packet.NutritionItemPacket;
import net.nutritionz.network.packet.NutritionPacket;
import net.nutritionz.platform.Services;

public class NutritionServerPacket {

    public static void handleSyncPacket(ServerPlayer serverPlayerEntity) {
        writeS2CNutritionPacket(serverPlayerEntity, (HungerManagerAccess) serverPlayerEntity.getFoodData());
        writeS2CEffectNutritionPacket(serverPlayerEntity);
    }

    public static void writeS2CNutritionPacket(ServerPlayer serverPlayerEntity, HungerManagerAccess hungerManagerAccess) {
        if (!Services.network().canSendToPlayer(serverPlayerEntity, NutritionPacket.PACKET_ID)) {
            return;
        }
        Services.network().sendToPlayer(serverPlayerEntity, new NutritionPacket(hungerManagerAccess.getNutritionLevel(0), hungerManagerAccess.getNutritionLevel(1), hungerManagerAccess.getNutritionLevel(2), hungerManagerAccess.getNutritionLevel(3), hungerManagerAccess.getNutritionLevel(4)));
    }

    public static void writeS2CItemNutritionPacket(ServerPlayer serverPlayerEntity) {
        if (!Services.network().canSendToPlayer(serverPlayerEntity, NutritionItemPacket.PACKET_ID)) {
            return;
        }
        List<Integer> itemIds = new ArrayList<>();
        List<Integer> nutritionValues = new ArrayList<>();
        NutritionMain.NUTRITION_ITEM_MAP.forEach((item, list) -> {
            itemIds.add(BuiltInRegistries.ITEM.getId(item));
            nutritionValues.addAll(list);
        });

        Services.network().sendToPlayer(serverPlayerEntity, new NutritionItemPacket(itemIds, nutritionValues));
    }

    public static void writeS2CEffectNutritionPacket(ServerPlayer serverPlayerEntity) {
        if (!Services.network().canSendToPlayer(serverPlayerEntity, NutritionEffectPacket.PACKET_ID)) {
            return;
        }
        List<Integer> positiveEffectCount = new ArrayList<>();
        List<Identifier> positiveEffectIds = new ArrayList<>();
        List<Integer> positiveEffectDurations = new ArrayList<>();
        List<Integer> positiveEffectAmplifiers = new ArrayList<>();
        List<Identifier> positiveAttributeIds = new ArrayList<>();
        List<Float> positiveAttributeValues = new ArrayList<>();
        List<String> positiveAttributeOperations = new ArrayList<>();

        for (int i = 0; i < NutritionType.values().length; i++) {
            int effectCount = 0;
            int attributeCount = 0;
            for (Object object : NutritionMain.NUTRITION_POSITIVE_EFFECTS.getOrDefault(i, List.of())) {
                if (object instanceof MobEffectInstance statusEffectInstance) {
                    positiveEffectIds.add(BuiltInRegistries.MOB_EFFECT.getKey(statusEffectInstance.getEffect().value()));
                    positiveEffectDurations.add(statusEffectInstance.getDuration());
                    positiveEffectAmplifiers.add(statusEffectInstance.getAmplifier());
                    effectCount++;
                } else {
                    Multimap<Holder<Attribute>, AttributeModifier> multimap = (Multimap<Holder<Attribute>, AttributeModifier>) object;
                    multimap.forEach((attribute, modifier) -> {
                        positiveAttributeIds.add(BuiltInRegistries.ATTRIBUTE.getKey(attribute.value()));
                        positiveAttributeValues.add((float) modifier.amount());
                        positiveAttributeOperations.add(modifier.operation().name());
                    });
                    attributeCount += multimap.size();
                }
            }
            positiveEffectCount.add(effectCount);
            positiveEffectCount.add(attributeCount);
        }

        List<Integer> negativeEffectCount = new ArrayList<>();
        List<Identifier> negativeEffectIds = new ArrayList<>();
        List<Integer> negativeEffectDurations = new ArrayList<>();
        List<Integer> negativeEffectAmplifiers = new ArrayList<>();
        List<Identifier> negativeAttributeIds = new ArrayList<>();
        List<Float> negativeAttributeValues = new ArrayList<>();
        List<String> negativeAttributeOperations = new ArrayList<>();

        for (int i = 0; i < NutritionType.values().length; i++) {
            int effectCount = 0;
            int attributeCount = 0;
            for (Object object : NutritionMain.NUTRITION_NEGATIVE_EFFECTS.getOrDefault(i, List.of())) {
                if (object instanceof MobEffectInstance statusEffectInstance) {
                    negativeEffectIds.add(BuiltInRegistries.MOB_EFFECT.getKey(statusEffectInstance.getEffect().value()));
                    negativeEffectDurations.add(statusEffectInstance.getDuration());
                    negativeEffectAmplifiers.add(statusEffectInstance.getAmplifier());
                    effectCount++;
                } else {
                    Multimap<Holder<Attribute>, AttributeModifier> multimap = (Multimap<Holder<Attribute>, AttributeModifier>) object;
                    multimap.forEach((attribute, modifier) -> {
                        negativeAttributeIds.add(BuiltInRegistries.ATTRIBUTE.getKey(attribute.value()));
                        negativeAttributeValues.add((float) modifier.amount());
                        negativeAttributeOperations.add(modifier.operation().name());
                    });
                    attributeCount += multimap.size();
                }
            }
            negativeEffectCount.add(effectCount);
            negativeEffectCount.add(attributeCount);
        }

        Services.network().sendToPlayer(serverPlayerEntity, new NutritionEffectPacket(positiveEffectCount, positiveEffectIds, positiveEffectDurations, positiveEffectAmplifiers, positiveAttributeIds, positiveAttributeValues, positiveAttributeOperations, negativeEffectCount, negativeEffectIds, negativeEffectDurations, negativeEffectAmplifiers, negativeAttributeIds, negativeAttributeValues, negativeAttributeOperations));
    }

}
