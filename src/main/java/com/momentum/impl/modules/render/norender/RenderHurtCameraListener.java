package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.RenderHurtCameraEvent;

/**
 * @author linus
 * @since 02/14/2023
 */
public class RenderHurtCameraListener extends FeatureListener<NoRenderModule, RenderHurtCameraEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderHurtCameraListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderHurtCameraEvent event) {

        // remove hurt camera effect
        if (feature.hurtCameraOption.getVal()) {

            // prevent from applying
            event.setCanceled(true);
        }
    }
}
