package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.thread.IThreaded;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;

/**
 * @author linus
 * @since 03/14/2023
 */
public class TickListener extends FeatureListener<AutoCrystalModule, TickEvent>
        implements IThreaded {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(AutoCrystalModule feature) {
        super(feature);
    }

    /**
     * Called when an event is posted by the event bus
     *
     * @param event The event
     */
    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // submit producers
        feature.crystalP.produce();
        feature.placementP.produce();

        // attack before packets
        if (feature.timingOption.getVal() ==
                Timing.VANILLA) {

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
