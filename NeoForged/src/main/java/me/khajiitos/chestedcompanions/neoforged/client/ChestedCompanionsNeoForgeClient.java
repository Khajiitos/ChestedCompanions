package me.khajiitos.chestedcompanions.neoforged.client;

import me.khajiitos.chestedcompanions.common.client.ModModelLayers;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.CatChestLayer;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.WolfChestLayer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ChestedCompanionsNeoForgeClient {
    public static void init() {
        // TODO: find out what the alternative on NeoForge is
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ChestedCompanionsNeoForgeClient::addLayers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ChestedCompanionsNeoForgeClient::registerLayerDefinitions);
    }

    private static void addLayers(EntityRenderersEvent.AddLayers e) {
        LivingEntityRenderer<Cat, CatModel<Cat>> catRenderer = e.getRenderer(EntityType.CAT);

        if (catRenderer != null) {
            catRenderer.addLayer(new CatChestLayer(catRenderer));
        }

        LivingEntityRenderer<Wolf, WolfModel<Wolf>> wolfRenderer = e.getRenderer(EntityType.WOLF);

        if (wolfRenderer != null) {
            wolfRenderer.addLayer(new WolfChestLayer(wolfRenderer));
        }
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(ModModelLayers.CAT_CHEST, () -> LayerDefinition.create(CatModel.createBodyMesh(new CubeDeformation(0.f)), 1, 1));
        e.registerLayerDefinition(ModModelLayers.WOLF_CHEST, WolfModel::createBodyLayer);
    }
}
