package boxesbreakbacks;

import boxesbreakbacks.network.OpenBackBoxPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BoxesBreakBacksClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyBinding openBackBoxKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.boxesbreakbacks.open", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "category.boxesbreakbacks.keys"));
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			while (openBackBoxKey.wasPressed()) {
				ClientPlayNetworking.send(new OpenBackBoxPayload());
			}
		});

	}
}