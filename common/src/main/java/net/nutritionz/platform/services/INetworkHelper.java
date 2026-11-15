package net.nutritionz.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {

    void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);

    void sendToServer(CustomPacketPayload payload);

    boolean canSendToPlayer(ServerPlayer player, CustomPacketPayload.Type<?> type);

    boolean canSendToServer(CustomPacketPayload.Type<?> type);

}
