package com.optimus.mixin;

import com.optimus.SkyblockOptimus;
import com.optimus.mod.Module;
import com.optimus.mod.mods.CrystalHollowMod;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2APacketParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class NetworkMixin {

    @Inject(method = "channelRead0*", at = @At("HEAD"))
    public void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        for (Module mod : SkyblockOptimus.getInstance().getModHandler().getMods()) {
            mod.onReceivePacket(packet);
        }

        if (!(packet instanceof S2APacketParticles)) return;

        S2APacketParticles packetIn = (S2APacketParticles) packet;

        CrystalHollowMod mod = (CrystalHollowMod) SkyblockOptimus.getInstance().getModHandler().getMod("Crystal Hollows");
        mod.handleParticles(packetIn);
    }
}
