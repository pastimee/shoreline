package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.world.RenderExplosionEvent;

/**
 * @author linus
 * @since 02/15/2023
 */
public class RenderExplosionListener extends FeatureListener<NoRenderModule, RenderExplosionEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderExplosionListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderExplosionEvent event) {

        // cancel explosion rendering
        if (feature.explosionsOption.getVal()) {

            // remove particles
            event.setCanceled(true);
        }
    }
}
