package com.momentum.impl.modules.combat.aura;

/**
 * Aura target priority mode
 *
 * @author linus
 * @since 03/02/2023
 */
public enum Priority {

    /**
     * Heuristic calc
     */
    SMART,

    /**
     * Target based on health
     */
    HEALTH,

    /**
     * Target based on distance
     */
    DISTANCE
}
