package com.unemployedgames.wolfcraft.item;

import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static Boolean TABSORTED = false;

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Wolfcraft.MODID);


    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main_mod_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Items.COMMAND_BLOCK))
                    .title(Component.translatable("creativetab.wolfcraft.main"))
                    .displayItems((pParameters, pOutput) -> {

                        // Add items and Blocks
                        pOutput.accept(ModItems.CAPPY_HAT.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
