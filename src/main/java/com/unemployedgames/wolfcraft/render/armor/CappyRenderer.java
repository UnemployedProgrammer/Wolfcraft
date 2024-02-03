package com.unemployedgames.wolfcraft.render.armor;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.armor.CappyHat;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CappyRenderer extends GeoArmorRenderer<CappyHat> {

    public CappyRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(Wolfcraft.MODID, "armor/cappy"))); // Using DefaultedItemGeoModel like this puts our 'location' as item/armor/example armor in the assets folders.
    }
}
