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
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
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
    protected void setupPosition(Cat cat, ModelPart chestModelPart, float scale) {
        ModelPart catBody = this.getParentModelBody();

        chestModelPart.xRot = HALF_PI;
        chestModelPart.yRot = HALF_PI;
        chestModelPart.zRot = HALF_PI;

        float invScale = 1.f / scale;

        if (cat.isBaby()) {
            chestModelPart.x = catBody.x;
            chestModelPart.y = catBody.y + (6.f * invScale);
            chestModelPart.z = catBody.z + (5.f * invScale);
        } else {
            chestModelPart.x = catBody.x;
            chestModelPart.y = catBody.y;
            chestModelPart.z = catBody.z;
        }

        // This is the rotation of the body when sitting.
        // Unless I find a better way, I will have to manually
        // adjust the chest's position.
        if (catBody.xRot == HALF_PI / 2) {
            chestModelPart.y += cat.isBaby() ? 6.0f : 8.0f;
            chestModelPart.z -= 6.0f;
        }
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull Cat pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, float pPartialTicks) {
        if (!CCConfig.hideCatChest.get()) {
            super.render(poseStack, multiBufferSource, pPackedLight, pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks);
        }
    }
}
