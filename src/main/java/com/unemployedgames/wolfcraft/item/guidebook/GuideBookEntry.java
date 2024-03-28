package com.unemployedgames.wolfcraft.item.guidebook;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record GuideBookEntry(Component title, Component content, ResourceLocation image) {
}
