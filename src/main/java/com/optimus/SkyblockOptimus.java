package com.optimus;

import com.optimus.config.Config;
import com.optimus.display.Graphics;
import com.optimus.listener.GUIListener;
import com.optimus.listener.ModuleListener;
import com.optimus.mod.ModuleHandler;
import com.optimus.mod.mods.replay.ReplayCommand;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.IOException;

@Getter
@Mod(modid = SkyblockOptimus.MODID, version = SkyblockOptimus.VERSION)
public class SkyblockOptimus {
    public static final String MODID = "optimus";
    public static final String VERSION = "1.0";
    private static SkyblockOptimus instance;

    private final Minecraft mc = Minecraft.getMinecraft();
    private Config config;
    private Graphics graphics;
    private ModuleHandler moduleHandler;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;

        moduleHandler = new ModuleHandler();
        graphics =  new Graphics();
        config = new Config();

        config.initialize();

        ClientCommandHandler.instance.registerCommand(new OptimusCommand());
        ClientCommandHandler.instance.registerCommand(new ReplayCommand());

        MinecraftForge.EVENT_BUS.register(graphics);
        MinecraftForge.EVENT_BUS.register(new GUIListener());
        MinecraftForge.EVENT_BUS.register(new ModuleListener());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                config.saveToDisk();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public ModuleHandler getModHandler() { return getModuleHandler(); }

    public static SkyblockOptimus getInstance() {
        return instance;
    }
}