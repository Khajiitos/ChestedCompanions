package me.khajiitos.chestedcompanions.common.mixin.renderstate;

import me.khajiitos.chestedcompanions.common.util.IChestEntityRenderState;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WolfRenderState.class)
public class WolfRenderStateMixin implements IChestEntityRenderState {
    @Unique
    private ItemStack chestedcompanions$chestItemStack = ItemStack.EMPTY;

    @Unique
    private boolean chestedcompanions$shouldRenderChestIcon = false;

    @Override
    public ItemStack chestedCompanions$getChestItemStack() {
        return chestedcompanions$chestItemStack;
    }

    @Override
    public void chestedCompanions$setChestItemStack(@NotNull ItemStack itemStack) {
        this.chestedcompanions$chestItemStack = itemStack;
    }

    @Override
    public boolean chestedCompanions$shouldRenderChestIcon() {
        return chestedcompanions$shouldRenderChestIcon;
    }

    @Override
    public void chestedCompanions$setShouldRenderChestIcon(boolean shouldRenderChestIcon) {
        this.chestedcompanions$shouldRenderChestIcon = shouldRenderChestIcon;
    }
}
