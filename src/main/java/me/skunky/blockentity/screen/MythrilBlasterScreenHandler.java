package me.skunky.blockentity.screen;

import me.skunky.blockentity.screen.slot.ModFuelSlot;
import me.skunky.blockentity.screen.slot.ModOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class MythrilBlasterScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public MythrilBlasterScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4), new ArrayPropertyDelegate(4));
    }

    public MythrilBlasterScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandler.MYTHRIL_BLASTER_SCREEN_HANDLER, syncId);
        checkSize(inventory, 4);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        inventory.onOpen(playerInventory.player);

        this.addSlot(new ModFuelSlot(inventory, 0, 18, 50));
        this.addSlot(new Slot(inventory, 1, 66, 16));
        this.addSlot(new Slot(inventory, 2, 66, 50));
        this.addSlot(new ModOutputSlot(inventory, 3, 114, 33));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public boolean hasFuel() {
        return propertyDelegate.get(2) > 0;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress + progressArrowSize / maxProgress : 0;
    }

    public int getScaledFuelProgress() {
        int fuelProgress = this.propertyDelegate.get(2);
        int maxFuelProgress = this.propertyDelegate.get(3);
        int fuelProgressSize = 14;

        return maxFuelProgress != 0 ? (int)(((float)fuelProgress / (float)maxFuelProgress) * fuelProgressSize) : 0;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if(slot != null && slot.hasStack()) {
            ItemStack oldStack = slot.getStack();
            newStack = oldStack.copy();
            if(invSlot < this.inventory.size()) {
                if(!this.insertItem(oldStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }

                if(oldStack.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
            }
        }
        return newStack;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for(int i = 0; i<3; i++) {
            for (int l = 0; l<9; l++) {
                this.addSlot(new Slot(playerInventory, l+i*9+9, 8+l*18, 86+i*18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i<9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8+i*18, 144));
        }
    }
}
