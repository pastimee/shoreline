package com.momentum.api.event;

import io.netty.util.internal.ConcurrentSet;

import java.util.Set;

/**
 * @author linus
 * @since 01/09/2023
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventBus implements IEventBus {

    // list of event listeners
    private final Set<Listener> listeners = new ConcurrentSet<>();

    /**
     * Subscribes a given event listener
     *
     * @param in The event listener
     */
    @Override
    public void subscribe(Listener in) {
        listeners.add(in);
    }

    /**
     * Unsubscribes a given event listener
     *
     * @param in The event listener
     */
    @Override
    public void unsubscribe(Listener in) {
        listeners.remove(in);
    }

    /**
     * Clears listeners
     */
    @Override
    public void clear() {
        listeners.clear();
    }

    /**
     * Invokes all listeners associated with an event
     *
     * @param event The event to search for
     */
    @Override
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