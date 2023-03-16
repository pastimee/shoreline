package com.momentum.impl.events.vanilla.renderer;

import com.momentum.api.event.Event;

/**
 * Called when the fog color is rendered
 *
 * @author linus
 * @since 03/09/2023
 */
public class RenderFogColorEvent extends Event {

    // fog color
    private int color;

    /**
     * Sets the fog color
     *
     * @param in The fog color
     */
    public void setColor(int in) {
        color = in;
    }

    /**
     * Gets the fog color
     *
     * @return The fog color
     */
    public int getColor() {
        return color;
    }
}
