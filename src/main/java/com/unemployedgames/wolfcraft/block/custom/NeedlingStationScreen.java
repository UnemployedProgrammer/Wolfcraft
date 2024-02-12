package com.unemployedgames.wolfcraft.block.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NeedlingStationScreen extends AbstractContainerScreen<NeedlingStationMenu> {
    private static final ResourceLocation INVENTORY =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/inventory.png");
    private static final ResourceLocation MAIN =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/main.png");

    private static final ResourceLocation NEEDLING_FIELD =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/needling.png");

    private Component chosenRecipeName = Component.literal("-> Air");
    private Component chosenRecipeNameAbove = Component.literal("> Air");
    private Component chosenRecipeNameBelow = Component.literal("> Air");

    private List<Component> chosenRecipeList = new ArrayList<>();
    public NeedlingStationScreen(NeedlingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        //INITLIST
        chosenRecipeList.add(chosenRecipeNameAbove);
        chosenRecipeList.add(chosenRecipeName);
        chosenRecipeList.add(chosenRecipeNameBelow);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, INVENTORY);
        int iW = 175; //ImageWidth
        int iH = 88; //ImageHeight

        int x = (width - iW) / 2 - 1;
        int y = (height - iH) / 2 + 38;
        int xOffsetInv = 115;
        int yOffsetInv = 30;

        guiGraphics.blit(INVENTORY, x + xOffsetInv, y + yOffsetInv, 0, 0, iW, iH, iW, iH);
        guiGraphics.blit(NEEDLING_FIELD, 20, (height - 200) / 2, 0, 0, 200, 200, 200, 200);
        //guiGraphics.blit(MAIN, 20, (height - 200) / 2, 0, 0, 200, 200, 200, 200);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderNeedle(mouseX, mouseY, guiGraphics);
    }

    public void renderNeedle(int mX, int mY, GuiGraphics gg) {
        int needlingFieldXMin = 20;
        int needlingFieldXMax = 220;
        int needlingFieldYMin = height / 2 - 100;
        int needlingFieldYMax = height / 2 + 100;
        if (WolfMath.isBetween(mX, needlingFieldXMin, needlingFieldXMax) && WolfMath.isBetween(mY, needlingFieldYMin, needlingFieldYMax)) {
            gg.renderItem(new ItemStack(ModItems.NEEDLE.get()), mX - 8, mY - 8);
        }
    }

    public void renderRecipeTooltip(int mX, int mY, GuiGraphics gg) {
        int tooltipFieldXMin = 20;
        int tooltipFieldXMax = 220;
        int tooltipFieldYMin = height / 2 - 100;
        int tooltipFieldYMax = height / 2 + 100;
        if (WolfMath.isBetween(mX, tooltipFieldXMin, tooltipFieldXMax) && WolfMath.isBetween(mY, tooltipFieldYMin, tooltipFieldYMax)) {
            gg.renderTooltip(minecraft.font, chosenRecipeList, Optional.empty(), 0, 0);
        }
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
}
