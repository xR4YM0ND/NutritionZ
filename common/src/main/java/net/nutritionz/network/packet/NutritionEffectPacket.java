package net.nutritionz.network.packet;

import java.util.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record NutritionEffectPacket(List<Integer> positiveEffectCount, List<Identifier> positiveEffectIds, List<Integer> positiveEffectDurations, List<Integer> positiveEffectAmplifiers,
                                    List<Identifier> positiveAttributeIds,
                                    List<Float> positiveAttributeValues, List<String> positiveAttributeOperations, List<Integer> negativeEffectCount, List<Identifier> negativeEffectIds,
                                    List<Integer> negativeEffectDurations,
                                    List<Integer> negativeEffectAmplifiers, List<Identifier> negativeAttributeIds, List<Float> negativeAttributeValues,
                                    List<String> negativeAttributeOperations) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NutritionEffectPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nutritionz", "nutrition_effect_packet"));

    private static final StreamCodec<ByteBuf, List<Integer>> INT_LIST = ByteBufCodecs.INT.apply(ByteBufCodecs.list());
    private static final StreamCodec<ByteBuf, List<Identifier>> IDENTIFIER_LIST = Identifier.STREAM_CODEC.apply(ByteBufCodecs.list());
    private static final StreamCodec<ByteBuf, List<Float>> FLOAT_LIST = ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list());
    private static final StreamCodec<ByteBuf, List<String>> STRING_LIST = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list());

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionEffectPacket> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> {
        INT_LIST.encode(buf, value.positiveEffectCount());
        IDENTIFIER_LIST.encode(buf, value.positiveEffectIds());
        INT_LIST.encode(buf, value.positiveEffectDurations());
        INT_LIST.encode(buf, value.positiveEffectAmplifiers());
        IDENTIFIER_LIST.encode(buf, value.positiveAttributeIds());
        FLOAT_LIST.encode(buf, value.positiveAttributeValues());
        STRING_LIST.encode(buf, value.positiveAttributeOperations());
        INT_LIST.encode(buf, value.negativeEffectCount());
        IDENTIFIER_LIST.encode(buf, value.negativeEffectIds());
        INT_LIST.encode(buf, value.negativeEffectDurations());
        INT_LIST.encode(buf, value.negativeEffectAmplifiers());
        IDENTIFIER_LIST.encode(buf, value.negativeAttributeIds());
        FLOAT_LIST.encode(buf, value.negativeAttributeValues());
        STRING_LIST.encode(buf, value.negativeAttributeOperations());
    }, buf -> new NutritionEffectPacket(INT_LIST.decode(buf), IDENTIFIER_LIST.decode(buf), INT_LIST.decode(buf), INT_LIST.decode(buf),
            IDENTIFIER_LIST.decode(buf), FLOAT_LIST.decode(buf), STRING_LIST.decode(buf),
            INT_LIST.decode(buf), IDENTIFIER_LIST.decode(buf), INT_LIST.decode(buf), INT_LIST.decode(buf),
            IDENTIFIER_LIST.decode(buf), FLOAT_LIST.decode(buf), STRING_LIST.decode(buf)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}


