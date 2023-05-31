package com.optimus.gui;

import com.optimus.SkyblockOptimus;
import com.optimus.mod.Module;
import com.optimus.mod.ModuleHandler;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class OptimusGuiScreen extends GuiScreen {

    private static final Minecraft mc = Minecraft.getMinecraft();



    @Override
    public void initGui() {
        super.initGui();

        ModuleHandler mods = SkyblockOptimus.getInstance().getModHandler();

        int row = 5;

        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonMargin = 10;
        int startX = (this.width - row * (buttonWidth + buttonMargin)) / 2;
        int startY = (this.height - buttonHeight) / 3;

        int i = 0;
        int buttonId = 0;
        for (Module mod : mods.getMods()) {

            System.out.println(mod.getName());
            GuiButton button = new GuiButton(buttonId, startX + i * (buttonWidth + buttonMargin), startY, buttonWidth, buttonHeight, mod.getName());
            this.buttonList.add(button);

            i++;
            buttonId++;

            if (i % row == 0) {
                startY += buttonHeight + 10;

                i = 0;
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        ModuleHandler mods = SkyblockOptimus.getInstance().getModHandler();

        Module mod = mods.getMod(button.id);
        mc.displayGuiScreen(mod.getGUI());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        Util.drawCenteredString("SkyblockOptimus", Color.CYAN.getRGB(), 10f, 3f);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
