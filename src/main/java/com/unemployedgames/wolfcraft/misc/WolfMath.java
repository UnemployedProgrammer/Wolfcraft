package com.unemployedgames.wolfcraft.misc;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

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

    public static class NeedlingPoints {
        public HashMap<String, String> pointsExisting = new HashMap<String, String>();

        public HashMap<String, String> realIndex = new HashMap<String, String>();
        public HashMap<String, Integer> pointsX = new HashMap<String, Integer>();
        public HashMap<String, Integer> pointsY = new HashMap<String, Integer>();

        public boolean isPointThere(int index) {
            if(!pointsExisting.isEmpty()) {
                return pointsExisting.containsKey("p-" + index);
            }
            return false;
        }

        public int getFakeIndex(int realIndex) {
            if(this.realIndex.containsKey(realIndex)) {
                return Integer.valueOf(this.realIndex.get(realIndex).replace("p-", ""));
            } else {
                return 100;
            }
        }

        public int getPointX(int index) {
            if(!pointsX.isEmpty()) {
                return pointsX.get("p-"+index);
            }
            return -100;
        }

        public int getPointY(int index) {
            if(!pointsY.isEmpty()) {
                return pointsY.get("p-"+index);
            }
            return -100;
        }

        private NeedlingPoints(String line1, String line2, String line3) {
            //line1
            if(line1.charAt(0) != ' ') {
                pointsExisting.put("p-"+line1.charAt(0), "p-"+line1.charAt(0));
                realIndex.put("0", "p-"+line1.charAt(0));
                pointsX.put("p-"+line1.charAt(0), 10);
                pointsY.put("p-"+line1.charAt(0), 10);
            }
            if(line1.charAt(1) != ' ') {
                pointsExisting.put("p-"+line1.charAt(1), "p-"+line1.charAt(1));
                realIndex.put("1", "p-"+line1.charAt(1));
                pointsX.put("p-"+line1.charAt(1), 90);
                pointsY.put("p-"+line1.charAt(1), 10);
            }
            if(line1.charAt(2) != ' ') {
                pointsExisting.put("p-"+line1.charAt(2), "p-"+line1.charAt(2));
                realIndex.put("2", "p-"+line1.charAt(2));
                pointsX.put("p-"+line1.charAt(2), 170);
                pointsY.put("p-"+line1.charAt(2), 10);
            }

            //line2

            if(line2.charAt(0) != ' ') {
                pointsExisting.put("p-"+line2.charAt(0), "p-"+line1.charAt(0));
                realIndex.put("3", "p-"+line1.charAt(0));
                pointsX.put("p-"+line2.charAt(0), 10);
                pointsY.put("p-"+line2.charAt(0), 90);
            }
            if(line2.charAt(1) != ' ') {
                pointsExisting.put("p-"+line2.charAt(1), "p-"+line1.charAt(1));
                realIndex.put("4", "p-"+line1.charAt(1));
                pointsX.put("p-"+line2.charAt(1), 90);
                pointsY.put("p-"+line2.charAt(1), 90);
            }
            if(line2.charAt(2) != ' ') {
                pointsExisting.put("p-"+line2.charAt(2), "p-"+line1.charAt(2));
                realIndex.put("5", "p-"+line1.charAt(2));
                pointsX.put("p-"+line2.charAt(2), 170);
                pointsY.put("p-"+line2.charAt(2), 90);
            }


            //line3

            if(line3.charAt(0) != ' ') {
                pointsExisting.put("p-"+line3.charAt(0), "p-"+line1.charAt(0));
                realIndex.put("6", "p-"+line1.charAt(0));
                pointsX.put("p-"+line3.charAt(0), 10);
                pointsY.put("p-"+line3.charAt(0), 170);
            }
            if(line3.charAt(1) != ' ') {
                pointsExisting.put("p-"+line3.charAt(1), "p-"+line1.charAt(1));
                realIndex.put("7", "p-"+line1.charAt(1));
                pointsX.put("p-"+line3.charAt(1), 90);
                pointsY.put("p-"+line3.charAt(1), 170);
            }
            if(line3.charAt(2) != ' ') {
                pointsExisting.put("p-"+line3.charAt(2), "p-"+line1.charAt(2));
                realIndex.put("8", "p-"+line1.charAt(2));
                pointsX.put("p-"+line3.charAt(2), 170);
                pointsY.put("p-"+line3.charAt(2), 170);
            }
        }

        public static class Builder {
            String l1;
            String l2;
            String l3;

            public Builder firstRecipeLine(String topLeft, String topCenter, String topRight) {
                this.l1 = topLeft+topCenter+topRight;
                return this;
            }
            public Builder midRecipeLine(String centerLeft, String centerCenter, String centerRight) {
                this.l2 = centerLeft+centerCenter+centerRight;
                return this;
            }
            public Builder lastRecipeLine(String bottomLeft, String bottomCenter, String bottomRight) {
                this.l3 = bottomLeft+bottomCenter+bottomRight;
                return this;
            }
            public Builder firstRecipeLine(String topLeftCenterRight) {
                this.l1 = topLeftCenterRight;
                return this;
            }
            public Builder midRecipeLine(String centerLeftCenterRight) {
                this.l2 = centerLeftCenterRight;
                return this;
            }
            public Builder lastRecipeLine(String bottomLeftCenterRight) {
                this.l3 = bottomLeftCenterRight;
                return this;
            }
            public NeedlingPoints build() {
                return new NeedlingPoints(l1, l2, l2);
            }
        }
    }
}
