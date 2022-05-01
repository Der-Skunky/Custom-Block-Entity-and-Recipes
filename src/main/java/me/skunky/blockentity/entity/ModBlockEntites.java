package me.skunky.blockentity.entity;

import me.skunky.blockentity.be.Be;
import me.skunky.blockentity.block.initBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModBlockEntites {
    /*
    This Class is used to register all Block Entities you Created
    public static BlockEntityType<YOUR BLOCK ENTITY CLASS> ENTITY NAME;
    */

    public static BlockEntityType<MythrilBlasterEntity> MYTHRIL_BLASTER;

    public static void registerBlockEntities() {
        MYTHRIL_BLASTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Be.MOD_ID, "mythril_blaster"),
                FabricBlockEntityTypeBuilder.create(MythrilBlasterEntity::new, initBlocks.MYTHRIL_BLASTER).build(null));
    }
}
