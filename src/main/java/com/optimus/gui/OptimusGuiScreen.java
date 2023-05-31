package com.optimus.gui;

import com.optimus.SkyblockOptimus;
import com.optimus.mod.Module;
import com.optimus.mod.ModuleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class OptimusGuiScreen extends GuiScreen {

    private static final Minecraft mc = Minecraft.getMinecraft();



    @Override
    public void initGui() {
        super.initGui();

        ModuleHandler mods = SkyblockOptimus.getInstance().getMods();

        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonMargin = 10;
        int startX = (this.width - mods.mods.size() * (buttonWidth + buttonMargin)) / 2;
        int startY = (this.height - buttonHeight) / 2;

        int i = 0;
        int buttonId = 0;
        for (Module mod : mods.mods) {

            System.out.println(mod.getName());
            GuiButton button = new GuiButton(buttonId, startX + i * (buttonWidth + buttonMargin), startY, buttonWidth, buttonHeight, mod.getName());
            this.buttonList.add(button);

            i++;
            buttonId++;

            if (i % 3 == 0) {
                startY += buttonHeight + 10;

                i = 0;
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        ModuleHandler mods = SkyblockOptimus.getInstance().getMods();

        Module mod = mods.getMod(button.id);
        mc.displayGuiScreen(mod.getGUI());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
