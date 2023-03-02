package com.momentum.api.feature;

import com.momentum.Momentum;
import com.momentum.api.registry.ILabel;
import com.momentum.impl.events.client.OptionUpdateEvent;

/**
 * Configurable value for a {@link Feature}
 *
 * @param <V> The value type
 * @author linus
 * @since 01/09/2023
 */
public class Option<V> extends Feature implements ILabel {

    // associated feature
    private Feature feature;

    // values
    private V val;

    // minimum and maximum bounds
    private V min;
    private V max;

    /**
     * Option with aliases
     *
     * @param name        The name of the option
     * @param aliases     The aliases of the option
     * @param val         The value of the option
     * @param description The description of the option
     */
    public Option(String name, String[] aliases, String description, V val) {
        super(name, aliases, description);
        this.val = val;
    }

    /**
     * Default option
     *
     * @param name The name of the option
     * @param val The value of the option
     * @param description The description of the option
     */
    public Option(String name, String description, V val) {
        this(name, new String[0], description, val);
    }

    /**
     * Number option
     *
     * @param name        The name of the option
     * @param aliases     The aliases of the option
     * @param min         The min value
     * @param val         The value of the option
     * @param max         The max value
     * @param description The description of the option
     */
    public Option(String name, String[] aliases, String description, V min, V val, V max) {
        this(name, aliases, description, val);
        this.min = min;
        this.max = max;
    }

    /**
     * Number option
     *
     * @param name        The name of the option
     * @param min         The min value
     * @param val         The value of the option
     * @param max         The max value
     * @param description The description of the option
     */
    public Option(String name, String description, V min, V val, V max) {
        this(name, new String[0], description, min, val, max);
    }

    /**
     * Gets the registry label
     *
     * @return The registry label (Must be unique!)
     */
    @Override
    public String getLabel() {

        // class name with identifier
        // String clazz = getClass().getDeclaringClass().getTypeName().toLowerCase();

        // class name
        // clazz = clazz.substring(0, clazz.length() - 6);

        // create label
        return feature.getName().toLowerCase() + "_" + name.toLowerCase() + "_config";
    }

    /**
     * Associate with feature
     *
     * @param in The feature
     */
    public void associate(Feature in) {
        feature = in;
    }

    /**
     * Gets the minimum bound
     *
     * @return The minimum bound
     */
    public V getMin() {
        return min;
    }

    /**
     * Gets the option value
     *
     * @return The option value
     */
    public V getVal() {
        return val;
    }

    /**
     * Gets the maximum bound
     *
     * @return The maximum bound
     */
    public V getMax() {
        return max;
    }

    /**
     * Sets the option value
     *
     * @param in The new option value
     */
    public void setVal(V in) {

        // update value
        val = in;

        // post option update event
        OptionUpdateEvent optionUpdateEvent = new OptionUpdateEvent(this);
        Momentum.EVENT_BUS.dispatch(optionUpdateEvent);
    }
}
