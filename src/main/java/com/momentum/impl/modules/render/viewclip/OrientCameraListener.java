package com.momentum.impl.modules.render.viewclip;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.OrientCameraEvent;

/**
 * @author linus
 * @since 03/08/2023
 */
public class OrientCameraListener extends FeatureListener<ViewClipModule, OrientCameraEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected OrientCameraListener(ViewClipModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OrientCameraEvent event) {

        // override the vanilla camera clip distance
        event.setCanceled(true);
        event.setDistance(feature.distanceOption.getVal());
    }
}
