package com.momentum.impl.modules.movement.sprint;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.entity.SprintingEvent;

/**
 * @author linus
 * @since 02/11/2023
 */
public class SprintingListener extends FeatureListener<SprintModule, SprintingEvent> {
    protected SprintingListener(SprintModule feature) {
        super(feature);
    }

    @Override
    public void invoke(SprintingEvent event) {

        // prevent stop sprint
        if (feature.modeOption.getVal() == SprintMode.RAGE) {

            // check if player is moving
            if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {

                // check hunger level, cannot sprint if hunger level is too low
                if (mc.player.getFoodStats().getFoodLevel() > 6) {

                    // prevent living update from canceling
                    event.setCanceled(true);
                }
            }
        }
    }
}
