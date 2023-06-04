package com.optimus.util;

import net.minecraft.client.entity.EntityPlayerSP;

import java.util.Random;

public class PlayerHeadRotator {

    private static final int FPS = 120;
    private EntityPlayerSP player;
    private boolean rotating;
    private float startYaw;
    private float startPitch;
    private float targetYaw;
    private float targetPitch;
    private long startTime;
    private long duration;
    private Thread thread;
    private float speed;

    public PlayerHeadRotator(EntityPlayerSP player) {
        this.player = player;
        this.rotating = false;

        this.speed = 15f;
    }

    public void setPlayer(EntityPlayerSP player) { this.player = player; }

    public void setSpeed(float f) { this.speed = f; }

    public void setTargetYawAndPitch(float yaw, float pitch) {
        float currYaw = player.rotationYaw;
        float currPitch = player.rotationPitch;

        float x = currYaw - yaw;
        float y = currPitch - pitch;

        double dist = Math.sqrt(x * x + y * y);

        long duration = Math.round(dist * speed);

        setTargetYawAndPitch(yaw, pitch, duration);
    }

    public void setTargetYawAndPitch(float yaw, float pitch, long duration) {
        this.startYaw = this.player.rotationYaw;
        this.startPitch = this.player.rotationPitch;
        this.targetYaw = yaw;
        this.targetPitch = pitch;
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public void startRotation() {
        this.rotating = true;

        thread = new Thread(() -> {
            while(true) {
                if (this.startTime == 0) {
                    this.rotating = false;
                    return;
                }

                long timeElapsed = System.currentTimeMillis() - this.startTime;

                if (timeElapsed >= this.duration) {
                    this.startTime = 0;
                    this.rotating = false;
                    return;
                }

                float progress = (float) timeElapsed / this.duration;
                float yaw = this.interpolate(this.startYaw, this.targetYaw, progress);
                float pitch = this.interpolate(this.startPitch, this.targetPitch, progress);

                this.player.rotationYaw = yaw;
                this.player.rotationPitch = pitch;

                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }

    public void stop() {
        if (thread == null) return;
        this.thread.stop();
        this.thread = null;
    }

    public boolean isRotating() { return rotating; }

    private float interpolate(float start, float end, float progress) {
        return start + (end - start) * progress;
    }
}