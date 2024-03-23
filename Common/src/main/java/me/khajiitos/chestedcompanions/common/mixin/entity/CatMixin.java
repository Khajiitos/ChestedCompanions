package me.khajiitos.chestedcompanions.common.mixin.entity;

import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.common.util.IChestEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal implements IChestEntity {
    // TODO: do something to avoid code repetition in the WolfMixin class
    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<Boolean> HAS_CHEST = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);

    @Unique
    protected PetChestContainer<Cat> inventory;

    @Unique
    protected ItemStack chestItemStack;

    protected CatMixin(EntityType<? extends TamableAnimal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean hasChest() {
        return this.getEntityData().get(HAS_CHEST);
    }

    @Override
    public void setHasChest(boolean hasChest) {
        this.getEntityData().set(HAS_CHEST, hasChest);
    }

    @Override
    public int getInventorySlots() {
        return CCConfig.catInventoryCapacity.get().containerRows * 9;
    }

    @Override
    public Container getInventory() {
        return this.inventory;
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    public void defineSynchedData(CallbackInfo ci) {
        this.getEntityData().define(HAS_CHEST, false);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this.hasChest()) {
            compoundTag.putBoolean("HasChest", this.hasChest());

            CompoundTag chestCompoundTag = new CompoundTag();
            (this.chestItemStack != null ? this.chestItemStack : new ItemStack(Items.CHEST)).save(chestCompoundTag);
            compoundTag.put("ChestItem", chestCompoundTag);
        }

        if (this.inventory != null) {
            compoundTag.put("CCItems", this.inventory.createTag());
        }
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.setHasChest(compoundTag.getBoolean("HasChest"));
        this.createInventory();

        if (this.hasChest()) {
            this.createInventory();
            this.inventory.fromTag(compoundTag.getList("CCItems", ListTag.TAG_COMPOUND));

            this.chestItemStack = compoundTag.contains("ChestItem", CompoundTag.TAG_COMPOUND) ? ItemStack.of(compoundTag.getCompound("ChestItem")) : ItemStack.EMPTY;

            if (this.chestItemStack == ItemStack.EMPTY) {
                this.chestItemStack = new ItemStack(Items.CHEST, 1);
            }
        }
    }

    @Unique
    private void createInventory() {
        this.inventory = new PetChestContainer<>((Cat)(Object)this, this.getInventorySlots(), this.inventory);
    }

    @Unique
    private void removeChestContent(boolean dropChest) {
        if (this.inventory != null) {
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack itemStack = this.inventory.getItem(i);

                if (!itemStack.isEmpty()) {
                    this.spawnAtLocation(itemStack, 0.25f);
                }
            }

            if (dropChest) {
                this.spawnAtLocation(this.chestItemStack != null ? this.chestItemStack : new ItemStack(Items.CHEST), 0.25f);
            }

            this.chestItemStack = null;
            this.inventory = null;
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        // Needs resizing, probably because of config change
        if (this.inventory != null && this.inventory.getContainerSize() != this.getInventorySlots()) {
            this.createInventory();
        }

        return new ChestMenu(CCConfig.catInventoryCapacity.get().menuType, i, inventory, this.getInventory(), CCConfig.catInventoryCapacity.get().containerRows);
    }

    public void openCustomInventoryScreen(Player player) {
        player.openMenu(this);
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    public void mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!this.level.isClientSide && player.isCrouching() == CCConfig.invertShiftToOpen.get() && (CCConfig.publicChest.get() || player.getUUID().equals(this.getOwnerUUID()))) {
            if (this.hasChest()) {
                ItemStack inHand = player.getItemInHand(interactionHand);

                if (inHand.is(Items.SHEARS)) {
                    inHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(interactionHand));

                    this.setHasChest(false);
                    this.removeChestContent(!player.getAbilities().instabuild);
                    this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, 1.5F);
                } else {
                    this.openCustomInventoryScreen(player);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else if (CCConfig.allowChestOnCats.get() && (!this.isBaby() || CCConfig.allowChestOnBabyCat.get())) {
                ItemStack inHand = player.getItemInHand(interactionHand);

                if (inHand.is(Items.CHEST)) {
                    this.chestItemStack = inHand.copy();
                    this.chestItemStack.setCount(1);

                    if (!player.getAbilities().instabuild) {
                        inHand.shrink(1);
                    }

                    this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.setHasChest(true);
                    this.createInventory();
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.hasChest()) {
            this.removeChestContent(true);
        }
    }
}
