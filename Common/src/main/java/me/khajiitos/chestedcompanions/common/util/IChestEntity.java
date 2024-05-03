package me.khajiitos.chestedcompanions.common.util;

import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
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

    class PetChestContainer<T extends TamableAnimal> extends SimpleContainer {
        private final T pet;

        // Used for increasing the size of the container, or shrinking it and dropping items
        public PetChestContainer(T pet, int inventorySlots, @Nullable PetChestContainer<?> oldContainer, HolderLookup.Provider provider) {
            super(inventorySlots);
            this.pet = pet;

            if (oldContainer != null) {
                this.fromTag(oldContainer.createTag(provider), provider);
            }
        }

        @Override
        public void fromTag(ListTag listTag, @NotNull HolderLookup.Provider provider) {
            this.clearContent();

            // Fixes items being mashed together when reopening the world
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag = listTag.getCompound(i);
                int slot = compoundTag.getByte("Slot") & 255;
                ItemStack itemStack = ItemStack.parseOptional(provider, compoundTag);

                if (slot >= this.getContainerSize()) {
                    this.pet.spawnAtLocation(itemStack);
                } else {
                    this.setItem(slot, itemStack);
                }
            }
        }

        @Override
        public @NotNull ListTag createTag(@NotNull HolderLookup.Provider provider) {
            ListTag listTag = new ListTag();

            for (int i = 0; i < this.getContainerSize();i++) {
                ItemStack itemStack = this.getItem(i);
                if (!itemStack.isEmpty()) {
                    Tag tag = itemStack.save(provider);
                    if (tag instanceof CompoundTag compoundTag) {
                        compoundTag.putByte("Slot", (byte)i);
                    }
                    listTag.add(tag);
                }
            }

            return listTag;
        }
    }
}
