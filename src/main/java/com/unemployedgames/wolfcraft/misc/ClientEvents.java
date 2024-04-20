package com.unemployedgames.wolfcraft.misc;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.block.entity.entities.drying_rack.DryingRackRenderer;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableEntityRenderer;
import com.unemployedgames.wolfcraft.modmainmenu.MainMenuButton;
import com.unemployedgames.wolfcraft.render.ui.screen.YouAreBannedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.mutable.MutableObject;

@Mod.EventBusSubscriber(modid = Wolfcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keys.INSTANCE.exampleKey);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Entities
        event.registerBlockEntityRenderer(BlockEntitys.TABLE.get(), TableEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntitys.DRYING_RACK.get(), DryingRackRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }
}