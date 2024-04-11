package com.unemployedgames.wolfcraft.misc;

import com.unemployedgames.wolfcraft.item.guidebook.GuideBookScreen;
import com.unemployedgames.wolfcraft.modmainmenu.IssuesAndSuggestionsScreen;
import com.unemployedgames.wolfcraft.modmainmenu.ModMainScreen;
import com.unemployedgames.wolfcraft.render.ui.debugthings.DebugScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ClientHooks {
    public static void openHomePageScreen() {
        //Minecraft.getInstance().setScreen(new CopyCatButtonConfigurationScreen(pos));
        Minecraft.getInstance().setScreen(new IssuesAndSuggestionsScreen(true));
    }

    public static void openGuideBook() {
        //Minecraft.getInstance().setScreen(new CopyCatButtonConfigurationScreen(pos));

        //Minecraft.getInstance().setScreen(new GuideBookScreen(false, null));

        //DEBUG!!!
        Minecraft.getInstance().setScreen(new DebugScreen());
    }
}
