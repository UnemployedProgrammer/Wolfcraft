package com.unemployedgames.wolfcraft.modmainmenu;


import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.misc.*;
import com.unemployedgames.wolfcraft.render.ui.screen.YouAreBannedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class MainMenuButton extends Button {
    public static final ItemStack ICON = new ItemStack(ModItems.WOLF_LEATHER_DRY.get());
    public boolean services = false;

    public MainMenuButton(int x, int y) {
        super(x, y, 20, 20, Components.immutableEmpty(), MainMenuButton::click, DEFAULT_NARRATION);
        AreThereServices.areServicesThere();
    }
    @Override
    public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {
        if (AreThereServices.CONNECTING) {
            pGuiGraphics.blit(new ResourceLocation(Wolfcraft.MODID, "textures/gui/noconnection.png"), getX() + 2, getY() + 2, 0,0,16,16,16,16);
            if(isHovered()) {
                pGuiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable("core.wolfcraft.api.reconnect")), Optional.empty(), getX() + 10, getY() + 10);
            }
        }

        if(AreThereServices.SERVICES & !AreThereServices.CONNECTING) {
            pGuiGraphics.renderItem(ICON, getX() + 2, getY() + 2);
            if(isHovered()) {
                pGuiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable("creativetab.wolfcraft.main")), Optional.empty(), getX() + 10, getY() + 10);
            }
        } else if (!AreThereServices.SERVICES & !AreThereServices.CONNECTING) {
            pGuiGraphics.blit(new ResourceLocation(Wolfcraft.MODID, "textures/gui/noconnection.png"), getX() + 2, getY() + 2, 0,0,16,16,16,16);
            if(isHovered()) {
                pGuiGraphics.renderTooltip(Minecraft.getInstance().font, List.of(Component.translatable("core.wolfcraft.api.unavailable")), Optional.empty(), getX() + 10, getY() + 10);
            }
        }
    }

    public static void click(Button b) {
        if (AreThereServices.SERVICES) ClientHooks.openHomePageScreen();
    }

    public static class SingleMenuRow {
        public final String left, right;
        public SingleMenuRow(String left, String right) {
            this.left = I18n.get(left);
            this.right = I18n.get(right);
        }
        public SingleMenuRow(String center) {
            this(center, center);
        }
    }

    public static class MenuRows {
        public static final MenuRows MAIN_MENU = new MenuRows(Arrays.asList(
                new SingleMenuRow("menu.singleplayer"),
                new SingleMenuRow("menu.multiplayer"),
                new SingleMenuRow("fml.menu.mods", "menu.online"),
                new SingleMenuRow("narrator.button.language", "narrator.button.accessibility")
        ));

        public static final MenuRows INGAME_MENU = new MenuRows(Arrays.asList(
                new SingleMenuRow("menu.returnToGame"),
                new SingleMenuRow("gui.advancements", "gui.stats"),
                new SingleMenuRow("menu.sendFeedback", "menu.reportBugs"),
                new SingleMenuRow("menu.options", "menu.shareToLan"),
                new SingleMenuRow("menu.returnToMenu")
        ));

        protected final List<String> leftButtons, rightButtons;

        public MenuRows(List<SingleMenuRow> variants) {
            leftButtons = variants.stream().map(r -> r.left).collect(Collectors.toList());
            rightButtons = variants.stream().map(r -> r.right).collect(Collectors.toList());
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    public static class OpenConfigButtonHandler {

        @SubscribeEvent
        public static void onGuiInit(ScreenEvent.Init event) {
            Screen gui = event.getScreen();

            MenuRows menu = null;
            int rowIdx = 0, offsetX = 0;
            if (gui instanceof TitleScreen) {
                menu = MenuRows.MAIN_MENU;
                rowIdx = 1;
                offsetX = -4;
            } else if (gui instanceof PauseScreen) {
                menu = MenuRows.INGAME_MENU;
                rowIdx = 1;
                offsetX = -4;
            }

            if (rowIdx != 0 && menu != null) {
                boolean onLeft = offsetX < 0;
                String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);

                int offsetX_ = offsetX;
                MutableObject<GuiEventListener> toAdd = new MutableObject<>(null);
                event.getListenersList()
                        .stream()
                        .filter(w -> w instanceof AbstractWidget)
                        .map(w -> (AbstractWidget) w)
                        .filter(w -> w.getMessage()
                                .getString()
                                .equals(target))
                        .findFirst()
                        .ifPresent(w -> toAdd
                                .setValue(new MainMenuButton(w.getX() + offsetX_ + (onLeft ? -20 : w.getWidth()), w.getY())));
                if (toAdd.getValue() != null)
                    event.addListener(toAdd.getValue());
            }
        }

    }
}
