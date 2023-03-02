package com.momentum.api.registry;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry implementation backed by {@link java.util.concurrent.ConcurrentHashMap}
 *
 * @author linus
 * @since 02/02/2023
 */
public class Registry<T extends ILabel> implements IRegistry<T> {

    // register backed by HashMap
    protected final Map<String, T> register;

    /**
     * Creates a new register
     */
    public Registry() {
        register = new ConcurrentHashMap<>();
    }

    /**
     * Registers given data
     *
     * @param in The data to register
     */
    @SuppressWarnings("unchecked")
    @Override
    public void register(ILabel in) {

        // null check
        if (in == null) {
            throw new NullPointerException();
        }

        // add to register
        register.put(in.getLabel(), (T) in);
    }

    @SafeVarargs
    @Override
    public final void register(ILabel... in) {

        // index through list
        for (ILabel tRegistryData : in) {

            // register data
            register(tRegistryData);
        }
    }

    @Override
    public void unregister(String l) {

        // null check
        if (l == null) {
            throw new NullPointerException();
        }

        // add to register
        register.remove(l);
    }

    /**
     * Finds a certain key's value
     *
     * @return The key's value
     */
    @Override
    public T lookup(String l) {

        // find by label
        return register.get(l);
    }

    /**
     * Gets the data in the registry
     *
     * @return The collection of the data
     */
    public Collection<T> getData() {

        // return values collection in register
        return register.values();
    }
}
