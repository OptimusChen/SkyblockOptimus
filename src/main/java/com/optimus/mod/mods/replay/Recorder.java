package com.optimus.mod.mods.replay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class Recorder {

    static class Frame {
        private final List<KeyBinding> pressed;
        private final long time;
        private float yaw;
        private float pitch;

        public Frame(long time) {
            this.pressed = new ArrayList<>();
            this.time = time;

            this.yaw = 0f;
            this.pitch = 0f;
        }

        @Override
        public String toString() {
            return String.format("Frame{time=%s,yaw=%s,pitch=%s,pressed=%s}", time, yaw, pitch, pressed);
        }

        public float getYaw() { return yaw; }
        public float getPitch() { return pitch; }
        public long getTime() { return time; }
        public List<KeyBinding> getPressed() { return pressed; }
    }

    private Thread thread;
    private final ReplayMod replay;
    private boolean recording = false;
    private final List<Frame> frames = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    public Recorder(ReplayMod replay) {
        this.replay = replay;
    }

    public void record() {
        GameSettings settings = mc.gameSettings;
        EntityPlayerSP player = mc.thePlayer;

        long start = System.currentTimeMillis();

        recording = true;

        thread = new Thread(() -> {
            while (recording) {
                long time = System.currentTimeMillis() - start;

                Frame frame = new Frame(time);

                frame.yaw = player.rotationYaw;
                frame.pitch = player.rotationPitch;

                for (KeyBinding key : settings.keyBindings) {
                    if (key.isKeyDown()) frame.getPressed().add(key);
                }

                frames.add(frame);

                try {
                    Thread.sleep(1000 / replay.getFPS());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }

    public void stop() {
        recording = false;

        thread.stop();
    }

    public void clearFrames() {
        this.frames.clear();
    }

    public boolean isRecording() { return recording; }

    public List<Frame> getFrames() { return frames; }
}
