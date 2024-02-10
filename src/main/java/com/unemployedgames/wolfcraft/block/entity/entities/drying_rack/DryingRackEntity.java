package com.unemployedgames.wolfcraft.block.entity.entities.drying_rack;

import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.block.entity.root.RootOneStackInventoryBlockEntity;
import com.unemployedgames.wolfcraft.debug.DebugInfoEntryCollection;
import com.unemployedgames.wolfcraft.misc.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DryingRackEntity extends RootOneStackInventoryBlockEntity implements TickableBlockEntity {
    private int ticksUntilDry = 0;
    public DryingRackEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitys.TABLE.get(), pPos, pBlockState);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("ticks_until_dry", ticksUntilDry);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ticksUntilDry = pTag.getInt("ticks_until_dry");
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

    public void newDryTicks() {
        Random random = new Random();
        ticksUntilDry = random.nextInt(601) + 2000;
    }

    public Level getLvl() {
        return this.level;
    }
    public BlockPos getPos() {
        return this.worldPosition;
    }

    @Override
    public DebugInfoEntryCollection getDebugInfoString(DebugInfoEntryCollection debugCollection) {
        DebugInfoEntryCollection col = super.getDebugInfoString(debugCollection);
        return col;
    }
}
