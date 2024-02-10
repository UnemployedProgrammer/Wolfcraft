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
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackEntity> {

    private final BlockEntityRendererProvider.Context context;

    public DryingRackRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(DryingRackEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
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
        pPoseStack.translate(0.5, 0.6 , 0.1);
        pPoseStack.scale((float) 0.7, (float) 0.7, (float) 0.7);
        //pPoseStack.mulPose(Axis.XN.rotationDegrees((float) 0));
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
