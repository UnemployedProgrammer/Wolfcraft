package com.unemployedgames.wolfcraft.item.guidebook;

import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AllGuideEntrys {
    public static List<GuideBookEntry> GUIDE_BOOK_ENTRYS = new ArrayList<>();

    public static GuideBookEntry WELCOME = new GuideBookEntry(Component.literal("Wolfcraft"), Component.literal("Random Desi"), new ResourceLocation(Wolfcraft.MODID, "textures/gui/wolfcraft_title.png"));


    public static void addAllEntries() {

    }
}
