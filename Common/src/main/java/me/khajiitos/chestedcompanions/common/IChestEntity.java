package me.khajiitos.chestedcompanions.common;

import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.HasCustomInventoryScreen;

public interface IChestEntity extends HasCustomInventoryScreen, MenuProvider {
    boolean hasChest();
    void setHasChest(boolean hasChest);
    int getInventorySlots();
    Container getInventory();
}
