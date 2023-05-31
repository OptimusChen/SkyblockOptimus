package com.optimus.util;

import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerHeadRotator {
    private final EntityPlayerSP player;
    private float startYaw;
    private float startPitch;
    private float targetYaw;
    private float targetPitch;
    private long startTime;
    private long duration;

    public PlayerHeadRotator(EntityPlayerSP player) {
        this.player = player;
    }

    public void setTargetYawAndPitch(float yaw, float pitch, long duration) {
        this.startYaw = this.player.rotationYaw;
        this.startPitch = this.player.rotationPitch;
        this.targetYaw = yaw;
        this.targetPitch = pitch;
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public void onUpdate() {
        if (this.startTime == 0) {
            return;
        }

        long timeElapsed = System.currentTimeMillis() - this.startTime;

        if (timeElapsed >= this.duration) {
            this.player.rotationYaw = this.targetYaw;
            this.player.rotationPitch = this.targetPitch;
            this.startTime = 0;
            return;
        }

        float progress = (float) timeElapsed / this.duration;
        float yaw = this.interpolate(this.startYaw, this.targetYaw, progress);
        float pitch = this.interpolate(this.startPitch, this.targetPitch, progress);

        this.player.rotationYaw = yaw;
        this.player.rotationPitch = pitch;
    }

    private float interpolate(float start, float end, float progress) {
        return start + (end - start) * progress;
    }
}