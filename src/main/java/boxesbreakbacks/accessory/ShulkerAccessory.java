package boxesbreakbacks.accessory;

import boxesbreakbacks.BoxesBreakBacks;
import boxesbreakbacks.component.ModDataCompontentTypes;
import boxesbreakbacks.component.ShulkerAccessoryAnimationDataComponent;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;

public class ShulkerAccessory implements Accessory {
    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        ShulkerAccessoryAnimationDataComponent data =
                stack.getOrDefault(
                    ModDataCompontentTypes.SHULKER_ACCESSORY_ANIMATION_DATA,
                        new ShulkerAccessoryAnimationDataComponent(
                                ShulkerBoxBlock.getColor(stack.getItem())
                        )
                );
        assert data != null;
        if (reference.entity().getWorld().isClient)
            data.updateAnimation();
        else
            data.updateAnimation((ServerPlayerEntity) reference.entity(), (ServerWorld) reference.entity().getWorld(), reference.slot());
        stack.set(ModDataCompontentTypes.SHULKER_ACCESSORY_ANIMATION_DATA, data);
//        if (reference.entity().getWorld().isClient)
//            BoxesBreakBacks.LOGGER.info("current state: {}, animation progress: {}", data.getAnimationStage(), data.getAnimationProgress());
        Accessory.super.tick(stack, reference);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference reference) {
        if (reference.entity().getWorld().isClient)
            return;
        stack.set(ModDataCompontentTypes.SHULKER_ACCESSORY_ANIMATION_DATA, new ShulkerAccessoryAnimationDataComponent(ShulkerBoxBlock.getColor(stack.getItem())));
        Accessory.super.onEquip(stack, reference);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference reference) {
        if (reference.entity().getWorld().isClient)
            return;
        stack.remove(ModDataCompontentTypes.SHULKER_ACCESSORY_ANIMATION_DATA);
//        stack.set(ModDataCompontentTypes.SHULKER_ACCESSORY_ANIMATION_DATA, null);
        Accessory.super.onUnequip(stack, reference);
    }
}
