package com.unemployedgames.wolfcraft.misc;

import com.mojang.logging.LogUtils;
import com.unemployedgames.wolfcraft.modmainmenu.IssuesAndSuggestionsScreen;
import com.unemployedgames.wolfcraft.render.ui.screen.YouAreBannedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ForgeClientBus {
    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init event) {
        Screen gui = event.getScreen();

        if(gui instanceof TitleScreen) {
            checkBans(gui);
        }
    }

    public static void checkBans(Screen gui) {
        try {
            CompletableFuture<ApiRequests.PlayerBan> future = ApiRequests.getBanAsync(Minecraft.getInstance().getUser().getName());
            future.thenAccept(playerBan -> {
                try {
                    ApiRequests.PlayerBan pBan = ApiRequests.getBan(Minecraft.getInstance().getUser().getName());
                    if (pBan == null) return;
                    if(pBan.banned) {
                        if(!BanAcception.accepedBan()) {
                            Minecraft.getInstance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    Minecraft.getInstance().setScreen(new YouAreBannedScreen(pBan.banDetails, gui.getMinecraft()));
                                }
                            });
                            BanAcception.acceptBan();
                        }
                    } else {
                        if(BanAcception.accepedBan()) {
                            BanAcception.deleteAcceptBan();
                            Minecraft.getInstance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
                                    SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("ban.wolfcraft.bye"), (Component)null);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    LogUtils.getLogger().warn(e.getMessage());
                }
            });
        } catch (Exception e) {
            LogUtils.getLogger().warn(e.getMessage());
        }
    }
}
