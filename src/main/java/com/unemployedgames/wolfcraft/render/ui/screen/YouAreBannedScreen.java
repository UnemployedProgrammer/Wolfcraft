package com.unemployedgames.wolfcraft.render.ui.screen;

import com.unemployedgames.wolfcraft.misc.ApiRequests;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.BanNoticeScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class YouAreBannedScreen extends Screen {
    ApiRequests.BanDetails details = new ApiRequests.BanDetails();

    public YouAreBannedScreen(ApiRequests.BanDetails details) {
        super(Component.literal("You are Banned!"));
        this.details = details;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.drawString(minecraft.font, Component.literal(details == null && details.reason == null ? "Unknown" : details.reason), 10, 10, -1, false);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
