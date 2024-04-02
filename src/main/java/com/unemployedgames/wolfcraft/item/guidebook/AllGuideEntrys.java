package com.unemployedgames.wolfcraft.item.guidebook;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.block.ModBlocks;
import com.unemployedgames.wolfcraft.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AllGuideEntrys {
    public static List<GuideBookEntry> GUIDE_BOOK_ENTRYS = new ArrayList<>();
    public static List<GuideBookEntry> GUIDE_BOOK_ENTRYS_SITE1 = new ArrayList<>();

    public static GuideBookEntry WELCOME = new GuideBookEntry(Component.literal("Wolfcraft"), Component.literal("Wolfcraft is an open source Minecraft mod."), ModItems.GUIDE_BOOK.get());
    public static GuideBookEntry NEEDLING_STATION = new GuideBookEntry(Component.literal("Needling Station"), Component.literal("In Wolfcraft, a needling block /lis a valuable tool for crafting /litems by sewing materials /ltogether. To utilize it, simply /lplace the desired number of /litems in the input slot, hover /lover the preview above the input /lslot to change the recipe if /lneeded, click the stitching /lpoints in the correct sequence, /land if the sequence isn't clear, /lclick a random point to illuminate /lthe next point you need to click /lfor a brief moment, then retrieve /lyour crafted item from the output /lslot."), ModBlocks.NEEDLING_STATION.get().asItem());


    public static void addAllEntries() {
        GUIDE_BOOK_ENTRYS.add(WELCOME);
        GUIDE_BOOK_ENTRYS.add(NEEDLING_STATION);

        //Site 1
        GUIDE_BOOK_ENTRYS_SITE1.add(WELCOME);
        GUIDE_BOOK_ENTRYS_SITE1.add(NEEDLING_STATION);
    }
}
