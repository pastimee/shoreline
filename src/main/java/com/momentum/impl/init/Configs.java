package com.momentum.impl.init;

import com.momentum.Momentum;
import com.momentum.api.config.Config;
import com.momentum.impl.configs.ClickGuiConfig;
import com.momentum.impl.configs.DefaultConfig;
import com.momentum.impl.configs.UserConfig;

/**
 * @author linus
 * @since 02/11/2023
 */
public class Configs {

    // MAINTAIN ORDER
    // config instances
    public static final ClickGuiConfig CLICKGUI_CONFIG;
    public static final DefaultConfig DEFAULT_CONFIG;
    public static final UserConfig USER_CONFIG;

    /**
     * Saves all configurations
     */
    public static void save() {

        // save order
        CLICKGUI_CONFIG.save();
        DEFAULT_CONFIG.save();
        USER_CONFIG.save();
    }

    /**
     * Saves a specific option profile
     *
     * @param in The option profile
     */
    public static void save(String in) {

        // save order
        CLICKGUI_CONFIG.save();
        // DEFAULT_CONFIG.save(in);
        USER_CONFIG.save(in);
    }

    /**
     * Loads all configurations
     */
    public static void load() {

        // load order
        CLICKGUI_CONFIG.load();
        DEFAULT_CONFIG.load();
        USER_CONFIG.load();
    }

    /**
     * Loads a specific option profile
     *
     * @param in The option profile
     */
    public static void load(String in) {

        // load order
        // CLICKGUI_CONFIG.load();
        // DEFAULT_CONFIG.load(in);
        USER_CONFIG.load(in);
    }

    /**
     * Gets the registered config
     *
     * @param label The config label
     * @return The registered config
     */
    private static Config<?> getRegisteredConfig(String label) {

        // module from registry
        return Momentum.CONFIG_REGISTRY.lookup(label);
    }

    static {

        // configs
        CLICKGUI_CONFIG = (ClickGuiConfig) getRegisteredConfig("clickgui_file");
        DEFAULT_CONFIG = (DefaultConfig) getRegisteredConfig("default_file");
        USER_CONFIG = (UserConfig) getRegisteredConfig("user_file");
    }
}
