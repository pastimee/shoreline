package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.tileentity.RenderBeaconBeamEvent;

/**
 * @author linus
 * @since 02/18/2023
 */
public class RenderBeaconBeamListener extends FeatureListener<NoRenderModule, RenderBeaconBeamEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderBeaconBeamListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderBeaconBeamEvent event) {

        // prevent beacon beam from rendering
        if (feature.beaconBeamOption.getVal()) {

            // cancel event
            event.setCanceled(true);
        }
    }
}
