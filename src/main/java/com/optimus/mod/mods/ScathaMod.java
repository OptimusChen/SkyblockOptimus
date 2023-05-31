package com.optimus.mod.mods;

import com.optimus.SkyblockOptimus;
import com.optimus.mod.Toggleable;
import com.optimus.util.Util;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

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

            SkyblockOptimus.getInstance().text = "Worm Spawned!";

            Util.delayTask(2000, () -> SkyblockOptimus.getInstance().text = null);
        }
    }
}
