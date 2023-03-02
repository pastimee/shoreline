package com.momentum.api.event;

/**
 * @author linus
 * @since 01/09/2023
 */
public class Event {

    // event info
    private boolean cancel;

    /**
     * Cancels the event
     *
     * @param in The new cancel state
     */
    public void setCanceled(boolean in) {
        cancel = in;
    }

    /**
     * Checks whether the event is canceled
     *
     * @return Whether the event is canceled
     */
    public boolean isCanceled() {
        return cancel;
    }
}
