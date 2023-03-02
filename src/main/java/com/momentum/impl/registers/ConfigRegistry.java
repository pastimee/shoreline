package com.momentum.impl.registers;

import com.momentum.api.config.Config;
import com.momentum.api.registry.Registry;
import com.momentum.impl.configs.ClickGuiConfig;
import com.momentum.impl.configs.DefaultConfig;
import com.momentum.impl.configs.UserConfig;

/**
 * Registry of configs
 *
 * @author linus
 * @since 02/08/2023
 */
@SuppressWarnings("rawtypes")
public class ConfigRegistry extends Registry<Config> {

    /**
     * Registry of configs
     */
    public ConfigRegistry() {

        // register configs
        register(
                new UserConfig(),
                new DefaultConfig(),
                new ClickGuiConfig()
        );
    }
}
