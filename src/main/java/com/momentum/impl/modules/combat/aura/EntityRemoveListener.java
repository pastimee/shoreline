package com.momentum.impl.modules.combat.aura;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.world.EntityRemoveEvent;

/**
 * @author linus
 * @since 03/07/2023
 */
public class EntityRemoveListener extends FeatureListener<AuraModule, EntityRemoveEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected EntityRemoveListener(AuraModule feature) {
        super(feature);
    }

    @Override
    public void invoke(EntityRemoveEvent event) {

        // player has died
        if (event.getEntity() == mc.player) {

            // disable on death
            if (feature.respawnDisableOption.getVal()) {

                // disable before player respawns
                feature.disable();
            }
        }
    }
}
