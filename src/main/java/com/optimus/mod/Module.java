package com.optimus.mod;

import com.optimus.SkyblockOptimus;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final SkyblockOptimus main = SkyblockOptimus.getInstance();

    protected ModuleGui gui;

    public Module(ModuleGui gui) {
        this.gui = gui;
    }

    public abstract String getName();
    public void init() {}

    public void tick() { }

    public void onKeyPressed(InputEvent.KeyInputEvent e) { }

    public ModuleGui getGUI() { return gui; }

    public Module() {
        init();
    }

    public void onReceivePacket(Packet<?> packet) { }
}
