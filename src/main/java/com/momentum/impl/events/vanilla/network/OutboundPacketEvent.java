package com.momentum.impl.events.vanilla.network;

import com.momentum.api.event.Event;
import net.minecraft.network.Packet;

/**
 * Called when the client sends the server a packet
 *
 * @author linus
 * @since 01/09/2023
 */
public class OutboundPacketEvent extends Event {

    // sent packet
    private final Packet<?> packet;

    /**
     * Initializes the sent packet field
     *
     * @param packet The sent packet
     */
    public OutboundPacketEvent(Packet<?> packet) {
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
