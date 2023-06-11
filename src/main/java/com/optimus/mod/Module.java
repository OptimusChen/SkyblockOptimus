package com.optimus.mod;

import com.optimus.SkyblockOptimus;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final SkyblockOptimus main = SkyblockOptimus.getInstance();

    public Module() {
        init();
    }

    public void init() {}

    public void tick() { }

    public void onKeyPressed(InputEvent.KeyInputEvent e) { }

    public void onReceivePacket(Packet<?> packet) { }

    public void onRightClick() { }

    public abstract String getName();

    public abstract ModuleGui getGUI();
}
