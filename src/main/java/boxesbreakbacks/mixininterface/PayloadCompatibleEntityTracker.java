package boxesbreakbacks.mixininterface;

import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Unique;

public interface PayloadCompatibleEntityTracker {
    @SuppressWarnings("unused")
    <T extends CustomPayload> void boxesBreakBacks$sendToOtherNearbyPlayers(T payload);

    @Unique
    <T extends CustomPayload> void boxesBreakBacks$sendToNearbyPlayers(T payload);
}
