package com.optimus;

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

import java.awt.*;

@Mod(modid = SkyblockOptimus.MODID, version = SkyblockOptimus.VERSION)
public class SkyblockOptimus {
    public static final String MODID = "optimus";
    public static final String VERSION = "1.0";
    private static SkyblockOptimus instance;

    public static SkyblockOptimus getInstance() {
        return instance;
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    private ModuleHandler mods;
    public String text = null;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;

        mods = new ModuleHandler();

        ClientCommandHandler.instance.registerCommand(new OptimusCommand());

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new GUIListener());
        MinecraftForge.EVENT_BUS.register(new ModuleListener());
    }

    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent.Text event) {
        if (text == null) return;

        Util.drawCenteredString(text, Color.RED.getRGB(), 5f);
    }

    public ModuleHandler getMods() { return mods; }
}