package boxesbreakbacks.inventory;

import boxesbreakbacks.component.ModDataComponentTypes;
import boxesbreakbacks.component.ShulkerAccessoryAnimationDataComponent;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ShulkerBoxPortableScreenHandler extends ShulkerBoxScreenHandler {
    @SuppressWarnings("unused")
    protected final ServerPlayerEntity  player;
    protected final ItemStack shulkerBox;
    public ShulkerBoxPortableScreenHandler(int id, PlayerInventory inventory, ServerPlayerEntity player, ItemStack shulkerBox) {
        super(id, inventory, new ShulkerBoxInventory(player, shulkerBox, 3 * 9));
        this.player = player;
        this.shulkerBox = shulkerBox;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex < 0 || slotIndex >= slots.size()) {
            super.onSlotClick(slotIndex, button, actionType, player);
            return;
        }
        ItemStack stack = slots.get(slotIndex).getStack();

        if (stack == shulkerBox) {
            return;
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }
    public static void open(ServerPlayerEntity player, ItemStack shulkerBox) {
        shulkerBox.apply(
                ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA,
                new ShulkerAccessoryAnimationDataComponent(
                        ShulkerBoxBlock.getColor(
                                shulkerBox.getItem()
                        )
                ), component -> component.setAnimationStage(ShulkerAccessoryAnimationDataComponent.AnimationStage.OPENING));
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return shulkerBox.getName();
            }

            @Override
            public ScreenHandler createMenu(int i, PlayerInventory inventory, PlayerEntity player) {
                if (!(player instanceof ServerPlayerEntity serverPlayer)) {
                    return null;
                }
                return new ShulkerBoxPortableScreenHandler(i, inventory, serverPlayer, shulkerBox);
            }
        });
    }
}
