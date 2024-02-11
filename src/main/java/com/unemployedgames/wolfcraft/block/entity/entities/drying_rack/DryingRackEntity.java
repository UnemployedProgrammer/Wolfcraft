package com.unemployedgames.wolfcraft.block.entity.entities.drying_rack;

import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.block.entity.root.RootOneStackInventoryBlockEntity;
import com.unemployedgames.wolfcraft.debug.DebugInfoEntryCollection;
import com.unemployedgames.wolfcraft.misc.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class DryingRackEntity extends RootOneStackInventoryBlockEntity implements TickableBlockEntity {
    private int ticksUntilDry = 0;
    private String recipe = "";
    public DryingRackEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitys.DRYING_RACK.get(), pPos, pBlockState);
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
        pTag.putString("recipe", recipe);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ticksUntilDry = pTag.getInt("ticks_until_dry");
        recipe = pTag.getString("recipe");
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide())
            return;

        tickSub();
        setChanged();

        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void newDryTicks() {
        Random random = new Random();
        ticksUntilDry = random.nextInt(601) + 2000;
    }

    public void tickSub() {
        if(ticksUntilDry >= 1 && recipe != "") {
            ticksUntilDry = ticksUntilDry - 1;
            setChanged();
        } else {
            if(recipe != "") {
                setItem(new ItemStack(ItemsThatCanDry.getDryItem(recipe)));
                recipe = "";
                ticksUntilDry = 0;
                setChanged();
            }
        }
    }

    public String getRecipe() {
        return recipe;
    }

    public void setTicksUntilDry(int ticksUntilDry) {
        this.ticksUntilDry = ticksUntilDry;
        setChanged();
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
        setChanged();
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
        col.addNumber("ticks_until_dry", ticksUntilDry);
        col.addString("recipe", recipe);
        return col;
    }
}
