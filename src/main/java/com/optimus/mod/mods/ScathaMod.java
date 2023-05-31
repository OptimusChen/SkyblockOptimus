package com.optimus.mod.mods;

import com.optimus.SkyblockOptimus;
import com.optimus.mod.Toggleable;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

import java.awt.*;

public class ScathaMod extends Toggleable {
    @Override
    public String getName() {
        return "Scatha Farming";
    }

    @Override
    public void onReceivePacket(Packet<?> packet) {
        if (!isEnabled()) return;
        if (!(packet instanceof S02PacketChat)) return;

        S02PacketChat chat = (S02PacketChat) packet;

        String message = chat.getChatComponent().getUnformattedText();

        if (message.contains("You hear the sound of something approaching...")) {
            mc.thePlayer.playSound("random.orb", 1, 0.5f);

            main.getGraphics().sendTitle("Worm Spawned!", "", Color.RED.getRGB());
        }
    }
}
