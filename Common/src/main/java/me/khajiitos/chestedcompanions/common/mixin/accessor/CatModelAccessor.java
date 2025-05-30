package me.khajiitos.chestedcompanions.common.mixin.accessor;

import net.minecraft.client.model.FelineModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FelineModel.class)
public interface CatModelAccessor {
    @Accessor
    ModelPart getBody();
}
