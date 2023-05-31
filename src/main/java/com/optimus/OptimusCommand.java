package com.optimus;

import com.optimus.gui.OptimusGuiScreen;
import com.optimus.mod.mods.CrystalHollowMod;
import com.optimus.util.PlayerHeadRotator;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class OptimusCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "optimus";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Util.delayTask(100, () -> Minecraft.getMinecraft().displayGuiScreen(new OptimusGuiScreen()));
    }
}
