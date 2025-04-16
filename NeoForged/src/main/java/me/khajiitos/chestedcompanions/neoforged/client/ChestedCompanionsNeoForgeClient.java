package me.khajiitos.chestedcompanions.neoforged.client;

import me.khajiitos.chestedcompanions.common.client.config.cloth.ClothConfigCheck;
import me.khajiitos.chestedcompanions.common.client.config.cloth.ClothConfigScreenMaker;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.CatChestLayer;
import me.khajiitos.chestedcompanions.common.client.renderer.layer.WolfChestLayer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ChestedCompanionsNeoForgeClient {
    public static void init(ModContainer modContainer) {
        IEventBus eventBus = modContainer.getEventBus();

        if (eventBus != null) {
            eventBus.addListener(ChestedCompanionsNeoForgeClient::addLayers);
        }

        if (ClothConfigCheck.isInstalled()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (modContainer1, screen) -> ClothConfigScreenMaker.create(screen));
        }
    }

    private static void addLayers(EntityRenderersEvent.AddLayers e) {
        LivingEntityRenderer<Cat, CatRenderState, CatModel> catRenderer = e.getRenderer(EntityType.CAT);

        if (catRenderer != null) {
            catRenderer.addLayer(new CatChestLayer(catRenderer));
        }

        LivingEntityRenderer<Wolf, WolfRenderState, WolfModel> wolfRenderer = e.getRenderer(EntityType.WOLF);

        if (wolfRenderer != null) {
            wolfRenderer.addLayer(new WolfChestLayer(wolfRenderer));
        }
    }
}
