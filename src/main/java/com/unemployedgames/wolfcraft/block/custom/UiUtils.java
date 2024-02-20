package com.unemployedgames.wolfcraft.block.custom;

import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class UiUtils {
    public void drawLine(int startX, int startY, int endX, int endY, String textureOfLinePath) {
        // Calculate texture sizes and positions
        int textureWidth = 1;
        int textureHeight = (int) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        int textureX = startX + (endX - startX) / 2 - textureWidth / 2;
        int textureY = startY + (endY - startY) / 2 - textureHeight / 2;

        // Draw the texture
        float angle = (float) Math.atan2(endY - startY, endX - startX);
        Minecraft.getInstance().getTextureManager().bindForSetup(new ResourceLocation(Wolfcraft.MODID, textureOfLinePath));
        GL11.glPushMatrix();
        GL11.glTranslated(textureX, textureY, 0);
        GL11.glRotated(Math.toDegrees(angle), 0, 0, 1);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(textureWidth, 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(textureWidth, textureHeight);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, textureHeight);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}
