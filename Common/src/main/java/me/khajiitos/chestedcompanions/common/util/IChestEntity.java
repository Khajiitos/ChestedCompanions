package me.khajiitos.chestedcompanions.common.util;

import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IChestEntity extends HasCustomInventoryScreen, MenuProvider {
    InventoryCapacity chestedCompanions$getInventoryCapacity();
    PetChestContainer<?> chestedCompanions$getInventory();
    void chestedCompanions$setChestItemStack(ItemStack itemStack);
    ItemStack chestedCompanions$getChestItemStack();
    void chestedCompanions$createInventory();
    void chestedCompanions$removeInventory();
    void chestedCompanions$removeChestContent(boolean dropChest);
    boolean chestedCompanions$allowChest();
    boolean chestedCompanions$allowChestOnBaby();

    default boolean chestedCompanions$hasChest() {
        return !this.chestedCompanions$getChestItemStack().isEmpty();
    }

    default boolean chestedCompanions$isValidChestItem(ItemStack itemStack) {
        return itemStack.is(ChestedCompanions.PET_CHEST_ITEM);
    }

    class PetChestContainer<T extends TamableAnimal & IChestEntity> extends SimpleContainer {
        private final T pet;

        // Used for increasing the size of the container, or shrinking it and dropping items
        public PetChestContainer(T pet, int inventorySlots, @Nullable PetChestContainer<?> oldContainer) {
            super(inventorySlots);
            this.pet = pet;
            if (oldContainer != null) {
                this.clearContent();
                for (int i = 0; i < oldContainer.getContainerSize(); i++) {
                    putOrDropItem(new ItemStackWithSlot(i, oldContainer.getItem(i)));
                }
            }
        }

        public void storeAsItemListWithSlot(ValueOutput.TypedOutputList<ItemStackWithSlot> list) {
            for (int i = 0; i < this.getContainerSize(); i++) {
                ItemStack itemStack = this.getItem(i);

                if (!itemStack.isEmpty()) {
                    list.add(new ItemStackWithSlot(i, itemStack));
                }
            }
        }

        public void loadFromItemListWithSlot(ValueInput.TypedInputList<ItemStackWithSlot> list) {
            this.clearContent();
            list.forEach(this::putOrDropItem);
        }

        public void putOrDropItem(ItemStackWithSlot itemStackWithSlot) {
            if (itemStackWithSlot.slot() >= this.getContainerSize()) {
                if (this.pet.level() instanceof ServerLevel serverLevel) {
                    this.pet.spawnAtLocation(serverLevel, itemStackWithSlot.stack());
                }
                return;
            }
            this.setItem(itemStackWithSlot.slot(), itemStackWithSlot.stack());
        }

        @Override
        public boolean stillValid(@NotNull Player player) {
            return !this.pet.isRemoved() && this.pet.chestedCompanions$getInventory() == this && player.canInteractWithEntity(this.pet, 4.0F);
        }
    }
}
