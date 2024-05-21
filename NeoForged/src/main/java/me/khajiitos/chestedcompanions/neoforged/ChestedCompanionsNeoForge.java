package me.khajiitos.chestedcompanions.neoforged;

import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.neoforged.client.ChestedCompanionsNeoForgeClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(ChestedCompanions.MOD_ID)
public class ChestedCompanionsNeoForge {
    public ChestedCompanionsNeoForge(ModContainer modContainer) {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            ChestedCompanionsNeoForgeClient.init(modContainer);
        }
    }
}