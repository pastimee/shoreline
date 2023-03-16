package com.momentum.impl.events.vanilla.renderer;

import com.momentum.api.event.Event;

/**
 * Called when the view camera is oriented
 *
 * @author linus
 * @since 03/08/2023
 */
public class OrientCameraEvent extends Event {

    // camera distance
    private double distance;

    /**
     * Gets the camera distance
     *
     * @return The camera distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets the camera distance
     *
     * @param in The new camera distance
     */
    public void setDistance(double in) {
        distance = in;
    }
}
