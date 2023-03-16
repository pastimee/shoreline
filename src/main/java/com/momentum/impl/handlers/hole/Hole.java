package com.momentum.impl.handlers.hole;

import net.minecraft.util.math.BlockPos;

/**
 * Hole info holder class
 *
 * @author linus
 * @since 03/15/2023
 */
public class Hole {

    // hole info
    private final BlockPos pos;
    private final HoleType type;

    /**
     * Initializes hole info
     */
    public Hole(BlockPos pos, HoleType type) {
        this.pos = pos;
        this.type = type;
    }

    /**
     * Gets the hole's position
     *
     * @return The hole's position
     */
    public BlockPos getPos() {
        return pos;
    }

    /**
     * Gets the hole's type
     *
     * @return The hole's type
     */
    public HoleType getHoleType() {
        return type;
    }

    /**
     * Checks whether a hole is a double hole
     *
     * @return Whether the hole is a double hole
     */
    public boolean isDouble() {
        return type.equals(HoleType.DOUBLE_RESISTANT_X)
                || type.equals(HoleType.DOUBLE_MIXED_X)
                || type.equals(HoleType.DOUBLE_UNBREAKABLE_X)
                || type.equals(HoleType.DOUBLE_RESISTANT_Z)
                || type.equals(HoleType.DOUBLE_MIXED_Z)
                || type.equals(HoleType.DOUBLE_UNBREAKABLE_Z);
    }

    /**
     * Checks whether a hole is a quad hole
     *
     * @return Whether the hole is a quad hole
     */
    public boolean isQuad() {
        return type.equals(HoleType.QUAD_RESISTANT)
                || type.equals(HoleType.QUAD_MIXED)
                || type.equals(HoleType.QUAD_UNBREAKABLE);
    }
}
