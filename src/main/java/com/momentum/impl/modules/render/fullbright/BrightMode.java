package com.momentum.impl.modules.render.fullbright;

/**
 * Brightness mode
 *
 * @author linus
 * @since 02/21/2023
 */
public enum BrightMode {

    /**
     * Updates the world's gamma value
     */
    GAMMA,

    /**
     * Applies the {@link net.minecraft.init.MobEffects} NightVision effect
     */
    POTION
}
