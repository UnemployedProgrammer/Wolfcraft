package com.unemployedgames.wolfcraft.debug;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.StonecutterMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DebugInfoEntryCollection {
    protected HashMap<String, Integer> numberNBTList = new HashMap<String, Integer>();
    protected HashMap<String, String> stringNBTList = new HashMap<String, String>();
    protected HashMap<String, Boolean> booleanNBTList = new HashMap<String, Boolean>();
    protected HashMap<String, CompoundTag> compoundNBTList = new HashMap<String, CompoundTag>();

    public void addNumber(String key, int value) {
        numberNBTList.put(key, value);
    }

    public void addString(String key, String value) {
        stringNBTList.put(key, value);
    }

    public void addBoolean(String key, boolean value) {
        booleanNBTList.put(key, value);
    }

    public void addCompoundTag(String key, CompoundTag value) {
        compoundNBTList.put(key, value);
    }

    public List<Component> getNumbers() {
        List<Component> com = new ArrayList<>();

        for (String key : numberNBTList.keySet()) {
            com.add(Component.literal(key + ": " + ChatFormatting.GOLD + numberNBTList.get(key).toString()));
        }

        return com;
    }

    public List<Component> getStrings() {
        List<Component> com = new ArrayList<>();

        for (String key : stringNBTList.keySet()) {
            com.add(Component.literal(key + ": " + ChatFormatting.GREEN + '"' + stringNBTList.get(key) + '"'));
        }

        return com;
    }

    public List<Component> getBooleans() {
        List<Component> com = new ArrayList<>();

        for (String key : booleanNBTList.keySet()) {
            ChatFormatting colorBool = booleanNBTList.get(key) ? ChatFormatting.GREEN : ChatFormatting.RED;
            com.add(Component.literal(key + ": " + colorBool + booleanNBTList.get(key)));
        }
        return com;
    }

    public List<Component> getCompounds() {
        List<Component> com = new ArrayList<>();

        for (String key : compoundNBTList.keySet()) {
            com.add(Component.literal(key + ": " + ChatFormatting.BLUE + compoundNBTList.get(key)));
        }

        return com;
    }
}
