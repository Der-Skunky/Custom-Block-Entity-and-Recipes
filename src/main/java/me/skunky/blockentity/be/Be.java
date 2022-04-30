package me.skunky.blockentity.be;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.logging.Logger;

public class Be implements ModInitializer {
    public static final String MOD_ID = "be";
    public static final Logger LOG = Logger.getLogger(MOD_ID);
    public static final ItemGroup BLOCK_ENTITY = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "BlockEntity"), () -> new ItemStack(Items.CLOCK));

    @Override
    public void onInitialize() {

    }
}
