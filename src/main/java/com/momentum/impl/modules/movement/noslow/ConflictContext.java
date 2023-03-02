package com.momentum.impl.modules.movement.noslow;

import net.minecraftforge.client.settings.IKeyConflictContext;

/**
 * Dummy key conflict context
 *
 * @author linus
 * @since 02/13/2023
 */
public enum ConflictContext implements IKeyConflictContext {

    /**
     * Dummy key conflict context that allows keys to be pressed in GUI screens
     */
    DEFAULT_CONTEXT {

        /**
         * Checks whether or not a conflict is currently active
         *
         * @return Whether or not this conflict is currently active
         */
        @Override
        public boolean isActive() {
            return false;
        }

        /**
         * Sets the conflict with another context
         *
         * @param other The conflict with another context
         * @return Whether it conflicts with another context
         */
        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return false;
        }
    }
}
