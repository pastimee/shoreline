package com.momentum.impl.modules.movement.velocity;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.entity.PushOutOfBlocksEvent;

/**
 * @author linus
 * @since 01/23/2023
 */
public class PushOutOfBlocksListener extends FeatureListener<VelocityModule, PushOutOfBlocksEvent> {
    protected PushOutOfBlocksListener(VelocityModule module) {
        super(module);
    }

    @Override
    public void invoke(PushOutOfBlocksEvent event) {

        // null check
        if (mc.player == null || mc.world == null) {
            return;
        }

        // no push blocks
        if (feature.blocksOption.getVal()) {

            // cancel event
            event.setCanceled(true);
        }
    }
}
