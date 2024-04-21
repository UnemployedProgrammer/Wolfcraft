package com.unemployedgames.wolfcraft.render.ui.screen;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.misc.ApiRequests;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.BanNoticeScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class YouAreBannedScreen extends Screen {
    ApiRequests.BanDetails details = new ApiRequests.BanDetails();
    SoundManager manager = Minecraft.getInstance().getSoundManager();
    //Checkbox soundBox;
    public float translateBy = 0f;
    public float direction = 1f;
    public int directionCounter = 1;
    public boolean animates = true;


    public YouAreBannedScreen(ApiRequests.BanDetails details, Minecraft minecraft) {
        super(Component.literal("You are Banned!"));
        this.details = details;
        this.minecraft = minecraft;
    }

    @Override
    protected void init() {
        super.init();
        //soundBox = addRenderableWidget(new Checkbox(width - 30, 10, 20, 20, Component.literal("Sound"), true, false));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.translatable("ban.wolfcraft.title"), width / 2, 10, -1);
        //pGuiGraphics.drawString(minecraft.font, Component.translatable("ban.wolfcraft.sound"), width - 70, 15, -1, false);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.translatable("ban.wolfcraft.reason"), width / 2, height / 2 - 20, -1);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.literal(details == null && details.reason == null ? "Unknown Reason" : details.reason), width / 2, height / 2 - 10, -1);
        //pGuiGraphics.drawString(minecraft.font, Component.literal(details == null && details.reason == null ? "Unknown Reason" : details.reason), 10, 10, -1, false);
        int xBcOffset = width / 2;
        int yBcOffset = height / 2 + 10;
        int ggContentOffset = 0;
        for (Component contentSplit : WolfMath.splitComponent(Component.translatable("ban.wolfcraft.thatyoucannotdo"), "-nl-")) {
            pGuiGraphics.drawCenteredString(minecraft.font, contentSplit, xBcOffset, yBcOffset + ggContentOffset, -1);
            ggContentOffset = ggContentOffset + 9;
        }

        //pGuiGraphics.pose().pushPose();
        //pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(deg));
        //pGuiGraphics.pose().translate(0f, translateBy, 0f);

        //if (this.animates) {
        //            directionCounter++;
        //            translateBy = translateBy + (direction * 1.5f);
        //
        //            if(directionCounter == 25) {
        //                direction = -1f;
        //                if(soundBox.selected()) {
        //                    manager.play(SimpleSoundInstance.forUI(SoundEvents.ANVIL_LAND, 1.0F));
        //                }
        //            }
        //            if(directionCounter == 50) {
        //                direction = 1f;
        //                directionCounter = 0;
        //            }
        //        } else {
        //            direction = 1f;
        //            directionCounter = 0;
        //            translateBy = 0f;
        //        }

        //pGuiGraphics.blit(new ResourceLocation(Wolfcraft.MODID, "textures/gui/ban/hammer.png"), width / 2 - 32, height / 2 + 75,0,0,16,16,16,16);
        //pGuiGraphics.pose().popPose();

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
