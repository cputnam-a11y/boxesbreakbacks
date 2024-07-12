package boxesbreakbacks.render;

import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
public class ElytraAccessoryRenderer implements AccessoryRenderer {

    private static final Identifier SKIN = Identifier.ofVanilla("textures/entity/elytra.png");
    private final ElytraEntityModel<LivingEntity> elytra;

    public ElytraAccessoryRenderer() {
        this.elytra = new ElytraEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.ELYTRA));
    }
    @SuppressWarnings("unchecked")
    @Override
    public <M extends LivingEntity> void render(ItemStack stack, SlotReference reference, MatrixStack matrices, EntityModel<M> model, VertexConsumerProvider multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity livingEntity = reference.entity();
        if (reference.entity().getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA))
            return;
        Identifier identifier;
        if (livingEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
            SkinTextures skinTextures = abstractClientPlayerEntity.getSkinTextures();
            if (skinTextures.elytraTexture() != null) {
                identifier = skinTextures.elytraTexture();
            } else if (skinTextures.capeTexture() != null && abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE)) {
                identifier = skinTextures.capeTexture();
            } else {
                identifier = SKIN;
            }
        } else {
            identifier = SKIN;
        }

        matrices.push();
        matrices.translate(0.0F, 0.0F, 0.125F);
        model.copyStateTo((EntityModel<M>) this.elytra);
        this.elytra.setAngles(livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, headPitch);
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(multiBufferSource, RenderLayer.getArmorCutoutNoCull(identifier), stack.hasGlint());
        this.elytra.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
