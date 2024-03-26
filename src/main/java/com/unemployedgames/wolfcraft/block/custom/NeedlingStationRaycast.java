package com.unemployedgames.wolfcraft.block.custom;

import java.util.ArrayList;

public class NeedlingStationRaycast {

    public static ArrayList<Point> performRaycast(int startX, int startY, int endX, int endY) {
        ArrayList<Point> result = new ArrayList<>();
        //Linear zwichen Punkten interpoliren
        int dx = endX - startX;
        int dy = endY - startY;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        double xIncrement = (double) dx / steps;
        double yIncrement = (double) dy / steps;
        //Punkte zum result adden
        for (int i = 0; i <= steps; i++) {
            int newX = (int) (startX + i * xIncrement);
            int newY = (int) (startY + i * yIncrement);
            result.add(new Point(newX, newY));
        }
        return result;
    }
    // Just die Punkte klasse zum einfachen saven
    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
