package boxesbreakbacks.mixin;

import boxesbreakbacks.mixininterface.PayloadCompatibleEntityTracker;
import boxesbreakbacks.mixininterface.PayloadCompatibleServerChunkLoadingManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkLoadingManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerChunkLoadingManager.class)
public class ServerChunkLoadingManagerMixin implements PayloadCompatibleServerChunkLoadingManager {
    @Final
    @Shadow
    private Int2ObjectMap<ServerChunkLoadingManager.EntityTracker> entityTrackers;
    @SuppressWarnings("RedundantCast")
    public <T extends CustomPayload> void boxesBreakBacks$sendToNearbyPlayers(ServerPlayerEntity player, T payload) {
        ((PayloadCompatibleEntityTracker) entityTrackers.get(player.getId())).boxesBreakBacks$sendToNearbyPlayers(payload);
    }
}
