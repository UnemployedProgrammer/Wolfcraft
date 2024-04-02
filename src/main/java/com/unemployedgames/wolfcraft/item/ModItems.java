package com.unemployedgames.wolfcraft.item;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.armor.CappyHat;
import com.unemployedgames.wolfcraft.item.armor.WetWolfArmor;
import com.unemployedgames.wolfcraft.item.custom.GetAllItems;
import com.unemployedgames.wolfcraft.item.custom.informationglasses.InformationGlasses;
import com.unemployedgames.wolfcraft.item.guidebook.GuideBookItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
    public static final RegistryObject<InformationGlasses> INFORMATION_GLASSES = ITEMS.register("glasses",
            () -> new InformationGlasses(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> PLATE = ITEMS.register("plate",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOLF_LEATHER_WET = ITEMS.register("wet_wolf_leather",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOLF_LEATHER_DRY = ITEMS.register("dry_wolf_leather",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GETALL = ITEMS.register("all",
            () -> new GetAllItems(new Item.Properties()));

    public static final RegistryObject<Item> NEEDLE = ITEMS.register("needle",
            () -> new GetAllItems(new Item.Properties()));

    public static final RegistryObject<WetWolfArmor> WET_WOLF_ARMOR_HELMET = ITEMS.register("wet_wolf_armor_helmet",
            () -> new WetWolfArmor(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<WetWolfArmor> WET_WOLF_ARMOR_CHESTPLATE = ITEMS.register("wet_wolf_armor_chestplate",
            () -> new WetWolfArmor(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<WetWolfArmor> WET_WOLF_ARMOR_LEGGINS = ITEMS.register("wet_wolf_armor_leggins",
            () -> new WetWolfArmor(ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<WetWolfArmor> WET_WOLF_ARMOR_BOOTS = ITEMS.register("wet_wolf_armor_boots",
            () -> new WetWolfArmor(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<GuideBookItem> GUIDE_BOOK = ITEMS.register("guide",
            () -> new GuideBookItem()); // got_wolf_leather_wet


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
