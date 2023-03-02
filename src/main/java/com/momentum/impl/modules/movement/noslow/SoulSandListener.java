package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.block.SoulSandEvent;

/**
 * @author linus
 * @since 02/13/2023
 */
public class SoulSandListener extends FeatureListener<NoSlowModule, SoulSandEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected SoulSandListener(NoSlowModule feature) {
        super(feature);
    }

    @Override
    public void invoke(SoulSandEvent event) {

        // prevent slowdown from soul sand
        if (feature.soulSandOption.getVal()) {

            // cancel soul sand collision
            event.setCanceled(true);
        }
    }
}
