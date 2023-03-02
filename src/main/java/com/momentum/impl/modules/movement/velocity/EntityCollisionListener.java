package com.momentum.impl.modules.movement.velocity;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.entity.player.EntityCollisionEvent;

/**
 * @author linus
 * @since 01/23/2023
 */
public class EntityCollisionListener extends FeatureListener<VelocityModule, EntityCollisionEvent> {
    protected EntityCollisionListener(VelocityModule module) {
        super(module);
    }

    @Override
    public void invoke(EntityCollisionEvent event) {

        // cancel velocity from entities
        if (feature.entitiesOption.getVal()) {

            // cancel event
            event.setCanceled(true);
        }
    }
}
