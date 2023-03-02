package com.momentum.impl.modules.combat.autobowrelease;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 02/20/2023
 */
public class AutoBowReleaseModule extends Module {

    // release options
    public final Option<Integer> ticksOption =
            new Option<>("Ticks", "Ticks to draw the bow", 3, 3, 20);
    public final Option<Boolean> tpsSyncOption =
            new Option<>("TpsSync", "Syncs release ticks to server ticks", false);

    // listeners
    public final TickListener tickListener =
            new TickListener(this);

    public AutoBowReleaseModule() {
        super("AutoBowRelease", new String[] {"FastBow", "BowSpam"}, "Automatically releases bows", ModuleCategory.COMBAT);

        // options
        associate(
                ticksOption,
                tpsSyncOption,
                bind,
                drawn
        );

        // listeners
        associate(
                tickListener
        );
    }
}
