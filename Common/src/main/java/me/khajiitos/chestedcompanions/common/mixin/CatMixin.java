package me.khajiitos.chestedcompanions.common.mixin;

import me.khajiitos.chestedcompanions.common.IChestEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
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
    private static final EntityDataAccessor<Boolean> HAS_CHEST = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);
    @Unique
    protected SimpleContainer inventory;

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
        return 9;
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
        }

        if (this.inventory != null) {
            compoundTag.put("Items", this.inventory.createTag());
        }
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.setHasChest(compoundTag.getBoolean("HasChest"));
        this.createInventory();

        if (this.hasChest()) {
            this.inventory.fromTag(compoundTag.getList("Items", ListTag.TAG_LIST));
        }
    }

    private void createInventory() {
        this.inventory = new SimpleContainer(this.getInventorySlots());
    }

    private void removeChestContent() {
        if (this.inventory != null) {
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack itemStack = this.inventory.getItem(i);

                if (!itemStack.isEmpty()) {
                    this.spawnAtLocation(itemStack, 0.25f);
                }
            }
            this.spawnAtLocation(new ItemStack(Items.CHEST), 0.25f);
            this.inventory = null;
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ChestMenu(MenuType.GENERIC_9x1, i, inventory, this.getInventory(), 1);
    }

    public void openCustomInventoryScreen(Player player) {
        player.openMenu(this);
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    public void mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!this.level.isClientSide && !player.isCrouching() && player.getUUID().equals(this.getOwnerUUID())) {
            if (this.hasChest()) {
                ItemStack inHand = player.getItemInHand(interactionHand);

                if (inHand.is(Items.SHEARS)) {
                    inHand.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(interactionHand);
                    });

                    this.setHasChest(false);
                    this.removeChestContent();
                    this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, 1.5F);
                } else {
                    this.openCustomInventoryScreen(player);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                ItemStack inHand = player.getItemInHand(interactionHand);

                if (inHand.is(Items.CHEST)) {
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
}
