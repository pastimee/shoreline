package com.momentum.impl.modules.movement.speed;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

/**
 * @author linus
 * @since 02/19/2023
 */
public class InboundPacketListener extends FeatureListener<SpeedModule, InboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected InboundPacketListener(SpeedModule feature) {
        super(feature);
    }

    @Override
    public void invoke(InboundPacketEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // lag packet
        if (event.getPacket() instanceof SPacketPlayerPosLook) {

            // reset
            feature.speed = 0;
            feature.distance = 0;
            feature.accelerate = false;
            feature.timeout = 0;
            feature.strafeStage = 4;
        }
    }
}
