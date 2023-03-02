package com.momentum.api.event;

import io.netty.util.internal.ConcurrentSet;

import java.util.Set;

/**
 * @author linus
 * @since 01/09/2023
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventBus {

    // list of event listeners
    private final Set<Listener> listeners = new ConcurrentSet<>();

    /**
     * Clears listeners
     */
    public void clear() {
        listeners.clear();
    }

    /**
     * Subscribes a given event listener
     *
     * @param in The event listener
     */
    public void subscribe(Listener in) {
        listeners.add(in);
    }

    /**
     * Unsubscribes a given event listener
     *
     * @param in The event listener
     */
    public void unsubscribe(Listener in) {
        listeners.remove(in);
    }

    /**
     * Invokes all listeners associated with an event
     *
     * @param event The event to search for
     */
    public void dispatch(Event event) {

        // check all listeners
        listeners.forEach(l -> {

            // type match
            if (l.getType() == event.getClass()) {

                // invoke listener
                l.invoke(event);
            }
        });
    }
}