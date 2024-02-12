package com.unemployedgames.wolfcraft.block.entity.entities.drying_rack;

import com.unemployedgames.wolfcraft.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class ItemsThatCanDry {
    private static HashMap<String, Item> itemsWet = new HashMap<>();
    private static HashMap<String, Item> itemsDry = new HashMap<>();

    private static String addItem(Item wet, Item dry, String key) {
        itemsWet.put(key, wet);
        itemsDry.put(key, dry);
        return key;
    }

    public static Item getWetItem(String key) {
        return itemsWet.get(key);
    }

    public static Item getDryItem(String key) {
        return itemsDry.get(key);
    }

    public static String getKeyFromWetItem(Item wetItem) {
        for (String s : itemsWet.keySet()) {
            if(wetItem == itemsWet.get(s))
                return s;
        }
        return "";
    }

    public static String getKeyFromDryItem(Item dryItem) {
        for (String s : itemsDry.keySet()) {
            if(dryItem == itemsDry.get(s))
                return s;
        }
        return "";
    }

    public static String getKeyFromItem(Item wetOrDryItem) {
        for (String s : itemsDry.keySet()) {
            if(wetOrDryItem == itemsDry.get(s))
                return s;
        }
        for (String s : itemsWet.keySet()) {
            if(wetOrDryItem == itemsWet.get(s))
                return s;
        }
        return "";
    }

    public static boolean canItemDry(Item wetOrDryItem) {
        for (String s : itemsWet.keySet()) {
            if(wetOrDryItem == itemsWet.get(s))
                return true;
        }
        return false;
    }

    /////// ITEMS //////

    public static String SPONGE = addItem(Items.WET_SPONGE, Items.SPONGE, "sponge");
    public static String WOLF_LEATHER = addItem(ModItems.WOLF_LEATHER_WET.get(), ModItems.WOLF_LEATHER_DRY.get(), "wolf_leather");

    //public static String LEAHTER = addItem(Items.LEATHER, Items.LEAD, "leatherlead");
}
