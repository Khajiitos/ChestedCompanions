package me.khajiitos.chestedcompanions.fabric.client;

import me.khajiitos.chestedcompanions.common.client.renderer.layer.CatChestLayer;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.WolfChestLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;

public class ChestedCompanionsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, renderer, registrationHelper, ctx) -> {
            if (renderer instanceof CatRenderer catRenderer) {
                registrationHelper.register(new CatChestLayer(catRenderer));
            } else if (renderer instanceof WolfRenderer wolfRenderer) {
                registrationHelper.register(new WolfChestLayer(wolfRenderer));
            }
        });
    }
}
