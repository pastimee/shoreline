package com.momentum.api.event;

/**
 * Invoker class
 *
 * @author linus
 * @since 02/02/2023
 */
public interface Invoker<E> {

    /**
     * Invokes the listener
     *
     * @param event The event to listen for
     */
    void invoke(E event);
}
