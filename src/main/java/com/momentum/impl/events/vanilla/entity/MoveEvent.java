package com.momentum.impl.events.vanilla.entity;

import com.momentum.api.event.Event;

/**
 * Called when the player moves
 *
 * @author linus
 * @since 02/13/2023
 */
public class MoveEvent extends Event {

    // motion
    private double x, y, z;

    /**
     * Called when the player moves
     */
    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets the x motion
     *
     * @return The x motion
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x motion
     *
     * @param in The new x motion
     */
    public void setX(double in) {
        x = in;
    }

    /**
     * Gets the y motion
     *
     * @return The y motion
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y motion
     *
     * @param in The new y motion
     */
    public void setY(double in) {
        y = in;
    }

    /**
     * Gets the z motion
     *
     * @return The z motion
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the z motion
     *
     * @param in The new z motion
     */
    public void setZ(double in) {
        z = in;
    }
}
