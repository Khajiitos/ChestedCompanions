package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.common.mixin.accessor.WolfModelAccessor;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.NotNull;

public class WolfChestLayer extends ChestLayer<WolfRenderState, WolfModel> {
    public WolfChestLayer(RenderLayerParent<WolfRenderState, WolfModel> renderLayerParent) {
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
    protected void setupPosition(WolfRenderState wolfRenderState, ModelPart chestModelPart) {
        ModelPart wolfBody = this.getParentModelBody();

        chestModelPart.xRot = HALF_PI;
        chestModelPart.yRot = HALF_PI;
        chestModelPart.zRot = HALF_PI;

        if (wolfRenderState.isBaby) {
            chestModelPart.x = wolfBody.x;
            chestModelPart.y = wolfBody.y + (wolfRenderState.isSitting ? 1.0f : 0.0f);
            chestModelPart.z = wolfBody.z - 1.f;
            chestModelPart.xScale = 0.3f;
            chestModelPart.yScale = 0.3f;
            chestModelPart.zScale = 0.3f;
        } else {
            chestModelPart.x = wolfBody.x;
            chestModelPart.y = wolfBody.y;
            chestModelPart.z = wolfBody.z;
            chestModelPart.xScale = 0.6f;
            chestModelPart.yScale = 0.6f;
            chestModelPart.zScale = 0.6f;
        }
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull WolfRenderState renderState, float v, float v1) {
        if (!CCConfig.hideWolfChest.get()) {
            super.render(poseStack, multiBufferSource, pPackedLight, renderState, v, v1);
        }
    }
}
