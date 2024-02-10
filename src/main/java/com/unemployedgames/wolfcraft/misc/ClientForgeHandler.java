package com.unemployedgames.wolfcraft.misc;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.debug.DebugOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Wolfcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if(Keys.INSTANCE.exampleKey.consumeClick() && minecraft.player != null) {
            if(DebugOverlay.isOpen)
                DebugOverlay.isOpen = false;
            else
                DebugOverlay.isOpen = true;
        }
    }
}
