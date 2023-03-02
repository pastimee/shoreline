package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.entity.RenderEntityItemEvent;

/**
 * @author linus
 * @since 03/01/2023
 */
public class RenderEntityItemListener extends FeatureListener<NoRenderModule, RenderEntityItemEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderEntityItemListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderEntityItemEvent event) {

        // hide dropped items
        if (feature.noItemsOption.getVal() == NoItems.HIDE) {

            // prevent dropped item entity rendering
            event.setCanceled(true);
        }
    }
}
