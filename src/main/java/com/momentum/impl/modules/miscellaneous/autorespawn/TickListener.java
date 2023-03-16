package com.momentum.impl.modules.miscellaneous.autorespawn;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;

/**
 * @author linus
 * @since 03/10/2023
 */
public class TickListener extends FeatureListener<AutoRespawnModule, TickEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(AutoRespawnModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // check if the player is dead
        if (mc.player.isDead && feature.respawn) {

            // passed our respawn delay
            if (feature.delay.passed(500)) {

                // respawn player
                mc.player.respawnPlayer();
                feature.respawn = false;
            }
        }
    }
}
