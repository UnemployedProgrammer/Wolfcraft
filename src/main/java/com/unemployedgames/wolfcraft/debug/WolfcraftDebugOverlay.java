package com.unemployedgames.wolfcraft.debug;

import com.unemployedgames.wolfcraft.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class WolfcraftDebugOverlay {
    private static final DebugOverlayInfos infos = new DebugOverlayInfos();
    public static void eventHandler(RenderGuiEvent.Pre event) {
        if (Config.isDebug && (!(Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SPECTATOR) || !Minecraft.getInstance().options.hideGui)) {
            int w = event.getWindow().getGuiScaledWidth();
            int h = event.getWindow().getGuiScaledHeight();

            Level world = null;
            double x = 0;
            double y = 0;
            double z = 0;

            Player entity = Minecraft.getInstance().player;
            if (entity != null) {
                world = entity.level();
                x = entity.getX();
                y = entity.getY();
                z = entity.getZ();
            }

            DebugOverlay.render(event, infos);

        }

    }
}
