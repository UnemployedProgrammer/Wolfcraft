package com.unemployedgames.wolfcraft.item.custom.informationglasses;

import com.unemployedgames.wolfcraft.debug.DebugInfoEntryCollection;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class GlassesInformationCollection extends DebugInfoEntryCollection {
    public Component getGooglesToolTip() {
        String newLine = "\n";
        MutableComponent output = MutableComponent.create(ComponentContents.EMPTY);

        output.append(Component.translatable("glasses.wolfcraft.information"));
        for (String key : numberNBTList.keySet()) {
            output.append(Component.translatable("glasses.wolfcraft.property.number." + key.toLowerCase().replace(" ", "_")).append(Component.literal(": " + numberNBTList.get(key))));
        }
        for (String key : stringNBTList.keySet()) {
            output.append(Component.translatable("glasses.wolfcraft.property.string." + key.toLowerCase().replace(" ", "_")).append(Component.literal(": " + stringNBTList.get(key))));
        }
        for (String key : stringNBTList.keySet()) {
            output.append(Component.translatable("glasses.wolfcraft.property.boolean." + key.toLowerCase().replace(" ", "_")).append(Component.literal(": ")).append(Component.translatable(getYesOrNo(booleanNBTList.get(key)))));
        }
        return output;
    }

    public List<Component> getGooglesTooltipLines() {
        List<Component> output = new ArrayList<>();

        output.add(Component.translatable("glasses.wolfcraft.information"));
        for (String key : numberNBTList.keySet()) {
            output.add(Component.translatable("glasses.wolfcraft.property.number." + key.toLowerCase().replace(" ", "_")).append(Component.literal(": " + numberNBTList.get(key))));
        }

        for (String key : stringNBTList.keySet()) {
            output.add(Component.translatable("glasses.wolfcraft.property.string." + key.toLowerCase().replace(" ", "_")).append(Component.literal(": " + stringNBTList.get(key))));
        }

        for (String key : booleanNBTList.keySet()) {
            output.add(Component.translatable("glasses.wolfcraft.property.boolean." + key.toLowerCase().replace(" ", "_")).append(Component.literal(": ").append(Component.translatable(getYesOrNo(booleanNBTList.get(key))))));
        }

        return output;
    }

    private String getYesOrNo(boolean yesOrNo) {
        return yesOrNo ? "glasses.wolfcraft.boolvalue.yes" : "glasses.wolfcraft.boolvalue.no";
    }
}
