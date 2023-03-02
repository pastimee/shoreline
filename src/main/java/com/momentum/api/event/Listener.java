package com.momentum.api.event;

import com.momentum.api.registry.ILabel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Listener class that listens for a certain event
 *
 * @param <E> The event type to listen for
 * @author linus
 * @since 01/09/2023
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Listener<E extends Event> implements Invoker<E>, ILabel {

    // event type
    private Class<E> type;

    // listener priority
    private int priority;

    /**
     * Default constructor
     */
    protected Listener() {

        // generic type (event type)
        Type generic = getClass().getGenericSuperclass();

        // check generic type
        if (generic instanceof ParameterizedType) {
            for (Type type : ((ParameterizedType) generic).getActualTypeArguments()) {

                // make sure type is valid
                if (!(type instanceof Class) || !Event.class.isAssignableFrom((Class<E>) type)) {
                    continue;
                }

                // initialize listener type
                this.type = (Class<E>) type;
                break;
            }
        }
    }

    /**
     * Listener with a custom priority
     *
     * @param priority Listener priority
     */
    protected Listener(int priority) {
        this();
        this.priority = priority;
    }

    /**
     * Called when an event is posted by the event bus
     *
     * @param event The event
     */
    public abstract void invoke(E event);

    /**
     * Gets the registry label
     *
     * @return The registry label (Must be unique!)
     */
    @Override
    public String getLabel() {

        // lowercase of the type name
        String type = getType().getTypeName().toLowerCase();

        // event name
        type = type.substring(0, type.length() - 8);

        // create label
        return type + "_listener";
    }

    /**
     * Gets the event {@link Event} type to listen for
     *
     * @return The event {@link Event} type to listen for
     */
    public Class<E> getType() {
        return type;
    }

    /**
     * Gets the listener priority (highest priority gets called first)
     *
     * @return The listener priority
     */
    public int getPriority() {
        return priority;
    }
}
