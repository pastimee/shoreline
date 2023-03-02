package com.momentum.impl.events.forge;

import com.momentum.api.event.Event;
import net.minecraft.util.MovementInput;

/**
 * Called when the player input is updated
 *
 * @author linus
 * @since 02/23/2023
 */
public class InputUpdateEvent extends Event {

    // player movement input
    private final MovementInput movementInput;

    /**
     * Called when the player input is updated
     *
     * @param movementInput The player movement input info
     */
    public InputUpdateEvent(MovementInput movementInput) {
        this.movementInput = movementInput;
    }

    /**
     * Gets the current player's movement input
     *
     * @return The current player's movement input
     */
    public MovementInput getMovementInput() {
        return movementInput;
    }
}
