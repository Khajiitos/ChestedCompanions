package me.khajiitos.chestedcompanions.common.mixin.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.client.renderer.ChestIconRenderer;
import me.khajiitos.chestedcompanions.common.util.IChestEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfRenderer.class)
public abstract class WolfRendererMixin {

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/world/entity/animal/Wolf;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    public void render(Wolf wolf, float $$1, float $$2, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CallbackInfo ci) {
        if (ChestIconRenderer.shouldRender(wolf)) {
            ChestIconRenderer.render(poseStack, multiBufferSource, packedLight, wolf, wolf.getBoundingBox().getYsize() + 0.25);
        }
    }
}
