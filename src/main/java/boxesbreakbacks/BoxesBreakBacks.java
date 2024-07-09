package boxesbreakbacks;

import boxesbreakbacks.inventory.ShulkerBoxPortableScreenHandler;
import boxesbreakbacks.network.OpenBackBoxPayload;
import boxesbreakbacks.tag.ModTags;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.event.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BoxesBreakBacks implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "boxesbreakbacks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		PayloadTypeRegistry.playC2S().register(OpenBackBoxPayload.ID, OpenBackBoxPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(OpenBackBoxPayload.ID, (payload, context) -> {
			AccessoriesCapability cap = AccessoriesCapability.get(context.player());
			if (cap == null)
				return;
			List<SlotEntryReference> slotReferences = cap.getEquipped((s) -> s.isIn(ModTags.BOXES));
			if (slotReferences.isEmpty())
				return;
			ShulkerBoxPortableScreenHandler.open(context.player(), slotReferences.getFirst().stack());
		});
		EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
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
							// damage(int amount, ServerWorld world, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback)
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
		LOGGER.info("Hello Fabric world!");
	}
}