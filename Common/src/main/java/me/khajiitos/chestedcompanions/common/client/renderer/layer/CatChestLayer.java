package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.common.mixin.accessor.CatModelAccessor;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.animal.Cat;
import org.jetbrains.annotations.NotNull;

public class CatChestLayer extends ChestLayer<Cat, CatModel<Cat>> {
    public CatChestLayer(RenderLayerParent<Cat, CatModel<Cat>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    protected ModelPart getParentModelBody() {
        return ((CatModelAccessor)this.getParentModel()).getBody();
    }

    @Override
    protected Vec3i positionLeftChestCube() {
        return new Vec3i(-22, 4, 4);
    }

    @Override
    protected Vec3i positionRightChestCube() {
        return new Vec3i(-22, 4, -7);
    }

    @Override
    protected void setupPosition(Cat cat, ModelPart leftChestModelPart, ModelPart rightChestModelPart) {
        ModelPart catBody = this.getParentModelBody();

        // Setting rotation for both left and right chest model parts
        leftChestModelPart.xRot = rightChestModelPart.xRot = HALF_PI;
        leftChestModelPart.yRot = rightChestModelPart.yRot = HALF_PI;
        leftChestModelPart.zRot = rightChestModelPart.zRot = HALF_PI;

        if (cat.isBaby()) {
            // Adjusting position for baby cat
            leftChestModelPart.x = rightChestModelPart.x = catBody.x;
            leftChestModelPart.y = rightChestModelPart.y = catBody.y + 6.f;
            leftChestModelPart.z = rightChestModelPart.z = catBody.z + 5.f;
            leftChestModelPart.xScale = rightChestModelPart.xScale = 0.3f;
            leftChestModelPart.yScale = rightChestModelPart.yScale = 0.3f;
            leftChestModelPart.zScale = rightChestModelPart.zScale = 0.3f;
        } else {
            // Adjusting position for adult cat
            leftChestModelPart.x = rightChestModelPart.x = catBody.x;
            leftChestModelPart.y = rightChestModelPart.y = catBody.y;
            leftChestModelPart.z = rightChestModelPart.z = catBody.z;
            leftChestModelPart.xScale = rightChestModelPart.xScale = 0.6f;
            leftChestModelPart.yScale = rightChestModelPart.yScale = 0.6f;
            leftChestModelPart.zScale = rightChestModelPart.zScale = 0.6f;
        }

        // Adjusting position of the chest when sitting
        if (catBody.xRot == HALF_PI / 2) {
            leftChestModelPart.y += cat.isBaby() ? 6.0f : 8.0f;
            leftChestModelPart.z -= 6.0f;
            rightChestModelPart.y += cat.isBaby() ? 6.0f : 8.0f;
            rightChestModelPart.z -= 6.0f;
        }
    }


    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull Cat pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, float pPartialTicks) {
        if (!CCConfig.hideCatChest.get()) {
            super.render(poseStack, multiBufferSource, pPackedLight, pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks);
        }
    }
}
