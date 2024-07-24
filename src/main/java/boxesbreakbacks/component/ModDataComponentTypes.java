package boxesbreakbacks.component;

import boxesbreakbacks.BoxesBreakBacks;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<ShulkerAccessoryAnimationDataComponent> SHULKER_ACCESSORY_ANIMATION_DATA =
            register(
                    Identifier.of(BoxesBreakBacks.MOD_ID, "shulker_animation_data"),
                    builder ->
                            builder
                                    .codec(
                                            ShulkerAccessoryAnimationDataComponent.CODEC
                                    )
                                    .packetCodec(
                                            ShulkerAccessoryAnimationDataComponent.PACKET_CODEC
                                    )

            );
    @SuppressWarnings("EmptyMethod")
    public static void init() {

    }
    private static <T> ComponentType<T> register(Identifier id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return  Registry.register(Registries.DATA_COMPONENT_TYPE, id.toString(), builderOperator.apply(ComponentType.builder()).build());
    }
}
