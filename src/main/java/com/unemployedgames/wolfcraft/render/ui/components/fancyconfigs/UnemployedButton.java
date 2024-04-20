package com.unemployedgames.wolfcraft.render.ui.components.fancyconfigs;

import com.mojang.math.Axis;
import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.render.ui.Icons;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UnemployedButton extends AbstractButton {

    private OnPress onPress;
    private Icons icon;
    private Component msg;

    public static String BACKGROUND_BUTTON_SMALL = "textures/gui/unemployed_comps/button_small.png";

    public float translateBy = 0f;
    public float direction = 1f;
    public int directionCounter = 1;

    public UnemployedButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Icons icon, OnPress press) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.onPress = press;
        this.icon = icon;
        this.msg = pMessage;
    }

    @Override
    public void onPress() {
        onPress.onPress(this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();
        //pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(deg));
        pGuiGraphics.pose().translate(0f, translateBy, 0f);

        if (this.isHovered()) {
            directionCounter++;
            translateBy = translateBy + (direction * 0.06f);

            if(directionCounter == 80) {
                direction = -1f;
            }
            if(directionCounter == 160) {
                direction = 1f;
                directionCounter = 0;
            }
        } else {
            direction = 1f;
            directionCounter = 0;
            translateBy = 0f;
        }

        pGuiGraphics.blit(new ResourceLocation(Wolfcraft.MODID, BACKGROUND_BUTTON_SMALL), getX(), getY(),0,0,16,16,16,16);
        pGuiGraphics.blit(icon.getIcon(), 3 + getX(),3 + getY(),0,0, 10, 10, 20,20);
        pGuiGraphics.pose().popPose();
        //super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick); WOULD RENDER A BUTTON
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, msg);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(UnemployedButton pButton);
    }
}
