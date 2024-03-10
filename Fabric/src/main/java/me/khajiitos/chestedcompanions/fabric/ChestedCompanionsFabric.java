package me.khajiitos.chestedcompanions.fabric;

import me.khajiitos.chestedcompanions.common.config.CCConfig;
import net.fabricmc.api.ModInitializer;

public class ChestedCompanionsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CCConfig.init();
    }
}
