package com.momentum.impl.modules.combat.aura;

/**
 * TPS sync mode
 *
 * @author linus
 * @since 03/02/2023
 */
public enum TpsSync {

    /**
     * Latest TPS calc
     */
    LAST,

    /**
     * TPS average
     */
    AVERAGE,

    /**
     * Lowest TPS sync
     */
    MINIMAL
}
