package com.momentum.impl.events.vanilla.entity;

import com.momentum.api.event.Event;

/**
 * Called after the onUpdateWalkingPlayer method
 *
 * @author linus
 * @since 02/21/2023
 */
public class UpdatePlayerSpEvent extends Event {

    // how many times to run the update event
    private int iterations;

    /**
     * Sets the number of iterations
     * @param in The new number of iterations
     */
    public void setIterations(int in) {
        iterations = in;
    }

    /**
     * Gets the iterations
     * @return The number of iterations
     */
    public int getIterations() {
        return iterations;
    }
}
