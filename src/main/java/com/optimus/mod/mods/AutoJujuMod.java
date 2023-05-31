package com.optimus.mod.mods;

import com.optimus.mod.Toggleable;
import com.optimus.util.Util;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class AutoJujuMod extends Toggleable {

    @Override
    public String getName() {
        return "Auto-Juju";
    }

    @Override
    public void tick() {
        super.tick();

        if (!isEnabled()) return;

        Entity targeted = Util.getTargetedEntity(mc.thePlayer);

        if (targeted == null) return;
        if (!(targeted instanceof EntityEnderman) && !(targeted instanceof EntityWolf) && !(targeted instanceof EntityZombie)) return;

        ItemStack item = mc.thePlayer.getHeldItem();

        if (item == null) return;
        if (!item.hasDisplayName()) return;
        if (!item.getDisplayName().toLowerCase().contains("shortbow")) return;

        Util.rightClick();
    }
}
