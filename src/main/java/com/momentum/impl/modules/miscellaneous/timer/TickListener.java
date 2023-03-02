package com.momentum.impl.modules.miscellaneous.timer;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import com.momentum.asm.mixins.vanilla.accessors.ITimer;
import com.momentum.impl.events.vanilla.TickEvent;
import com.momentum.impl.init.Handlers;

/**
 * @author linus
 * @since 02/26/2023
 */
public class TickListener extends FeatureListener<TimerModule, TickEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(TimerModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // sync to server tps
        // the concept behind this being that client actions are more synchronized with server actions
        if (feature.tpsSyncOption.getVal()) {

            // sync tick speed
            float sync = Math.max(Handlers.TICK_HANDLER.getTps() / 20.0f, 0.01f);
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f / sync);
        }
    }
}
