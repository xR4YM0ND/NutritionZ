package net.nutritionz.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NutritionPacket(int carbohydrateLevel, int proteinLevel, int fatLevel, int vitaminLevel, int mineralLevel) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NutritionPacket> PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("nutritionz", "nutrition_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionPacket> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> {
        buf.writeInt(value.carbohydrateLevel());
        buf.writeInt(value.proteinLevel());
        buf.writeInt(value.fatLevel());
        buf.writeInt(value.vitaminLevel());
        buf.writeInt(value.mineralLevel());
    }, buf -> new NutritionPacket(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}


