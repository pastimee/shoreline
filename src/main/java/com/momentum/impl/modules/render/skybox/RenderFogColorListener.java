package com.momentum.impl.modules.render.skybox;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.RenderFogColorEvent;
import com.momentum.impl.init.Modules;

/**
 * @author linus
 * @since 03/09/2023
 */
public class RenderFogColorListener extends FeatureListener<SkyboxModule, RenderFogColorEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderFogColorListener(SkyboxModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderFogColorEvent event) {

        // apply sky color to fog
        if (feature.fogOption.getVal()) {

            // override color
            event.setCanceled(true);
            event.setColor(Modules.COLOR_MODULE.getColorInt());
        }
    }
}
