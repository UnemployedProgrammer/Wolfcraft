package com.unemployedgames.wolfcraft.render.armor;

import com.unemployedgames.wolfcraft.Wolfcraft;
import com.unemployedgames.wolfcraft.item.armor.CappyHat;
import com.unemployedgames.wolfcraft.item.custom.informationglasses.InformationGlasses;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GlassesRenderer extends GeoArmorRenderer<InformationGlasses> {

    public GlassesRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(Wolfcraft.MODID, "armor/glasses"))); // Using DefaultedItemGeoModel like this puts our 'location' as item/armor/example armor in the assets folders.
    }
}
