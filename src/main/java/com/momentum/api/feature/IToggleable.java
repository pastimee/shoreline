package com.momentum.api.feature;

/**
 * IToggleable property that allows modification of the enable state
 *
 * @author linus
 * @since 02/02/2023
 */
public interface IToggleable {

    /**
     * Binds the feature to a keybind
     */
    void bind(int in);

    /**
     * Called when the feature is bound to a key
     */
    void onBind();

    /**
     * Toggles the enable state
     */
    void toggle();

    /**
     * Called when the feature is toggled
     */
    void onToggle();

    /**
     * Enables the feature
     */
    void enable();

    /**
     * Called when the feature is enabled
     */
    void onEnable();

    /**
     * Disables the feature
     */
    void disable();

    /**
     * Called when the feature is disabled
     */
    void onDisable();
}
