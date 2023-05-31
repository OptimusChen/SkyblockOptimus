package com.optimus.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected ModuleGui gui;

    public Module(ModuleGui gui) {
        this.gui = gui;
    }

    public abstract String getName();
    public void init() {}

    public void tick() { }

    public ModuleGui getGUI() { return gui; }

    public Module() {
        init();
    }

    public void onReceivePacket(Packet<?> packet) { }
}
