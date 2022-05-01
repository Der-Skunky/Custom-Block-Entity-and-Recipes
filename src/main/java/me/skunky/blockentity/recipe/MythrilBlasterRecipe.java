package me.skunky.blockentity.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class MythrilBlasterRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItem;

    public MythrilBlasterRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItem) {
        this.id = id;
        this.output = output;
        this.recipeItem = recipeItem;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient()) { return false; }
        if(recipeItem.get(0).test(inventory.getStack(1))) {
            return recipeItem.get(1).test(inventory.getStack(2));
        }
        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<MythrilBlasterRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "mythril_blaster";
    }

    public static class Serializer implements RecipeSerializer<MythrilBlasterRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "mythril_blaster";

        @Override // Sync recipe on Client
        public MythrilBlasterRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(2, Ingredient.EMPTY); //defines the amount of Input Slots

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new MythrilBlasterRecipe(id, output, inputs);
        }

        @Override // Sync recipe on Server
        public MythrilBlasterRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new MythrilBlasterRecipe(id, output, inputs);
        }

        @Override // Sync recipe on Server
        public void write(PacketByteBuf buf, MythrilBlasterRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for(Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
