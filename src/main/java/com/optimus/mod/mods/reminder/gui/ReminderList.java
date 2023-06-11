package com.optimus.mod.mods.reminder.gui;

import com.optimus.mod.mods.reminder.Reminder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class ReminderList extends GuiScrollingList {

    public List<Reminder> elements;
    public int selected = -1;

    public ReminderList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, List<Reminder> elements) {
        super(client, width, height, top, bottom, left, entryHeight);

        this.elements = elements;
    }

    @Override
    protected int getSize() {
        return elements.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        selected = index;
    }

    @Override
    protected boolean isSelected(int index) {
        return index == selected;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        Reminder reminder = elements.get(slotIdx);
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

        String text = (slotIdx + 1) + ": " + reminder.getTrigger().getName() + " " + reminder.getTriggerString();
        font.drawString(text, entryRight - 200, slotTop + 2, Color.WHITE.getRGB());
    }
}
