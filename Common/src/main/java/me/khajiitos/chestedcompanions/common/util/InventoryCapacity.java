package me.khajiitos.chestedcompanions.common.util;

import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public enum InventoryCapacity {
    ONE_ROW(MenuType.GENERIC_9x1, 1),
    TWO_ROWS(MenuType.GENERIC_9x2, 2),
    THREE_ROWS(MenuType.GENERIC_9x3, 3);

    public final MenuType<ChestMenu> menuType;
    public final int containerRows;

    InventoryCapacity(MenuType<ChestMenu> menuType, int containerRows) {
        this.menuType = menuType;
        this.containerRows = containerRows;
    }
}
