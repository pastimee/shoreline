package com.momentum.impl.events.client;

import com.momentum.api.event.Event;
import com.momentum.api.feature.Option;

/**
 * Called when an option's value is updated
 *
 * @author linus
 * @since 02/13/2023
 */
public class OptionUpdateEvent extends Event {

    // option that updated
    private final Option<?> option;

    /**
     * Called when an option's value is updated
     */
    public OptionUpdateEvent(Option<?> option) {
        this.option = option;
    }

    /**
     * Gets the updated option
     *
     * @return The updated option
     */
    public Option<?> getOption() {
        return option;
    }
}
