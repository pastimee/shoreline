package com.momentum.impl.modules.miscellaneous.timer;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import com.momentum.asm.mixins.vanilla.accessors.ITimer;
import com.momentum.impl.events.client.OptionUpdateEvent;

/**
 * @author linus
 * @since 02/13/2023
 */
public class OptionUpdateListener extends FeatureListener<TimerModule, OptionUpdateEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected OptionUpdateListener(TimerModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OptionUpdateEvent event) {

        // ticks option is changes
        if (event.getOption() == feature.ticksOption) {

            // check if currently syncing
            if (!feature.tpsSyncOption.getVal()) {

                // update tick length
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50 / feature.ticksOption.getVal());
                feature.pticksOption = feature.ticksOption.getVal();
            }
        }
    }
}
