package com.unemployedgames.wolfcraft.recipe;

import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Wolfcraft.MODID);

    public static final RegistryObject<RecipeSerializer<NeedlingRecipeType>> NEEDLING_SERIALIZER =
            SERIALIZERS.register("needling", () -> NeedlingRecipeType.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
