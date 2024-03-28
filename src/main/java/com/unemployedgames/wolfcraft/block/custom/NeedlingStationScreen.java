package com.unemployedgames.wolfcraft.block.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.misc.ModSounds;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import com.unemployedgames.wolfcraft.recipe.NeedlingRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
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
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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

    private static final ResourceLocation NEEDLING_POINT =
            new ResourceLocation(Wolfcraft.MODID, "textures/gui/needling_station/point.png");

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

        //recipes.add(new RawNeedlingRecipe(ModItems.WOLF_LEATHER_WET.get(), 8, ModItems.WET_WOLF_ARMOR_CHESTPLATE.get(), 1, "1 7", "286", "345"));
        //recipes.add(new RawNeedlingRecipe(ModItems.WOLF_LEATHER_WET.get(), 7, ModItems.WET_WOLF_ARMOR_LEGGINS.get(), 1, "345", "2 6", "1 7"));
        try {
            List<NeedlingRecipeType> crecipes = minecraft.level.getRecipeManager().getAllRecipesFor(NeedlingRecipeType.Type.INSTANCE);
            LoggerFactory.getLogger(NeedlingStationBlock.class).info("Size of Recipes List: " + crecipes.size());
            for (NeedlingRecipeType recipe : crecipes) {
                recipes.add(new RawNeedlingRecipe(recipe.getInputItem().getItems()[0].getItem(), recipe.getInputItemCount(), recipe.getOutput().getItem(), recipe.getOutput().getCount(), recipe.getRecipeLine1(), recipe.getRecipeLine2(), recipe.getRecipeLine3()));
            }
        } catch (Exception e) {
            LogUtils.getLogger().info(e.toString());
            e.printStackTrace();
            ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
            SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.literal("Failed to Fetch Recipes, view logs for more info!"), (Component)null);
        }
    }

    public void onRecipeItemChange(ItemStack currentItem) {
        writeRecipesForItemList(currentItem.getItem());
    }

    public void onRecipeItemChangeDetector() {
        if(previousRecipeInputItem != menu.inputSlot.getItem()) {
            onRecipeItemChange(menu.inputSlot.getItem());
            previousRecipeInputItem = menu.inputSlot.getItem();
            LogUtils.getLogger().info("Just changed the Needling Recipe, throwing off all voids!");
        }
    }

    private void writeRecipesForItemList(Item slotInputItem) {
        LogUtils.getLogger().info("Rewrote Needling Recipes for " + slotInputItem.getDescriptionId());
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
                LogUtils.getLogger().info("Setting the Needling Points, here there are: \n " + recipeCurrent.getRecipeLine1() + "\n" + recipeCurrent.getRecipeLine2() + "\n" + recipeCurrent.getRecipeLine3());
            }
        }
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
        renderNeedle(mouseX, mouseY, guiGraphics);
        renderOptions(guiGraphics, mouseX, mouseY, delta);
        renderRecipeTooltip(mouseX, mouseY, guiGraphics);
        renderNeedlingLines(mouseX, mouseY, guiGraphics);
        renderPoints(guiGraphics);
        renderTooltip(guiGraphics, mouseX, mouseY);
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
                gg.fill(renderPoint.getX(), renderPoint.getY(), renderPoint.getX() + 2, renderPoint.getY() + 2, -1);
            }
        }
    }

    public void renderPoints(GuiGraphics gg) {
        if(recipeListForItem.isEmpty() || recipeItem.is(Items.BARRIER) || needlingPoints == null) return;
        //TODO: all Dosnt Work, Change!!!
        needlingPointsSplit.clear();
        //INDS
        int r0 = needlingPoints.getFakeIndex(0);
        int r1 = needlingPoints.getFakeIndex(1);
        int r2 = needlingPoints.getFakeIndex(2);
        int r3 = needlingPoints.getFakeIndex(3);
        int r4 = needlingPoints.getFakeIndex(4);
        int r5 = needlingPoints.getFakeIndex(5);
        int r6 = needlingPoints.getFakeIndex(6);
        int r7 = needlingPoints.getFakeIndex(7);
        int r8 = needlingPoints.getFakeIndex(8);
        //Line 1
        if(needlingPoints.isPointThere(r0)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r0),
                    needlingPoints.getPointY(r0),
                    1, r0
            ));
        }

        if(needlingPoints.isPointThere(r1)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r1),
                    needlingPoints.getPointY(r1),
                    1, r1
            ));
        }

        if(needlingPoints.isPointThere(r2)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r2),
                    needlingPoints.getPointY(r2),
                    1, r2
            ));
        }

        if(needlingPoints.isPointThere(r3)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r3),
                    needlingPoints.getPointY(r3),
                    1, r3
            ));
        }

        if(needlingPoints.isPointThere(r4)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r4),
                    needlingPoints.getPointY(r4),
                    1, r4
            ));
        }

        if(needlingPoints.isPointThere(r5)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r5),
                    needlingPoints.getPointY(r5),
                    1, r5
            ));
        }

        if(needlingPoints.isPointThere(r6)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r6),
                    needlingPoints.getPointY(r6),
                    1, r6
            ));
        }

        if(needlingPoints.isPointThere(r7)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r7),
                    needlingPoints.getPointY(r7),
                    1, r7
            ));
        }

        if(needlingPoints.isPointThere(r8)) {
            needlingPointsSplit.add(new NeedlingPoint(
                    needlingPoints.getPointX(r8),
                    needlingPoints.getPointY(r8),
                    1, r8
            ));
        }

        // FINALLY, RENDER! (AND ADD BOUNDING BOXES :( )
        int fieldOffsetX = 20;
        int fieldOffsetY = (height - 200) / 2;
        for (NeedlingPoint needlingPoint : needlingPointsSplit) {
            gg.blit(NEEDLING_POINT, fieldOffsetX + needlingPoint.x(), fieldOffsetY + needlingPoint.y(), 0,0,20,20,10,10);
            System.out.println(needlingPointsSplit);
        }

    }
    //OLD WRONG CODE KEPT FOR MAYBE FUTURE USES:
    //needlingPointsSplit.add(addRenderableWidget(new NeedlingPoint(needlingPoints.getPointX(needlingPoints.getFakeIndex(0)),
    //        needlingPoints.getPointY(needlingPoints.getFakeIndex(0)),
    //        needlingPoints.getFakeIndex(0),
    //        pButton -> needlePointPressed(0, needlingPoints.getFakeIndex(0)))));

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
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {


        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
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
