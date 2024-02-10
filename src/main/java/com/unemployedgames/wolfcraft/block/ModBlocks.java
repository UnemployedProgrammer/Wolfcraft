package com.unemployedgames.wolfcraft.block;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.block.entity.entities.drying_rack.DryingRackBlock;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableBlock;
import com.unemployedgames.wolfcraft.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Wolfcraft.MODID);

    //public static final RegistryObject<Block> HOWL_BLOCK = registerBlock("block_of_howl",
    //() -> new Block(BlockBehaviour.Properties.of()
    //.destroyTime(2f)
    //.instrument(NoteBlockInstrument.SNARE)
    //.strength(2f, 3f)
    //.pushReaction(PushReaction.PUSH_ONLY)
    //.sound(SoundType.STONE)
    //.mapColor(DyeColor.BLUE)
    //.requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> TABLE = registerBlock("table",
            () -> new TableBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(0.5f, 1f)
                    .pushReaction(PushReaction.PUSH_ONLY)
                    .sound(SoundType.WOOD)
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> DRYING_RACK = registerBlock("drying_rack",
            () -> new DryingRackBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(0.5f, 1f)
                    .pushReaction(PushReaction.PUSH_ONLY)
                    .sound(SoundType.WOOD)
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .noOcclusion()
            ));



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}