package boxesbreakbacks.codec;

import boxesbreakbacks.animation_data.ShulkerAnimationData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.NotNull;

public class ShulkerAnimationDataPacketCodec implements PacketCodec<ByteBuf, ShulkerAnimationData> {
    @Override
    @NotNull
    public ShulkerAnimationData decode(ByteBuf buf) {
        return new ShulkerAnimationData();
    }

    @Override
    public void encode(ByteBuf buf, ShulkerAnimationData value) {

    }
}
