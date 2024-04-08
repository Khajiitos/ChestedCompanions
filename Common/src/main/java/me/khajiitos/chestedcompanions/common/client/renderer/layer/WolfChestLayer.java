package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.common.mixin.accessor.WolfModelAccessor;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.NotNull;

public class WolfChestLayer extends ChestLayer<Wolf, WolfModel<Wolf>> {
    public WolfChestLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    protected ModelPart getParentModelBody() {
        return ((WolfModelAccessor)this.getParentModel()).getBody();
    }

    @Override
    protected Vec3i positionLeftChestCube() {
        return new Vec3i(-8, -4, 6);
    }

    @Override
    protected Vec3i positionRightChestCube() {
        return new Vec3i(-8, -4, -9);
    }

    @Override
    protected void setupPosition(Wolf wolf, ModelPart leftChestModelPart, ModelPart rightChestModelPart) {
        ModelPart wolfBody = this.getParentModelBody();

        // Setting rotation for both left and right chest model parts
        rightChestModelPart.xRot = HALF_PI;
        leftChestModelPart.yRot = rightChestModelPart.yRot = HALF_PI;
        leftChestModelPart.zRot = rightChestModelPart.zRot = HALF_PI;

        leftChestModelPart.xRot = -HALF_PI;

        if (wolf.isBaby()) {
            // Adjusting position for baby wolf
            leftChestModelPart.x = rightChestModelPart.x = wolfBody.x;
            leftChestModelPart.y = rightChestModelPart.y = wolfBody.y + (wolf.isInSittingPose() ? 4.f : 5.f);
            leftChestModelPart.z = rightChestModelPart.z = wolfBody.z - 1.f;
            leftChestModelPart.xScale = rightChestModelPart.xScale = 0.3f;
            leftChestModelPart.yScale = rightChestModelPart.yScale = 0.3f;
            leftChestModelPart.zScale = rightChestModelPart.zScale = 0.3f;
        } else {
            // Adjusting position for adult wolf
            leftChestModelPart.x = rightChestModelPart.x = wolfBody.x;
            leftChestModelPart.y = rightChestModelPart.y = wolfBody.y;
            leftChestModelPart.z = rightChestModelPart.z = wolfBody.z;
            leftChestModelPart.xScale = rightChestModelPart.xScale = 0.6f;
            leftChestModelPart.yScale = rightChestModelPart.yScale = 0.6f;
            leftChestModelPart.zScale = rightChestModelPart.zScale = 0.6f;
        }

        leftChestModelPart.x += 8;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull Wolf pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, float pPartialTicks) {
        if (!CCConfig.hideWolfChest.get()) {
            super.render(poseStack, multiBufferSource, pPackedLight, pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks);
        }
    }
}
