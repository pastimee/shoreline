package com.momentum.impl.modules.render.skybox;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.world.RenderSkyColorEvent;
import com.momentum.impl.init.Modules;

/**
 * @author linus
 * @since 03/09/2023
 */
public class RenderSkyColorListener extends FeatureListener<SkyboxModule, RenderSkyColorEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderSkyColorListener(SkyboxModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderSkyColorEvent event) {

        // set sky color
        event.setCanceled(true);
        event.setColor(Modules.COLOR_MODULE.getColorInt());
    }
}
