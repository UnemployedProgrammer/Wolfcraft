package com.unemployedgames.wolfcraft.block.entity.entities.table;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TableEntityRenderer implements BlockEntityRenderer<TableEntity> {

    private final BlockEntityRendererProvider.Context context;

    public TableEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(TableEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if(pBlockEntity.getLvl().getBlockState(pBlockEntity.getBlockPos()).isAir())
            return;
        ItemStack stack = pBlockEntity.getItem();
        if(stack.isEmpty() || !pBlockEntity.getLvl().getBlockState(pBlockEntity.getPos()).getValue(BooleanProperty.create("plate")).booleanValue())
            return;

        Level level = pBlockEntity.getLevel();
        if(level == null)
            return;

        BlockPos pos = pBlockEntity.getBlockPos().above();

        int packedLight = LightTexture.pack(
                level.getBrightness(LightLayer.BLOCK, pos),
                level.getBrightness(LightLayer.SKY, pos)
        );

        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 1.1 , 0.5);
        pPoseStack.scale((float) 0.5, (float) 0.5, (float) 0.5);
        pPoseStack.mulPose(Axis.XN.rotationDegrees((float) 90));
        this.context.getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                level,
                0
        );
        pPoseStack.popPose();
    }
}
