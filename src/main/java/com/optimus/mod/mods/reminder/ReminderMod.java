package com.optimus.mod.mods.reminder;

import com.optimus.mod.Module;
import com.optimus.mod.ModuleGui;
import com.optimus.mod.mods.reminder.gui.ReminderGui;
import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReminderMod extends Module {

    private final ReminderGui gui = new ReminderGui(this);
    private final List<Reminder> reminders = new ArrayList<>();

    public String lastChatMessage;

    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return "Ability Reminder";
    }

    @Override
    public ModuleGui getGUI() {
        return gui;
    }

    @Override
    public void onRightClick() {
        triggerReminders(ReminderTrigger.RIGHT_CLICK);
    }

    @Override
    public void onReceivePacket(Packet<?> packet) {
        if (!(packet instanceof S02PacketChat)) return;

        S02PacketChat chat = (S02PacketChat) packet;

        lastChatMessage = chat.getChatComponent().getUnformattedText();

        triggerReminders(ReminderTrigger.CHAT);
    }

    private void triggerReminders(ReminderTrigger trigger) {
        for (Reminder reminder : reminders) {
            if (reminder.getTrigger().equals(trigger)) {
                reminder.trigger();
            }
        }
    }
}
