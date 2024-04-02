package com.unemployedgames.wolfcraft.item.guidebook;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class GuideBookPageButton extends ImageButton {
    final Minecraft minecraft;
    final GuideBookEntry entry;
    final int wX;
    final int wY;

    public GuideBookPageButton(int pX, int pY, GuideBookEntry entry, Minecraft minecraft) {
        super(pX, pY, 100, 16, 0, 0, new ResourceLocation(Wolfcraft.MODID, "textures/gui/guidebook/go_to_page.png"), (pButton) -> {
            SoundManager manager = minecraft.getSoundManager();
            manager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
            minecraft.setScreen(new GuideBookScreen(true, entry));
        });
        this.minecraft = minecraft;
        this.entry = entry;
        this.wX = pX;
        this.wY = pY;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.drawString(minecraft.font, WolfMath.cutComponent(entry.title(), 18), wX + 2, wY + 4, 0, false);
        pGuiGraphics.blit(new ResourceLocation(Wolfcraft.MODID, "textures/gui/guidebook/go_to_page_arrow.png"), wX + 90, wY + 4, 0,0,8,8,8,8);
    }
}
