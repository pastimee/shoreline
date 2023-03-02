package com.momentum.impl.ui.button;

import com.momentum.api.feature.Option;
import com.momentum.impl.ui.DrawableRect;

/**
 * Abstract button for configs
 *
 * @param <V> The config return type
 */
public class OptionButton<V> extends DrawableRect {

    // option
    protected final Option<V> option;

    /**
     * Config button
     *
     * @param x             The x-position
     * @param y             The y-position
     * @param option The associated config
     */
    protected OptionButton(float x, float y, Option<V> option) {
        super(x, y, 85, 15);

        // assign config
        this.option = option;
    }

    /**
     * Draw function with position updates
     *
     * @param ix     The x position
     * @param iy     The y position
     * @param mouseX The mouse's x position
     * @param mouseY The mouse's y position
     */
    protected void draw(float ix, float iy, int mouseX, int mouseY) {

    }

    /**
     * Draws the component
     *
     * @param mouseX The x-position of the mouse
     * @param mouseY The y-position of the mouse
     */
    @Override
    protected void draw(int mouseX, int mouseY) {

        // position update
        draw(x, y, mouseX, mouseY);
    }

    /**
     * Clicked the component
     *
     * @param mouseX      The x-position of the mouse
     * @param mouseY      The y-position of the mouse
     * @param mouseButton The pressed button
     */
    @Override
    protected void click(int mouseX, int mouseY, int mouseButton) {

    }

    /**
     * Typed on the keyboard
     *
     * @param charTyped The character typed
     * @param key       The key pressed
     */
    @Override
    public void type(char charTyped, int key) {

    }

    /**
     * Gets the height of the button
     *
     * @return The height of the button
     */
    public float getHeight() {
        return height;
    }
}
