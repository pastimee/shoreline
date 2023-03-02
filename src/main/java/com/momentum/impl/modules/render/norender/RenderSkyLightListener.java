package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.world.RenderSkyLightEvent;

/**
 * @author linus
 * @since 03/01/2023
 */
public class RenderSkyLightListener extends FeatureListener<NoRenderModule, RenderSkyLightEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderSkyLightListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderSkyLightEvent event) {

        // prevent skylight from rendering
        // crash exploit
        if (feature.skylightOption.getVal()) {

            // cancel render
            event.setCanceled(true);
        }
    }
}
