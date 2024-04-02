package com.unemployedgames.wolfcraft.item.guidebook;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public record GuideBookEntry(Component title, Component content, Item image) {
}
