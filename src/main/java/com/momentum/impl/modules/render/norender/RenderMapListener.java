package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.gui.RenderMapEvent;

/**
 * @author linus
 * @since 03/01/2023
 */
public class RenderMapListener extends FeatureListener<NoRenderModule, RenderMapEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderMapListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderMapEvent event) {

        // prevent maps from rendering
        if (feature.mapOption.getVal()) {

            // cancel map item rendering
            event.setCanceled(true);
        }
    }
}
