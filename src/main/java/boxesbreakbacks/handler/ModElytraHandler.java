package boxesbreakbacks.handler;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class ModElytraHandler {
    static {
        EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
            if (entity.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA))
                return false;
            AccessoriesCapability cap = AccessoriesCapability.get(entity);
            if (cap == null)
                return false;
            List<SlotEntryReference> slotReferences = cap.getEquipped(Items.ELYTRA);
            if (slotReferences.isEmpty())
                return false;
            SlotEntryReference slotReference = slotReferences.getFirst();
            ItemStack elytraStack = slotReference.stack();
            if (ElytraItem.isUsable(elytraStack)) {
                if (tickElytra) {
                    int nextRoll = entity.getFallFlyingTicks() + 1;

                    if (!entity.getWorld().isClient && nextRoll % 10 == 0) {
                        if ((nextRoll / 10) % 2 == 0) {
                            elytraStack.damage(1, (ServerWorld) entity.getWorld(), (entity instanceof ServerPlayerEntity) ? (ServerPlayerEntity) entity : null, (item) -> {
                                slotReference.reference().setStack(ItemStack.EMPTY);
                            });
                        }
                        entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
                    }
                }
                return true;
            }
            return false;
        });
    }
    public static void init() {}
}
