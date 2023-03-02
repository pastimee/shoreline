package com.momentum.api.event;

/**
 * @author linus
 * @since 01/16/2023
 */
public class StageEvent<T extends Enum> extends Event {

    // current event stage
    private T stage;

    /**
     * Sets the event stage
     *
     * @param in The new event stage
     */
    public void setStage(T in) {
        stage = in;
    }

    /**
     * Gets the current event stage
     *
     * @return The current event stage
     */
    public T getStage() {
        return stage;
    }
}
