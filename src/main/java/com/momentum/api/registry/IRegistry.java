package com.momentum.api.registry;

/**
 * Interface enabling the ability to configure persistent settings
 *
 * @author linus
 * @since 02/02/2023
 */
public interface IRegistry<T> {

    /**
     * Registers given data
     *
     * @param in The data to register
     */
    void register(ILabel in);

    /**
     * Registers given data
     *
     * @param in The data to register
     */
    void register(ILabel... in);

    /**
     * Unregisters given data
     *
     * @param l The given data label
     */
    void unregister(String l);

    /**
     * Searches for the given label
     *
     * @param l The given label
     * @return The data
     */
    T lookup(String l);
}
