package com.momentum.api.event;

/**
 * @author linus
 * @since 03/06/2023
 */
public interface IEventBus {

    /**
     * Subscribes a given event listener
     *
     * @param in The event listener
     */
    void subscribe(Listener in);

    /**
     * Unsubscribes a given event listener
     *
     * @param in The event listener
     */
    void unsubscribe(Listener in);

    /**
     * Clears listeners
     */
    void clear();

    /**
     * Invokes all listeners associated with an event
     *
     * @param event The event to search for
     */
    void dispatch(Event event);
}
