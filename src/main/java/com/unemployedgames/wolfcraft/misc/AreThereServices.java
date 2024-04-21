package com.unemployedgames.wolfcraft.misc;

import com.mojang.logging.LogUtils;
import com.unemployedgames.wolfcraft.modmainmenu.MainMenuButton;
import net.minecraft.client.Minecraft;

import java.util.concurrent.CompletableFuture;

public class AreThereServices {

    public static Boolean SERVICES = false;
    public static Boolean CONNECTING = false;

    public static void areServicesThere() {
        CONNECTING = true;
        try {
            CompletableFuture<ApiRequests.PlayerBan> future = ApiRequests.getBanAsync(Minecraft.getInstance().getUser().getName());
            future.thenAccept(playerBan -> {
                try {
                    ApiRequests.PlayerBan pBan = ApiRequests.getBan(Minecraft.getInstance().getUser().getName());
                    if (pBan == null) {
                        SERVICES = false;
                    } else {
                        SERVICES = true;
                    }
                    CONNECTING = false;
                } catch (Exception e) {
                    LogUtils.getLogger().warn(e.getMessage());
                }
            });
        } catch (Exception e) {
            LogUtils.getLogger().warn(e.getMessage());
        }
    }

    //TODO: REPLACE WITH EXTRA REQUEST, DO NOT SEND BAN, FOR PERFORMANCE!
}
