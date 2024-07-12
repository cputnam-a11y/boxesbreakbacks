package boxesbreakbacks.component;

import net.minecraft.block.entity.ShulkerBoxBlockEntity;

public class ShulkerAccessoryAnimationDataComponent {
    private ShulkerBoxBlockEntity.AnimationStage animationStage;
    private float animationProgress;
    private float prevAnimationProgress;
    public enum AnimationStage {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING;

        private AnimationStage() {
        }
    }
}
