package com.unemployedgames.wolfcraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.flag.FeatureFlagRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;

public class NeedlingRecipeType implements Recipe<SimpleContainer> {

    private final Ingredient inputItem;
    private final ItemStack output;
    private final int inputItemCount;
    private final String recipeLine1;
    private final String recipeLine2;
    private final String recipeLine3;
    private final ResourceLocation id;

    public NeedlingRecipeType(Ingredient inputItem, ItemStack output, int inputItemCount, ResourceLocation id, String recipeLine1, String recipeLine2, String recipeLine3) {
        this.inputItem = inputItem;
        this.output = output;
        this.inputItemCount = inputItemCount;
        this.id = id;
        this.recipeLine1 = recipeLine1;
        this.recipeLine2 = recipeLine2;
        this.recipeLine3 = recipeLine3;
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

    public int getInputItemCount() {
        return inputItemCount;
    }

    public String getRecipeLine3() {
        return recipeLine3;
    }

    public String getRecipeLine2() {
        return recipeLine2;
    }

    public String getRecipeLine1() {
        return recipeLine1;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static class Type implements RecipeType<NeedlingRecipeType> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "needling";
    }

    public static class Serializer implements RecipeSerializer<NeedlingRecipeType> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Wolfcraft.MODID, "needling");

        @Override
        public NeedlingRecipeType fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            int inputCount = GsonHelper.getAsInt(pSerializedRecipe, "inputCount");
            String l1 = GsonHelper.getAsString(pSerializedRecipe, "recipeLine1");
            String l2 = GsonHelper.getAsString(pSerializedRecipe, "recipeLine2");
            String l3 = GsonHelper.getAsString(pSerializedRecipe, "recipeLine3");
            JsonObject ingredients = GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient");
            Ingredient inputItem = Ingredient.fromJson(ingredients);
            return new NeedlingRecipeType(inputItem, output, inputCount, pRecipeId, l1, l2, l3);
        }

        @Override
        public @Nullable NeedlingRecipeType fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient in = Ingredient.fromNetwork(pBuffer);
            ItemStack output = pBuffer.readItem();
            int inCount = pBuffer.readInt();
            String l1 = pBuffer.readComponent().getString();
            String l2 = pBuffer.readComponent().getString();
            String l3 = pBuffer.readComponent().getString();
            return new NeedlingRecipeType(in, output, inCount, pRecipeId, l1, l2, l3);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, NeedlingRecipeType pRecipe) {
            pRecipe.getInputItem().toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeInt(pRecipe.getInputItemCount());
            pBuffer.writeComponent(Component.literal(pRecipe.getRecipeLine1()));
            pBuffer.writeComponent(Component.literal(pRecipe.getRecipeLine2()));
            pBuffer.writeComponent(Component.literal(pRecipe.getRecipeLine3()));
        }
    }
}
