package net.nutritionz.network.packet;

import java.util.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NutritionEffectPacket(List<Integer> positiveEffectCount, List<ResourceLocation> positiveEffectIds, List<Integer> positiveEffectDurations, List<Integer> positiveEffectAmplifiers,
                                    List<ResourceLocation> positiveAttributeIds,
                                    List<Float> positiveAttributeValues, List<String> positiveAttributeOperations, List<Integer> negativeEffectCount, List<ResourceLocation> negativeEffectIds,
                                    List<Integer> negativeEffectDurations,
                                    List<Integer> negativeEffectAmplifiers, List<ResourceLocation> negativeAttributeIds, List<Float> negativeAttributeValues,
                                    List<String> negativeAttributeOperations) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NutritionEffectPacket> PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("nutritionz", "nutrition_effect_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionEffectPacket> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> {
        buf.writeCollection(value.positiveEffectCount(), FriendlyByteBuf::writeInt);
        buf.writeCollection(value.positiveEffectIds(), FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(value.positiveEffectDurations(), FriendlyByteBuf::writeInt);
        buf.writeCollection(value.positiveEffectAmplifiers(), FriendlyByteBuf::writeInt);
        buf.writeCollection(value.positiveAttributeIds(), FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(value.positiveAttributeValues(), FriendlyByteBuf::writeFloat);
        buf.writeCollection(value.positiveAttributeOperations(), FriendlyByteBuf::writeUtf);
        buf.writeCollection(value.negativeEffectCount(), FriendlyByteBuf::writeInt);
        buf.writeCollection(value.negativeEffectIds(), FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(value.negativeEffectDurations(), FriendlyByteBuf::writeInt);
        buf.writeCollection(value.negativeEffectAmplifiers(), FriendlyByteBuf::writeInt);
        buf.writeCollection(value.negativeAttributeIds(), FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(value.negativeAttributeValues(), FriendlyByteBuf::writeFloat);
        buf.writeCollection(value.negativeAttributeOperations(), FriendlyByteBuf::writeUtf);
    }, buf -> new NutritionEffectPacket(buf.readList(FriendlyByteBuf::readInt), buf.readList(FriendlyByteBuf::readResourceLocation), buf.readList(FriendlyByteBuf::readInt), buf.readList(FriendlyByteBuf::readInt),
            buf.readList(FriendlyByteBuf::readResourceLocation), buf.readList(FriendlyByteBuf::readFloat), buf.readList(FriendlyByteBuf::readUtf),
            buf.readList(FriendlyByteBuf::readInt), buf.readList(FriendlyByteBuf::readResourceLocation), buf.readList(FriendlyByteBuf::readInt), buf.readList(FriendlyByteBuf::readInt),
            buf.readList(FriendlyByteBuf::readResourceLocation), buf.readList(FriendlyByteBuf::readFloat), buf.readList(FriendlyByteBuf::readUtf)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}


