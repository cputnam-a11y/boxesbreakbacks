package boxesbreakbacks;

import boxesbreakbacks.entrypoint.AccessoriesEntrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class BoxesBreakBacksClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		if (FabricLoader.getInstance().isModLoaded("accessories"))
			AccessoriesEntrypoint.init();
	}
}