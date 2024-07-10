package boxesbreakbacks.codec;

import boxesbreakbacks.animation_data.ShulkerAnimationData;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ShulkerAnimationDataCodec {
    public static Codec<ShulkerAnimationData> CODEC = Codec.unit(ShulkerAnimationData::new);
}
