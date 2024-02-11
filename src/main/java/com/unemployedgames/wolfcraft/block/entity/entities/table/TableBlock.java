package com.unemployedgames.wolfcraft.block.entity.entities.table;

import com.mojang.datafixers.util.Pair;
import com.unemployedgames.wolfcraft.Config;
import com.unemployedgames.wolfcraft.block.ModBlocks;
import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.misc.TickableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.PlaySoundCommand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.EnchantedGoldenAppleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TableBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty HASPLATE = BooleanProperty.create("plate");

    public TableBlock(Properties pProperties) {
        super(pProperties);
    }

    private VoxelShape TOP = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private VoxelShape LF = Block.box(13.0D, 0.0D, 1.0D, 15.0D, 15.0D, 3.0D);
    private VoxelShape LB = Block.box(1.0D, 0.0D, 1.0D, 3.0D, 15.0D, 3.0D);
    private VoxelShape RF = Block.box(13.0D, 0.0D, 13.0D, 15.0D, 15.0D, 15.0D);
    private VoxelShape RB = Block.box(1.0D, 0.0D, 13.0D, 3.0D, 15.0D, 15.0D);


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(TOP,LF,LB,RB,RF);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntitys.TABLE.get().create(pPos, pState);

    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pState.getValue(HASPLATE).booleanValue()) {
            if (pLevel.getBlockEntity(pPos) instanceof TableEntity table) {
                if(!pPlayer.isShiftKeyDown()) {
                    if (alreadyHasFood(pLevel, pPos)) {
                        consumeFood(table.getItem(), pPlayer, pLevel);
                        table.setItem(ItemStack.EMPTY);
                    } else {
                        if(pPlayer.getItemInHand(pHand).isEdible()) {
                            table.setItem(new ItemStack(pPlayer.getItemInHand(pHand).getItem()));
                            pPlayer.getItemInHand(pHand).shrink(1);
                        }
                    }
                } else {
                    updateBooleanBlockStateProperty(pState, pPos, pLevel, HASPLATE, false);
                    pPlayer.getInventory().add(new ItemStack(ModItems.PLATE.get()));
                    pPlayer.getInventory().add(table.getItem());
                    table.setItem(ItemStack.EMPTY);
                }
            }
        } else {
            if(pPlayer.getItemInHand(pHand).is(ModItems.PLATE.get())) {
                pPlayer.getItemInHand(pHand).shrink(1);
                updateBooleanBlockStateProperty(pState, pPos, pLevel, HASPLATE, true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static void updateBooleanBlockStateProperty(BlockState blockState, BlockPos blockPos, Level world, BooleanProperty property, boolean val) {
        BlockState newblockstate = blockState.setValue(property, val);
        world.setBlockAndUpdate(blockPos, newblockstate);
    }

    private boolean consumeFood(ItemStack food, Player plr, Level lvl) {
        if(food.getItem().isEdible()) {
            //plr.getFoodData().eat(food.getItem(), food, plr);
            //List<Pair<MobEffectInstance, Float>> foodEffects = food.getFoodProperties(plr).getEffects();
            //lvl.playSound(plr, plr.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.BLOCKS);
            plr.eat(lvl, food);
            try {
                food.getItem().finishUsingItem(food, lvl, plr);
            } catch (Exception e) {
                ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
                if(Config.isDebug) {
                    SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.literal("DEVMODE: Error while executeing: 'finishUsingItem(Arg arg)'"), (Component)null);
                    plr.sendSystemMessage(Component.literal("Expection: "));
                    plr.sendSystemMessage(Component.literal(e.getMessage()));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.is(pNewState.getBlock()) || pLevel.isClientSide()) {
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
            System.out.println("Same");
            return;
        }

        System.out.println("Diffrent");

        if(pState.hasProperty(HASPLATE)) {
            System.out.println("Has A Plate Value");
            if (pState.getValue(HASPLATE).booleanValue()) {
                System.out.println("HHas A Plate");
                pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), new ItemStack(ModItems.PLATE.get())));
            }
        }

        if(pLevel.getBlockEntity(pPos) instanceof TableEntity table) {
            if(!table.getItem().isEmpty()) {
                System.out.println("Has a Item");
                pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), table.getItem()));
                table.setItem(ItemStack.EMPTY);
            }
        }
        try {
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        } catch (Exception e) {
            //for (Player player : pLevel.players()) {
            //    player.sendSystemMessage(Component.literal("fail remove be"));
            //}
            System.out.println("err remove be table");
        }
    }



    public static boolean alreadyHasFood(Level lvl, BlockPos bP) {
        if (lvl.getBlockEntity(bP) instanceof TableEntity table) {
            return !table.getItem().isEmpty();
        }
        return false;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTickerHelper(pLevel);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(HASPLATE, false);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        //pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.valueOf(false)), 4);
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, HASPLATE);
        //pBuilder.add(TRIGGERED);
    }
}
