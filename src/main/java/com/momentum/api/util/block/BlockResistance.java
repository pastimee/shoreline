package com.momentum.api.util.block;

/**
 * The resistance level of the block
 *
 * @author linus
 * @since 02/18/2023
 */
public enum BlockResistance {

    /**
     * Blocks that can be replaced by other blocks
     */
    REPLACEABLE,

    /**
     * Blocks that can be broken with tools in survival mode
     */
    BREAKABLE,

    /**
     * Blocks that are resistant to explosions
     */
    RESISTANT,

    /**
     * Blocks that are unbreakable with tools in survival mode
     */
    UNBREAKABLE
}
