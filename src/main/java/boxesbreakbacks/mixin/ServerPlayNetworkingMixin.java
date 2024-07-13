package boxesbreakbacks.mixin;

import boxesbreakbacks.BoxesBreakBacks;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworking.class)
public class ServerPlayNetworkingMixin {
    @Inject(method = "send", at = @At(value = "HEAD"))
    private static void onSend(ServerPlayerEntity player, CustomPayload payload, CallbackInfo ci) {
//        BoxesBreakBacks.LOGGER.info("sent: {} to {}", payload.getId(), player.getUuid());
    }
}
