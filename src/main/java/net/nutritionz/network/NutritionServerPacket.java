package net.nutritionz.network;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;

public class NutritionServerPacket {

    public static final Identifier NUTRITION_SYNC_PACKET = new Identifier("nutritionz", "nutrition_sync_packet");
    public static final Identifier SEND_NUTRITION_PACKET = new Identifier("nutritionz", "send_nutrition_packet");

    public static final Identifier ITEM_NUTRITION_PACKET = new Identifier("nutritionz", "item_nutrition_packet");
    public static final Identifier EFFECT_NUTRITION_PACKET = new Identifier("nutritionz", "effect_nutrition_packet");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SEND_NUTRITION_PACKET, (server, player, handler, buffer, sender) -> {
            server.execute(() -> {
                writeS2CNutritionPacket(player, ((HungerManagerAccess) player.getHungerManager()));
                writeS2CEffectNutritionPacket(handler);
            });
        });
    }

    public static void writeS2CNutritionPacket(ServerPlayerEntity serverPlayerEntity, HungerManagerAccess hungerManagerAccess) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(hungerManagerAccess.getNutritionLevel(0));
        buf.writeInt(hungerManagerAccess.getNutritionLevel(1));
        buf.writeInt(hungerManagerAccess.getNutritionLevel(2));
        buf.writeInt(hungerManagerAccess.getNutritionLevel(3));
        buf.writeInt(hungerManagerAccess.getNutritionLevel(4));
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(NUTRITION_SYNC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

    public static void writeS2CItemNutritionPacket(ServerPlayNetworkHandler networkHandler) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        NutritionMain.NUTRITION_ITEM_MAP.forEach((item, list) -> {
            buf.writeInt(Registries.ITEM.getRawId(item));
            for (int i = 0; i < list.size(); i++) {
                buf.writeInt(list.get(i));
            }
        });
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(ITEM_NUTRITION_PACKET, buf);
        networkHandler.sendPacket(packet);
    }

    public static void writeS2CEffectNutritionPacket(ServerPlayNetworkHandler networkHandler) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        processEffects(buf, NutritionMain.NUTRITION_POSITIVE_EFFECTS.entrySet().iterator(), NutritionMain.NUTRITION_POSITIVE_EFFECTS.size());
        processEffects(buf, NutritionMain.NUTRITION_NEGATIVE_EFFECTS.entrySet().iterator(), NutritionMain.NUTRITION_NEGATIVE_EFFECTS.size());
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(EFFECT_NUTRITION_PACKET, buf);
        networkHandler.sendPacket(packet);
    }

    @SuppressWarnings("unchecked")
    private static void processEffects(PacketByteBuf buf, Iterator<Map.Entry<Integer, List<Object>>> iterator, int size) {
        buf.writeInt(size);
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<Object>> entry = iterator.next();
            buf.writeInt(entry.getKey());
            buf.writeInt(entry.getValue().size());
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (entry.getValue().get(i) instanceof StatusEffectInstance) {
                    buf.writeBoolean(true);
                    StatusEffectInstance statusEffectInstance = (StatusEffectInstance) entry.getValue().get(i);
                    buf.writeIdentifier(Registries.STATUS_EFFECT.getId(statusEffectInstance.getEffectType()));
                    buf.writeInt(statusEffectInstance.getDuration());
                    buf.writeInt(statusEffectInstance.getAmplifier());
                } else {
                    buf.writeBoolean(false);
                    Multimap<EntityAttribute, EntityAttributeModifier> multimap = (Multimap<EntityAttribute, EntityAttributeModifier>) entry.getValue().get(i);
                    multimap.forEach((attribute, modifier) -> {
                        buf.writeIdentifier(Registries.ATTRIBUTE.getId(attribute));
                        buf.writeUuid(modifier.getId());
                        buf.writeFloat((float) modifier.getValue());
                        buf.writeString(modifier.getOperation().name());
                        return;
                    });
                }
            }
        }
    }

}
