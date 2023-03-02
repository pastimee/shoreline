package com.momentum.api.handler;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.api.registry.ILabel;
import io.netty.util.internal.ConcurrentSet;

import java.util.Arrays;
import java.util.Set;

/**
 * Similar to always enabled modules, run in the background,
 * provide important client functions
 *
 * @author linus
 * @since 03/01/2023
 */
@SuppressWarnings("rawtypes")
public class Handler implements ILabel {

    // listeners
    private final Set<Listener> listeners = new ConcurrentSet<>();

    /**
     * Gets the label
     *
     * @return The label
     */
    @Override
    public String getLabel() {

        // class name with identifier
        String clazz = getClass().getSimpleName().toLowerCase();

        // class name
        clazz = clazz.substring(0, clazz.length() - 7);

        // create label
        return clazz + "_handler";
    }

    /**
     * Subscribe all listeners
     */
    public void subscribe() {

        // all listeners
        for (Listener l : listeners) {

            // subscribe listener
            Momentum.EVENT_BUS.subscribe(l);
        }
    }

    /**
     * Add listener to list
     *
     * @param in The listener to add
     */
    protected void associate(Listener in) {

        // add listener
        listeners.add(in);
    }

    /**
     * Adds all listeners to list
     *
     * @param in The listeners to add
     */
    protected void associate(Listener... in) {

        // add all
        listeners.addAll(Arrays.asList(in));
    }
}
