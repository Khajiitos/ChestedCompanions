package me.khajiitos.chestedcompanions.neoforged.client;

import me.khajiitos.chestedcompanions.common.client.config.cloth.ClothConfigCheck;
import me.khajiitos.chestedcompanions.common.client.config.cloth.ClothConfigScreenMaker;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.CatChestLayer;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.WolfChestLayer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ChestedCompanionsNeoForgeClient {
    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ChestedCompanionsNeoForgeClient::addLayers);

        if (ClothConfigCheck.isInstalled()) {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ClothConfigScreenMaker::create);
        }
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
}
