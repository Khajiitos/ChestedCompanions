package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import me.khajiitos.chestedcompanions.common.mixin.accessor.CatModelAccessor;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.animal.Cat;

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
    protected void setupPosition(ModelPart chestModelPart) {
        ModelPart catBody = this.getParentModelBody();

        chestModelPart.x = catBody.x;
        chestModelPart.y = catBody.y;
        chestModelPart.z = catBody.z;

        // This is the rotation of the body when sitting.
        // Unless I find a better way, I will have to manually
        // adjust the chest's position.
        if (catBody.xRot == HALF_PI / 2) {
            chestModelPart.y += 8.0f;
            chestModelPart.z -= 6.0f;
        }

        chestModelPart.xRot = HALF_PI;
        chestModelPart.yRot = HALF_PI;
        chestModelPart.zRot = HALF_PI;

        chestModelPart.xScale = 0.6f;
        chestModelPart.yScale = 0.6f;
        chestModelPart.zScale = 0.6f;
    }
}
