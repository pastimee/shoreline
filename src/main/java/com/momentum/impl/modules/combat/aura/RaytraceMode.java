package com.momentum.impl.modules.combat.aura;

/**
 * Raytrace offset mode
 *
 * @author linus
 * @since 03/06/2023
 */
public enum RaytraceMode {

    /**
     * Scans entire bounding box
     */
    SCAN,

    /**
     * Checks if eyes are visible
     */
    EYES,

    /**
     * Checks if feet are visible
     */
    FEET
}
