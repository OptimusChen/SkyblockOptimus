package com.optimus.listener;

import com.optimus.gui.OptimusGuiScreen;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GUIListener {

    @SubscribeEvent
    public void onGui(GuiScreenEvent.InitGuiEvent.Post e) {
        if (Util.isGuiInstance(e.gui, GuiIngameMenu.class)) {
            e.buttonList.add(new GuiButton(e.buttonList.size() + 1, 30, e.gui.height - 20 - 5, Minecraft.getMinecraft().fontRendererObj.getStringWidth("Optimus") + 10, 20, "Optimus"));
        }
    }

    @SubscribeEvent
    public void onGui(GuiScreenEvent.ActionPerformedEvent.Post e) {
        if (Util.isGuiInstance(e.gui, GuiIngameMenu.class) && e.button.displayString.equals("Optimus")) {
            Minecraft.getMinecraft().displayGuiScreen(new OptimusGuiScreen());
        }
    }
}
