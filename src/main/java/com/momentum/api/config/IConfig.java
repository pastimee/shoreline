package com.momentum.api.config;

/**
 * Config helper class
 *
 * @author linus
 * @since 02/02/2023
 * @param <T> The config type
 */
public interface IConfig<T> {

    /**
     * Saves all configurations
     */
    void save();

    /**
     * Saves a specific option profile
     *
     * @param in The option profile
     */
    void save(String in);

    /**
     * Loads all configurations
     */
    void load();

    /**
     * Loads a specific option profile
     *
     * @param in The option profile
     */
    void load(String in);
}
