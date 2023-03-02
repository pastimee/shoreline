package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.gui.RenderPumpkinOverlayEvent;

/**
 * @author linus
 * @since 02/14/2023
 */
public class RenderPumpkinOverlayListener extends FeatureListener<NoRenderModule, RenderPumpkinOverlayEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderPumpkinOverlayListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderPumpkinOverlayEvent event) {

        // cancel the pumpkin overlay when wearing pumpkins
        if (feature.pumpkinOverlayOption.getVal()) {

            // prevent pumpkin overlay from rendering
            event.setCanceled(true);
        }
    }
}
