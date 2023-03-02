package com.momentum.impl.events.vanilla.network;

import com.momentum.api.event.Event;
import net.minecraft.network.Packet;

/**
 * Called when the server sends the client a packet
 *
 * @author linus
 * @since 01/09/2023
 */
public class InboundPacketEvent extends Event {

    // received packet
    private final Packet<?> packet;

    /**
     * Initializes the received packet field
     *
     * @param packet The received packet
     */
    public InboundPacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    /**
     * Gets the packet
     *
     * @return The packet
     */
    public Packet<?> getPacket() {
        return packet;
    }
}
