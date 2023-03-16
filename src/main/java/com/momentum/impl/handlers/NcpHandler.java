package com.momentum.impl.handlers;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

/**
 * Manages NCP anticheat measures
 *
 * @author linus
 * @since 02/21/2023
 */
public class NcpHandler extends Handler {

    // last rubberband time
    private long rubberband;

    /**
     * Manages NCP anticheat measures
     */
    public NcpHandler() {

        // rubberband impl
        Momentum.EVENT_BUS.subscribe(new Listener<InboundPacketEvent>() {

            @Override
            public void invoke(InboundPacketEvent event) {

                // packet for rubberbands
                if (event.getPacket() instanceof SPacketPlayerPosLook) {

                    // mark last rubberband time
                    rubberband = System.currentTimeMillis();
                }
            }
        });
    }

    /**
     * Gets the time since the last rubberband
     *
     * @return The time since the last rubberband
     */
    public long getLastRubberband() {

        // calc time since last rubberband
        return System.currentTimeMillis() - rubberband;
    }
}
