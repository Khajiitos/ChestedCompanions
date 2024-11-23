package me.khajiitos.chestedcompanions.common.mixin.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.client.renderer.ChestIconRenderer;
import me.khajiitos.chestedcompanions.common.util.IChestEntity;
import me.khajiitos.chestedcompanions.common.util.IChestEntityRenderState;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.Cat;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatRenderer.class)
public abstract class CatRendererMixin extends MobRenderer<Cat, CatRenderState, CatModel> {
    public CatRendererMixin(EntityRendererProvider.Context $$0, CatModel $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public void render(@NotNull CatRenderState catRenderState, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        super.render(catRenderState, poseStack, multiBufferSource, packedLight);

        if (catRenderState instanceof IChestEntityRenderState renderState && renderState.chestedCompanions$shouldRenderChestIcon()) {
            ChestIconRenderer.render(poseStack, multiBufferSource, packedLight, catRenderState, catRenderState.boundingBoxHeight + 0.25);
        }
    }

    @Inject(at = @At("TAIL"), method = "extractRenderState(Lnet/minecraft/world/entity/animal/Cat;Lnet/minecraft/client/renderer/entity/state/CatRenderState;F)V")
    public void extractRenderState(Cat cat, CatRenderState renderState, float v, CallbackInfo ci) {
        if (cat instanceof IChestEntity chestEntity && renderState instanceof IChestEntityRenderState chestEntityRenderState) {
            chestEntityRenderState.chestedCompanions$setChestItemStack(chestEntity.chestedCompanions$getChestItemStack());
            chestEntityRenderState.chestedCompanions$setShouldRenderChestIcon(ChestIconRenderer.shouldRender(cat));
        }
    }
}
