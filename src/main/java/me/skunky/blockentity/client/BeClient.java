package me.skunky.blockentity.client;

import me.skunky.blockentity.block.initBlocks;
import me.skunky.blockentity.screen.ModScreenHandler;
import me.skunky.blockentity.screen.MythrilBlasterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

public class BeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(initBlocks.MYTHRIL_BLASTER, RenderLayer.getCutout());

        ScreenRegistry.register(ModScreenHandler.MYTHRIL_BLASTER_SCREEN_HANDLER, MythrilBlasterScreen::new);
    }
}
