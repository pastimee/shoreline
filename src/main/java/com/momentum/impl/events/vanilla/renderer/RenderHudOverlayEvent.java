package com.momentum.impl.events.vanilla.renderer;

import com.momentum.api.event.Event;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;

/**
 * Called when hud overlays are rendered
 *
 * @author linus
 * @since 02/14/2023
 */
public class RenderHudOverlayEvent extends Event {

    // overlay type
    private final OverlayType type;

    /**
     * Called when hud overlays are rendered
     *
     * @param type The overlay type
     */
    public RenderHudOverlayEvent(OverlayType type) {
        this.type = type;
    }

    /**
     * Gets the overlay type of the render
     *
     * @return tTe overlay type of the render
     */
    public OverlayType getOverlayType() {
        return type;
    }
}
