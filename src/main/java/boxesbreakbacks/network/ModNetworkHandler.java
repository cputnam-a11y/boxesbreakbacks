package boxesbreakbacks.network;

import boxesbreakbacks.inventory.ShulkerBoxPortableScreenHandler;
import boxesbreakbacks.tag.ModTags;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.List;

public class ModNetworkHandler {
    static {
        PayloadTypeRegistry.playC2S().register(OpenBackBoxPayload.ID, OpenBackBoxPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ShulkerStateChangePayload.ID, ShulkerStateChangePayload.CODEC);
    }
    static {
        ServerPlayNetworking.registerGlobalReceiver(OpenBackBoxPayload.ID, (payload, context) -> {
            AccessoriesCapability cap = AccessoriesCapability.get(context.player());
            if (cap == null)
                return;
            List<SlotEntryReference> slotReferences = cap.getEquipped((s) -> s.isIn(ModTags.BOXES));
            if (slotReferences.isEmpty())
                return;
            ShulkerBoxPortableScreenHandler.open(context.player(), slotReferences.getFirst().stack());
        });
    }
    @SuppressWarnings("EmptyMethod")
    public static void init() {}
}
