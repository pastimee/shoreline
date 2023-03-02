package com.momentum.impl.events.forge.event;

import com.momentum.api.event.Event;

/**
 * Called when the player sends a message in chat
 *
 * @author linus
 * @since 02/19/2023
 */
public class ClientSendMessageEvent extends Event {

    // message sent in chat
    private final String message;

    /**
     * Instantiated when the player sends a message in chat
     *
     * @param message The message sent in chat
     */
    public ClientSendMessageEvent(String message) {
        this.message = message;
    }

    /**
     * Gets the message sent in chat
     *
     * @return The message sent in chat
     */
    public String getMessage() {
        return message;
    }
}
