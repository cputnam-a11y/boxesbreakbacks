package boxesbreakbacks;

import boxesbreakbacks.inventory.ShulkerBoxPortableScreenHandler;
import boxesbreakbacks.network.OpenBackBoxPayload;
import boxesbreakbacks.tag.ModTags;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.event.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

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
			Optional<TrinketComponent> comp = TrinketsApi.getTrinketComponent(context.player());
			if (comp.isEmpty())
				return;
			List<Pair<SlotReference, ItemStack>> temp = comp.get().getEquipped((s) -> s.isIn(ModTags.BOXES));
			if (temp.isEmpty())
				return;
			ShulkerBoxPortableScreenHandler.open(context.player(), temp.getFirst().getRight());
		});
		EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(entity);
			if (optionalTrinketComponent.isEmpty())
				return false;
			TrinketComponent trinketComponent = optionalTrinketComponent.get();
			if (!trinketComponent.isEquipped(Items.ELYTRA))
				return false;
			Pair<SlotReference, ItemStack> itemPair = trinketComponent.getEquipped(Items.ELYTRA).getFirst();
			ItemStack elytraStack = itemPair.getRight();

			if (ElytraItem.isUsable(elytraStack)) {
				if (tickElytra) {
					int nextRoll = entity.getFallFlyingTicks() + 1;

					if (!entity.getWorld().isClient && nextRoll % 10 == 0) {
						if ((nextRoll / 10) % 2 == 0) {
							elytraStack.damage(1,entity.getRandom() ,(entity instanceof ServerPlayerEntity) ? (ServerPlayerEntity) entity : null, () -> {
								itemPair.getLeft().inventory().setStack(itemPair.getLeft().index(), new ItemStack(Items.AIR.asItem()));
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