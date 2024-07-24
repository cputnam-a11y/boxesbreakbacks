package boxesbreakbacks.network;

import boxesbreakbacks.BoxesBreakBacks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class OpenBackBoxPayload implements CustomPayload {
    public static final Id<OpenBackBoxPayload> ID = new Id<>(Identifier.of(BoxesBreakBacks.MOD_ID, "open_back_box"));
    public static final PacketCodec<PacketByteBuf, OpenBackBoxPayload> CODEC = PacketCodec.unit(new OpenBackBoxPayload());
    public OpenBackBoxPayload() {
    }
    @Override
    public Id<OpenBackBoxPayload> getId() {
        return ID;
    }
}
