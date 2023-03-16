package com.momentum.api.module;

/**
 * Module without toggle capabilities
 *
 * @author linus
 * @since 02/08/2023
 */
public class ConcurrentModule extends Module implements IConcurrent {

    /**
     * Module with aliases
     *
     * @param name The name of the module
     * @param aliases The aliases of the module
     * @param description The description of the module
     * @param category The category that the module will appear under in the UI
     */
    public ConcurrentModule(String name, String[] aliases, String description, ModuleCategory category) {
        super(name, aliases, description, category);

        // always enabled
        enabled.setVal(true);
    }

    /**
     * Default module
     *
     * @param name The name of the module
     * @param description The description of the module
     * @param category The category that the module will appear under in the UI
     */
    public ConcurrentModule(String name, String description, ModuleCategory category) {
        this(name, new String[] {}, description, category);
    }

    @Override
    public void toggle() {
        // remove toggle
    }

    @Override
    public void enable() {
        // remove enable
    }

    @Override
    public void disable() {
        // remove disable
    }
}
