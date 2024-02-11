package com.unemployedgames.wolfcraft.item;

import com.unemployedgames.wolfcraft.block.ModBlocks;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class AllBlocksAndItems {
    public static List<Item> all = new ArrayList<>();

    public static void add() {
        //all.add(ModItems.EXAMPLEITEM);
        //all.add(ModBlocks.EXAMPLEBLOCK.get().asItem());

        all.add(ModItems.PLATE.get());
        all.add(ModBlocks.TABLE.get().asItem());
        all.add(ModBlocks.DRYING_RACK.get().asItem());
    }
}
