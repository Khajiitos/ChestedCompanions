package me.khajiitos.chestedcompanions.common.util;

import me.khajiitos.chestedcompanions.common.config.CCConfig;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ChestEntityCommon {

    public static <T extends TamableAnimal & IChestEntity> void mobInteract(T chestEntity, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!chestEntity.level.isClientSide && player.isCrouching() == CCConfig.invertShiftToOpen.get() && player.getUUID().equals(chestEntity.getOwnerUUID())) {
            if (chestEntity.chestedCompanions$hasChest()) {
                ItemStack inHand = player.getItemInHand(interactionHand);
                if (inHand.is(Items.SHEARS)) {
                    inHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(interactionHand));

                    chestEntity.chestedCompanions$removeChestContent(!player.getAbilities().instabuild);
                    chestEntity.playSound(SoundEvents.DONKEY_CHEST, 1.0F, 1.5F);
                } else {
                    chestEntity.openCustomInventoryScreen(player);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else if (chestEntity.chestedCompanions$allowChest() && (!chestEntity.isBaby() || chestEntity.chestedCompanions$allowChestOnBaby())) {
                ItemStack inHand = player.getItemInHand(interactionHand);

                if (chestEntity.chestedCompanions$isValidChestItem(inHand)) {
                    ItemStack newItemStack = inHand.copy();
                    newItemStack.setCount(1);
                    chestEntity.chestedCompanions$setChestItemStack(newItemStack);

                    if (!player.getAbilities().instabuild) {
                        inHand.shrink(1);
                    }

                    chestEntity.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (chestEntity.getRandom().nextFloat() - chestEntity.getRandom().nextFloat()) * 0.2F + 1.0F);
                    chestEntity.chestedCompanions$createInventory();
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }

    public static <T extends TamableAnimal & IChestEntity> void addAdditionalSaveData(T chestEntity, CompoundTag compoundTag) {
        if (chestEntity.chestedCompanions$hasChest()) {
            ItemStack chestItemStack = chestEntity.chestedCompanions$getChestItemStack();
            CompoundTag chestCompoundTag = new CompoundTag();
            chestItemStack.save(chestCompoundTag);
            compoundTag.put("ChestItem", chestCompoundTag);
        }

        if (chestEntity.chestedCompanions$getInventory() != null) {
            compoundTag.put("CCItems", chestEntity.chestedCompanions$getInventory().createTag());
        }
    }

    public static <T extends TamableAnimal & IChestEntity> void readAdditionalSaveData(T chestEntity, CompoundTag compoundTag) {
        CompoundTag itemTag = compoundTag.getCompound("ChestItem");

        ItemStack chestItemStack;
        if (itemTag.contains("id", CompoundTag.TAG_STRING)) {
            ResourceLocation resourceLocation = new ResourceLocation(itemTag.getString("id"));
            if (!Registry.ITEM.containsKey(resourceLocation)) {
                // Probably loaded a world after removing a mod - replace with vanilla chest
                chestItemStack = new ItemStack(Items.CHEST);
            } else {
                chestItemStack = ItemStack.of(itemTag);
            }
        } else if (compoundTag.getBoolean("HasChest")) {
            // Loaded the mod with an old version of the mod that didn't keep the ItemStack - replace with vanilla chest
            chestItemStack = new ItemStack(Items.CHEST);
        } else {
            // No chest
            return;
        }

        if (!chestItemStack.isEmpty()) {
            chestEntity.chestedCompanions$setChestItemStack(chestItemStack);
            chestEntity.chestedCompanions$createInventory();
            chestEntity.chestedCompanions$getInventory().fromTag(compoundTag.getList("CCItems", ListTag.TAG_COMPOUND));
        }
    }

    public static <T extends TamableAnimal & IChestEntity> void removeChestContent(T chestEntity, boolean dropChest) {
        IChestEntity.PetChestContainer<?> inventory = chestEntity.chestedCompanions$getInventory();
        if (inventory != null) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);

                if (!itemStack.isEmpty()) {
                    chestEntity.spawnAtLocation(itemStack, 0.25f);
                }
            }

            if (dropChest) {
                ItemStack chestItemStack = chestEntity.chestedCompanions$getChestItemStack();
                if (!chestItemStack.isEmpty()) {
                    chestEntity.spawnAtLocation(chestItemStack, 0.25f);
                }
            }

            chestEntity.chestedCompanions$setChestItemStack(ItemStack.EMPTY);
            chestEntity.chestedCompanions$removeInventory();
        }
    }

    public static <T extends TamableAnimal & IChestEntity> AbstractContainerMenu createMenu(T chestEntity, int i, @NotNull Inventory inventory, @NotNull Player player) {
        InventoryCapacity inventoryCapacity = chestEntity.chestedCompanions$getInventoryCapacity();

        // Needs resizing, probably because of config change
        if (chestEntity.chestedCompanions$getInventory() != null && chestEntity.chestedCompanions$getInventory().getContainerSize() != inventoryCapacity.containerRows * 9) {
            chestEntity.chestedCompanions$createInventory();
        }

        return new ChestMenu(inventoryCapacity.menuType, i, inventory, chestEntity.chestedCompanions$getInventory(), inventoryCapacity.containerRows);
    }
}