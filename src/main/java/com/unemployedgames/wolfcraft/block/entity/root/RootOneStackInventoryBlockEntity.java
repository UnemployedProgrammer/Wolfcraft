package com.unemployedgames.wolfcraft.block.entity.root;

import com.unemployedgames.wolfcraft.debug.DebugInfoEntryCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RootOneStackInventoryBlockEntity extends BlockEntity implements IHaveDebugInfo {
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            RootOneStackInventoryBlockEntity.this.setChanged();
        }
    };

    public final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);

    public RootOneStackInventoryBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", this.inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.inventory.deserializeNBT(pTag.getCompound("inventory"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.optional.cast();
        }

        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getItem() {
        return this.inventory.getStackInSlot(0);
    }
    public void setItem(ItemStack itemStack) {
        this.inventory.setStackInSlot(0, itemStack);
        setChanged();
    }

    public LazyOptional<ItemStackHandler> getOptional() {
        return optional;
    }


    @Override
    public DebugInfoEntryCollection getDebugInfoString(DebugInfoEntryCollection debugCollection) {
        debugCollection.addCompoundTag("inventory", this.inventory.serializeNBT());
        return debugCollection;
    }
}
