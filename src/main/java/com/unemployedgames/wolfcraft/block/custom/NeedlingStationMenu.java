package com.unemployedgames.wolfcraft.block.custom;

import com.unemployedgames.wolfcraft.block.ModBlocks;
import com.unemployedgames.wolfcraft.block.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NeedlingStationMenu extends AbstractContainerMenu {

    private final Level level;
    private final Player player;
    private final ContainerData data;

    private IItemHandler internal;

    final Slot inputSlot;
    /** The inventory slot that stores the output of the crafting recipe. */
    final Slot resultSlot;
    int resultSlotXOffset = 54;
    Runnable slotUpdateListener = () -> {
    };
    public final Container container = new SimpleContainer(1) {
        /**
         * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            NeedlingStationMenu.this.slotsChanged(this);
            NeedlingStationMenu.this.slotUpdateListener.run();
        }
    };
    /** The inventory that stores the output of the crafting recipe. */
    final ResultContainer resultContainer = new ResultContainer();

    private final ContainerLevelAccess access;

    public NeedlingStationMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, new SimpleContainerData(2), null);
    }

    private final Map<Integer, Slot> customSlots = new HashMap<>();

    public NeedlingStationMenu(int pContainerId, Inventory inv, ContainerData data, ContainerLevelAccess access) {
        super(ModMenuTypes.NEEDLING_STATION_MENU.get(), pContainerId);
        checkContainerSize(inv, 2);
        this.level = inv.player.level();
        this.player = inv.player;
        this.data = data;
        this.access = access;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);

        int xSlots = 143;
        int ySlots = 31;

        this.inputSlot = this.addSlot(new Slot(this.container, 0, xSlots + 6, ySlots));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, xSlots + 6 +  resultSlotXOffset, ySlots){
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }
        });

        this.internal = new ItemStackHandler(2);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }


    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (this.access != null) {
            return stillValid(this.access,
                    pPlayer, ModBlocks.NEEDLING_STATION.get());
        } else {
            return stillValid(ContainerLevelAccess.NULL,
                    pPlayer, ModBlocks.NEEDLING_STATION.get());
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        int xOffset = 115;
        int yOffset = 30;
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, xOffset + 8 + l * 18,  yOffset + 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        int xOffset = 115;
        int yOffset = 30;
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, xOffset + 8 + i * 18, 142 + yOffset));
        }
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (playerIn instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
                //for (int j = 0; j < internal.getSlots(); ++j) {
                //    playerIn.drop(internal.extractItem(j, internal.getStackInSlot(j).getCount(), false), false);
                //}
                if(!container.isEmpty() && !container.getItem(0).isEmpty()) {
                    playerIn.drop(container.getItem(0), false);
                    container.getItem(0).setCount(0);
                }
                if(!resultContainer.isEmpty() && !resultContainer.getItem(1).isEmpty()) {
                    playerIn.drop(resultContainer.getItem(1), false);
                    resultContainer.getItem(1).setCount(0);
                }

            } else {
                //for (int i = 0; i < internal.getSlots(); ++i) {
                //    playerIn.getInventory().placeItemBackInInventory(internal.extractItem(i, internal.getStackInSlot(i).getCount(), false));
                //}
                if(!container.isEmpty() && !container.getItem(0).isEmpty()) {
                    playerIn.getInventory().placeItemBackInInventory(container.getItem(0), false);
                    container.getItem(0).setCount(0);
                }
                if(!resultContainer.isEmpty() && !resultContainer.getItem(1).isEmpty()) {
                    playerIn.getInventory().placeItemBackInInventory(resultContainer.getItem(1), false);
                    resultContainer.getItem(1).setCount(0);
                }
            }
        }
    }

}
