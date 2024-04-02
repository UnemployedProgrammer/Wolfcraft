package com.unemployedgames.wolfcraft.item.guidebook;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuideBookScreen extends Screen {

    public List<GuideBookPageButton> allButtons = new ArrayList<>();

    public List<Component> splitText = new ArrayList<>();

    public boolean inPage = false;
    public GuideBookEntry guideBookEntry = null;
    public int xBcOffset = 0;
    public int yBcOffset = 0;

    public GuideBookScreen(boolean inPage, @Nullable GuideBookEntry toDisplay) {
        super(Component.translatable("gui.wolfcraft.guidebook.title"));
        this.guideBookEntry = toDisplay;
        if(inPage && toDisplay != null) {
            splitText = WolfMath.splitComponent(toDisplay.content(), "/l");
        }
        this.inPage = inPage;
    }

    @Override
    public void init() {
        xBcOffset = width / 2 - 136;
        yBcOffset = height / 2 - 90;
        //allButtons.add(addRenderableWidget(new GuideBookPageButton(xBcOffset + 20, yBcOffset + 20, new GuideBookEntry(Component.literal("hi, this is a title test, how long can it be? This long? I don't think so?"), Component.literal("content"), new ResourceLocation(Wolfcraft.MODID, "textures/gui/wolfcraft_title.png")), minecraft)));
        if(!inPage) {
            renderSite1();
        }

        addRenderableWidget(new Button.Builder(Component.translatable(inPage ? "gui.wolfcraft.backbtnwitharrow" : "gui.wolfcraft.closebtnwitharrow"), (pButton -> {
            if(GuideBookScreen.this.inPage) {
                GuideBookScreen.this.minecraft.setScreen(new GuideBookScreen(false, null));
            } else {
                GuideBookScreen.this.minecraft.popGuiLayer();
            }
        })).bounds(5, 5, 150, 20).build());

        super.init();
    }

    public void renderSite1() {
        int yAlready = 0;
        for (GuideBookEntry guideBookEntry : AllGuideEntrys.GUIDE_BOOK_ENTRYS_SITE1) {
            allButtons.add(addRenderableWidget(new GuideBookPageButton(xBcOffset + 15, yBcOffset + 20 + yAlready, guideBookEntry, minecraft)));
            yAlready = yAlready + 16;
        }
    }

    public void renderInPage(GuiGraphics gg) {
        xBcOffset = width / 2 - 136;
        yBcOffset = height / 2 - 90;
        int ggContentOffset = 0;
        gg.renderItem(new ItemStack(guideBookEntry.image()), xBcOffset + 15, yBcOffset + 15);
        gg.drawString(minecraft.font, guideBookEntry.title(), xBcOffset + 35, yBcOffset + 15, 0, false);
        for (Component contentSplit : splitText) {
            gg.drawString(minecraft.font, contentSplit, xBcOffset + 15, yBcOffset + 35 + ggContentOffset, 0, false);
            ggContentOffset = ggContentOffset + 8;
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.blit(new ResourceLocation(Wolfcraft.MODID, "textures/gui/guidebook/background.png"), width / 2 - 136, height / 2 - 90, 0,0, 273, 180, 273, 180);
        if(inPage) {
            renderInPage(pGuiGraphics);
        }
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
