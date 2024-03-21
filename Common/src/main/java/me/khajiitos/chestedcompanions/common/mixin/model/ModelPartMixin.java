package me.khajiitos.chestedcompanions.common.mixin.model;

import com.mojang.blaze3d.vertex.PoseStack;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.ChestLayer;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPart.class)
public class ModelPartMixin {

    @Inject(at = @At("TAIL"), method = "translateAndRotate")
    public void translateAndRotate(PoseStack poseStack, CallbackInfo ci) {
        float scale = ChestLayer.forceScaleForNextRender;

        if (scale != -1.0f) {
            poseStack.scale(scale, scale, scale);
        }
    }
}
