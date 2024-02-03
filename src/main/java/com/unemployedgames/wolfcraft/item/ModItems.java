package com.unemployedgames.wolfcraft.item;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.armor.CappyHat;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.example.item.WolfArmorItem;
import software.bernie.example.registry.ItemRegistry;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Wolfcraft.MODID);

    //public static final RegistryObject<Item> WET_WOLF_LEATHER = ITEMS.register("wet_wolf_leather",
    //        () -> new Item(new Item.Properties()));
    public static final RegistryObject<CappyHat> CAPPY_HAT = ITEMS.register("cappy",
            () -> new CappyHat(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
