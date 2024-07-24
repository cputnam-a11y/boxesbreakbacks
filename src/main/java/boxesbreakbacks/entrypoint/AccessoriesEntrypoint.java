package boxesbreakbacks.entrypoint;

import boxesbreakbacks.BoxesBreakBacksConstants;
import boxesbreakbacks.accessory.ShulkerAccessory;
import boxesbreakbacks.component.ModDataComponentTypes;
import boxesbreakbacks.handler.ModElytraHandler;
import boxesbreakbacks.network.ModNetworkHandler;
import io.wispforest.accessories.api.AccessoriesAPI;
import net.minecraft.item.Item;

public class AccessoriesEntrypoint {
    public static void init() {
        ModDataComponentTypes.init();
        ModNetworkHandler.init();
        ModElytraHandler.init();
        for (Item item : BoxesBreakBacksConstants.SHULKERS)
            AccessoriesAPI.registerAccessory(item, new ShulkerAccessory());

    }
}
