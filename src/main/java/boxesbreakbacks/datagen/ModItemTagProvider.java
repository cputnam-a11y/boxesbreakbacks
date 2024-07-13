package boxesbreakbacks.datagen;

import boxesbreakbacks.BoxesBreakBacksConstants;
import boxesbreakbacks.tag.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    /**
     * Construct an {@link ItemTagProvider} tag provider <b>with</b> an associated {@link BlockTagProvider} tag provider.
     *
     * @param output            The {@link FabricDataOutput} instance
     * @param completableFuture
     */
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    /**
     * Implement this method and then use {@link FabricTagProvider#getOrCreateTagBuilder} to get and register new tag builders.
     *
     * @param wrapperLookup
     */
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        List<Item> Shulkers = BoxesBreakBacksConstants.SHULKERS;
        var trinketTagBuilder = getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("accessories", "back")));
        var boxTagBuilder = getOrCreateTagBuilder(ModTags.BOXES);
        var shulkerTagBuilder = getOrCreateTagBuilder(ModTags.SHULKERS);
        for (Item shulker : Shulkers) {
            shulkerTagBuilder.add(shulker);
        }
        boxTagBuilder.addOptionalTag(ModTags.SHULKERS);
        trinketTagBuilder.addOptionalTag(ModTags.BOXES);
        trinketTagBuilder.add(Items.ELYTRA);
    }
}
