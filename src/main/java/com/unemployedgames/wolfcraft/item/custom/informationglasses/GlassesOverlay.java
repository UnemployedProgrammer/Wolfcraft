package com.unemployedgames.wolfcraft.item.custom.informationglasses;

import com.unemployedgames.wolfcraft.Config;
import com.unemployedgames.wolfcraft.debug.DebugOverlay;
import com.unemployedgames.wolfcraft.debug.DebugOverlayInfos;
import com.unemployedgames.wolfcraft.item.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.Set;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class GlassesOverlay {
    private static final DebugOverlayInfos infos = new DebugOverlayInfos();
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        if (!(Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SPECTATOR) || !Minecraft.getInstance().options.hideGui) {
            BlockEntity be = getHoveringBlockEntity();
            if(be != null && shouldDisplay()) {
                renderOverlay(event.getGuiGraphics(), be);
            }
        }

        if (Config.isDebug && (!(Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SPECTATOR) || !Minecraft.getInstance().options.hideGui)) {
            DebugOverlay.render(event, infos);
        }
    }

    public static BlockEntity getHoveringBlockEntity() {
        HitResult objectMouseOver = Minecraft.getInstance().hitResult;
        if (!(objectMouseOver instanceof BlockHitResult)) {
            return null;
        } else {
            BlockHitResult result = (BlockHitResult) objectMouseOver;
            ClientLevel world = Minecraft.getInstance().level;
            BlockPos pos = result.getBlockPos();
            try {
                return world.getBlockEntity(pos);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static boolean shouldDisplay() {
        Set<Item> wornArmor = new ObjectOpenHashSet<>();
        for (ItemStack stack : Minecraft.getInstance().player.getArmorSlots()) {
            if (!stack.isEmpty())
                wornArmor.add(stack.getItem());
        }
        return  wornArmor.containsAll(ObjectArrayList.of(ModItems.INFORMATION_GLASSES.get()));
    }

    // RENDER [UP THERE: TECH STUFF]

    public static void renderOverlay(GuiGraphics guiGraphics, BlockEntity hoveringBlockEntity) {
        boolean isGlassesAble = hoveringBlockEntity instanceof IHaveGlassesInformation;

        if (isGlassesAble) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, ((IHaveGlassesInformation) hoveringBlockEntity).getGlassesInformations(new GlassesInformationCollection()).getGooglesTooltipLines(), Optional.empty(), Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2);
        }
    }
}
