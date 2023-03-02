package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.forge.InputUpdateEvent;

/**
 * @author linus
 * @since 02/13/2023
 */
public class InputUpdateListener extends FeatureListener<NoSlowModule, InputUpdateEvent> {
    protected InputUpdateListener(NoSlowModule feature) {
        super(feature);
    }

    @Override
    public void invoke(InputUpdateEvent event) {

        // check if player is slowed down
        if (feature.isSlowed()) {

            // remove vanilla slowdown effect
            event.getMovementInput().moveForward *= 5;
            event.getMovementInput().moveStrafe *= 5;
        }
    }
}
