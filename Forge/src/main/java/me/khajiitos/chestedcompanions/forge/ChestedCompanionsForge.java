package me.khajiitos.chestedcompanions.forge;

import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.forge.client.ChestedCompanionsForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(ChestedCompanions.MOD_ID)
public class ChestedCompanionsForge {
    public ChestedCompanionsForge(IEventBus eventBus) {
        CCConfig.init();

        if (FMLLoader.getDist() == Dist.CLIENT) {
            ChestedCompanionsForgeClient.init(eventBus);
        }
    }

    // To support older versions of Forge
    public ChestedCompanionsForge() {
        this(FMLJavaModLoadingContext.get().getModEventBus());
    }
}