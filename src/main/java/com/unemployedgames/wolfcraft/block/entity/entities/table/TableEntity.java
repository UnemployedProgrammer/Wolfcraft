package com.unemployedgames.wolfcraft.block.entity.entities.table;

import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.block.entity.root.RootOneStackInventoryBlockEntity;
import com.unemployedgames.wolfcraft.debug.DebugInfoEntryCollection;
import com.unemployedgames.wolfcraft.debug.DebugOverlayInfos;
import com.unemployedgames.wolfcraft.misc.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TableEntity extends RootOneStackInventoryBlockEntity implements TickableBlockEntity {
    public TableEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitys.TABLE.get(), pPos, pBlockState);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide())
            return;

        setChanged();

        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public Level getLvl() {
        return this.level;
    }
    public BlockPos getPos() {
        return this.worldPosition;
    }

    //@Override
    //    public DebugInfoEntryCollection getDebugInfoString(DebugInfoEntryCollection debugCollection) {
    //        DebugInfoEntryCollection col = super.getDebugInfoString(debugCollection);
    //        col.addBoolean("example_true_boolean", true);
    //        col.addBoolean("example_false_boolean", false);
    //        col.addNumber("example_number", 1306);
    //        col.addString("example_string", "Hallo ich bin Sebastian!");
    //        return col;
    //    }
}
