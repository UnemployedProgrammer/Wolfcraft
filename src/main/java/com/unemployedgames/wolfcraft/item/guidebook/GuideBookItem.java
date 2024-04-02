package com.unemployedgames.wolfcraft.item.guidebook;

import com.unemployedgames.wolfcraft.item.ModItems;
import com.unemployedgames.wolfcraft.misc.ClientHooks;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class GuideBookItem extends Item {
    public GuideBookItem() {
        super(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1));
    }

    //@Override
    //    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    //        pPlayer.setDeltaMovement(0, 0.5, 0);
    //        pLevel.addParticle(ParticleTypes.EXPLOSION, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 0.1, 0.1, 0.1);
    //        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    //    }


    //@Override
    //    public InteractionResult useOn(UseOnContext pContext) {
    //        Level pLevel = pContext.getLevel();
    //        Player pPlayer = pContext.getPlayer();
    //
    //        pPlayer.setDeltaMovement(0, 1, 0);
    //        pPlayer.playSound(SoundEvents.FIRECHARGE_USE);
    //        pLevel.addParticle(ParticleTypes.EXPLOSION, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 0.1, 0.1, 0.1);
    //        pPlayer.getCooldowns().addCooldown(ModItems.GUIDE_BOOK.get(), 15);
    //
    //        return InteractionResult.PASS;
    //    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.playSound(SoundEvents.BOOK_PAGE_TURN);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openGuideBook());
        pPlayer.getCooldowns().addCooldown(ModItems.GUIDE_BOOK.get(), 100);
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
