package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.api.event.EventStage;
import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.entity.UpdateWalkingPlayerEvent;

/**
 * @author linus
 * @since 03/14/2023
 */
public class UpdateWalkingPlayerListener extends FeatureListener<AutoCrystalModule, UpdateWalkingPlayerEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected UpdateWalkingPlayerListener(AutoCrystalModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdateWalkingPlayerEvent event) {

        // find target before rotation
        if (event.getStage() == EventStage.POST &&
                feature.timingOption.getVal() == Timing.SEQUENTIAL) {

            // submit consumers
            if (!feature.crystals.isEmpty()) {

                // attack
                feature.crystalC.consume();
            }

            // submit consumers
            if (!feature.placements.isEmpty()) {

                // check should place
                if (feature.placeOption.getVal()) {

                    // place
                    feature.placementC.consume();
                }
            }
        }
    }
}
