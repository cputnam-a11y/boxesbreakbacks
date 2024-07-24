package boxesbreakbacks.network.client;

import boxesbreakbacks.component.ModDataComponentTypes;
import boxesbreakbacks.component.ShulkerAccessoryAnimationDataComponent;
import boxesbreakbacks.network.ShulkerStateChangePayload;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class ModNetworkHandler {
    static {
        ClientPlayNetworking.registerGlobalReceiver(ShulkerStateChangePayload.ID,
                (payload, context) -> context.client().execute(() -> handleShulkerStateChange(payload, context)));
    }
    @SuppressWarnings("EmptyMethod")
    public static void init() {

    }
    private static void handleShulkerStateChange(ShulkerStateChangePayload payload, ClientPlayNetworking.Context context) {
        if (context.client().world == null)
            return;
        LivingEntity entity = payload.getEntity(Objects.requireNonNull(context.client().world));
        AccessoriesContainer container =  Objects.requireNonNull(entity.accessoriesCapability()).getContainers().get("back");
        for (var pair : container.getAccessories()) {
            int slot = pair.getFirst();
            ItemStack stack = pair.getSecond();
            if (slot == payload.getSlotNumber())
                stack.apply(ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA,
                        new ShulkerAccessoryAnimationDataComponent(stack),
                        component -> component.setAnimationStage(payload.getAnimationStage())
                );
            Objects.requireNonNull(entity.accessoriesCapability()).updateContainers();
        }
    }
}
