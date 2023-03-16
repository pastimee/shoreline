package com.momentum.impl.modules.combat.aura;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.network.DisconnectEvent;

/**
 * @author linus
 * @since 03/07/2023
 */
public class DisconnectListener extends FeatureListener<AuraModule, DisconnectEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected DisconnectListener(AuraModule feature) {
        super(feature);
    }

    @Override
    public void invoke(DisconnectEvent event) {

        // disable on disconnect
        feature.disable();
    }
}
