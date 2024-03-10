package me.khajiitos.chestedcompanions.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IChestEntity extends HasCustomInventoryScreen, MenuProvider {
    boolean hasChest();
    void setHasChest(boolean hasChest);
    int getInventorySlots();
    Container getInventory();

    class PetChestContainer<T extends TamableAnimal> extends SimpleContainer {
        private final T pet;

        public PetChestContainer(T pet, int inventorySlots) {
            this(pet, inventorySlots, null);
        }

        // Used for increasing the size of the container, or shrinking it and dropping items
        public PetChestContainer(T pet, int inventorySlots, @Nullable PetChestContainer<T> oldContainer) {
            super(inventorySlots);
            this.pet = pet;

            if (oldContainer != null) {
                this.fromTag(oldContainer.createTag());
            }
        }

        @Override
        public void fromTag(ListTag listTag) {
            this.clearContent();

            // Fixes items being mashed together when reopening the world
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag = listTag.getCompound(i);
                int slot = compoundTag.getByte("Slot") & 255;
                ItemStack itemStack = ItemStack.of(compoundTag);

                if (slot >= this.getContainerSize()) {
                    this.pet.spawnAtLocation(itemStack);
                } else {
                    this.setItem(slot, itemStack);
                }
            }
        }

        @Override
        public @NotNull ListTag createTag() {
            ListTag listTag = new ListTag();

            for (int i = 0; i < this.getContainerSize();i++) {
                ItemStack itemStack = this.getItem(i);
                if (!itemStack.isEmpty()) {
                    CompoundTag tag = new CompoundTag();
                    tag.putByte("Slot", (byte)i);
                    listTag.add(itemStack.save(tag));
                }
            }

            return listTag;
        }
    }
}
