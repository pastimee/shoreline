package com.momentum.impl.modules.movement.velocity;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.entity.player.PushedByWaterEvent;

/**
 * @author linus
 * @since 01/23/2023
 */
public class PushedByWaterListener extends FeatureListener<VelocityModule, PushedByWaterEvent> {
    protected PushedByWaterListener(VelocityModule module) {
        super(module);
    }

    @Override
    public void invoke(PushedByWaterEvent event) {

        // cancel velocity from liquids
        if (feature.liquidsOption.getVal()) {

            // cancel event
            event.setCanceled(true);
        }
    }
}
