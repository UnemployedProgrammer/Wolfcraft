package com.unemployedgames.wolfcraft.block.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.misc.ModSounds;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NeedlingStationScreen extends AbstractContainerScreen<NeedlingStationMenu> {
    private static final ResourceLocation INVENTORY =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/inventory.png");
    private static final ResourceLocation MAIN =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/main.png");

    private static final ResourceLocation NEEDLING_FIELD =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/needling.png");

    private static final ResourceLocation SLOT =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/slot.png");

    private static final ResourceLocation ARROW =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/arrow.png");

    private static final String STRING_LINE = "textures/gui/needling_station/line.png";
    private WolfMath.NeedlingPoints needlingPoints = new WolfMath.NeedlingPoints.Builder().firstRecipeLine(" ", " ", " ").midRecipeLine(" ", " ", " ").lastRecipeLine(" ", " ", " ").build();
    private List<NeedlingPoint> needlingPointsSplit = new ArrayList<>();

    private Component chosenRecipeName = Component.literal("-> Air");
    private Component chosenRecipeNameAbove = Component.literal("> Air");
    private Component chosenRecipeNameBelow = Component.literal("> Air");

    private List<RawNeedlingRecipe> recipes = new ArrayList<>();

    private int recipeInd = 0;

    private int optionsXOffset = 0;
    private int optionsYOffset = 0;

    private List<Component> chosenRecipeList = new ArrayList<>();
    private List<Item> recipeListForItem = new ArrayList<>();
    private ItemStack recipeItem = new ItemStack(Items.BARRIER);
    private ItemStack previousRecipeInputItem = new ItemStack(Items.BARRIER);
    private RawNeedlingRecipe recipeCurrent = null;

    private int lastRecipePointX = 40;
    private int lastRecipePointY = 40;

    private NeedlingStationMenu menu;

    public NeedlingStationScreen(NeedlingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.menu = pMenu;
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        //INITLIST
        updateIndex();

        recipes.add(new RawNeedlingRecipe(ModItems.WOLF_LEATHER_WET.get(), 8, ModItems.WET_WOLF_ARMOR_CHESTPLATE.get(), 1, "1 7", "286", "345"));
        recipes.add(new RawNeedlingRecipe(ModItems.WOLF_LEATHER_WET.get(), 7, ModItems.WET_WOLF_ARMOR_LEGGINS.get(), 1, "345", "2 6", "1 7"));
    }

    private void setRecipePoints() {
        needlingPoints = new WolfMath.NeedlingPoints.Builder().firstRecipeLine(recipeCurrent.getRecipeLine1()).midRecipeLine(recipeCurrent.getRecipeLine2()).lastRecipeLine(recipeCurrent.getRecipeLine3()).build();
    }

    public void onRecipeItemChange(ItemStack currentItem) {
        writeRecipesForItemList(currentItem.getItem());
    }

    public void onRecipeItemChangeDetector() {
        if(previousRecipeInputItem != menu.inputSlot.getItem()) {
            onRecipeItemChange(menu.inputSlot.getItem());
            previousRecipeInputItem = menu.inputSlot.getItem();
        }
    }

    private void writeRecipesForItemList(Item slotInputItem) {
        if(recipes.isEmpty()) return;
        recipeListForItem.clear();
        for (RawNeedlingRecipe recipe : recipes) {
            if(recipe.isInput(slotInputItem)) {
                recipeListForItem.add(recipe.getOutput());
                recipeItem = new ItemStack(recipeListForItem.get(0));
                recipeInd = 0;
            }
        }
    }

    private void setRecipeForSelectedItem() {
        if(recipes.isEmpty()) return;
        for (RawNeedlingRecipe recipe : recipes) {
            if(recipe.isOutput(recipeItem.getItem())) {
                recipeCurrent = recipe;
                needlingPoints = new WolfMath.NeedlingPoints.Builder().firstRecipeLine(recipe.getRecipeLine1()).midRecipeLine(recipe.getRecipeLine2()).lastRecipeLine(recipe.getRecipeLine3()).build();
            }
        }
        setRecipePoints();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        onRecipeItemChangeDetector();
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

        optionsXOffset = 265;
        optionsYOffset = 20;

        // TODO: ADJUST SCREEN POS!!!

        guiGraphics.blit(INVENTORY, x + xOffsetInv, y + yOffsetInv, 0, 0, iW, iH, iW, iH);
        guiGraphics.blit(NEEDLING_FIELD, 20, (height - 200) / 2, 0, 0, 200, 200, 200, 200);

        //RENDER TRANSPARENT SLOT RECIPE

        ResourceLocation transparentslot = new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/slot.png");
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); //RESET FOR FURTHER ADO

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, INVENTORY);

        int iWS = 175; //ImageWidth
        int iHS = 88; //ImageHeight
        int xS = (width - iWS) / 2 - 1;
        int yS = (height - iHS) / 2 + 38;
        int xOffsetMod = 142;
        int yOffsetMod = -105;

        optionsXOffset = xS + xOffsetMod;
        optionsYOffset = yS + yOffsetMod;

        guiGraphics.blit(MAIN, optionsXOffset, optionsYOffset, 0, 0, 146, 83, 146, 83);
        guiGraphics.blit(transparentslot, optionsXOffset + 5, optionsYOffset + 5, 0, 0, 18, 18, 18, 18);

        guiGraphics.blit(SLOT, optionsXOffset + 6, optionsYOffset + 58,0, 0, 18, 18, 18, 18);
        guiGraphics.blit(SLOT, optionsXOffset + 60, optionsYOffset + 58,0, 0, 18, 18, 18, 18);
        guiGraphics.blit(ARROW, optionsXOffset + 32, optionsYOffset + 58,0, 0, 16, 16, 16, 16);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderNeedle(mouseX, mouseY, guiGraphics);
        renderOptions(guiGraphics, mouseX, mouseY, delta);
        renderRecipeTooltip(mouseX, mouseY, guiGraphics);
        renderNeedlingLines(mouseX, mouseY, guiGraphics);
    }

    private void renderOptions(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if(!recipeListForItem.isEmpty()) {
            guiGraphics.renderItem(recipeItem, optionsXOffset + 6, optionsYOffset + 6); //TODO: CORRECT TEXTURE - DONE
            guiGraphics.drawString(minecraft.font, WolfMath.cutComponent(recipeItem.getItem().getDescription(), 18), optionsXOffset + 26, optionsYOffset + 5, -12829636, false); // recipeItem.getItem().getDescription() - WWWWW x4 == 20x l. char
            //guiGraphics.blit(new ResourceLocation(Wolfcraft.MODID, "textures/gui/icons/middle_mouse_wheel.png"), optionsXOffset + 26, optionsYOffset + 14, 0, 0, 11, 12, 11, 12);
        } else {
            guiGraphics.renderItem(new ItemStack(Blocks.BARRIER.asItem()), optionsXOffset + 6, optionsYOffset + 6); // TODO: CORRECT TEXTURE - DONE
            guiGraphics.drawString(minecraft.font, WolfMath.cutComponent(Component.translatable("gui.wolfcraft.needling_station.no_recipe_1"), 19), optionsXOffset + 26, optionsYOffset + 5, -12829636, false); // recipeItem.getItem().getDescription() - WWWWW x4 == 20x l. char
            guiGraphics.drawString(minecraft.font, WolfMath.cutComponent(Component.translatable("gui.wolfcraft.needling_station.no_recipe_2"), 19), optionsXOffset + 26, optionsYOffset + 14, -12829636, false); // recipeItem.getItem().getDescription() - WWWWW x4 == 20x l. char
        }
    }

    private void updateIndex() {
        if(!(recipeListForItem.size() - 1 < recipeInd)) {
            recipeItem = new ItemStack(recipeListForItem.get(recipeInd));
        }
        chosenRecipeList.clear();
        if(!(recipeListForItem.size() - 1 < recipeInd + 1)) {
            chosenRecipeList.add(Component.literal("> ").append(recipeListForItem.get(recipeInd + 1).getDescription()));
        }
        if(!(recipeListForItem.size() - 1 < recipeInd)) {
            chosenRecipeList.add(Component.literal("-> ").append(recipeListForItem.get(recipeInd).getDescription()));
        }
        if(!(0 > recipeInd - 1)) {
            chosenRecipeList.add(Component.literal("> ").append(recipeListForItem.get(recipeInd - 1).getDescription()));
        }
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

    public void renderNeedlingLines(int mX, int mY, GuiGraphics gg) {
        int needlingFieldXMin = 20;
        int needlingFieldXMax = 220;
        int needlingFieldYMin = height / 2 - 100;
        int needlingFieldYMax = height / 2 + 100;
        if (WolfMath.isBetween(mX, needlingFieldXMin, needlingFieldXMax) && WolfMath.isBetween(mY, needlingFieldYMin, needlingFieldYMax)) {
            for (NeedlingStationRaycast.Point renderPoint : NeedlingStationRaycast.performRaycast(lastRecipePointX, lastRecipePointY, mX, mY)) {
                gg.fill(renderPoint.getX(), renderPoint.getY(), renderPoint.getX() + 1, renderPoint.getY() + 1, -1);
            }
        }
    }

    public void renderPoints(GuiGraphics gg) {
        //Line 1
        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(0))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(0)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(0)),
                    needlingPoints.getFakeIndex(0),
                    pButton -> needlePointPressed(0, needlingPoints.getFakeIndex(0)))));
        }

        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(1))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(1)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(1)),
                    needlingPoints.getFakeIndex(1),
                    pButton -> needlePointPressed(1, needlingPoints.getFakeIndex(1)))));
        }

        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(2))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(2)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(2)),
                    needlingPoints.getFakeIndex(2),
                    pButton -> needlePointPressed(2, needlingPoints.getFakeIndex(2)))));
        }
        // Line 2
        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(3))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(3)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(3)),
                    needlingPoints.getFakeIndex(3),
                    pButton -> needlePointPressed(3, needlingPoints.getFakeIndex(3)))));
        }

        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(4))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(4)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(4)),
                    needlingPoints.getFakeIndex(4),
                    pButton -> needlePointPressed(4, needlingPoints.getFakeIndex(4)))));
        }

        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(5))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(5)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(5)),
                    needlingPoints.getFakeIndex(5),
                    pButton -> needlePointPressed(5, needlingPoints.getFakeIndex(5)))));
        }
        // Line 3
        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(6))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(6)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(6)),
                    needlingPoints.getFakeIndex(6),
                    pButton -> needlePointPressed(6, needlingPoints.getFakeIndex(6)))));
        }

        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(7))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(7)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(7)),
                    needlingPoints.getFakeIndex(7),
                    pButton -> needlePointPressed(7, needlingPoints.getFakeIndex(7)))));
        }

        if(needlingPoints.isPointThere(needlingPoints.getFakeIndex(8))) {
            needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(8)),
                    needlingPoints.getPointY(needlingPoints.getFakeIndex(8)),
                    needlingPoints.getFakeIndex(8),
                    pButton -> needlePointPressed(8, needlingPoints.getFakeIndex(8)))));
        }
    }

    public void needlePointPressed(int needlePointRealIndex, int needlePointFakeIndex) {

    }

    public void renderRecipeTooltip(int mX, int mY, GuiGraphics gg) {
        if(recipeListForItem.isEmpty()) return;
        int tooltipFieldXMin = optionsXOffset + 5;
        int tooltipFieldXMax = optionsXOffset + 150;
        int tooltipFieldYMin = optionsYOffset + 5;
        int tooltipFieldYMax = optionsYOffset + 5 + 18;
        if (WolfMath.isBetween(mX, tooltipFieldXMin, tooltipFieldXMax) && WolfMath.isBetween(mY, tooltipFieldYMin, tooltipFieldYMax)) {
            gg.renderTooltip(minecraft.font, chosenRecipeList, Optional.empty(), optionsXOffset + 19, optionsYOffset + 10);
        }
    }

    private boolean shouldScroll(int mX, int mY) {
        int needlingFieldXMin = 20;
        int needlingFieldXMax = 220;
        int needlingFieldYMin = height / 2 - 100;
        int needlingFieldYMax = height / 2 + 100;
        return !(WolfMath.isBetween(mX, needlingFieldXMin, needlingFieldXMax) && WolfMath.isBetween(mY, needlingFieldYMin, needlingFieldYMax));
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        System.out.println(pDelta);
        if(recipeListForItem.size() != 0 && shouldScroll(((int)pMouseX), ((int)pMouseY))) {
            recipeInd = (int) (recipeInd + pDelta);
            if(recipeInd <= 0) {
                recipeInd = 0;
            }
            if(recipeInd >= recipeListForItem.size()) {
                recipeInd--;
            }
                try {
                    //minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.SCROLL_WHEEL.get(), SoundSource.BLOCKS);
                    menu.getLevel().playSound(menu.getPlayer(), menu.getPlayer().blockPosition(), ModSounds.SCROLL_WHEEL.get(), SoundSource.BLOCKS);
                } catch (Exception e) {
                    ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
                    SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.needling_station.failed_mouse_wheel_sound"), (Component)null);
                    e.printStackTrace();
                }
                updateIndex();
        }

        setRecipeForSelectedItem();
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
}
