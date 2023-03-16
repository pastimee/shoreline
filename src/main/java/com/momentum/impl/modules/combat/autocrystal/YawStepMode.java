package com.momentum.impl.modules.combat.autocrystal;

/**
 * YawStep mode
 *
 * @author linus
 * @since 03/13/2023
 */
public enum YawStepMode {

    /**
     * Applies yawstep calcs when attacking and placing
     */
    FULL,

    /**
     * Applies yawstep calcs when attacking
     */
    SEMI,

    /**
     * Yawstep calcs off
     */
    OFF
}
