package com.momentum.impl.registers;

import com.momentum.api.handler.Handler;
import com.momentum.api.registry.Registry;
import com.momentum.impl.handlers.*;
import com.momentum.impl.handlers.hole.HoleHandler;

/**
 * Registry of handlers
 *
 * @author linus
 * @since 03/01/2023
 */
public class HandlerRegistry extends Registry<Handler> {

    /**
     * Registry of handlers
     */
    public HandlerRegistry() {

        // handlers
        register(
                new MacroHandler(),
                new CommandHandler(),
                new NcpHandler(),
                new TickHandler(),
                new PopHandler(),
                new RotationHandler(),
                new HoleHandler()
        );
    }
}
