package com.momentum.impl.events.forge;

import com.momentum.api.event.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Called when a player left clicks while targeting a block
 *
 * @author linus
 * @since 03/09/2023
 */
public class LeftClickBlockEvent extends Event {

    // block info
    private final BlockPos pos;
    private final EnumFacing face;

    /**
     * Called when a player left clicks while targeting a block
     *
     * @param pos The block position
     * @param face The block face
     */
    public LeftClickBlockEvent(BlockPos pos, EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }

    /**
     * Gets the position of the block
     *
     * @return The position of the block
     */
    public BlockPos getPos() {
        return pos;
    }

    /**
     * Gets the face of the block
     *
     * @return The face of the block
     */
    public EnumFacing getFace() {
        return face;
    }
}
