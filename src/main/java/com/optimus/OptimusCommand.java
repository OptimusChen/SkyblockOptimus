package com.optimus;

import com.optimus.gui.OptimusGuiScreen;
import com.optimus.mod.mods.replay.Recorder;
import com.optimus.mod.mods.replay.Replayer;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.awt.*;

public class OptimusCommand extends CommandBase {

    private final Recorder recorder = new Recorder();
    private final Replayer replayer = new Replayer(recorder);

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
        if (args.length == 0) {
            Util.delayTask(10, () -> Minecraft.getMinecraft().displayGuiScreen(new OptimusGuiScreen()));
            return;
        }

        if (args[0].equals("record")) {
            if (!recorder.isRecording()) {
                recorder.record();

                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("start record"));
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("stop record"));
                recorder.stop();
            }
        } else if (args[0].equals("replay")) {
            if (replayer.isPlaying()) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("stop play"));
                replayer.stop(false);
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("start play"));
                replayer.play();
            }
        } else if (args[0].equals("clear")) {
            recorder.clearFrames();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("cleared frames: " + recorder.getFrames()));
        } else if (args[0].equals("frames")) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(recorder.getFrames().size() + " frames: " + recorder.getFrames()));
        } else if (args[0].equals("loop")) {
            if (replayer.isLoop()) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("stop loop"));
                replayer.setLoop(false);
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("start loop"));
                replayer.setLoop(true);
            }
        }
    }
}
