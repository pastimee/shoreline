package com.momentum.api.util.rotation;

import net.minecraft.util.math.MathHelper;

/**
 * @author linus
 * @since 03/06/2023
 */
public class Rotation {

    // rotation values
    private float yaw, pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Gets the yaw value for the rotation
     *
     * @return The yaw value for the rotation
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Sets the yaw value for the rotation
     *
     * @param in The new yaw value for the rotation
     */
    public void setYaw(float in) {
        yaw = in;
    }

    /**
     * Gets the pitch value for the rotation
     *
     * @return The pitch value for the rotation
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch value for the rotation
     *
     * @param in The new pitch value for the rotation
     */
    public void setPitch(float in) {

        // clamp to bounds
        pitch = MathHelper.clamp(in, -90.0f, 90.0f);
    }
}

