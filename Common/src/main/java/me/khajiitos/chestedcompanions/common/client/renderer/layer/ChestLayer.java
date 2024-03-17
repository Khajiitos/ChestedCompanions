package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.common.util.IChestEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public abstract class ChestLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    // We will only use the chest from the donkey texture
    private static final ResourceLocation DONKEY_LOCATION = new ResourceLocation("textures/entity/horse/donkey.png");
    protected static final float HALF_PI = (float)Math.PI / 2.f;

    private final ModelPart modelPart;

    public ChestLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);

        Vec3i positionLeftChestCube = this.positionLeftChestCube();
        Vec3i positionRightChestCube = this.positionRightChestCube();

        ModelPart.Cube left = new ModelPart.Cube(
                26, 21,
                positionLeftChestCube.getX(), positionLeftChestCube.getY(), positionLeftChestCube.getZ(),
                8, 8, 3,
                1.f, 1.f, 1.f,
                false,
                64, 64
        );

        ModelPart.Cube right = new ModelPart.Cube(
                26, 21,
                positionRightChestCube.getX(), positionRightChestCube.getY(), positionRightChestCube.getZ(),
                8, 8, 3,
                1.f, 1.f, 1.f,
                true,
                64, 64
        );

        this.modelPart = new ModelPart(List.of(left, right), new HashMap<>());
    }

    protected abstract ModelPart getParentModelBody();
    protected abstract void setupPosition(T entity, ModelPart chestModelPart);

    protected abstract Vec3i positionLeftChestCube();
    protected abstract Vec3i positionRightChestCube();

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, float pPartialTicks) {
        if (!(pEntity instanceof IChestEntity chestEntity) || !chestEntity.hasChest()) {
            return;
        }

        this.setupPosition(pEntity, this.modelPart);
        this.modelPart.render(poseStack, multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(DONKEY_LOCATION)), pPackedLight, LivingEntityRenderer.getOverlayCoords(pEntity, 0.f), 1.f, 1.f, 1.f, 1.f);
    }

    /* TODO: uncomment and make it work
    public void renderChestIcon(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, float pPartialTicks) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CHEST_ICON_LOCATION));

        float minU = 0;
        float maxU = 1;
        float minV = 0;
        float maxV = 1;

        float scale = 1.f / 32.f; // Adjust as needed

        int overlayCoords = LivingEntityRenderer.getOverlayCoords(pEntity, 0.f);

        poseStack.pushPose();
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        //poseStack.last().pose().setRotationXYZ(0, 0,0);
        poseStack.scale(-scale, -scale, scale);

        Matrix4f matrix4f = poseStack.last().pose();

        vertexConsumer.vertex(matrix4f, 0, 0, 0).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(overlayCoords).uv2(pPackedLight).normal(1.0F, 1.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, 0, 16, 0).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(overlayCoords).uv2(pPackedLight).normal(1.0F, 1.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, 16, 16, 0).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(overlayCoords).uv2(pPackedLight).normal(1.0F, 1.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, 16, 0, 0).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(overlayCoords).uv2(pPackedLight).normal(1.0F, 1.0F, 1.0F).endVertex();

        poseStack.popPose();
    }
     */
}
