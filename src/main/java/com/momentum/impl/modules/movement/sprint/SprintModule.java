package com.momentum.impl.modules.movement.sprint;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.util.render.Formatter;

/**
 * @author linus
 * @since 02/11/2023
 */
public class SprintModule extends Module {

    // sprint options
    public final Option<SprintMode> modeOption =
            new Option<>("Mode", "Mode for sprinting", SprintMode.RAGE);

    // listeners
    public final TickListener tickListener =
            new TickListener(this);
    public final SprintingListener sprintingListener =
            new SprintingListener(this);

    /**
     *
     */
    public SprintModule() {
        super("Sprint", new String[] {"AutoSprint"}, "Automatically sprints", ModuleCategory.MOVEMENT);

        // options
        associate(
                modeOption,
                bind,
                drawn
        );

        // listeners
        associate(
                tickListener,
                sprintingListener
        );
    }

    @Override
    public String getData() {

        // array list data is mode
        return Formatter.formatEnum(modeOption.getVal());
    }
}
