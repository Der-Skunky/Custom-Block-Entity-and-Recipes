package me.skunky.blockentity.block;

import me.skunky.blockentity.be.Be;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class initBlocks {

    public static final Block MYTHRIL_BLASTER = registerBlock("mythril_blaster",
            new MythrilBlaster(FabricBlockSettings.of(Material.METAL)), null);

    private static Block registerBlock(String name, Block block, @Nullable String tooltipkey) {
        Registry.register(Registry.ITEM, new Identifier(Be.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(Be.BLOCK_ENTITY)) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                        if(tooltipkey != null) {
                            tooltip.add(new TranslatableText(tooltipkey));
                        }
                    }
                });
        return Registry.register(Registry.BLOCK, new Identifier(Be.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        Be.LOG.info("Registering ModBlocks for " + Be.MOD_ID);
    }
}
