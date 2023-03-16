package com.momentum.api.util.rotation;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author linus
 * @since 03/06/2023
 */
public class RotationUtil {

    /**
     * Gets the yaw difference from one vector to another
     *
     * @param from The start vector
     * @param to The end vector
     * @return The yaw difference
     */
    public static float yawTo(Vec3d from, Vec3d to) {

        // yaw difference
        return (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(
                to.subtract(from).z, to.subtract(from).x)) - 90.0f);
    }

    /**
     * Gets the pitch difference from one vector to another
     *
     * @param from The start vector
     * @param to The end vector
     * @return The pitch difference
     */
    public static float pitchTo(Vec3d from, Vec3d to) {

        // yaw difference
        return (float) MathHelper.wrapDegrees(Math.toDegrees(-Math.atan2(
                to.subtract(from).y, Math.hypot(to.subtract(from).x, to.subtract(from).z))));
    }

    /**
     * Gets the rotation difference between two vectors
     *
     * @param from The start vector
     * @param to The end vector
     * @return The rotation difference
     */
    public static Rotation diff(Vec3d from, Vec3d to) {

        // diffs
        float yaw = yawTo(from, to);
        float pitch = pitchTo(from, to);

        // return as rotation obj
        return new Rotation(yaw, pitch);
    }

    /**
     * Interpolated look vector
     *
     * @param rotation The player rotation
     * @return The interpolated look vector
     */
    public static Vec3d getLook(Rotation rotation) {

        // gets the look
        return getVec(rotation);
    }

    /**
     * Gets the vector for a specified rotation
     *
     * @param rotation The rotation
     * @return The vector for the specified rotation
     */
    public static Vec3d getVec(Rotation rotation) {

        // convert rotation to degrees
        float yaw = -rotation.getYaw() * 0.017453292f;
        float pitch = -rotation.getPitch() * 0.017453292f;

        // get vector
        // {@see Entity.class}
        float f = MathHelper.cos(yaw - (float) Math.PI);
        float f1 = MathHelper.sin(yaw - (float) Math.PI);
        float f2 = -MathHelper.cos(pitch);
        float f3 = MathHelper.sin(pitch);
        return new Vec3d(f1 * f2, f3, f * f2);
    }
}
