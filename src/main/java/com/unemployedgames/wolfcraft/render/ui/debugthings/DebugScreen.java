package com.unemployedgames.wolfcraft.render.ui.debugthings;

import com.unemployedgames.wolfcraft.modmainmenu.IssuesAndSuggestionsScreen;
import com.unemployedgames.wolfcraft.render.ui.Icons;
import com.unemployedgames.wolfcraft.render.ui.components.SmoothProgessBar;
import com.unemployedgames.wolfcraft.render.ui.components.fancyconfigs.UnemployedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DebugScreen extends Screen {

    SmoothProgessBar pB;
    Button start;

    UnemployedButton btn;

    public DebugScreen() {
        super(Component.literal("DB"));
    }

    @Override
    protected void init() {
        super.init();

        if (true) { //this.minecraft == null
            Minecraft.getInstance().setScreen(new AlertScreen(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getInstance().setScreen(new IssuesAndSuggestionsScreen(true));
                }
            }, Component.literal("Minecraft is null"), Component.literal("java.lang.NullPointerException: Cannot read field \"level\" because \"this.minecraft\" is null" +
                    "at net.minecraft.client.gui.screens.Screen.renderBackground(Screen.java:359) ~[forge-1.20.1-47.2.19_mapped_parchment_2023.06.26-1.20.1-recomp.jar:?] {re:classloading,pl:accesstransformer:B,pl:runtimedistcleaner:A}...  ~ 10 more lines..")));
        }


        //pB = addRenderableWidget(new SmoothProgessBar(10, 10, 500, 20, Component.literal("Bar")));
        //start = addRenderableWidget(new Button.Builder(Component.literal("Start Animation"), pButton -> {DebugScreen.this.pB.animateBarTo(50);} ).bounds(50, 50, 500, 20).build());

        btn = addRenderableWidget(new UnemployedButton(width / 2 - 25, height / 2 - 25, 50, 50, Component.literal("Test btn"), Icons.PRESS_ME_DEBUG, pButton -> {
            System.out.println("Dieser knopf kann gedrückt werden!");
        }));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        //pB.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
