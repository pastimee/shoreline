package com.momentum.impl.events.vanilla.entity;

import com.momentum.api.event.EventStage;
import com.momentum.api.event.StageEvent;

/**
 * Called when movement packets are sent
 *
 * @author linus
 * @since 03/06/2023
 */
public class UpdateWalkingPlayerEvent extends StageEvent<EventStage> {

    // new position
    private double x, y, z;

    // new rotation
    private float yaw, pitch;

    // new onGround
    private boolean onGround;

    /**
     * Initializes to vanilla values
     */
    public UpdateWalkingPlayerEvent(double x, double y, double z, float yaw,
                                    float pitch,  boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    /**
     * Gets the new x pos
     *
     * @return The new x pos
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the new x pos
     *
     * @param in The new x pos
     */
    public void setX(double in) {
        x = in;
    }

    /**
     * Gets the new y pos
     *
     * @return The new y pos
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the new y pos
     *
     * @param in The new y pos
     */
    public void setY(double in) {
        y = in;
    }

    /**
     * Gets the new z pos
     *
     * @return The new z pos
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the new z pos
     *
     * @param in The new z pos
     */
    public void setZ(double in) {
        z = in;
    }

    /**
     * Gets the new yaw
     *
     * @return The new yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Gets the new pitch
     *
     * @return The new pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the new yaw
     *
     * @param in The new yaw
     */
    public void setYaw(float in) {
        yaw = in;
    }

    /**
     * Sets the new pitch
     *
     * @param in The new pitch
     */
    public void setPitch(float in) {
        pitch = in;
    }

    /**
     * Gets the new onGround
     *
     * @return The new onGround
     */
    public boolean getOnGround() {
        return onGround;
    }

    /**
     * Sets the new onGround
     *
     * @param in The new onGround
     */
    public void setOnGround(boolean in) {
        onGround = in;
    }
}
