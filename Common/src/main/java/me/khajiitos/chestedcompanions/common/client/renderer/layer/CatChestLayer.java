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

        chestModelPart.xRot = catBody.xRot;
        chestModelPart.yRot = (float)Math.PI / 2.f;
        chestModelPart.zRot = (float)Math.PI / 2.f;

        chestModelPart.xScale = 0.6f;
        chestModelPart.yScale = 0.6f;
        chestModelPart.zScale = 0.6f;
    }
}
