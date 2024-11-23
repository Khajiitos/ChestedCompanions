package me.khajiitos.chestedcompanions.common.mixin.entity;

import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.common.util.ChestEntityCommon;
import me.khajiitos.chestedcompanions.common.util.IChestEntity;
import me.khajiitos.chestedcompanions.common.util.InventoryCapacity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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

    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<ItemStack> chestedCompanions$CHEST_ITEM = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.ITEM_STACK);

    @Unique
    protected PetChestContainer<?> chestedCompanions$inventory;

    protected CatMixin(EntityType<? extends TamableAnimal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public InventoryCapacity chestedCompanions$getInventoryCapacity() {
        return CCConfig.catInventoryCapacity.get();
    }

    @Override
    public boolean chestedCompanions$allowChest() {
        return CCConfig.allowChestOnCats.get();
    }

    @Override
    public boolean chestedCompanions$allowChestOnBaby() {
        return CCConfig.allowChestOnBabyCat.get();
    }

    @Override
    public PetChestContainer<?> chestedCompanions$getInventory() {
        return this.chestedCompanions$inventory;
    }

    @Override
    public ItemStack chestedCompanions$getChestItemStack() {
        return this.getEntityData().get(chestedCompanions$CHEST_ITEM);
    }

    public void chestedCompanions$setChestItemStack(ItemStack itemStack) {
        this.getEntityData().set(chestedCompanions$CHEST_ITEM, itemStack);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    public void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(chestedCompanions$CHEST_ITEM, ItemStack.EMPTY);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        ChestEntityCommon.addAdditionalSaveData(this, compoundTag);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        ChestEntityCommon.readAdditionalSaveData(this, compoundTag);
    }

    @Unique
    @Override
    public void chestedCompanions$createInventory() {
        this.chestedCompanions$inventory = new IChestEntity.PetChestContainer<>(this, this.chestedCompanions$getInventoryCapacity().containerRows * 9, this.chestedCompanions$inventory, this.registryAccess());
    }

    @Override
    public void chestedCompanions$removeInventory() {
        this.chestedCompanions$inventory = null;
    }

    @Unique
    public void chestedCompanions$removeChestContent(boolean dropChest) {
        ChestEntityCommon.removeChestContent(this, dropChest);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return ChestEntityCommon.createMenu(this, i, inventory, player);
    }

    public void openCustomInventoryScreen(Player player) {
        player.openMenu(this);
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    public void mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        ChestEntityCommon.mobInteract(this, player, interactionHand, cir);
    }

    @Override
    protected void dropEquipment(@NotNull ServerLevel serverLevel) {
        super.dropEquipment(serverLevel);
        if (this.chestedCompanions$hasChest()) {
            this.chestedCompanions$removeChestContent(true);
        }
    }
}
