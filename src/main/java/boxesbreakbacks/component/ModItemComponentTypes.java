package boxesbreakbacks.component;

import boxesbreakbacks.animation_data.ShulkerAnimationData;
import boxesbreakbacks.codec.ShulkerAnimationDataCodec;
import boxesbreakbacks.codec.ShulkerAnimationDataPacketCodec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModItemComponentTypes {
    public static final ComponentType<ShulkerAnimationData> SHULKER_ANIMATION_DATA = register("boxes_break_backs_shulker_animation_data", (builder) -> builder.codec(ShulkerAnimationDataCodec.CODEC).packetCodec(new ShulkerAnimationDataPacketCodec()));
    public static void init() {

    }
    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, (builderOperator.apply(ComponentType.builder())).build());
    }
}
