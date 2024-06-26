package com.unemployedgames.wolfcraft;

import com.mojang.logging.LogUtils;
import com.unemployedgames.wolfcraft.block.ModBlocks;
import com.unemployedgames.wolfcraft.block.ModMenuTypes;
import com.unemployedgames.wolfcraft.block.custom.NeedlingStationMenu;
import com.unemployedgames.wolfcraft.block.custom.NeedlingStationScreen;
import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.item.AllBlocksAndItems;
import com.unemployedgames.wolfcraft.item.ModCreativeTabs;
import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.item.guidebook.AllGuideEntrys;
import com.unemployedgames.wolfcraft.misc.ModSounds;
import com.unemployedgames.wolfcraft.recipe.ModRecipes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagRegistry;
import net.minecraft.world.flag.FeatureFlagUniverse;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Wolfcraft.MODID)
public class Wolfcraft
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "wolfcraft";
    public static final String MINECRAFT_VERSION = "1.20.1";
    public static final String MOD_VERSION = "1.0.0";
    public static final String GeckoLibNeededVersionURLModrinth = "https://modrinth.com/mod/geckolib/version/pyB0jIsx";
    public static final String DataAPIUrl = "https://2504a1a0-e9ff-4df6-9fcf-e775306018d2-00-1qwrruca359zg.janeway.replit.dev/"; // ONLY TEST

    public static final boolean GLWarnMSG = false;
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public Wolfcraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        BlockEntitys.BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModSounds.register(modEventBus);
        ModRecipes.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerDatapack);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.NEEDLING_STATION_MENU.get(), NeedlingStationScreen::new);

            AllGuideEntrys.addAllEntries();
        }
    }

    private void justAUselessTest() {
        //Not executed and only for Figuring Out!!!
        //Reempty after brainstorming


    }

    public void registerDatapack(final AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            Path path = ModList.get().getModFileById("wolfcraft").getFile().findResource("resourcepacks/wolfcrafts_beginners_guide");
            Pack builtinDataPack = Pack.readMetaAndCreate(
                    "wolfcraft:beginners_guide",
                    Component.translatable("wolfcraft.pack_beginner"),
                    false,
                    (a) -> new PathPackResources(a, path, false),
                    PackType.SERVER_DATA,
                    Pack.Position.TOP,
                    PackSource.create((arg) -> Component.translatable("pack.nameAndSource", arg, Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY), false)
            );

            event.addRepositorySource((packConsumer) -> packConsumer.accept(builtinDataPack));
        }

    }

}
