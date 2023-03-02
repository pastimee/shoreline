package com.momentum.impl.modules.movement.fastfall;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.entity.MoveEvent;

/**
 * @author linus
 * @since 02/21/2023
 */
public class MoveListener extends FeatureListener<FastFallModule, MoveEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected MoveListener(FastFallModule feature) {
        super(feature);
    }

    @Override
    public void invoke(MoveEvent event) {

        // movement lock impl
        if (feature.typeOption.getVal() == FallType.SHIFT && feature.lock) {

            // override motion
            event.setCanceled(true);

            // cancel movement
            event.setX(0);
            event.setY(0);
            event.setZ(0);
            mc.player.motionX = 0;
            mc.player.motionY = 0;
            mc.player.motionZ = 0;

            // update pause ticks
            feature.ticks++;

            // passed wait time
            if (feature.ticks > feature.shiftTicksOption.getVal()) {

                // unlock
                feature.lock = false;
            }
        }
    }
}
