package boxesbreakbacks.network;

import boxesbreakbacks.BoxesBreakBacks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import static boxesbreakbacks.component.ShulkerAccessoryAnimationDataComponent.AnimationStage;

public class ShulkerStateChangePayload implements CustomPayload {
    public static Id<ShulkerStateChangePayload> ID = new Id<>(Identifier.of(BoxesBreakBacks.MOD_ID, "change_shulker_state"));
    public static PacketCodec<PacketByteBuf, ShulkerStateChangePayload> CODEC = PacketCodec.ofStatic(ShulkerStateChangePayload::write, ShulkerStateChangePayload::new);
    private final int entityId;
    private final AnimationStage animationStage;
    private final int slotNumber;
    ShulkerStateChangePayload(PacketByteBuf buf) {
        entityId = buf.readInt();
        animationStage = AnimationStage.getOrdinal(buf.readInt());
        slotNumber = buf.readInt();
    }
    public ShulkerStateChangePayload(LivingEntity entity, AnimationStage animationStage, int slotNumber) {
        entityId = entity.getId();
        this.animationStage = animationStage;
        this.slotNumber = slotNumber;
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    private static void write(PacketByteBuf buf, ShulkerStateChangePayload payload) {
        buf.writeInt(payload.entityId);
        buf.writeInt(payload.animationStage.ordinal());
        buf.writeInt(payload.slotNumber);
    }

    public AnimationStage getAnimationStage() {
        return animationStage;
    }

    public LivingEntity getEntity(World world) {
        return (LivingEntity) world.getEntityById(entityId);
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}
