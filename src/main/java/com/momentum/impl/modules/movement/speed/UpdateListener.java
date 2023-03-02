package com.momentum.impl.modules.movement.speed;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;

/**
 * @author linus
 * @since 02/13/2023
 */
public class UpdateListener extends FeatureListener<SpeedModule, UpdateEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected UpdateListener(SpeedModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdateEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // diffs
        double xdiff = mc.player.posX - mc.player.prevPosX;
        double zdiff = mc.player.posZ - mc.player.prevPosZ;

        // update travelled dist
        feature.distance = Math.sqrt(xdiff * xdiff + zdiff * zdiff);
    }
}
