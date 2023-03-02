package com.momentum.impl.init;

import com.momentum.Momentum;
import com.momentum.api.handler.Handler;
import com.momentum.impl.handlers.CommandHandler;
import com.momentum.impl.handlers.MacroHandler;
import com.momentum.impl.handlers.NcpHandler;
import com.momentum.impl.handlers.PopHandler;
import com.momentum.impl.handlers.TickHandler;

/**
 * @author linus
 * @since 03/01/2023
 */
public class Handlers {

    // handlers
    public static final MacroHandler MODULE_HANDLER;
    public static final CommandHandler COMMAND_HANDLER;
    public static final NcpHandler NCP_HANDLER;
    public static final TickHandler TICK_HANDLER;
    public static final PopHandler POP_HANDLER;

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
        MODULE_HANDLER = (MacroHandler) getRegisteredHandler("module_handler");
        COMMAND_HANDLER = (CommandHandler) getRegisteredHandler("command_handler");
        NCP_HANDLER = (NcpHandler) getRegisteredHandler("ncp_handler");
        TICK_HANDLER = (TickHandler) getRegisteredHandler("tick_handler");
        POP_HANDLER = (PopHandler) getRegisteredHandler("pop_handler");

        // subscribe all
        MODULE_HANDLER.subscribe();
        COMMAND_HANDLER.subscribe();
        NCP_HANDLER.subscribe();
        TICK_HANDLER.subscribe();
        POP_HANDLER.subscribe();
    }
}
