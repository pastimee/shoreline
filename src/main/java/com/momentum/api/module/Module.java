package com.momentum.api.module;

import com.momentum.api.event.Listener;
import com.momentum.api.feature.Option;
import com.momentum.api.feature.Feature;
import com.momentum.api.feature.IToggleable;
import com.momentum.api.registry.ILabel;
import com.momentum.Momentum;
import com.momentum.impl.init.Handlers;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Client module that appears in the UI
 *
 * @author linus
 * @since 01/16/2023
 */
@SuppressWarnings("rawtypes")
public class Module extends Feature implements IToggleable, ILabel {

    // category of the module
    // determines which category the module will appear under in the UI
    private final ModuleCategory category;

    // listeners associated with this feature
    private final List<Listener> listeners = new ArrayList<>();

    // configurations associated with this feature
    private final List<Option> options = new ArrayList<>();

    // global options
    // enabled states
    protected final Option<Integer> bind =
            new Option<>("Bind", "Bind state", Keyboard.KEY_NONE);
    protected final Option<Boolean> enabled =
            new Option<>("Enabled", "Enabled state", false);
    protected final Option<Boolean> drawn =
            new Option<>("Drawn", "Drawn state", true);

    /**
     * Module with aliases
     *
     * @param name The name of the module
     * @param aliases The aliases of the module
     * @param description The description of the module
     * @param category The category that the module will appear under in the UI
     */
    public Module(String name, String[] aliases, String description, ModuleCategory category) {
        super(name, aliases, description);

        /*
        // catches IllegalArgumentException and IllegalAccessException
        try {

            // class declared fields
            for (Field f : getClass().getDeclaredFields()) {

                // option field
                if (f.getType().isInstance(Option.class) || Option.class.isAssignableFrom(f.getType())) {

                    // check accessibility
                    if (!f.isAccessible()) {

                        // access field
                        f.setAccessible(true);
                    }

                    // config value
                    Object config = f.get(this);

                    // add to list of options
                    options.add((Option) config);
                }

                // listener field
                else if (f.getType().isInstance(FeatureListener.class) || FeatureListener.class.isAssignableFrom(f.getType())) {

                    // check accessibility
                    if (!f.isAccessible()) {

                        // access field
                        f.setAccessible(true);
                    }

                    // config value
                    Object listener = f.get(this);

                    // add to list of options
                    listeners.add((Listener) listener);
                }
            }
        }

        // get method throws
        catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
         */

        // assign category on instantiation
        this.category = category;
    }

    /**
     * Default module
     *
     * @param name The name of the module
     * @param description The description of the module
     * @param category The category that the module will appear under in the UI
     */
    public Module(String name, String description, ModuleCategory category) {
        this(name, new String[] {}, description, category);
    }

    /**
     * Binds the feature to a key
     *
     * @param in The key
     */
    @Override
    public void bind(int in) {

        // set the feature bind
        bind.setVal(in);
        onBind();
    }

    /**
     * Drawn state
     *
     * @param in The new drawn state
     */
    public void draw(boolean in) {

        // set the drawn config
        drawn.setVal(in);
    }

    /**
     * Called when the feature is bound to a key
     */
    @Override
    public void onBind() {

        // make sure macro registry has been loaded
        if (Momentum.MACRO_REGISTRY != null) {

            // update macro
            Handlers.MACRO_HANDLER
                    .setKey(name.toLowerCase() + "_macro", bind.getVal());
        }
    }

    /**
     * Toggles the enabled state
     */
    @Override
    public void toggle() {

        // check enabled state
        if (enabled.getVal()) {
            enabled.setVal(false);
            onDisable();
        }

        // not enabled
        else {
            enabled.setVal(true);
            onEnable();
        }

        // event
        onToggle();
    }

    @Override
    public void onToggle() {

    }

    /**
     * Enables the module
     */
    @Override
    public void enable() {

        // enabled state = true
        enabled.setVal(true);
        onEnable();
    }

    /**
     * Called when feature is enabled
     */
    @Override
    public void onEnable() {

        // subscribe all listeners
        for (Listener d : listeners) {

            // subscribe
            Momentum.EVENT_BUS.subscribe(d);
        }
    }

    /**
     * Disables the module
     */
    @Override
    public void disable() {

        // enabled state = false
        enabled.setVal(false);
        onDisable();
    }

    /**
     * Called when feature is disabled
     */
    @Override
    public void onDisable() {

        // subscribe all listeners
        for (Listener d : listeners) {

            // subscribe
            Momentum.EVENT_BUS.unsubscribe(d);
        }
    }

    /**
     * Gets the registry label
     *
     * @return The registry label (Must be unique!)
     */
    @Override
    public String getLabel() {

        // create label
        return name.toLowerCase() + "_module";
    }

    /**
     * Associates options with this module
     *
     * @param config The options
     */
    public void associate(Option... config) {

        // associate all
        for (Option option : config) {
            
            // create association
            // add to list of associated options
            option.associate(this);
            options.add(option);
        }
    }

    /**
     * Associates listeners with this module
     *
     * @param config The listeners
     */
    public void associate(Listener... config) {

        // associate all
        // add to list of associated listeners
        listeners.addAll(Arrays.asList(config));
    }

    /**
     * Gets the module's category
     *
     * @return The module's category
     */
    public ModuleCategory getCategory() {
        return category;
    }

    /**
     * Gets the keybind
     *
     * @return The keybind
     */
    public int getBind() {
        return bind.getVal();
    }

    /**
     * Checks whether the module is enabled
     *
     * @return Whether the module is enabled
     */
    public boolean isEnabled() {
        return enabled.getVal();
    }

    /**
     * Checks whether the module is drawn
     *
     * @return Whether the module is drawn
     */
    public boolean isDrawn() {
        return drawn.getVal();
    }

    /**
     * Gets the configurations associated with this module
     *
     * @return The configurations associated with this module
     */
    public Collection<Option> getOptions() {
        return options;
    }

    /**
     * Gets the arraylist data
     *
     * @return The arraylist data
     */
    public String getData() {

        // implemented by module
        return "";
    }
}
