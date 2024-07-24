package boxesbreakbacks.mixin;

import boxesbreakbacks.mixininterface.PayloadCompatibleEntityTracker;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkLoadingManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

@Mixin(ServerChunkLoadingManager.EntityTracker.class)
public class EntityTrackerMixin implements PayloadCompatibleEntityTracker {
    @Shadow
    @Final
    Entity entity;
    @Shadow
    @Final
    private Set<PlayerAssociatedNetworkHandler> listeners;
    @Unique
    public <T extends CustomPayload> void boxesBreakBacks$sendToOtherNearbyPlayers(T payload) {
        this.listeners.iterator();
        for (var handler : this.listeners)
            ServerPlayNetworking.send(handler.getPlayer(), payload);
    }

    @Unique
    public <T extends CustomPayload> void boxesBreakBacks$sendToNearbyPlayers(T payload) {
        this.boxesBreakBacks$sendToOtherNearbyPlayers(payload);
        if (this.entity instanceof ServerPlayerEntity spe) {
            ServerPlayNetworking.send(spe, payload);
        }

    }
}
