package com.momentum.impl.events.vanilla;

import com.momentum.api.event.Event;
import net.minecraft.client.gui.GuiScreen;

/**
 * Called when a gui is displayed
 *
 * @author linus
 * @since 03/10/2023
 */
public class DisplayGuiEvent extends Event {

    // displayed gui
    private final GuiScreen guiScreen;

    /**
     * Initialize displayed gui screen
     *
     * @param guiScreen The displayed gui screen
     */
    public DisplayGuiEvent(GuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }

    /**
     * Gets the displayed gui screen
     *
     * @return The displayed gui screen
     */
    public GuiScreen getGuiScreen() {
        return guiScreen;
    }
}
