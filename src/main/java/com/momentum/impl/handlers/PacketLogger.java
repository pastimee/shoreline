package com.momentum.impl.handlers;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Logs packets
 * DO NOT INCLUDE IN RELEASE BUILDS
 *
 * @author linus
 * @since 03/07/2023
 */
public class PacketLogger {

    // times
    private long clientPacketUseEntity;

    // last 20 updates
    private final Queue<Long> times = new ArrayDeque<>(20);

    /**
     * Logs packets
     * DO NOT INCLUDE IN RELEASE BUILDS
     */
    public PacketLogger() {

        // init times
        clientPacketUseEntity = System.currentTimeMillis();

        // outbound packet logger
        Momentum.EVENT_BUS.subscribe(new Listener<OutboundPacketEvent>() {

            /**
             * Called when an event is posted by the event bus
             *
             * @param event The event
             */
            @Override
            public void invoke(OutboundPacketEvent event) {

                // attack packets
                if (event.getPacket() instanceof CPacketUseEntity) {

                    // time since
                    long since = System.currentTimeMillis() - clientPacketUseEntity;

                    // log packet
                    System.out.println("Time since last attack: " + since);
                    System.out.println("Average time between attacks: " );

                    // reset
                    times.add(since);
                    clientPacketUseEntity = System.currentTimeMillis();
                }
            }
        });

        // inbound packet logger
        Momentum.EVENT_BUS.subscribe(new Listener<InboundPacketEvent>() {

            /**
             * Called when an event is posted by the event bus
             *
             * @param event The event
             */
            @Override
            public void invoke(InboundPacketEvent event) {

            }
        });
    }

    /**
     * Gets the average of the times
     *
     * @param times The times to average
     * @return The average of the times
     */
    private float average(Queue<Long> times) {

        // average time
        long avg = 0;

        // add times to average
        for (long t : times) {

            // clamp time
            avg += MathHelper.clamp(t, 0, 20);
        }

        // make sure times size isn't 0
        if (!times.isEmpty()) {

            // divide by total tomes
            avg /= times.size();
        }

        // avg
        return avg;
    }
}
