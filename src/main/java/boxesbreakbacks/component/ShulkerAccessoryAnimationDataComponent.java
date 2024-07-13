package boxesbreakbacks.component;

import boxesbreakbacks.mixininterface.PayloadCompatibleServerChunkLoadingManager;
import boxesbreakbacks.network.ShulkerStateChangePayload;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShulkerAccessoryAnimationDataComponent {
    private static final Codec<DyeColor> DYE_COLOR_CODEC =
            Codec.INT.xmap(
                    i -> i == -1 ? null : DyeColor.byId(i),
                    color -> color == null ? -1 : color.getId()
            );
    public static final Codec<ShulkerAccessoryAnimationDataComponent> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    DYE_COLOR_CODEC.fieldOf("cachedColor").forGetter(ShulkerAccessoryAnimationDataComponent::getColor),
                    Codec.STRING.xmap(AnimationStage::valueOf, Enum::name).fieldOf("animationStage").forGetter(ShulkerAccessoryAnimationDataComponent::getAnimationStage),
                    Codec.FLOAT.fieldOf("prevAnimationProgress").forGetter(ShulkerAccessoryAnimationDataComponent::getPrevAnimationProgress),
                    Codec.FLOAT.fieldOf("animationProgress").forGetter(ShulkerAccessoryAnimationDataComponent::getAnimationProgress)
            ).apply(instance, ShulkerAccessoryAnimationDataComponent::new));
    static PacketCodec<PacketByteBuf, ShulkerAccessoryAnimationDataComponent> PACKET_CODEC = PacketCodec.ofStatic(ShulkerAccessoryAnimationDataComponent::toBuf, ShulkerAccessoryAnimationDataComponent::fromBuf);
    private AnimationStage animationStage;
    private float animationProgress;
    private float prevAnimationProgress;
    @Nullable
    private final DyeColor cachedColor;
    // codec constructor
    private ShulkerAccessoryAnimationDataComponent(DyeColor color, AnimationStage animationStage, float prevAnimationProgress, float animationProgress) {
        cachedColor = color;
        this.animationStage = AnimationStage.CLOSED;
        this.animationProgress = 0;
        this.prevAnimationProgress = 0;
    }
    // public constructor
    public ShulkerAccessoryAnimationDataComponent(DyeColor color) {
        cachedColor = color;
        animationStage = AnimationStage.CLOSED;
        animationProgress = 0;
        prevAnimationProgress = 0;
    }
    public ShulkerAccessoryAnimationDataComponent(Item item) {
        cachedColor = ShulkerBoxBlock.getColor(item);
        animationStage = AnimationStage.CLOSED;
        animationProgress = 0;
        prevAnimationProgress = 0;
    }
    public ShulkerAccessoryAnimationDataComponent(ItemStack item) {
        cachedColor = ShulkerBoxBlock.getColor(item.getItem());
        animationStage = AnimationStage.CLOSED;
        animationProgress = 0;
        prevAnimationProgress = 0;
    }
    public enum AnimationStage {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING;
        private static final AnimationStage[] values = AnimationStage.values();
        public static AnimationStage getOrdinal(int ordinal) {
            return values[ordinal];
        }
    }
    public float getAnimationProgress(float delta) {
        return MathHelper.lerp(delta, this.prevAnimationProgress, this.animationProgress);
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public float getPrevAnimationProgress() {
        return prevAnimationProgress;
    }

    public ShulkerAccessoryAnimationDataComponent setAnimationStage(AnimationStage animationStage) {
        this.animationStage = animationStage;
        return this;
    }

    public AnimationStage getAnimationStage() {
        return animationStage;
    }

    public @Nullable DyeColor getColor() {
        return cachedColor;
    }
    public void updateAnimation() {
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage.ordinal()) {
            case 0:
                this.animationProgress = 0.0F;
                break;
            case 1:
                this.animationProgress += 0.1F;

                if (this.animationProgress >= 1.0F) {
                    this.animationStage = AnimationStage.OPENED;
                    this.animationProgress = 1.0F;
                }
                break;
            case 2:
                this.animationProgress = 1.0F;
                break;
            case 3:
                this.animationProgress -= 0.1F;

                if (this.animationProgress <= 0.0F) {
                    this.animationStage = AnimationStage.CLOSED;
                    this.animationProgress = 0.0F;
                }
        }

    }
    public void updateAnimation(ServerPlayerEntity player, ServerWorld world, int slotNumber) {
        AnimationStage previousStage = this.animationStage;
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage.ordinal()) {
            case 0:
                this.animationProgress = 0.0F;
                break;
            case 1:
                this.animationProgress += 0.1F;

                if (this.animationProgress >= 1.0F) {
                    this.animationStage = AnimationStage.OPENED;
                    this.animationProgress = 1.0F;
                }
                break;
            case 2:
                this.animationProgress = 1.0F;
                break;
            case 3:
                this.animationProgress -= 0.1F;

                if (this.animationProgress <= 0.0F) {
                    this.animationStage = AnimationStage.CLOSED;
                    this.animationProgress = 0.0F;
                }
        }
//        if (previousStage != this.animationStage && (this.animationStage != AnimationStage.CLOSED || this.animationStage != AnimationStage.OPENED))
//            ((PayloadCompatibleServerChunkLoadingManager)world.getChunkManager().chunkLoadingManager).boxesBreakBacks$sendToNearbyPlayers(player, new ShulkerStateChangePayload(player, animationStage, slotNumber));
    }
    static ShulkerAccessoryAnimationDataComponent fromBuf(PacketByteBuf buf) {
        ShulkerAccessoryAnimationDataComponent data;
        String colorString = buf.readString();
        if (Objects.equals(colorString, "null"))
            data = new ShulkerAccessoryAnimationDataComponent((DyeColor) null);
        else
            data = new ShulkerAccessoryAnimationDataComponent(DyeColor.valueOf(colorString));
        data.animationStage = AnimationStage.getOrdinal(buf.readInt());
        data.prevAnimationProgress = buf.readFloat();
        data.animationProgress = buf.readFloat();

       return data;
    }
    static void toBuf(PacketByteBuf buf, ShulkerAccessoryAnimationDataComponent data) {
        if (data.cachedColor != null)
            buf.writeString(data.cachedColor.name());
        else
            buf.writeString("null");
        buf.writeInt(data.animationStage.ordinal());
        buf.writeFloat(data.prevAnimationProgress);
        buf.writeFloat(data.animationProgress);
    }
}
