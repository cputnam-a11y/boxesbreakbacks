package boxesbreakbacks.datagen;

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
        List<Item> Shulkers = List.of(
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
            );
        var trinketTagBuilder = getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, new Identifier("trinkets", "chest/back")));
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
