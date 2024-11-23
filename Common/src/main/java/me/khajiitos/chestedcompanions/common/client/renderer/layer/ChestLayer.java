package me.khajiitos.chestedcompanions.common.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.common.util.IChestEntityRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class ChestLayer<T extends LivingEntityRenderState, M extends EntityModel<T>> extends RenderLayer<T, M> {
    protected static final float HALF_PI = (float)Math.PI / 2.f;
    private static final ResourceLocation BASE_CHEST_LOCATION = ResourceLocation.fromNamespaceAndPath(ChestedCompanions.MOD_ID, "textures/chest/chest.png");
    private static final HashMap<Item, ResourceLocation> CHEST_TEXTURES = new HashMap<>();

    private static ResourceLocation getResourceLocation(@Nullable ItemStack chestItem) {
        if (chestItem == null) {
            return BASE_CHEST_LOCATION;
        }

        if (!CHEST_TEXTURES.containsKey(chestItem.getItem())) {
            ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(chestItem.getItem());

            ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(ChestedCompanions.MOD_ID, "textures/chest/" + (itemLocation.getNamespace().equals("minecraft") ? "" : itemLocation.getNamespace() + "/") + itemLocation.getPath() + ".png");
            if (Minecraft.getInstance().getResourceManager().getResource(textureLocation).isPresent()) {
                CHEST_TEXTURES.put(chestItem.getItem(), textureLocation);
            } else {
                CHEST_TEXTURES.put(chestItem.getItem(), BASE_CHEST_LOCATION);
                return BASE_CHEST_LOCATION;
            }
            return textureLocation;
        } else {
            return CHEST_TEXTURES.get(chestItem.getItem());
        }
    }

    private final ModelPart modelPart;

    public ChestLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);

        Vec3i positionLeftChestCube = this.positionLeftChestCube();
        Vec3i positionRightChestCube = this.positionRightChestCube();

        ModelPart.Cube left = new ModelPart.Cube(
                0, 0,
                positionLeftChestCube.getX(), positionLeftChestCube.getY(), positionLeftChestCube.getZ(),
                8, 8, 3,
                1.f, 1.f, 1.f,
                false,
                22, 11,
                Set.of(Direction.values())
        );

        ModelPart.Cube right = new ModelPart.Cube(
                0, 0,
                positionRightChestCube.getX(), positionRightChestCube.getY(), positionRightChestCube.getZ(),
                8, 8, 3,
                1.f, 1.f, 1.f,
                true,
                22, 11,
                Set.of(Direction.values())
        );

        this.modelPart = new ModelPart(List.of(left, right), new HashMap<>());
    }

    protected abstract ModelPart getParentModelBody();
    protected abstract void setupPosition(T renderState, ModelPart chestModelPart);

    protected abstract Vec3i positionLeftChestCube();
    protected abstract Vec3i positionRightChestCube();

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, @NotNull T renderState, float v, float v1) {
        if (!(renderState instanceof IChestEntityRenderState chestEntityRenderState) || !chestEntityRenderState.chestedCompanions$hasChest()) {
            return;
        }

        this.setupPosition(renderState, this.modelPart);
        this.modelPart.render(poseStack, multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(getResourceLocation(chestEntityRenderState.chestedCompanions$getChestItemStack()))), pPackedLight, LivingEntityRenderer.getOverlayCoords(renderState, 0.f));
    }
}
