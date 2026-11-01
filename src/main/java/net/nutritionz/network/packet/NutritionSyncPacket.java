package net.nutritionz.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NutritionSyncPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NutritionSyncPacket> PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("nutritionz", "nutrition_sync_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionSyncPacket> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> {
    }, buf -> new NutritionSyncPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}


