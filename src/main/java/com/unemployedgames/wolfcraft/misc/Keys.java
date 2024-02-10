package com.unemployedgames.wolfcraft.misc;

import com.mojang.blaze3d.platform.InputConstants;
import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class Keys {
    public static final Keys INSTANCE = new Keys();

    private Keys() {
    }

    private static final String CATEGORY = "key.categories." + Wolfcraft.MODID;

    public final KeyMapping exampleKey = new KeyMapping(
            "key." + Wolfcraft.MODID + ".debugoverlay",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_F10, -1),
            CATEGORY
    );
}