package com.momentum.api.macro;

import com.momentum.api.feature.Feature;
import com.momentum.api.registry.ILabel;

/**
 * @author linus
 * @since 03/01/2023
 */
public abstract class Macro extends Feature implements IMacro, ILabel {

    // macro key
    private int key;

    /**
     * Macro default
     */
    public Macro(String name, int key) {
        super(name);
        this.key = key;
    }

    /**
     * Called when the macro key is pressed
     */
    @Override
    public abstract void onPress();

    /**
     * Sets the macro key
     *
     * @param in The new macro key
     */
    public void setKey(int in) {

        // update key
        key = in;
    }

    /**
     * Gets the macro key
     *
     * @return The macro key
     */
    public int getKey() {
        return key;
    }

    /**
     * Gets the label
     *
     * @return The label
     */
    @Override
    public String getLabel() {

        // macro label
        return name.toLowerCase() + "_macro";
    }
}
