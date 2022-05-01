package me.skunky.blockentity.screen;

import me.skunky.blockentity.be.Be;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandler {
    /*
    This class is used to register your Custom ScreenHandler for every custom Block Entity
    public static ScreenHandlerType<BLOCK ENTITY SCREEN HANDLER> SCREEN_HANDLER_NAME;
     */

    public static ScreenHandlerType<MythrilBlasterScreenHandler> MYTHRIL_BLASTER_SCREEN_HANDLER;

    public static void registerScreenHandler() {
        MYTHRIL_BLASTER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Be.MOD_ID, "mythril_blaster"), MythrilBlasterScreenHandler::new);
    }
}
