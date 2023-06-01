package com.optimus.mod.mods.replay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;

public class Replayer {

    private Thread thread;
    private int currentFrame = 0;
    private boolean loop = false;
    private boolean playing = false;
    private final Recorder recorder;
    private final Minecraft mc = Minecraft.getMinecraft();

    public Replayer(Recorder recorder) {
        this.recorder = recorder;
    }

    public void play() {
        EntityPlayerSP player = mc.thePlayer;

        playing = true;

        thread = new Thread(() -> {
            while (playing) {
                if (currentFrame >= recorder.getFrames().size()) {
                    Minecraft.getMinecraft().addScheduledTask(() -> stop(true));
                    return;
                }

                Recorder.Frame frame = recorder.getFrames().get(currentFrame);

                for (KeyBinding key : mc.gameSettings.keyBindings) {
                    KeyBinding.setKeyBindState(key.getKeyCode(), frame.getPressed().contains(key));
                }

                player.rotationYaw = frame.getYaw();
                player.rotationPitch = frame.getPitch();

                currentFrame++;

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }

    public void stop(boolean checkLoop) {
        playing = false;
        currentFrame = 0;

        thread.stop();

        System.out.println("loop: " + checkLoop + " " + loop);

        if (checkLoop && loop) {
            System.out.println("loop");
            play();
        }
    }

    public boolean isPlaying() { return playing; }

    public boolean isLoop() { return loop; }

    public void setLoop(boolean b) { loop = b; }
}
