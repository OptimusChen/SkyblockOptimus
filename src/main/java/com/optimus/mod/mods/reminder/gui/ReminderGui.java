package com.optimus.mod.mods.reminder.gui;

import com.optimus.mod.Module;
import com.optimus.mod.ModuleGui;
import com.optimus.mod.mods.reminder.Reminder;
import com.optimus.mod.mods.reminder.ReminderMod;
import com.optimus.mod.mods.reminder.ReminderTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.awt.*;
import java.io.IOException;

public class ReminderGui extends ModuleGui {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Reminder NEW_REMINDER = new Reminder(ReminderTrigger.CHAT, "Trigger String", "Display Message", Color.RED, 10, false);
    private final ReminderMod mod;
    private Reminder old;
    private Reminder selected = NEW_REMINDER;
    private ReminderList list;
    private GuiTextField triggerStringText;
    private GuiTextField displayStringText;
    private GuiTextField timerText;
    private GuiButton display;
    private GuiButton mode;
    private boolean creatingNew = true;

    public ReminderGui(Module mod) {
        super(mod);

        this.mod = (ReminderMod) mod;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.triggerStringText = new GuiTextField(0, mc.fontRendererObj, width / 2, height / 2, 200, 20);
        this.triggerStringText.setMaxStringLength(Integer.MAX_VALUE);
        this.triggerStringText.setText(selected.getTriggerString());

        this.displayStringText = new GuiTextField(1, mc.fontRendererObj, width / 2, height / 2 - 30, 200, 20);
        this.displayStringText.setMaxStringLength(Integer.MAX_VALUE);
        this.displayStringText.setText(selected.getMessage());

        this.timerText = new GuiTextField(6, mc.fontRendererObj, width / 2, height / 2 + 30, 200, 20);
        this.timerText.setMaxStringLength(Integer.MAX_VALUE);
        this.timerText.setText(selected.getTime() + "");

        this.mode = new GuiButton(2, width / 2, height / 2 - 90, "Mode: " + selected.getTrigger().getName());

        this.buttonList.add(mode);
        this.buttonList.add(new GuiButton(3, width / 2, height / 2 + 60, fontRendererObj.getStringWidth("Save") + 30, 20, "Save"));
        this.buttonList.add(new GuiButton(4, width / 2 + 120, height / 2 + 60, fontRendererObj.getStringWidth("Cancel") + 30, 20, "Cancel"));
        this.buttonList.add(new GuiButton(5, width / 2 + 57, height / 2 + 60, fontRendererObj.getStringWidth("Delete") + 30, 20, "Delete"));

        this.display = new GuiButton(6, width / 2, height / 2 - 60, "Display: " + selected.isDisplay());

        this.buttonList.add(display);

        list = new ReminderList(mc, 215, height - 10, 0, height, 0, 20, mod.getReminders());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        list.drawScreen(mouseX, mouseY, partialTicks);

        if (list.selected != -1 && creatingNew) {
            old = list.elements.get(list.selected);
            selected = new Reminder(old.getTrigger(), old.getTriggerString(), old.getMessage(), old.getColor(), old.getTime(), old.isDisplay());

            mode.displayString = "Mode: " + selected.getTrigger().getName();
            display.displayString = "Display: " + selected.isDisplay();

            triggerStringText.setText(selected.getTriggerString());
            displayStringText.setText(selected.getMessage());
            timerText.setText(selected.getTime() + "");
        }

        if (list.selected != -1) {
            creatingNew = false;
        }

        if (!creatingNew) {
            drawString(fontRendererObj, "Editing Reminder", width / 2, height / 2 - 120, Color.WHITE.getRGB());
        } else {
            drawString(fontRendererObj, "Creating new Reminder", width / 2, height / 2 - 120, Color.WHITE.getRGB());
        }

        triggerStringText.drawTextBox();
        displayStringText.drawTextBox();
        timerText.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {
            case 2:
                selected.setTrigger(selected.getTrigger().getNext());
                button.displayString = "Mode: " + selected.getTrigger().getName();
                break;
            case 3:
                selected.setTime(Integer.parseInt(timerText.getText()));
                selected.setMessage(displayStringText.getText());
                selected.setTriggerString(triggerStringText.getText());

                mod.getReminders().add(selected);

                if (!creatingNew) {
                    mod.getReminders().remove(old);

                    old = null;
                }

                list.selected = -1;
                selected = new Reminder(ReminderTrigger.CHAT, "Trigger String", "Display Message", Color.RED, 10, false);;

                reset();

                creatingNew = true;
                break;
            case 4:
                list.selected = -1;
                selected = new Reminder(ReminderTrigger.CHAT, "Trigger String", "Display Message", Color.RED, 10, false);;

                reset();

                System.out.println(selected.getTriggerString());

                creatingNew = true;
                break;
            case 5:
                if (old != null) mod.getReminders().remove(old);

                list.selected = -1;
                selected = new Reminder(ReminderTrigger.CHAT, "Trigger String", "Display Message", Color.RED, 10, false);;

                reset();

                creatingNew = true;
                break;
            case 6:
                selected.setDisplay(!selected.isDisplay());
                button.displayString = "Display: " + selected.isDisplay();
                break;
        }
    }

    private void reset() {
        mode.displayString = "Mode: " + selected.getTrigger().getName();
        display.displayString = "Display: " + selected.isDisplay();

        triggerStringText.setText(selected.getTriggerString());
        displayStringText.setText(selected.getMessage());
        timerText.setText(selected.getTime() + "");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        triggerStringText.mouseClicked(mouseX, mouseY, mouseButton);
        displayStringText.mouseClicked(mouseX, mouseY, mouseButton);
        timerText.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        triggerStringText.textboxKeyTyped(typedChar, keyCode);
        displayStringText.textboxKeyTyped(typedChar, keyCode);
        timerText.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        triggerStringText.updateCursorCounter();
        displayStringText.updateCursorCounter();
        timerText.updateCursorCounter();
    }
}
