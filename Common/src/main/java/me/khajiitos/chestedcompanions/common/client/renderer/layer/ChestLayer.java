package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.IChestEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class ChestLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    // We will only use the chest from the donkey texture
    private static final ResourceLocation DONKEY_LOCATION = new ResourceLocation("textures/entity/horse/donkey.png");

    public ChestLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    protected abstract ModelPart getParentModelBody();
    protected abstract void setupPosition(ModelPart chestModelPart);

    protected abstract Vec3i positionLeftChestCube();
    protected abstract Vec3i positionRightChestCube();

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, float pPartialTicks) {
        if (!(pEntity instanceof IChestEntity chestEntity) || !chestEntity.hasChest()) {
            return;
        }

        Vec3i positionLeftChestCube = this.positionLeftChestCube();
        Vec3i positionRightChestCube = this.positionRightChestCube();

        ModelPart.Cube left = new ModelPart.Cube(
                26, 21,
                positionLeftChestCube.getX(), positionLeftChestCube.getY(), positionLeftChestCube.getZ(),
                8, 8, 3,
                1.f, 1.f, 1.f,
                false,
                64, 64,
                Set.of(Direction.values())
        );

        ModelPart.Cube right = new ModelPart.Cube(
                26, 21,
                positionRightChestCube.getX(), positionRightChestCube.getY(), positionRightChestCube.getZ(),
                8, 8, 3,
                1.f, 1.f, 1.f,
                true,
                64, 64,
                Set.of(Direction.values())
        );

        ModelPart modelPart = new ModelPart(List.of(left, right), new HashMap<>());
        this.setupPosition(modelPart);
        modelPart.render(poseStack, multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(DONKEY_LOCATION)), pPackedLight, LivingEntityRenderer.getOverlayCoords(pEntity, 0.f), 1.f, 1.f, 1.f, 1.f);
    }
}
