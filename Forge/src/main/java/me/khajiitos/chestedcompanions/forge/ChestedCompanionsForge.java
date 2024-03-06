package me.khajiitos.chestedcompanions.forge;

import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.forge.client.ChestedCompanionsForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ChestedCompanions.MOD_ID)
public class ChestedCompanionsForge {
    public ChestedCompanionsForge() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ChestedCompanionsForgeClient::init);
    }
}