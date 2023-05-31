package com.optimus;

import com.optimus.gui.OptimusGuiScreen;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.awt.*;

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
        if (args.length == 0) Util.delayTask(10, () -> Minecraft.getMinecraft().displayGuiScreen(new OptimusGuiScreen()));
    }
}
