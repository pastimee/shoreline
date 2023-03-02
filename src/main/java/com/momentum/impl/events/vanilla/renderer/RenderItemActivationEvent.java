package com.momentum.impl.events.vanilla.renderer;

import com.momentum.api.event.Event;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Called when an item activation animation is rendered
 *
 * @author linus
 * @since 02/14/2023
 */
public class RenderItemActivationEvent extends Event {

    // itemActivationItem
    private final ItemStack item;

    /**
     * Saves the item being activated
     *
     * @param item The item being activated
     */
    public RenderItemActivationEvent(ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the item being activated
     *
     * @return The item being activated
     */
    public Item getItem() {
        return item != null ? item.getItem() : null;
    }
}
