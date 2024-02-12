package com.unemployedgames.wolfcraft.item.custom;

import com.unemployedgames.wolfcraft.item.AllBlocksAndItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GetAllItems extends Item {
    public GetAllItems(Properties pProperties) {
        super(pProperties);
    }
    private static Component header = Component.translatable("item.wolfcraft.all.inv");
    private static Component sub = Component.translatable("item.wolfcraft.all.inv.tooltip");

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        pStack.shrink(1);
        try {
            AllBlocksAndItems.all.clear();
        } catch (Exception e) {
            System.out.println("already Empty!");
        }
        AllBlocksAndItems.add();
        if(pEntity instanceof Player plr) {
            for (Item item : AllBlocksAndItems.all) {
                plr.getInventory().add(new ItemStack(item));
            }
        }
    }
}
