package com.unemployedgames.wolfcraft.block.custom;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public class NeedlingPoint extends ImageButton {

    int indexOf = 0;
    int x;
    int y;

    public NeedlingPoint(int pX, int pY, int indexOf, OnPress pOnPress) {
        super(pX, pY, 20, 20, 0, 0, new ResourceLocation("wolfcraft", "textures/gui/needling_station/point.png"), pOnPress);
        x = pX;
        y = pY;
    }

    public int getIndexOf() {
        return indexOf;
    }
}
