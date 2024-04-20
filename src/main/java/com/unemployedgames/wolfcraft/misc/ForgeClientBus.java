package com.unemployedgames.wolfcraft.misc;

import com.unemployedgames.wolfcraft.modmainmenu.IssuesAndSuggestionsScreen;
import com.unemployedgames.wolfcraft.render.ui.screen.YouAreBannedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ForgeClientBus {
    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init event) {
        Screen gui = event.getScreen();

        if(gui instanceof TitleScreen) {
            try {
                ApiRequests.PlayerBan pBan = ApiRequests.getBan(Minecraft.getInstance().getUser().getName());
                if (pBan == null) return;
                if(pBan.banned) {
                    if(!BanAcception.accepedBan()) {
                        Minecraft.getInstance().setScreen(new YouAreBannedScreen(pBan.banDetails));
                        BanAcception.acceptBan();
                    }
                } else {
                    if(BanAcception.accepedBan()) BanAcception.deleteAcceptBan();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
