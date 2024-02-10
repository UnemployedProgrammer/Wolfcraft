package com.unemployedgames.wolfcraft.block.entity;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.block.ModBlocks;
import com.unemployedgames.wolfcraft.block.entity.entities.drying_rack.DryingRackEntity;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntitys {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Wolfcraft.MODID);

    public static final RegistryObject<BlockEntityType<TableEntity>> TABLE = BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register("table",
            () -> BlockEntityType.Builder.of(TableEntity::new, ModBlocks.TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<DryingRackEntity>> DRYING_RACK = BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register("drying_rack",
            () -> BlockEntityType.Builder.of(DryingRackEntity::new, ModBlocks.DRYING_RACK.get()).build(null));
}
