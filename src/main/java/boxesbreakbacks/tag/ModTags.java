package boxesbreakbacks.tag;

import boxesbreakbacks.BoxesBreakBacks;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Item> BOXES = TagKey.of(RegistryKeys.ITEM, Identifier.of(BoxesBreakBacks.MOD_ID, "boxes"));
    public static final TagKey<Item> SHULKERS = TagKey.of(RegistryKeys.ITEM, Identifier.of(BoxesBreakBacks.MOD_ID, "shulkers"));
}
