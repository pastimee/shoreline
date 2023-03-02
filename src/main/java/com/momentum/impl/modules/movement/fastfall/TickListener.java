package com.momentum.impl.modules.movement.fastfall;

import com.momentum.Momentum;
import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import com.momentum.impl.init.Handlers;
import com.momentum.impl.init.Modules;

/**
 * @author linus
 * @since 02/21/2023
 */
public class TickListener extends FeatureListener<FastFallModule, TickEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(FastFallModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // update on ground state
        feature.pground = mc.player.onGround;

        // reverse step
        if (feature.typeOption.getVal() == FallType.STEP) {

            // don't fast fall when using speed
            if (Modules.SPEED_MODULE.isEnabled()) {
                return;
            }

            // recent lagback
            if (Handlers.NCP_HANDLER.getLastRubberband() < 1000) {
                return;
            }

            // don't attempt to fast fall while jumping or sneaking
            if (mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) {
                return;
            }

            // only fast fall if the player is on the ground
            if (mc.player.onGround) {

                // curr fall height
                double fall = feature.getHeightFromGround();

                // make sure fall is not too high
                if (fall != -1) {

                    // adjust player velocity
                    mc.player.motionY = -3.0f;
                    // mc.player.connection.sendPacket(new CPacketPlayer(false));
                }
            }
        }
    }
}
