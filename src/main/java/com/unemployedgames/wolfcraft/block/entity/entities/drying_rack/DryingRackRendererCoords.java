package com.unemployedgames.wolfcraft.block.entity.entities.drying_rack;

import com.mojang.math.Axis;
import net.minecraft.core.Direction;

public class DryingRackRendererCoords {
    public static class Translate {
        public static float Y = 0.6f;
        public class NORTH implements DryingRackRendererCoordImpl {

            @Override
            public float getX() {
                return 0.5f;
            }

            @Override
            public float getZ() {
                return 0.9f;
            }
        }

        public class EAST implements DryingRackRendererCoordImpl {
            @Override
            public float getX() {
                return 0.1f;
            }

            @Override
            public float getZ() {
                return 0.5f;
            }
        }

        public class SOUTH implements DryingRackRendererCoordImpl {
            @Override
            public float getX() {
                return 0.5f;
            }

            @Override
            public float getZ() {
                return 0.1f;
            }
        }

        public class WEST implements DryingRackRendererCoordImpl {
            @Override
            public float getX() {
                return 0.9f;
            }

            @Override
            public float getZ() {
                return 0.5f;
            }
        }

        public DryingRackRendererCoordImpl getDirection(Direction direction) {
            switch (direction) {
                case NORTH -> {
                    return new NORTH();
                }
                case EAST -> {
                    return new EAST();
                }
                case SOUTH -> {
                    return new SOUTH();
                }
                case WEST -> {
                    return new WEST();
                }
                default -> {
                    return new NORTH();
                }
            }
        }

    }

    public static class MulPos {
        public static int manipulateAbout = 90;
        public static Axis aroundAxis = Axis.YP;

        public static boolean needsAMul(Direction direction) {
            switch (direction) {
                case NORTH -> {
                    return false;
                }
                case EAST -> {
                    return true;
                }
                case SOUTH -> {
                    return false;
                }
                case WEST -> {
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }
    }
}
