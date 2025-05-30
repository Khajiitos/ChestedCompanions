package me.khajiitos.chestedcompanions.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.common.util.IChestEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ChestIconRenderer {
    private static final ResourceLocation CHEST_ICON_LOCATION = ResourceLocation.fromNamespaceAndPath(ChestedCompanions.MOD_ID, "textures/chest_icon.png");
    private static final float SCALE = 1.f / 64.f;

    public static <T extends LivingEntity> boolean shouldRender(T entity) {
        if (entity != Minecraft.getInstance().getEntityRenderDispatcher().crosshairPickEntity) {
            return false;
        }

        if (!(entity instanceof IChestEntity chestEntity) || !chestEntity.chestedCompanions$hasChest()) {
            return false;
        }

        if (!Minecraft.renderNames()) {
            return false;
        }

        if (entity instanceof Cat) {
            return CCConfig.showChestIconOnCats.get();
        } else if (entity instanceof Wolf) {
            return CCConfig.showChestIconOnWolves.get();
        }

        return false;
    }

    public static void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull LivingEntityRenderState entityRenderState, double yOffset) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CHEST_ICON_LOCATION));
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(entityRenderState, 0.f);

        poseStack.pushPose();
        poseStack.translate(0.0, yOffset, 0.0);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(-SCALE, -SCALE, SCALE);

        Matrix4f matrix4f = poseStack.last().pose();

        vertexConsumer.addVertex(matrix4f, 0, 0, 0).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setOverlay(overlayCoords).setLight(pPackedLight).setNormal(1.0F, 1.0F, 1.0F);
        vertexConsumer.addVertex(matrix4f, 0, 16, 0).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setOverlay(overlayCoords).setLight(pPackedLight).setNormal(1.0F, 1.0F, 1.0F);
        vertexConsumer.addVertex(matrix4f, 16, 16, 0).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setOverlay(overlayCoords).setLight(pPackedLight).setNormal(1.0F, 1.0F, 1.0F);
        vertexConsumer.addVertex(matrix4f, 16, 0, 0).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setOverlay(overlayCoords).setLight(pPackedLight).setNormal(1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}
