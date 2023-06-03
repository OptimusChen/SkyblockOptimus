package com.optimus.mod.mods.replay;

import com.optimus.SkyblockOptimus;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ReplayCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "replay";
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
            Util.sendMessage("wrong usage bruh", EnumChatFormatting.RED);
            return;
        }

        ReplayMod replay = (ReplayMod) SkyblockOptimus.getInstance().getModHandler().getMod("Replay");

        Recorder recorder = replay.getRecorder();
        Replayer replayer = replay.getReplayer();

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "record":
                if (!recorder.isRecording()) {
                    recorder.record();

                    Util.sendMessage("Recording started!", EnumChatFormatting.GREEN);
                } else {
                    Util.sendMessage("Recording stopped!", EnumChatFormatting.GREEN);
                    recorder.stop();
                }
                break;
            case "replay":
            case "play":
                if (!replayer.isPlaying()) {
                    Util.sendMessage("Replay started!", EnumChatFormatting.GREEN);
                    replayer.play();
                } else {
                    Util.sendMessage("Replay stopped!", EnumChatFormatting.GREEN);
                    replayer.stop(false);
                }
                break;
            case "clear":
                Util.sendMessage("Successfully cleared " + recorder.getFrames().size() + " frames from recorder.", EnumChatFormatting.GREEN);

                recorder.clearFrames();
                break;
            case "loop":
                replayer.setLoop(!replayer.isLoop());

                Util.sendMessage("Loop status: " + replayer.isLoop() + ".", EnumChatFormatting.GREEN);
                break;
        }
    }
}
