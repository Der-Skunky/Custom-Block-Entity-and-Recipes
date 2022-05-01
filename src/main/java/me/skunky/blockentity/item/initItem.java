package me.skunky.blockentity.item;

import me.skunky.blockentity.be.Be;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class initItem {

    public static final Item MYTHRIL_INGOT = registerItem("mythril_ingot",
            new Item(new FabricItemSettings().group(Be.BLOCK_ENTITY)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Be.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Be.LOG.info("Registering Mod Items for " + Be.MOD_ID);
    }
}
