package me.khajiitos.chestedcompanions.neoforged.mixin;

import net.neoforged.fml.loading.ModSorter;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModSorter.class)
public class ModSorterMixin {

    @Inject(at = @At("HEAD"), method = "modVersionNotContained", cancellable = true)
    public void modVersionNotContained(IModInfo.ModVersion mv, Map<String, ArtifactVersion> modVersions, CallbackInfoReturnable<Boolean> cir) {
        System.out.println(mv.getModId() + " - " + mv.getOwner().getModId());
        cir.setReturnValue(true);
    }
}
