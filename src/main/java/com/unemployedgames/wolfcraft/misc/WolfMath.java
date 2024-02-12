package com.unemployedgames.wolfcraft.misc;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.StringUtils;

public class WolfMath {
    public static final int TICKS_PER_SECOND = 20;
    public static final int SECONDS_PER_MINUTE = 60;

    public static String ticksToFormattedSecondsAndMinutes(int ticks) {
        if (ticks < 0) {
            throw new IllegalArgumentException("Ticks cannot be negative.");
        }

        // Calculate minutes, ensuring non-negative result
        int minutes = Math.max(0, ticks / (TICKS_PER_SECOND * SECONDS_PER_MINUTE));
        int remainingTicks = ticks % (TICKS_PER_SECOND * SECONDS_PER_MINUTE);

        // Calculate seconds within 60-second limit
        int seconds = Math.min(59, remainingTicks / TICKS_PER_SECOND);

        return String.format("%02d:%02d", minutes, seconds);
    }

    public static boolean isBetween(int number, int min, int max) {
        // Ensure min is less than or equal to max
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        // Handle negative numbers correctly
        if (number < 0 && min < 0 && max >= 0) {
            return number >= min && number <= -1; // Number must be less than or equal to -1
        } else if (number >= 0 && min < 0 && max < 0) {
            return number >= 0 && number <= max; // Number must be between 0 and max
        } else {
            // Standard check for positive or mixed ranges
            return number >= min && number <= max;
        }
    }

    public static Component cutComponent(Component componentToCut, int maxLength) {
        return Component.literal(StringUtils.abbreviate(componentToCut.getString(), "...", maxLength));
    }

}
