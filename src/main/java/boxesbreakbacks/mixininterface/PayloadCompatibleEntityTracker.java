package boxesbreakbacks.mixininterface;

import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Unique;

public interface PayloadCompatibleEntityTracker {
    public <T extends CustomPayload> void boxesBreakBacks$sendToOtherNearbyPlayers(T payload);

    @Unique
    public <T extends CustomPayload> void boxesBreakBacks$sendToNearbyPlayers(T payload);
}
