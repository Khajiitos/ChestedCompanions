package me.khajiitos.chestedcompanions.fabric;

import me.khajiitos.chestedcompanions.common.client.renderer.layer.CatChestLayer;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.WolfChestLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;

public class ChestedCompanionsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, renderer, registrationHelper, ctx) -> {
            if (renderer instanceof CatRenderer catRenderer) {
                registrationHelper.register(new CatChestLayer(catRenderer));
            } else if (renderer instanceof WolfRenderer wolfRenderer) {
                registrationHelper.register(new WolfChestLayer(wolfRenderer));
            }
        });
    }
}
