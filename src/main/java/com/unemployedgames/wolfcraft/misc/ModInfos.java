package com.unemployedgames.wolfcraft.misc;

import net.minecraftforge.fml.ModList;

public class ModInfos {
    public static boolean isGeckoLibLoaded() {
        return ModList.get().isLoaded("geckolib");
    }
}
