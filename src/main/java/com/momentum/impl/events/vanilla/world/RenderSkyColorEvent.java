package com.momentum.impl.events.vanilla.world;

import com.momentum.api.event.Event;

/**
 * Called when the sky color is rendered
 *
 * @author linus
 * @since 03/09/2023
 */
public class RenderSkyColorEvent extends Event {

    // sky color
    private int color;

    /**
     * Sets the sky color
     *
     * @param in The sky color
     */
    public void setColor(int in) {
        color = in;
    }

    /**
     * Gets the sky color
     *
     * @return The sky color
     */
    public int getColor() {
        return color;
    }
}
