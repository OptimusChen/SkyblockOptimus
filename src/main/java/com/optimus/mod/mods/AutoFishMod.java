package com.optimus.mod.mods;

import com.optimus.mod.ModuleGui;
import com.optimus.mod.Toggleable;
import com.optimus.util.Util;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;

public class AutoFishMod extends Toggleable {

    private final AutoFishGui gui = new AutoFishGui(this);
    private boolean cd = false;

    @Override
    public String getName() {
        return "Auto-Fish";
    }

    @Override
    public ModuleGui getGUI() {
        return gui;
    }

    @Override
    public void onReceivePacket(Packet<?> packet) {
        if (!isEnabled()) return;
        if (!(packet instanceof S29PacketSoundEffect)) return;

        S29PacketSoundEffect sound = (S29PacketSoundEffect) packet;

        if (gui.debug) {
            mc.thePlayer.addChatMessage(new ChatComponentText(sound.getSoundName()));
        }

        String text = gui.textField.getText();

        if (sound.getSoundName().equalsIgnoreCase(text) && mc.thePlayer.fishEntity != null && !cd) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.thePlayer.worldObj, mc.thePlayer.getHeldItem());

            cd = true;

            Util.delayTask(500, () -> mc.playerController.sendUseItem(mc.thePlayer, mc.thePlayer.worldObj, mc.thePlayer.getHeldItem()));
            Util.delayTask(5000, () -> cd = false);
        }
    }

    private static class AutoFishGui extends ToggleGui {

        private AutoFishMod mod;
        private GuiTextField textField = null;
        private boolean debug = false;

        public AutoFishGui(Toggleable mod) {
            super(mod);

            this.mod = (AutoFishMod) mod;
        }

        @Override
        public void initGui() {
            super.initGui();

            if (textField == null) {
                this.textField = new GuiTextField(1, mc.fontRendererObj, width / 2, height / 4, 200, 20);
                this.textField.setMaxStringLength(Integer.MAX_VALUE);

                this.textField.xPosition = (width - textField.getWidth()) / 2;
            }

            String text = "Debug: " + debug;

            buttonList.add(new GuiButton(2, (width - fontRendererObj.getStringWidth(text)) / 2, height / 3, fontRendererObj.getStringWidth(text) + 10, 20, text));
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);

            textField.drawTextBox();
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);

            if (button.id == 2) {
                debug = !debug;

                String text = "Debug: " + debug;

                button.displayString = text;

                button.setWidth(fontRendererObj.getStringWidth(text) + 10);
            }
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            textField.textboxKeyTyped(typedChar, keyCode);
        }

        @Override
        public void updateScreen() {
            super.updateScreen();
            textField.updateCursorCounter();
        }
    }
}
