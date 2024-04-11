package com.unemployedgames.wolfcraft.render.ui;

import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.resources.ResourceLocation;

public enum Icons {
    BACK("back"),
    SAVE("save"),
    CLOSE("close"),
    DELETE("delete"),
    REFRESH("refresh"),
    PRESS_ME_DEBUG("db");

    private String icon;

    private Icons(String icon) {
        this.icon = icon;
    }

    public ResourceLocation getIcon() {
        return new ResourceLocation(Wolfcraft.MODID, "textures/gui/icons/" + icon + ".png");
    }
}
