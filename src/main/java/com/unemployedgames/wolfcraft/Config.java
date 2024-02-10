package com.unemployedgames.wolfcraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Wolfcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue DEBUG_ENABLED = BUILDER
            .comment("DevMode: Testing Purposes // Please only on Client. (Could Crash)")
            .define("isDevMode", false);

    //private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
    //        .comment("A magic number")
    //        .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    //public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
    //        .comment("What you want the introduction message to be for the magic number")
    //        .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    //private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
    //        .comment("A list of items to log on common setup.")
    //        .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean isDebug;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        isDebug = DEBUG_ENABLED.get();
    }
}
