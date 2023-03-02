package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.block.SlimeEvent;

/**
 * @author linus
 * @since 02/13/2023
 */
public class SlimeListener extends FeatureListener<NoSlowModule, SlimeEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected SlimeListener(NoSlowModule feature) {
        super(feature);
    }

    @Override
    public void invoke(SlimeEvent event) {

        // prevent slime slowdown
        if (feature.slimeOption.getVal()) {

            // cancel player from walking on slime
            event.setCanceled(true);
        }
    }
}
