package boxesbreakbacks.entrypoint;

import boxesbreakbacks.network.OpenBackBoxPayload;
import boxesbreakbacks.render.ElytraAccessoryRenderer;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;

import java.util.List;

public class AccessoriesEntrypoint {
    public static void init() {
        KeyBinding openBackBoxKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.boxesbreakbacks.open", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "category.boxesbreakbacks.keys"));
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            while (openBackBoxKey.wasPressed()) {
                ClientPlayNetworking.send(new OpenBackBoxPayload());
            }
        });
        AccessoriesRendererRegistry.registerRenderer(Items.ELYTRA, ElytraAccessoryRenderer::new);
        List.of(
                Items.SHULKER_BOX,
                Items.WHITE_SHULKER_BOX,
                Items.ORANGE_SHULKER_BOX,
                Items.MAGENTA_SHULKER_BOX,
                Items.LIGHT_BLUE_SHULKER_BOX,
                Items.YELLOW_SHULKER_BOX,
                Items.LIME_SHULKER_BOX,
                Items.PINK_SHULKER_BOX,
                Items.GRAY_SHULKER_BOX,
                Items.LIGHT_GRAY_SHULKER_BOX,
                Items.CYAN_SHULKER_BOX,
                Items.PURPLE_SHULKER_BOX,
                Items.BLUE_SHULKER_BOX,
                Items.BROWN_SHULKER_BOX,
                Items.GREEN_SHULKER_BOX,
                Items.RED_SHULKER_BOX,
                Items.BLACK_SHULKER_BOX
        ).forEach(AccessoriesRendererRegistry::registerNoRenderer);
    }
}
