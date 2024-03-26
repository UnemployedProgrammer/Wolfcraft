package com.unemployedgames.wolfcraft.misc;

import com.unemployedgames.wolfcraft.modmainmenu.IssuesAndSuggestionsScreen;
import com.unemployedgames.wolfcraft.modmainmenu.ModMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ClientHooks {
    public static void openHomePageScreen() {
        //Minecraft.getInstance().setScreen(new CopyCatButtonConfigurationScreen(pos));
        Minecraft.getInstance().setScreen(new IssuesAndSuggestionsScreen(true));
    }
}
