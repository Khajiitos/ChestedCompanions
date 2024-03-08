package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import me.khajiitos.chestedcompanions.common.mixin.accessor.WolfModelAccessor;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.animal.Wolf;

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
    protected void setupPosition(ModelPart chestModelPart) {
        ModelPart wolfBody = this.getParentModelBody();

        chestModelPart.x = wolfBody.x;
        chestModelPart.y = wolfBody.y;
        chestModelPart.z = wolfBody.z;

        chestModelPart.xRot = HALF_PI;
        chestModelPart.yRot = HALF_PI;
        chestModelPart.zRot = HALF_PI;

        chestModelPart.xScale = 0.6f;
        chestModelPart.yScale = 0.6f;
        chestModelPart.zScale = 0.6f;
    }
}
