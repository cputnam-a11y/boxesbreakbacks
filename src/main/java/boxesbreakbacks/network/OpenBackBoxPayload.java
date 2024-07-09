package boxesbreakbacks.network;

import boxesbreakbacks.BoxesBreakBacks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class OpenBackBoxPayload implements CustomPayload {
    public static Id<OpenBackBoxPayload> ID = new Id<>(Identifier.of(BoxesBreakBacks.MOD_ID, "open_back_box"));
    public static PacketCodec<PacketByteBuf, OpenBackBoxPayload> CODEC = PacketCodec.ofStatic(OpenBackBoxPayload::write, OpenBackBoxPayload::new);
    public OpenBackBoxPayload() {
    }
    OpenBackBoxPayload(PacketByteBuf buf) {
    }
    @Override
    public Id<OpenBackBoxPayload> getId() {
        return ID;
    }
    static void write(PacketByteBuf buf, OpenBackBoxPayload payload) {
    }
}
