package com.optimus.listener;

import com.optimus.SkyblockOptimus;
import com.optimus.mod.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleListener {

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        for (Module mod : SkyblockOptimus.getInstance().getMods().mods) {
            mod.tick();
        }
    }
}
