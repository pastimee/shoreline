package com.momentum.impl.handlers.hole;

/**
 * Hole type
 *
 * @author linus
 * @since 03/15/2023
 */
public enum HoleType {

    /**
     * Resistant hole -> Resistant to explosions but breakable
     */
    RESISTANT,

    /**
     * Mixed hole -> Mix of resistant and unbreakable blocks
     */
    MIXED,

    /**
     * Unbreakable hole -> Unbreakable blocks, resistant to explosions
     */
    UNBREAKABLE,

    /**
     * Resistant double hole -> Resistant to explosions but breakable
     */
    DOUBLE_RESISTANT_X,

    /**
     * Resistant double hole -> Resistant to explosions but breakable
     */
    DOUBLE_RESISTANT_Z,

    /**
     * Mixed double hole -> Mix of resistant and unbreakable blocks
     */
    DOUBLE_MIXED_X,

    /**
     * Mixed double hole -> Mix of resistant and unbreakable blocks
     */
    DOUBLE_MIXED_Z,

    /**
     * Unbreakable double hole -> Unbreakable blocks, resistant to explosions
     */
    DOUBLE_UNBREAKABLE_X,

    /**
     * Unbreakable double hole -> Unbreakable blocks, resistant to explosions
     */
    DOUBLE_UNBREAKABLE_Z,

    /**
     * Resistant quad hole -> Resistant to explosions but breakable
     */
    QUAD_RESISTANT,

    /**
     * Mixed quad hole -> Mix of resistant and unbreakable blocks
     */
    QUAD_MIXED,

    /**
     * Unbreakable quad hole -> Unbreakable blocks, resistant to explosions
     */
    QUAD_UNBREAKABLE,

    /**
     * Void hole -> Hole to outside world bounds
     */
    VOID
}