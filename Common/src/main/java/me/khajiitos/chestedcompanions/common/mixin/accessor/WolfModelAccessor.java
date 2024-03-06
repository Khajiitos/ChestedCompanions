package me.khajiitos.chestedcompanions.common.mixin.accessor;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WolfModel.class)
public interface WolfModelAccessor {
    @Accessor
    ModelPart getBody();
}
