package com.unemployedgames.wolfcraft.render.ui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;

import java.awt.geom.Point2D;

public class SmoothProgessBar extends AbstractWidget {

    private WolfMath.AnimationBezier animator;
    private boolean animates = false;
    private Point2D.Float lastFramePoint;

    public SmoothProgessBar(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
        animateBarTo(0);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Matrix4f matrix4f = pGuiGraphics.pose().last().pose();
        VertexConsumer vertexconsumer = pGuiGraphics.bufferSource().getBuffer(RenderType.gui());
        int packedCol = 0xFFFFFFFF;

        if (animates) {
            lastFramePoint = animator.getNextFramePoint();
            if (animator.isLastFriendDone()) {
                animates = false;
            }
        }

        float f3 = (float) FastColor.ARGB32.alpha(packedCol) / 255.0F;
        float f = (float)FastColor.ARGB32.red(packedCol) / 255.0F;
        float f1 = (float)FastColor.ARGB32.green(packedCol) / 255.0F;
        float f2 = (float)FastColor.ARGB32.blue(packedCol) / 255.0F;

        float maxX = (float) getX() + lastFramePoint.x;

        vertexconsumer.vertex(matrix4f, maxX, (float) getX(), (float)0).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, maxX, (float) getX(), (float)0).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, maxX, (float) getX(), (float)0).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, maxX, (float) getX(), (float)0).color(f, f1, f2, f3).endVertex();

        pGuiGraphics.flush();
    }

    public WolfMath.AnimationBezier getAnimator() {
        return animator;
    }

    public boolean isInAnimation() {
        return animates;
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, Component.literal("Progress Bar"));
    }

    public void animateBarTo(int percentage) {
        if (percentage > 100) return;
        double togoto = WolfMath.calculatePercentageOfValue(percentage, width);
        Point2D.Float startPoint = new Point2D.Float(0, 0);
        Point2D.Float endPoint = new Point2D.Float(Math.round(togoto), 0);

        animator = new WolfMath.AnimationBezier(startPoint, endPoint);
        animates = true;
    }
}
