package com.optimus.mod.mods;

import com.optimus.mod.Module;
import com.optimus.mod.ModuleGui;
import com.optimus.util.PlayerHeadRotator;
import com.optimus.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.io.IOException;

public class CrystalHollowMod extends Module {

    private CrystalHollowGui gui;
    public boolean autoChest = false;
    private BlockPos chest = null;
    public PlayerHeadRotator headRotator = null;

    @Override
    public void init() {
        gui = new CrystalHollowGui(this);
    }

    @Override
    public String getName() {
        return "Crystal Hollows";
    }

    @Override
    public ModuleGui getGUI() {
        return gui;
    }

    @Override
    public void tick() {
        if (headRotator != null) headRotator.onUpdate();

        if (!autoChest) {
            chest = null;
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
        if (chest == null) return;

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

            headRotator = new PlayerHeadRotator(mc.thePlayer);
            headRotator.setTargetYawAndPitch((float) (-1.0f * yaw), (float) (-1.0f * pitch), 100);
        }
    }

    static class CrystalHollowGui extends ModuleGui {

        private final CrystalHollowMod scanner;

        public CrystalHollowGui(Module mod) {
            super(mod);

            scanner = (CrystalHollowMod) mod;
        }

        @Override
        public void initGui() {
            super.initGui();

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

            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
