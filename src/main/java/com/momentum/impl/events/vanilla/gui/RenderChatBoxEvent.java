package com.momentum.impl.events.vanilla.gui;

import com.momentum.api.event.Event;

/**
 * Called when the chat text box is rendered
 *
 * @author linus
 * @since 02/19/2023
 */
public class RenderChatBoxEvent extends Event {

    // chat
    private String text;

    /**
     * Gets the chat box text
     *
     * @return The chat box text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the chat box text
     *
     * @param in The text
     */
    public void setText(String in) {
        text = in;
    }
}
