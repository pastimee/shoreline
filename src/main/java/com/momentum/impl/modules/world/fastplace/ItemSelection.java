package com.momentum.impl.modules.world.fastplace;

/**
 * How to select items
 *
 * @author linus
 * @since 02/19/2023
 */
public enum ItemSelection {

    /**
     * Only uses whitelist items
     */
    WHITELIST,

    /**
     * Only uses items not in the blacklist
     */
    BLACKLIST,

    /**
     * Uses all items
     */
    ALL
}
