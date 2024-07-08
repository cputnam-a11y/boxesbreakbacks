package boxesbreakbacks.inventory;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.ContainerLootComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ShulkerBoxInventory implements Inventory {
    protected DefaultedList<ItemStack> items;
    protected ItemStack shulkerBox;
    protected int invSize;
    ShulkerBoxInventory(ServerPlayerEntity player, ItemStack shulkerBox, int inventorySize) {
        this.shulkerBox = shulkerBox;
        this.invSize = inventorySize;
        this.items = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        ContainerComponent comp = shulkerBox.get(DataComponentTypes.CONTAINER);
        if (comp != null) {
            comp.copyTo(items);
        }
        ContainerLootComponent comp2 = shulkerBox.get(DataComponentTypes.CONTAINER_LOOT);
        if (comp2 != null) {
            generateLoot(player, comp2);
            shulkerBox.remove(DataComponentTypes.CONTAINER_LOOT);
        }
    }
    public void generateLoot(@Nullable ServerPlayerEntity player, ContainerLootComponent loot) {
        if (player == null) {
            return;
        }
        LootTable loottable = player.server.getReloadableRegistries().getLootTable(loot.lootTable());

        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld) player.getWorld());
        builder.luck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player);

        loottable.supplyInventory(this, builder.build(LootContextTypes.CHEST), loot.seed());
        markDirty();
    }
    @Override
    public int size() {
        return invSize;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(items, slot, amount);
        markDirty();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = Inventories.removeStack(items, slot);
        markDirty();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        markDirty();
    }

    @Override
    public void markDirty() {
        shulkerBox.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(items));
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        Optional<TrinketComponent> trinkOption = TrinketsApi.getTrinketComponent(player);
        if (trinkOption.isEmpty())
            return false;
        TrinketComponent trinks = trinkOption.get();
        return trinks.isEquipped((s) -> s == shulkerBox) || player.getInventory().contains(shulkerBox);
    }

    @Override
    public void clear() {
        items.clear();
        markDirty();
    }
    protected static float getVariatedPitch(World world) {
        return world.getRandom().nextFloat() * 0.1F + 0.9F;
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), getOpenSound(), SoundCategory.BLOCKS, 0.5F, getVariatedPitch(player.getWorld()));
    }

    @Override
    public void onClose(PlayerEntity player) {
        markDirty();
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), getCloseSound(), SoundCategory.BLOCKS, 0.5F, getVariatedPitch(player.getWorld()));
    }
    protected SoundEvent getOpenSound() {
        return SoundEvents.BLOCK_SHULKER_BOX_OPEN;
    }

    protected SoundEvent getCloseSound() {
        return SoundEvents.BLOCK_SHULKER_BOX_CLOSE;
    }
}
