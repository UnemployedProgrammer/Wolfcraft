package com.unemployedgames.wolfcraft.block.entity.entities.drying_rack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.unemployedgames.wolfcraft.block.entity.entities.table.TableEntity;
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
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackEntity> {

    private final BlockEntityRendererProvider.Context context;

    public DryingRackRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(DryingRackEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if(pBlockEntity.getLvl().getBlockState(pBlockEntity.getBlockPos()).isAir())
            return;
        if(!pBlockEntity.getLvl().getBlockState(pBlockEntity.getBlockPos()).hasProperty(DirectionalBlock.FACING))
            return;
        ItemStack stack = pBlockEntity.getItem();

        Level level = pBlockEntity.getLevel();
        if(level == null)
            return;

        BlockPos pos = pBlockEntity.getBlockPos().above();

        int packedLight = LightTexture.pack(
                level.getBrightness(LightLayer.BLOCK, pos),
                level.getBrightness(LightLayer.SKY, pos)
        );

        pPoseStack.pushPose();
        float XTranslate, YTranslate, ZTranslate;
        DryingRackRendererCoords.Translate translate = new DryingRackRendererCoords.Translate();
        if(pBlockEntity.getLvl().getBlockState(pBlockEntity.getBlockPos()).isAir())
            return;
        DryingRackRendererCoordImpl direction = translate.getDirection(pBlockEntity.getLvl().getBlockState(pBlockEntity.getBlockPos()).getValue(DirectionalBlock.FACING));

        XTranslate = direction.getX();
        YTranslate = DryingRackRendererCoords.Translate.Y;
        ZTranslate = direction.getZ();
        pPoseStack.translate(XTranslate, YTranslate , ZTranslate);
        pPoseStack.scale((float) 0.7, (float) 0.7, (float) 0.7);
        if(pBlockEntity.getLvl().getBlockState(pBlockEntity.getBlockPos()).isAir())
            return;
        if(DryingRackRendererCoords.MulPos.needsAMul(pBlockEntity.getLvl().getBlockState(pBlockEntity.getBlockPos()).getValue(DirectionalBlock.FACING))) {
            pPoseStack.mulPose(DryingRackRendererCoords.MulPos.aroundAxis.rotationDegrees((float) DryingRackRendererCoords.MulPos.manipulateAbout));
        }
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
