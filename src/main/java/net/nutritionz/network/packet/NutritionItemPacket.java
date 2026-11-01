package net.nutritionz.network.packet;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NutritionItemPacket(List<Integer> itemIds, List<Integer> nutritionValues) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NutritionItemPacket> PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("nutritionz", "nutrition_item_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionItemPacket> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> {
        buf.writeCollection(value.itemIds, FriendlyByteBuf::writeInt);
        buf.writeCollection(value.nutritionValues, FriendlyByteBuf::writeInt);
    }, buf -> new NutritionItemPacket(buf.readList(FriendlyByteBuf::readInt), buf.readList(FriendlyByteBuf::readInt)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}


