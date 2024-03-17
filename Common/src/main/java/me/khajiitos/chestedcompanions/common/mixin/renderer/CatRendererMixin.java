package me.khajiitos.chestedcompanions.common.mixin.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.client.renderer.ChestIconRenderer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.Cat;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CatRenderer.class)
public abstract class CatRendererMixin extends MobRenderer<Cat, CatModel<Cat>> {
    public CatRendererMixin(EntityRendererProvider.Context $$0, CatModel<Cat> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public void render(@NotNull Cat cat, float $$1, float $$2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        super.render(cat, $$1, $$2, poseStack, multiBufferSource, packedLight);

        if (ChestIconRenderer.shouldRender(cat)) {
            ChestIconRenderer.render(poseStack, multiBufferSource, packedLight, cat, cat.getBoundingBox().getYsize() + 0.25);
        }
    }
}
