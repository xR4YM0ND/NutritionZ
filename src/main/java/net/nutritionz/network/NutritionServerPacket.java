package net.nutritionz.network;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
import net.nutritionz.network.packet.NutritionSyncPacket;

public class NutritionServerPacket {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(NutritionPacket.PACKET_ID, NutritionPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(NutritionItemPacket.PACKET_ID, NutritionItemPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(NutritionEffectPacket.PACKET_ID, NutritionEffectPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(NutritionSyncPacket.PACKET_ID, NutritionSyncPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(NutritionSyncPacket.PACKET_ID, (payload, context) -> {
            context.server().execute(() -> {
                writeS2CNutritionPacket(context.player(), ((HungerManagerAccess) context.player().getFoodData()));
                writeS2CEffectNutritionPacket(context.player());
            });
        });
    }

    public static void writeS2CNutritionPacket(ServerPlayer serverPlayerEntity, HungerManagerAccess hungerManagerAccess) {
        ServerPlayNetworking.send(serverPlayerEntity, new NutritionPacket(hungerManagerAccess.getNutritionLevel(0), hungerManagerAccess.getNutritionLevel(1), hungerManagerAccess.getNutritionLevel(2), hungerManagerAccess.getNutritionLevel(3), hungerManagerAccess.getNutritionLevel(4)));
    }

    public static void writeS2CItemNutritionPacket(ServerPlayer serverPlayerEntity) {
        List<Integer> itemIds = new ArrayList<>();
        List<Integer> nutritionValues = new ArrayList<>();
        NutritionMain.NUTRITION_ITEM_MAP.forEach((item, list) -> {
            itemIds.add(BuiltInRegistries.ITEM.getId(item));
            nutritionValues.addAll(list);
        });

        ServerPlayNetworking.send(serverPlayerEntity, new NutritionItemPacket(itemIds, nutritionValues));
    }

    public static void writeS2CEffectNutritionPacket(ServerPlayer serverPlayerEntity) {
        List<Integer> positiveEffectCount = new ArrayList<>();
        List<ResourceLocation> positiveEffectIds = new ArrayList<>();
        List<Integer> positiveEffectDurations = new ArrayList<>();
        List<Integer> positiveEffectAmplifiers = new ArrayList<>();
        List<ResourceLocation> positiveAttributeIds = new ArrayList<>();
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
        List<ResourceLocation> negativeEffectIds = new ArrayList<>();
        List<Integer> negativeEffectDurations = new ArrayList<>();
        List<Integer> negativeEffectAmplifiers = new ArrayList<>();
        List<ResourceLocation> negativeAttributeIds = new ArrayList<>();
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

        ServerPlayNetworking.send(serverPlayerEntity, new NutritionEffectPacket(positiveEffectCount, positiveEffectIds, positiveEffectDurations, positiveEffectAmplifiers, positiveAttributeIds, positiveAttributeValues, positiveAttributeOperations, negativeEffectCount, negativeEffectIds, negativeEffectDurations, negativeEffectAmplifiers, negativeAttributeIds, negativeAttributeValues, negativeAttributeOperations));
    }

}
