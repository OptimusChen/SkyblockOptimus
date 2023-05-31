package com.optimus.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ModuleGui extends GuiScreen {

    private final Minecraft mc = Minecraft.getMinecraft();
    protected final Module mod;
    private String status = "";
    private int statusColor = 0;

    public ModuleGui(Module mod) {
        this.mod = mod;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.5, 1.5, 1.5);
        drawString(mc.fontRendererObj, mod.getName(), width / 2, height / 20, 0xFFFFFF);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.5, 1.5, 1.5);
        drawCenteredString(mc.fontRendererObj, status, width / 2 - 10, height / 5 - 25, statusColor);
        GlStateManager.popMatrix();
    }

    public void setStatusText(String text, int color) {
        this.status = text;
        this.statusColor = color;
    }
}
