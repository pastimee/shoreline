package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.particle.RenderBarrierEvent;

/**
 * @author linus
 * @since 02/19/2023
 */
public class RenderBarrierListener extends FeatureListener<NoRenderModule, RenderBarrierEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderBarrierListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderBarrierEvent event) {

        // prevent barriers from rendering
        if (feature.barrierOption.getVal()) {

            // cancel particle rendering
            event.setCanceled(true);
        }
    }
}
