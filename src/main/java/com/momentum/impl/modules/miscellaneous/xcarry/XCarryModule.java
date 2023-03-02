package com.momentum.impl.modules.miscellaneous.xcarry;

import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 02/26/2023
 */
public class XCarryModule extends Module {

    // listeners
    public final OutboundPacketListener outboundPacketListener =
            new OutboundPacketListener(this);

    public XCarryModule() {
        super("XCarry", new String[] {"ExtraSlots", "SecretClose"}, "Prevents server knowing about inventory actions", ModuleCategory.MISCELLANEOUS);

        // options
        associate(
                bind,
                drawn
        );

        // listeners
        associate(
            outboundPacketListener
        );
    }
}
