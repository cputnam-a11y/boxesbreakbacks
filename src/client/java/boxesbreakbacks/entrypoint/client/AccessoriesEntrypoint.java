package boxesbreakbacks.entrypoint.client;

import boxesbreakbacks.BoxesBreakBacksConstants;
import boxesbreakbacks.network.client.ModNetworkHandler;
import boxesbreakbacks.network.OpenBackBoxPayload;
import boxesbreakbacks.render.ElytraAccessoryRenderer;
import boxesbreakbacks.render.ShulkerAccessoryRenderer;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;

public class AccessoriesEntrypoint {
    public static void init() {
        ModNetworkHandler.init();
        KeyBinding openBackBoxKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.boxesbreakbacks.open", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "category.boxesbreakbacks.keys"));
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            while (openBackBoxKey.wasPressed()) {
                ClientPlayNetworking.send(new OpenBackBoxPayload());
            }
        });
        AccessoriesRendererRegistry.registerRenderer(Items.ELYTRA, ElytraAccessoryRenderer::new);
        BoxesBreakBacksConstants.SHULKERS.forEach((item) -> AccessoriesRendererRegistry.registerRenderer(item, ShulkerAccessoryRenderer::new));
    }
}
