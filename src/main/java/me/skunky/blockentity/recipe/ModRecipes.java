package me.skunky.blockentity.recipe;

import me.skunky.blockentity.be.Be;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {

    /*
    This Class is used to Register your Custom Recipe Types and the coresponding Recipes
     */

    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Be.MOD_ID, MythrilBlasterRecipe.Serializer.ID), MythrilBlasterRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(Be.MOD_ID, MythrilBlasterRecipe.Type.ID), MythrilBlasterRecipe.Type.INSTANCE);
    }
}
