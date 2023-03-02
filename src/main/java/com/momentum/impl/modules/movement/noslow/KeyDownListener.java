package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.settings.KeyDownEvent;

/**
 * @author linus
 * @since 02/13/2023
 */
public class KeyDownListener extends FeatureListener<NoSlowModule, KeyDownEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected KeyDownListener(NoSlowModule feature) {
        super(feature);
    }

    @Override
    public void invoke(KeyDownEvent event) {

        // prevent keys from being pressed in screens
        if (feature.isInScreen()) {

            // remove conflict context when pressing keys
            if (feature.inventoryMoveOption.getVal()) {

                // cancel context
                event.setCanceled(true);
            }
        }
    }
}
