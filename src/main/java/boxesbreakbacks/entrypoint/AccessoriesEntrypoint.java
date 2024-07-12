package boxesbreakbacks.entrypoint;

import boxesbreakbacks.handler.ModElytraHandler;
import boxesbreakbacks.network.ModNetworkHandler;
import io.wispforest.accessories.api.AccessoriesAPI;

public class AccessoriesEntrypoint {
    public static void init() {
        ModNetworkHandler.init();
        ModElytraHandler.init();
    }
}
