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
    protected void setupPosition(Wolf wolf, ModelPart chestModelPart) {
        ModelPart wolfBody = this.getParentModelBody();

        chestModelPart.xRot = HALF_PI;
        chestModelPart.yRot = HALF_PI;
        chestModelPart.zRot = HALF_PI;

        if (wolf.isBaby()) {
            chestModelPart.x = wolfBody.x;
            chestModelPart.y = wolfBody.y + (wolf.isInSittingPose() ? 4.f : 5.f);
            chestModelPart.z = wolfBody.z - 1.f;
        } else {
            chestModelPart.x = wolfBody.x;
            chestModelPart.y = wolfBody.y;
            chestModelPart.z = wolfBody.z;
        }
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull Wolf pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, float pPartialTicks) {
        if (!CCConfig.hideWolfChest.get()) {
            super.render(poseStack, multiBufferSource, pPackedLight, pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks);
        }
    }
}
