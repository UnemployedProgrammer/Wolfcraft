package com.unemployedgames.wolfcraft.misc;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.block.ModBlocks;
import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableBlock;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableEntity;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableEntityRenderer;
import com.unemployedgames.wolfcraft.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Wolfcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {


    public static void onBlockBroken_DEACTIVATED(BlockEvent.BreakEvent e) {
        boolean isTable = e.getState().is(ModBlocks.TABLE.get()) && !e.getLevel().isClientSide();
        System.out.println("Block Kaputt :(");

        if (isTable) {
            e.setCanceled(true);
            try {
                if(e.getState().hasProperty(BooleanProperty.create("plate")) && e.getState().getValue(BooleanProperty.create("plate")).booleanValue()) { // HAS PLATE
                    System.out.println("Hat Teller");
                    if (e.getLevel().getBlockEntity(e.getPos()) instanceof TableEntity table) { // HAS FOOD ON IT
                        System.out.println("Entity Gefunden");
                        try {
                            if(!table.getItem().isEmpty())
                                e.getLevel().addFreshEntity(new ItemEntity(table.getLevel(), e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), table.getItem()));

                            e.getLevel().addFreshEntity(new ItemEntity(table.getLevel(), e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), new ItemStack(ModItems.PLATE.get())));
                            table.setItem(ItemStack.EMPTY);
                            System.out.println("Gedroppt! :)");
                        } catch (Exception exception) {
                            //sendtoast
                            System.out.println("Nicht Gedroppt :(");
                        }
                        table.setItem(ItemStack.EMPTY);
                    }
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("FAIL");
            }
            e.getLevel().destroyBlock(e.getPos(), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {

    }
}