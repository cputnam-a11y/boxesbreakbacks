package boxesbreakbacks.render;

import boxesbreakbacks.component.ModDataComponentTypes;
import boxesbreakbacks.component.ShulkerAccessoryAnimationDataComponent;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

public class ShulkerAccessoryRenderer implements AccessoryRenderer {
    private final ShulkerEntityModel<?> model;
    public ShulkerAccessoryRenderer() {
        model = new ShulkerEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.SHULKER));
    }
    /**
     * Render method called within the {AccessoriesRenderLayer#render} when rendering a given Accessory on a given {@link LivingEntity}.
     * The given {@link SlotReference} refers to the slot based on its type, entity and index within the {AccessoriesContainer}.
     */
    @Override
    public <M extends LivingEntity> void render(ItemStack stack, SlotReference reference, MatrixStack matrices, EntityModel<M> model, VertexConsumerProvider multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Direction direction = Direction.NORTH;
        DyeColor dyeColor = ShulkerBoxBlock.getColor(stack.getItem());
        ShulkerAccessoryAnimationDataComponent data = stack.getOrDefault(ModDataComponentTypes.SHULKER_ACCESSORY_ANIMATION_DATA, new ShulkerAccessoryAnimationDataComponent(dyeColor));

        SpriteIdentifier spriteIdentifier;
        if (dyeColor == null) {
            spriteIdentifier = TexturedRenderLayers.SHULKER_TEXTURE_ID;
        } else {
            spriteIdentifier = TexturedRenderLayers.COLORED_SHULKER_BOXES_TEXTURES.get(dyeColor.getId());
        }
        AccessoryRenderer.translateToChest(matrices, (BipedEntityModel<? extends LivingEntity>) model, reference.entity());
        matrices.translate(-0.5F, -0.3125F, -2.251F);
        matrices.push();
        matrices.translate(0.5F, 0.5F, 0.5F);
//        float g = 0.9995F;
        matrices.scale(0.9995F, 0.9995F, 0.9995F);
        matrices.multiply(direction.getRotationQuaternion());
        matrices.scale(1.0F, -1.0F, -1.0F);
//        matrices.translate(0.0F, -1.0F, 0.0F);
        ModelPart modelPart = this.model.getLid();
        modelPart.setPivot(0.0F, 24.0F -  data.getAnimationProgress(partialTicks) * 0.5F * 16.0F, 0.0F);
        modelPart.yaw = 270.0F *  data.getAnimationProgress(partialTicks) * 0.017453292F;
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(multiBufferSource, RenderLayer::getEntityCutoutNoCull);
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
