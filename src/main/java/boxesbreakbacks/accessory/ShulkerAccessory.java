package boxesbreakbacks.accessory;

import boxesbreakbacks.component.ModDataComponentTypes;
import boxesbreakbacks.component.ShulkerAccessoryAnimationDataComponent;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ShulkerAccessory implements Accessory {
    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        ShulkerAccessoryAnimationDataComponent data =
                stack.getOrDefault(
                    ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA,
                        new ShulkerAccessoryAnimationDataComponent(
                                ShulkerBoxBlock.getColor(stack.getItem())
                        )
                );
        assert data != null;
        if (reference.entity().getWorld().isClient)
            data.updateAnimation();
        else
            data.updateAnimation((ServerPlayerEntity) reference.entity(), (ServerWorld) reference.entity().getWorld(), reference.slot());
        stack.set(ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA, data);
        Accessory.super.tick(stack, reference);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference reference) {
        if (reference.entity().getWorld().isClient)
            return;
        stack.set(ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA, new ShulkerAccessoryAnimationDataComponent(ShulkerBoxBlock.getColor(stack.getItem())));
        Accessory.super.onEquip(stack, reference);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference reference) {
        if (reference.entity().getWorld().isClient)
            return;
        stack.remove(ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA);
//        stack.set(ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA, null);
        Accessory.super.onUnequip(stack, reference);
    }
}
