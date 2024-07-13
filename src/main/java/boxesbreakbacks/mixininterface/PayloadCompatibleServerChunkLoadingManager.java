package boxesbreakbacks.mixininterface;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PayloadCompatibleServerChunkLoadingManager {
    <T extends CustomPayload> void boxesBreakBacks$sendToNearbyPlayers(ServerPlayerEntity player, T payload);
}
