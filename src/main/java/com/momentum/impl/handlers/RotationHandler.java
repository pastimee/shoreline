package com.momentum.impl.handlers;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.api.util.rotation.Rotation;
import com.momentum.asm.mixins.vanilla.accessors.ICPacketPlayer;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * Handles serverside rotations
 *
 * @author linus
 * @since 03/06/2023
 */
public class RotationHandler extends Handler {

    // serverside rotation
    private Rotation rotation =
            new Rotation(0.0f, 0.0f);

    /**
     * Handles serverside rotations
     */
    public RotationHandler() {

        // rotation tracker
        Momentum.EVENT_BUS.subscribe(new Listener<OutboundPacketEvent>() {

            /**
             * Called when an event is posted by the event bus
             *
             * @param event The event
             */
            @Override
            public void invoke(OutboundPacketEvent event) {

                // player update packet
                if (event.getPacket() instanceof CPacketPlayer) {

                    // packet from event
                    CPacketPlayer packet = (CPacketPlayer) event.getPacket();

                    // rotation packet
                    if (((ICPacketPlayer) packet).isRotating()) {

                        // rotation vals
                        float yaw = packet.getYaw(mc.player.rotationYaw);
                        float pitch = packet.getPitch(mc.player.rotationPitch);

                        // update rotation
                        rotation = new Rotation(yaw, pitch);
                    }
                }
            }
        });
    }

    /**
     * Gets the serverside rotations
     *
     * @return The serverside rotations
     */
    public Rotation getRotation() {
        return rotation;
    }
}
