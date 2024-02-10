package com.unemployedgames.wolfcraft.block.entity.entities.drying_rack;

import com.mojang.datafixers.util.Pair;
import com.unemployedgames.wolfcraft.Config;
import com.unemployedgames.wolfcraft.block.ModBlocks;
import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableEntity;
import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.misc.TickableBlockEntity;
import com.unemployedgames.wolfcraft.misc.WolfMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DryingRackBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public DryingRackBlock(Properties pProperties) {
        super(pProperties);
    }

    private VoxelShape FROM_BB = Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    private VoxelShape NORTH = Block.box(0.0D, 14.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    private VoxelShape EAST = Block.box(0.0D, 14.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    private VoxelShape SOUTH = FROM_BB;
    private VoxelShape WEST = Block.box(13.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape shape = FROM_BB;
        switch (pState.getValue(FACING)) {
            case NORTH -> shape = NORTH;
            case EAST -> shape = EAST;
            case SOUTH -> shape = SOUTH;
            case WEST -> shape = WEST;
            default -> shape = FROM_BB;
        }
        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntitys.DRYING_RACK.get().create(pPos, pState);

    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand == InteractionHand.MAIN_HAND) {
            if(pLevel.getBlockEntity(pPos) instanceof DryingRackEntity dryingRackEntity) {
                if(dryingRackEntity.getItem().isEmpty()) {
                    if(ItemsThatCanDry.canItemDry(pPlayer.getItemInHand(pHand).getItem())) {
                        dryingRackEntity.setItem(new ItemStack(pPlayer.getItemInHand(pHand).getItem()));
                        pPlayer.getItemInHand(pHand).shrink(1);
                        dryingRackEntity.setRecipe(ItemsThatCanDry.getKeyFromItem(dryingRackEntity.getItem().getItem()));
                        dryingRackEntity.newDryTicks();
                    }
                } else {
                    pPlayer.getInventory().add(dryingRackEntity.getItem());
                    dryingRackEntity.setItem(ItemStack.EMPTY);
                    dryingRackEntity.setRecipe("");
                    dryingRackEntity.setTicksUntilDry(0);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTickerHelper(pLevel);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
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
        pBuilder.add(FACING);
        //pBuilder.add(TRIGGERED);
    }
}
