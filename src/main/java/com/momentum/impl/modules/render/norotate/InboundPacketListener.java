package com.momentum.impl.modules.render.norotate;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.asm.mixins.vanilla.accessors.ISPacketPlayerPosLook;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

/**
 * @author linus
 * @since 03/13/2023
 */
public class InboundPacketListener extends FeatureListener<NoRotateModule, InboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected InboundPacketListener(NoRotateModule feature) {
        super(feature);
    }

    /**
     * Called when an event is posted by the event bus
     *
     * @param event The event
     */
    @Override
    public void invoke(InboundPacketEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // server rotations updated
        if (event.getPacket() instanceof SPacketPlayerPosLook) {

            // packet from event
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();

            // override the rotations
            ((ISPacketPlayerPosLook) packet).setYaw(mc.player.rotationYaw);
            ((ISPacketPlayerPosLook) packet).setPitch(mc.player.rotationPitch);
        }
    }
}
