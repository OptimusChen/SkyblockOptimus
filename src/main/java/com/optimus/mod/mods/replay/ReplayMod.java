package com.optimus.mod.mods.replay;

import com.optimus.mod.Module;
import com.optimus.mod.ModuleGui;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReplayMod extends Module {

    private static final List<Integer> FPS_MODES = Arrays.asList(60, 72, 80, 90, 120);
    private final Recorder recorder = new Recorder(this);
    private final Replayer replayer = new Replayer(this);
    private final ReplayGui gui = new ReplayGui(this);
    private int fps = 0;

    @Override
    public String getName() {
        return "Replay";
    }

    @Override
    public ModuleGui getGUI() {
        return gui;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public Replayer getReplayer() {
        return replayer;
    }

    public int getFPS() { return FPS_MODES.get(fps); }

    static class ReplayGui extends ModuleGui {

        private final ReplayMod replay;

        public ReplayGui(Module mod) {
            super(mod);

            this.replay = (ReplayMod) mod;
        }

        @Override
        public void initGui() {
            super.initGui();

            String text = "FPS: " + FPS_MODES.get(replay.fps);

            this.buttonList.add(new GuiButton(0, (width - fontRendererObj.getStringWidth(text)) / 2, height / 2, fontRendererObj.getStringWidth(text) + 10, 20, text));
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);

            if (button.id == 0) {
                replay.fps++;

                if (replay.fps >= FPS_MODES.size()) replay.fps = 0;

                String text = "FPS: " + FPS_MODES.get(replay.fps);

                button.displayString = text;
                button.setWidth(fontRendererObj.getStringWidth(text) + 10);
            }
        }
    }
}
