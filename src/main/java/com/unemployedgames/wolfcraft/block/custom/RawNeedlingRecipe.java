package com.unemployedgames.wolfcraft.block.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class RawNeedlingRecipe {
    private Item input;
    private int inputCount;
    private Item output;
    private int outputCount;

    private String recipeLine1;
    private String recipeLine2;
    private String recipeLine3;

    public RawNeedlingRecipe(Item input, int inputCount, Item output, int outputCount, String recipeLine1, String recipeLine2, String recipeLine3) {
        this.input = input;
        this.inputCount = inputCount;
        this.output = output;
        this.outputCount = outputCount;
        this.recipeLine1 = recipeLine1;
        this.recipeLine2 = recipeLine2;
        this.recipeLine3 = recipeLine3;
    }

    public boolean isInput(Item item) {
        return item == input;
    }

    public boolean isOutput(Item item) {
        return item == output;
    }

    public Item getInput() {
        return input;
    }

    public int getInputCount() {
        return inputCount;
    }

    public Item getOutput() {
        return output;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public String getRecipeLine1() {
        return recipeLine1;
    }

    public String getRecipeLine2() {
        return recipeLine2;
    }

    public String getRecipeLine3() {
        return recipeLine3;
    }
}
