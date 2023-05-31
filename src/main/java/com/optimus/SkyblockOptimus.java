package com.optimus;

import com.optimus.display.Graphics;
import com.optimus.listener.GUIListener;
import com.optimus.listener.ModuleListener;
import com.optimus.mod.ModuleHandler;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;

@Mod(modid = SkyblockOptimus.MODID, version = SkyblockOptimus.VERSION)
public class SkyblockOptimus {
    public static final String MODID = "optimus";
    public static final String VERSION = "1.0";
    private static SkyblockOptimus instance;

    private final Minecraft mc = Minecraft.getMinecraft();
    private Graphics graphics;
    private ModuleHandler mods;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;

        mods = new ModuleHandler();
        graphics =  new Graphics();

        ClientCommandHandler.instance.registerCommand(new OptimusCommand());

        MinecraftForge.EVENT_BUS.register(graphics);
        MinecraftForge.EVENT_BUS.register(new GUIListener());
        MinecraftForge.EVENT_BUS.register(new ModuleListener());
    }

    public ModuleHandler getModHandler() { return mods; }
    public Graphics getGraphics() { return graphics; }

    public static SkyblockOptimus getInstance() {
        return instance;
    }
}