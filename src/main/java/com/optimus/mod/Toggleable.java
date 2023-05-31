package com.optimus.mod;

import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

public abstract class Toggleable extends Module {

    private boolean enabled;
    private ToggleGui gui;

    public boolean isEnabled() { return enabled; }
    public void toggleEnabled() { enabled = !enabled; }

    @Override
    public void init() {
        super.init();

        this.gui = new ToggleGui(this);
    }

    @Override
    public ModuleGui getGUI() {
        return gui;
    }

    protected static class ToggleGui extends ModuleGui {

        private final Toggleable toggleable;

        public ToggleGui(Toggleable mod) {
            super(mod);

            toggleable = mod;
        }

        @Override
        public void initGui() {
            super.initGui();

            String text = "Enabled: " + toggleable.isEnabled();
            this.buttonList.add(new GuiButton(0, (width - fontRendererObj.getStringWidth(text)) / 2, height / 2, fontRendererObj.getStringWidth(text) + 10, 20, text));
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);

            if (button.id == 0) {
                toggleable.toggleEnabled();

                String text = "Enabled: " + toggleable.isEnabled();
                button.setWidth(fontRendererObj.getStringWidth(text) + 10   );
                button.displayString = text;
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();

            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
