package com.momentum.api.event;

import com.momentum.api.feature.Feature;
import com.momentum.api.util.Wrapper;

/**
 * Listener with associated feature
 *
 * @param <E> The event type to listen for
 * @author linus
 * @since 01/09/2023
 */
public abstract class FeatureListener<F extends Feature, E extends Event> extends Listener<E> implements Wrapper {

    // associated feature
    protected final F feature;

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected FeatureListener(F feature) {
        this.feature = feature;
    }

    /**
     * FeatureListener with custom priority
     *
     * @param feature  The associated feature
     * @param priority Listener priority
     */
    protected FeatureListener(F feature, int priority) {
        super(priority);
        this.feature = feature;
    }

    /**
     * Gets the registry label
     *
     * @return The registry label (Must be unique!)
     */
    @Override
    public String getLabel() {

        // feature name
        String f = feature.getName().toLowerCase();

        // merge feature
        return f + "_" + super.getLabel();
    }
}
