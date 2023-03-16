package com.momentum.impl.modules.render.noweather;

/**
 * World weather mode
 *
 * @author linus
 * @since 03/08/2023
 */
public enum Weather {

    /**
     * No rain
     */
    CLEAR(0),

    /**
     * Rain
     */
    RAIN(1),

    /**
     * Rain and thunder
     */
    THUNDER(2);

    // weather id
    private final int id;

    Weather(int id) {
        this.id = id;
    }

    /**
     * Gets the weather identifier
     *
     * @return the weather identifier
     */
    public int getWeatherId() {
        return id;
    }
}
