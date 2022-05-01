package me.skunky.blockentity.entity;

import me.skunky.blockentity.inventory.ImplementedInventory;
import me.skunky.blockentity.recipe.MythrilBlasterRecipe;
import me.skunky.blockentity.screen.MythrilBlasterScreenHandler;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MythrilBlasterEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    // The Default List sets the Inventory Size of the Entity
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    // The PropertyDelegate is important to sync the Block states
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public MythrilBlasterEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntites.MYTHRIL_BLASTER, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index){
                    case 0: return MythrilBlasterEntity.this.progress;
                    case 1: return MythrilBlasterEntity.this.maxProgress;
                    case 2: return MythrilBlasterEntity.this.fuelTime;
                    case 3: return MythrilBlasterEntity.this.maxFuelTime;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0: MythrilBlasterEntity.this.progress = value; break;
                    case 1: MythrilBlasterEntity.this.maxProgress = value; break;
                    case 2: MythrilBlasterEntity.this.fuelTime = value; break;
                    case 3: MythrilBlasterEntity.this.maxFuelTime = value; break;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("Mythril Blaster");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MythrilBlasterScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("blaster.progress", progress);
        nbt.putInt("blaster.fuelTime", fuelTime);
        nbt.putInt("blaster.maxFuelTime", maxFuelTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("blaster.progress");
        fuelTime = nbt.getInt("blaster.fuelTime");
        maxFuelTime = nbt.getInt("blaster.maxFuelTime");
    }

    /*
    This functions are needed to Craft in your Block entity
     */

    // Entity Checks if the Items match a Recipe
    private static boolean hasRecipe(MythrilBlasterEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<MythrilBlasterRecipe> match = world.getRecipeManager().getFirstMatch(MythrilBlasterRecipe.Type.INSTANCE, inventory, world);

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, match.get().getOutput());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inv, ItemStack output) {
        return inv.getStack(3).getItem() == output.getItem() || inv.getStack(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inv) {
        return inv.getStack(3).getMaxCount() > inv.getStack(3).getCount();
    }

    private static void craftItem(MythrilBlasterEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<MythrilBlasterRecipe> match = world.getRecipeManager()
                .getFirstMatch(MythrilBlasterRecipe.Type.INSTANCE, inventory, world);

        if(match.isPresent()) {
            entity.removeStack(1,1);
            entity.removeStack(2,1);

            entity.setStack(3, new ItemStack(match.get().getOutput().getItem(),
                    entity.getStack(3).getCount() + 1));

            entity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    /*
    This functions get Called every Tick, it is not needed if you don't need Fuel
     */
    public static void tick(World world, BlockPos pos, BlockState state, MythrilBlasterEntity entity) {
        if(isConsumingFuel(entity)) {
            entity.fuelTime--;
        }
        if(hasRecipe(entity)) {
            if(hasFuel(entity) && !isConsumingFuel(entity)) {
                entity.consumeFuel();
            }
            if(isConsumingFuel(entity)) {
                entity.progress++;
                if(entity.progress > entity.maxProgress) {
                    craftItem(entity);
                }
            }
        } else {
            entity.resetProgress();
        }
    }

    private static boolean hasFuel(MythrilBlasterEntity entity) {
        return !entity.getStack(0).isEmpty();
    }

    private static boolean isConsumingFuel(MythrilBlasterEntity entity) {
        return entity.fuelTime > 0;
    }

    private void consumeFuel() {
        if(!getStack(0).isEmpty()) {
            this.fuelTime = FuelRegistry.INSTANCE.get(this.removeStack(0, 1).getItem());
            this.maxFuelTime = this.fuelTime;
        }
    }
}
