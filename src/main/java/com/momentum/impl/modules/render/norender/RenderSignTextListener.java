package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.tileentity.RenderSignTextEvent;

/**
 * @author linus
 * @since 02/18/2023
 */
public class RenderSignTextListener extends FeatureListener<NoRenderModule, RenderSignTextEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderSignTextListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderSignTextEvent event) {

        // cancel sign text rendering
        if (feature.signTextOption.getVal()) {

            // prevent render
            event.setCanceled(true);
        }
    }
}
