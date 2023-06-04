package com.optimus.mod.mods;

import com.optimus.mod.Module;
import com.optimus.mod.ModuleGui;
import com.optimus.util.PlayerHeadRotator;
import com.optimus.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.io.IOException;

public class PowderMod extends Module {

    private PowderGui gui;
    public boolean autoChest = false;
    private BlockPos chest = null;
    public PlayerHeadRotator headRotator;

    @Override
    public void init() {
        gui = new PowderGui(this);
    }

    @Override
    public String getName() {
        return "Powder Grinding";
    }

    @Override
    public ModuleGui getGUI() {
        return gui;
    }

    @Override
    public void tick() {
        if (headRotator == null) headRotator = new PlayerHeadRotator(mc.thePlayer);

        if (!autoChest) {
            chest = null;
            headRotator.stop();
            return;
        }

        EntityPlayerSP p = mc.thePlayer;

        BlockPos pos = Util.getLookingAtBlock();

        if (pos == null) return;

        if (p.worldObj == null) return;
        if (p.worldObj.getBlockState(pos) == null) return;

        Block block = p.worldObj.getBlockState(pos).getBlock();

        if (block == null) return;
        if (!block.getUnlocalizedName().contains("chest")) {
            chest = null;
            return;
        }

        chest = pos;
    }

    public void handleParticles(S2APacketParticles packetIn) {
        if (!autoChest) return;
        if (chest == null) return;
        if (headRotator.isRotating()) return;

        if (!packetIn.getParticleType().equals(EnumParticleTypes.CRIT)) return;

        Vec3 pos = new Vec3(packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate());

        if (!Util.isPlayerLookingAtChest(mc.thePlayer, chest, mc.thePlayer.worldObj)) return;

        if (pos.distanceTo(new Vec3(chest.getX(), chest.getY(), chest.getZ())) < 2.5) {
            double particleX = packetIn.getXCoordinate();
            double particleY = packetIn.getYCoordinate();
            double particleZ = packetIn.getZCoordinate();

            Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
            double viewerX = viewer.posX;
            double viewerY = viewer.posY + viewer.getEyeHeight();
            double viewerZ = viewer.posZ;

            double dx = particleX - viewerX;
            double dy = particleY - viewerY;
            double dz = particleZ - viewerZ;

            double yaw = Math.atan2(dz, dx) * 180.0 / Math.PI - 90.0;
            double pitch = Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180.0 / Math.PI;

            yaw = -yaw;

            if (pitch < -90.0) {
                pitch = -90.0;
            } else if (pitch > 90.0) {
                pitch = 90.0;
            }

            String speed = gui.textField.getText();

            headRotator.setPlayer(mc.thePlayer);
            headRotator.setTargetYawAndPitch((float) (-1.0f * yaw), (float) (-1.0f * pitch));
            headRotator.setSpeed(Integer.parseInt(speed));
            headRotator.startRotation();
        }
    }

    @Override
    public void onReceivePacket(Packet<?> packet) {
        if (!(packet instanceof S02PacketChat)) return;

        S02PacketChat chat = (S02PacketChat) packet;

        String message = chat.getChatComponent().getUnformattedText();

        if (message.contains("You uncovered a treasure chest!")) {
            main.getGraphics().sendTitle("Treasure Chest!", "", Color.GREEN.getRGB());
            mc.thePlayer.playSound("random.orb", 1, 0.5f);
        }
    }

    static class PowderGui extends ModuleGui {

        private GuiTextField textField = null;
        private final PowderMod scanner;

        public PowderGui(Module mod) {
            super(mod);

            scanner = (PowderMod) mod;
        }

        @Override
        public void initGui() {
            super.initGui();

            if (textField == null) {
                this.textField = new GuiTextField(1, mc.fontRendererObj, width / 2, height / 4, 200, 20);
                this.textField.setMaxStringLength(Integer.MAX_VALUE);

                this.textField.xPosition = (width - textField.getWidth()) / 2;
                this.textField.setText("3");
            }

            this.buttonList.add(new GuiButton(0, width / 2, height / 2 - 30, "Auto-Chest: " + scanner.autoChest));
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);

            if (button.id == 0) {
                scanner.autoChest = !scanner.autoChest;
                button.displayString = "Auto-Chest: " + scanner.autoChest;
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();

            textField.drawTextBox();
            super.drawScreen(mouseX, mouseY, partialTicks);
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
