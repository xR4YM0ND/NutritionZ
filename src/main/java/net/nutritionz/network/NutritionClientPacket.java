package net.nutritionz.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;

@Environment(EnvType.CLIENT)
public class NutritionClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(NutritionServerPacket.NUTRITION_SYNC_PACKET, (client, handler, buf, sender) -> {
            int carbohydrateLevel = buf.readInt();
            int proteinLevel = buf.readInt();
            int fatLevel = buf.readInt();
            int vitaminLevel = buf.readInt();

            client.execute(() -> {
                ((HungerManagerAccess) client.player.getHungerManager()).addNutritionLevel(0, carbohydrateLevel);
                ((HungerManagerAccess) client.player.getHungerManager()).addNutritionLevel(1, proteinLevel);
                ((HungerManagerAccess) client.player.getHungerManager()).addNutritionLevel(2, fatLevel);
                ((HungerManagerAccess) client.player.getHungerManager()).addNutritionLevel(3, vitaminLevel);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NutritionServerPacket.ITEM_NUTRITION_PACKET, (client, handler, buf, sender) -> {
            List<Integer> list = new ArrayList<Integer>();
            while (buf.isReadable()) {
                list.add(buf.readInt());
            }
            client.execute(() -> {
                NutritionMain.NUTRITION_ITEM_MAP.clear();
                for (int i = 0; i < list.size(); i += 5) {
                    List<Integer> nutritionList = new ArrayList<Integer>();
                    nutritionList.add(list.get(i + 1));
                    nutritionList.add(list.get(i + 2));
                    nutritionList.add(list.get(i + 3));
                    nutritionList.add(list.get(i + 4));
                    NutritionMain.NUTRITION_ITEM_MAP.put(Registries.ITEM.get(list.get(i)), nutritionList);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NutritionServerPacket.EFFECT_NUTRITION_PACKET, (client, handler, buf, sender) -> {

            List<HashMap<Integer, List<Object>>> list = new ArrayList<HashMap<Integer, List<Object>>>();

            for (int i = 0; i < 2; i++) {
                HashMap<Integer, List<Object>> map = new HashMap<Integer, List<Object>>();
                int size = buf.readInt();

                for (int u = 0; u < size; u++) {
                    List<Object> objectList = new ArrayList<Object>();
                    int nutritionKey = buf.readInt();
                    int nutritionSize = buf.readInt();

                    for (int o = 0; o < nutritionSize; o++) {
                        if (buf.readBoolean()) {
                            Identifier identifier = buf.readIdentifier();
                            int duration = buf.readInt();
                            int amplifier = buf.readInt();
                            objectList.add(new StatusEffectInstance(Registries.STATUS_EFFECT.get(identifier), duration, amplifier, false, false, true));
                        } else {
                            Identifier identifier = buf.readIdentifier();
                            // String name = buf.readString();
                            float value = buf.readFloat();
                            String operation = buf.readString();
                            Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                            attributeModifiers.put(Registries.ATTRIBUTE.get(identifier),
                                    new EntityAttributeModifier(Registries.ATTRIBUTE.get(identifier).getTranslationKey(), value, Operation.valueOf(operation.toUpperCase())));
                            objectList.add(attributeModifiers);
                        }

                    }
                    map.put(nutritionKey, objectList);
                }
                list.add(map);
            }

            client.execute(() -> {
                NutritionMain.NUTRITION_POSITIVE_EFFECTS.clear();
                NutritionMain.NUTRITION_NEGATIVE_EFFECTS.clear();
                Iterator<HashMap<Integer, List<Object>>> iterator = list.iterator();
                boolean positive = true;
                while (iterator.hasNext()) {
                    HashMap<Integer, List<Object>> map = iterator.next();
                    if (positive) {
                        map.forEach((nutritionKey, effectList) -> {
                            NutritionMain.NUTRITION_POSITIVE_EFFECTS.put(nutritionKey, effectList);
                        });
                    } else {
                        map.forEach((nutritionKey, effectList) -> {
                            NutritionMain.NUTRITION_NEGATIVE_EFFECTS.put(nutritionKey, effectList);
                        });
                    }
                    positive = false;
                }
            });
        });
    }

    public static void writeC2SNutritionPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(NutritionServerPacket.SEND_NUTRITION_PACKET, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
    }
}
