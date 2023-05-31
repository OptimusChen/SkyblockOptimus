package com.optimus.listener;

import com.optimus.SkyblockOptimus;
import com.optimus.mod.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleListener {

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        for (Module mod : SkyblockOptimus.getInstance().getModHandler().getMods()) {
            mod.tick();
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent e) {
        for (Module mod : SkyblockOptimus.getInstance().getModHandler().getMods()) {
            mod.onKeyPressed(e);
        }
    }
}
