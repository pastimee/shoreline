package com.momentum.impl.modules.miscellaneous.autorespawn;

import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.util.time.Timer;

/**
 * @author linus
 * @since 03/09/2023
 */
public class AutoRespawnModule extends Module {

    // listeners
    public final TickListener tickListener =
            new TickListener(this);
    public final DisplayGuiListener displayGuiListener =
            new DisplayGuiListener(this);


    // respawn states
    protected boolean respawn;
    protected Timer delay =
            new Timer();

    public AutoRespawnModule() {
        super("AutoRespawn", "Automatically respawns", ModuleCategory.MISCELLANEOUS);

        // options
        associate(
                bind,
                drawn
        );

        // listeners
        associate(
                tickListener,
                displayGuiListener
        );
    }
}
