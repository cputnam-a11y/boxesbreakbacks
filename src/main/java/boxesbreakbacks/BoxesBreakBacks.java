package boxesbreakbacks;

import boxesbreakbacks.entrypoint.AccessoriesEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class BoxesBreakBacks implements ModInitializer {
	public static final String MOD_ID = "boxesbreakbacks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		if (FabricLoader.getInstance().isModLoaded("accessories"))
			AccessoriesEntrypoint.init();
	}
}