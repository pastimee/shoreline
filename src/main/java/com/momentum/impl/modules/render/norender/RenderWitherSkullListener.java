package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.entity.RenderWitherSkullEvent;

/**
 * @author linus
 * @since 02/18/2023
 */
public class RenderWitherSkullListener extends FeatureListener<NoRenderModule, RenderWitherSkullEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderWitherSkullListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderWitherSkullEvent event) {

        // prevent flying wither skulls from rendering
        if (feature.witherSkullOption.getVal()) {

            // cancel entity render
            event.setCanceled(true);
        }
    }
}
