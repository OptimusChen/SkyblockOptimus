package com.optimus.mod.mods;

import com.optimus.mod.Toggleable;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AutoF11Mod extends Toggleable {

    private final ArrayList<KeyBinding> keys = new ArrayList<>();
    @Override
    public String getName() {
        return "Auto-F11";
    }

    @Override
    public void onKeyPressed(InputEvent.KeyInputEvent e) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_GRAVE)) return;

        toggleEnabled();

        if (!isEnabled()) return;

        GameSettings settings = mc.gameSettings;

        for (KeyBinding key : settings.keyBindings) {
            if (key.isKeyDown() && !keys.contains(key)) keys.add(key);
        }
    }

    @Override
    public void tick() {
        for (KeyBinding key : keys) {
            KeyBinding.setKeyBindState(key.getKeyCode(), isEnabled());
        }

        if (!isEnabled()) {
            keys.clear();
        }
    }
}
