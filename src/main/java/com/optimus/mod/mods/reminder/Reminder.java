package com.optimus.mod.mods.reminder;

import com.optimus.SkyblockOptimus;
import com.optimus.util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import java.awt.*;

@Getter
@Setter
public class Reminder {

    private static final SkyblockOptimus main = SkyblockOptimus.getInstance();
    private ReminderTrigger trigger;
    private String triggerString;
    private boolean display;
    private String message;
    private Color color;
    private int time;

    private boolean timing = false;

    public Reminder(ReminderTrigger trigger, String triggerString, String message, Color color, int time, boolean display) {
        this.trigger = trigger;
        this.triggerString = triggerString;
        this.message = message;
        this.display = display;
        this.color = color;
        this.time = time;
    }

    public void trigger() {
        if (!check()) return;
        if (timing) return;

        timing = true;

        if (display) {
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

            final int[] currTime = {time};

            Thread t = new Thread(() -> {
                while (true) {
                    main.getGraphics().drawTemporaryText(triggerString + ": " + currTime[0] + "s", res.getScaledWidth() / 2, res.getScaledHeight() / 2, Color.RED.getRGB(), 1000);

                    currTime[0]--;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            t.start();

            Util.delayTask(time * 1000, t::stop);
        }

        Util.delayTask(time * 1000, () -> {
            main.getGraphics().sendTitle(message, "", color.getRGB());

            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 10, 0.5f);

            timing = false;
        });
    }

    private boolean check() {
        switch (trigger) {
            case CHAT:
                if (!getReminders().lastChatMessage.contains(triggerString)) return false;

                break;
            case RIGHT_CLICK:
                ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();

                if (item == null) return false;
                if (!item.hasDisplayName()) return false;
                if (!item.getDisplayName().replaceAll("\u00A7.", "").contains(triggerString)) return false;

                break;
        }

        return true;
    }

    private ReminderMod getReminders() {
        return (ReminderMod) SkyblockOptimus.getInstance().getModuleHandler().getMod("Ability Reminder");
    }
}
