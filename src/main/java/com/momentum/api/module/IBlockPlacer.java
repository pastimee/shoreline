package com.momentum.api.module;

import net.minecraft.util.math.BlockPos;

/**
 * @author linus
 * @since 03/13/2023
 */
public interface IBlockPlacer {

    /**
     * Place block
     *
     * @param in The position
     */
    boolean place(BlockPos in);
}
