package com.unemployedgames.wolfcraft.debug;

import com.google.common.collect.ImmutableSet;
import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.block.entity.root.IHaveDebugInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.event.RenderGuiEvent;

import java.util.Map;

public class DebugOverlay {
    public static void render(RenderGuiEvent.Pre event, DebugOverlayInfos infos) {
        BlockState hoveringBlock = infos.getHoveringBlock();
        boolean display = true;
        if(hoveringBlock == null || hoveringBlock.isAir())
            display = false;

        renderBackground(event.getGuiGraphics());
        renderHeader(event.getGuiGraphics());


        if(display) {
            Block hoveringBlockType = hoveringBlock.getBlock();
            ImmutableSet<Map.Entry<Property<?>, Comparable<?>>> hoveringBlockValues = hoveringBlock.getValues().entrySet();
            event.getGuiGraphics().renderItem(new ItemStack(hoveringBlockType.asItem()), 4, 52);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(hoveringBlockType.getName().getString()), 24, 52, -1, false);
            int offset = 0;
            for(Map.Entry<Property<?>, Comparable<?>> entry : hoveringBlockValues) {
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(getPropertyValueString(entry)), 4, 72 + offset, -1, false);
                offset = offset + 10;
            }

            if(infos.getHoveringBlockEntity() != null) {
                try {
                    boolean hasHoveringInformation = infos.getHoveringBlockEntity() instanceof IHaveDebugInfo;
                    if(hasHoveringInformation) {
                        DebugInfoEntryCollection collection = ((IHaveDebugInfo) infos.getHoveringBlockEntity()).getDebugInfoString(new DebugInfoEntryCollection());
                        offset = offset + 15;
                        if(!collection.getStrings().isEmpty()) {
                            for (Component string : collection.getStrings()) {
                                event.getGuiGraphics().drawString(Minecraft.getInstance().font, string, 4, 72 + offset, -1, false);
                                offset = offset + 10;
                            }
                        }
                        if(!collection.getNumbers().isEmpty()) {
                            for (Component string : collection.getNumbers()) {
                                event.getGuiGraphics().drawString(Minecraft.getInstance().font, string, 4, 72 + offset, -1, false);
                                offset = offset + 10;
                            }
                        }
                        if(!collection.getBooleans().isEmpty()) {
                            for (Component string : collection.getBooleans()) {
                                event.getGuiGraphics().drawString(Minecraft.getInstance().font, string, 4, 72 + offset, -1, false);
                                offset = offset + 10;
                            }
                        }
                        if(!collection.getCompounds().isEmpty()) {
                            for (Component string : collection.getCompounds()) {
                                event.getGuiGraphics().drawString(Minecraft.getInstance().font, string, 4, 72 + offset, -1, false);
                                offset = offset + 10;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("FAIL");
                }
            }
        }
    }

    /// SUBFUNCTIONS

    private static String getPropertyValueString(Map.Entry<Property<?>, Comparable<?>> pEntry) {
        Property<?> property = pEntry.getKey();
        Comparable<?> comparable = pEntry.getValue();
        String s = Util.getPropertyName(property, comparable);
        if (Boolean.TRUE.equals(comparable)) {
            s = ChatFormatting.GREEN + s;
        } else if (Boolean.FALSE.equals(comparable)) {
            s = ChatFormatting.RED + s;
        }

        return property.getName() + ": " + s;
    }
    public static void renderBackground(GuiGraphics gg) {
        gg.fill(2, 2, 250, 100, 0x00000020);
    }
    public static void renderHeader(GuiGraphics gg) {
        gg.blit(new ResourceLocation(Wolfcraft.MODID, "textures/gui/googles_debug.png"), 7, 0, 0, 0, 32, 32, 32, 32);
        gg.drawString(Minecraft.getInstance().font, Component.literal("Wolfcraft: Debug Screen"), 45, 8, -1, false);
        gg.fill(4, 48, 248, 50, -1);
    }
}
