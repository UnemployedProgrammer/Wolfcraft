package com.unemployedgames.wolfcraft.recipe;

import com.google.gson.JsonObject;
import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class DryingRecipeType implements Recipe<SimpleContainer> {

    private final Ingredient inputItem;
    private final ItemStack output;
    private final ResourceLocation id;
    private final String rid;

    public DryingRecipeType(Ingredient inputItem, ItemStack output, String rid, ResourceLocation id) {
        this.inputItem = inputItem;
        this.output = output;
        this.id = id;
        this.rid = rid;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> in = NonNullList.create();
        in.add(inputItem);
        return in;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    public String getRID() {
        return rid;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static class Type implements RecipeType<DryingRecipeType> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "drying";
    }

    public static class Serializer implements RecipeSerializer<DryingRecipeType> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Wolfcraft.MODID, "drying");

        @Override
        public DryingRecipeType fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            String id = GsonHelper.getAsString(pSerializedRecipe, "id");
            JsonObject ingredients = GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient");
            Ingredient inputItem = Ingredient.fromJson(ingredients);
            return new DryingRecipeType(inputItem, output, id, pRecipeId);
        }

        @Override
        public @Nullable DryingRecipeType fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient in = Ingredient.fromNetwork(pBuffer);
            ItemStack output = pBuffer.readItem();
            String id = pBuffer.readComponent().getString();
            return new DryingRecipeType(in, output, id, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DryingRecipeType pRecipe) {
            pRecipe.getInputItem().toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeComponent(Component.literal(pRecipe.getRID()));
        }
    }
}
