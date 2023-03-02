package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.RenderBlindnessEvent;

/**
 * @author linus
 * @since 02/14/2023
 */
public class RenderBlindnessListener extends FeatureListener<NoRenderModule, RenderBlindnessEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderBlindnessListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderBlindnessEvent event) {

        // cancel blindness effect
        if (feature.blindnessOption.getVal()) {

            // prevent blindness fog from rendering
            event.setCanceled(true);
        }
    }
}
