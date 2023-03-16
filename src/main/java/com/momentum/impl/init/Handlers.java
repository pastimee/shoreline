package com.momentum.impl.init;

import com.momentum.Momentum;
import com.momentum.api.handler.Handler;
import com.momentum.impl.handlers.*;
import com.momentum.impl.handlers.hole.HoleHandler;

/**
 * @author linus
 * @since 03/01/2023
 */
public class Handlers {

    // handlers
    public static final MacroHandler MACRO_HANDLER;
    public static final CommandHandler COMMAND_HANDLER;
    public static final NcpHandler NCP_HANDLER;
    public static final TickHandler TICK_HANDLER;
    public static final PopHandler POP_HANDLER;
    public static final RotationHandler ROTATION_HANDLER;
    public static final HoleHandler HOLE_HANDLER;

    /**
     * Gets the registered handler
     *
     * @param label The handler label
     * @return The registered handler
     */
    private static Handler getRegisteredHandler(String label) {

        // module from registry
        return Momentum.HANDLER_REGISTRY.lookup(label);
    }

    // init handlers
    static {

        // handlers
        MACRO_HANDLER = (MacroHandler) getRegisteredHandler("macro_handler");
        COMMAND_HANDLER = (CommandHandler) getRegisteredHandler("command_handler");
        NCP_HANDLER = (NcpHandler) getRegisteredHandler("ncp_handler");
        TICK_HANDLER = (TickHandler) getRegisteredHandler("tick_handler");
        POP_HANDLER = (PopHandler) getRegisteredHandler("pop_handler");
        ROTATION_HANDLER = (RotationHandler) getRegisteredHandler("rotation_handler");
        HOLE_HANDLER = (HoleHandler) getRegisteredHandler("hole_handler");
    }
}
