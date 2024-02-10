package com.unemployedgames.wolfcraft.debug;

import com.unemployedgames.wolfcraft.block.entity.BlockEntitys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderGuiEvent;

public class DebugOverlayInfos {
    //RenderGuiEvent.Pre event;
    static Minecraft minecraft;

    public static BlockPos lastHovered = null;
    public DebugOverlayInfos() {
        //this.event = event;
        minecraft = Minecraft.getInstance();
    }

    public  BlockState getHoveringBlock() {
        HitResult objectMouseOver = minecraft.hitResult;
        if (!(objectMouseOver instanceof BlockHitResult)) {
            lastHovered = null;
            return null;
        } else {
            BlockHitResult result = (BlockHitResult) objectMouseOver;
            ClientLevel world = minecraft.level;
            BlockPos pos = result.getBlockPos();
            try {
                return world.getBlockState(pos);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public BlockEntity getHoveringBlockEntity() {
        HitResult objectMouseOver = minecraft.hitResult;
        if (!(objectMouseOver instanceof BlockHitResult)) {
            lastHovered = null;
            return null;
        } else {
            BlockHitResult result = (BlockHitResult) objectMouseOver;
            ClientLevel world = minecraft.level;
            BlockPos pos = result.getBlockPos();
            try {
                return world.getBlockEntity(pos);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
