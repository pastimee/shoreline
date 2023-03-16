package com.momentum.impl.modules.render.norotate;

import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 03/13/2023
 */
public class NoRotateModule extends Module {

    // listeners
    public final InboundPacketListener inboundPacketListener =
            new InboundPacketListener(this);

    public NoRotateModule() {
        super("NoRotate", "Cancels server rotations", ModuleCategory.RENDER);

        // options
        associate(
                bind,
                drawn
        );

        // listeners
        associate(
                inboundPacketListener
        );
    }
}
