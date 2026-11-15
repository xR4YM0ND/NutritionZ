package net.nutritionz.network.packet;

import java.util.List;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record NutritionItemPacket(List<Integer> itemIds, List<Integer> nutritionValues) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NutritionItemPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nutritionz", "nutrition_item_packet"));

    private static final StreamCodec<ByteBuf, List<Integer>> INT_LIST = ByteBufCodecs.INT.apply(ByteBufCodecs.list());

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionItemPacket> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> {
        INT_LIST.encode(buf, value.itemIds);
        INT_LIST.encode(buf, value.nutritionValues);
    }, buf -> new NutritionItemPacket(INT_LIST.decode(buf), INT_LIST.decode(buf)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}


