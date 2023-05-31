package com.optimus.mod;

import com.optimus.mod.mods.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleHandler {

    public final List<Module> mods = new ArrayList<>();

    public ModuleHandler() {
        mods.add(new FlipperMod());
        mods.add(new CrystalHollowMod());
        mods.add(new AutoJujuMod());
        mods.add(new AutoFishMod());
        mods.add(new ScathaMod());
    }

    public int getModId(Module mod) {
        for (int i = 0; i < mods.size(); i++) {
            if (mods.get(i).getName().equals(mod.getName())) return i;
        }

        return 0;
    }

    public Module getMod(int i) { return mods.get(i); }

    public Module getMod(String name) {
        for (Module mod : mods) {
            if (mod.getName().equals(name)) return mod;
        }

        return null;
    }
}
