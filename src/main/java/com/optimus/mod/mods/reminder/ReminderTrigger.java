package com.optimus.mod.mods.reminder;

import org.apache.commons.lang3.text.WordUtils;

public enum ReminderTrigger {

    RIGHT_CLICK,
    CHAT;

    public String getName() {
        return WordUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
    }

    public ReminderTrigger getNext() {
        int index = 0;

        for (ReminderTrigger t : values()) {
            if (t == this) {
                break;
            }

            index++;
        }

        index++;

        if (index == values().length) index = 0;

        return values()[index];
    }
}
