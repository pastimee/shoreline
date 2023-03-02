package com.momentum.impl.modules.combat.autototem;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Mode for offhand item
 *
 * @author linus
 * @since 02/19/2023
 */
public enum TotemMode {

    /**
     * Switch to an End Crystal
     */
    CRYSTAL(Items.END_CRYSTAL),

    /**
     * Switch to a Golden Apple
     */
    GAPPLE(Items.GOLDEN_APPLE),

    /**
     * Switch to a Totem
     */
    TOTEM(Items.TOTEM_OF_UNDYING);

    // item associated with mode
    private final Item item;

    /**
     * The totem mode
     *
     * @param item The item associated with mode
     */
    TotemMode(Item item) {
        this.item = item;
    }

    /**
     * Gets the item associated with the mode
     *
     * @return The item associated with the mode
     */
    public Item getItem() {
        return item;
    }
}
