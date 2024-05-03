package me.khajiitos.chestedcompanions.forge.client;

import me.khajiitos.chestedcompanions.common.client.config.cloth.ClothConfigCheck;
import me.khajiitos.chestedcompanions.common.client.config.cloth.ClothConfigScreenMaker;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.CatChestLayer;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.WolfChestLayer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.function.Function;

public class ChestedCompanionsForgeClient {

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ChestedCompanionsForgeClient::addLayers);

        if (ClothConfigCheck.isInstalled()) {
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((Function<Screen, Screen>) ClothConfigScreenMaker::create));
        }
    }

    private static void addLayers(EntityRenderersEvent.AddLayers e) {
        LivingEntityRenderer<Cat, CatModel<Cat>> catRenderer = e.getEntityRenderer(EntityType.CAT);

        if (catRenderer != null) {
            catRenderer.addLayer(new CatChestLayer(catRenderer));
        }

        LivingEntityRenderer<Wolf, WolfModel<Wolf>> wolfRenderer = e.getEntityRenderer(EntityType.WOLF);

        if (wolfRenderer != null) {
            wolfRenderer.addLayer(new WolfChestLayer(wolfRenderer));
        }
    }
}
