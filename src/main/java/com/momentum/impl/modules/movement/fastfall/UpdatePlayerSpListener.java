package com.momentum.impl.modules.movement.fastfall;

import com.momentum.Momentum;
import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.entity.UpdatePlayerSpEvent;
import com.momentum.impl.init.Handlers;
import com.momentum.impl.init.Modules;

/**
 * @author linus
 * @since 02/21/2023
 */
public class UpdatePlayerSpListener extends FeatureListener<FastFallModule, UpdatePlayerSpEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected UpdatePlayerSpListener(FastFallModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdatePlayerSpEvent event) {

        // packet mode
        if (feature.typeOption.getVal() == FallType.SHIFT) {

            // don't fast fall when using speed
            if (Modules.SPEED_MODULE.isEnabled()) {
                return;
            }

            // recent lagback
            if (Handlers.NCP_HANDLER.getLastRubberband() < 1000) {
                return;
            }

            // don't attempt to fast fall while jumping or sneaking
            if (mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) {
                return;
            }

            // falling down the side of a block
            if (mc.player.motionY < 0 && (feature.pground && !mc.player.onGround)) {

                // curr fall height
                double fall = feature.getHeightFromGround();

                // make sure fall is not too high
                if (fall != -1) {

                    // stop motion
                    mc.player.motionX = 0;
                    mc.player.motionZ = 0;

                    // set update iterations
                    event.setCanceled(true);
                    event.setIterations(feature.shiftTicksOption.getVal());

                    // lock movement
                    feature.lock = true;
                    feature.ticks = 0;
                }
            }
        }
    }
}
