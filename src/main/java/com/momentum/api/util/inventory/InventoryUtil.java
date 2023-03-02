package com.momentum.api.util.inventory;

import com.momentum.api.util.Wrapper;
import net.minecraft.item.Item;

/**
 * @author linustouchtips
 * @since 05/06/2021
 */
public class InventoryUtil implements Wrapper {

    /**
     * Checks if the player is holding a specified item
     *
     * @param item The specified item
     * @return Whether the player is holding the specified item
     */
    public static boolean isHolding(Item item) {
        return mc.player.getHeldItemMainhand().getItem() == item;
    }

    /**
     * Checks if the player is holding a specified item
     *
     * @param clazz The specified item
     * @return Whether the player is holding the specified item
     */
    public static boolean isHolding(Class<? extends Item> clazz) {
        return clazz.isInstance(mc.player.getHeldItemMainhand().getItem());
    }
}
