package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.gui.RenderBossOverlayEvent;

/**
 * @author linus
 * @since 02/14/2023
 */
public class RenderBossOverlayListener extends FeatureListener<NoRenderModule, RenderBossOverlayEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderBossOverlayListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderBossOverlayEvent event) {

        // remove boss overlay from hud
        if (feature.bossOverlayOption.getVal()) {

            // prevent boss overlays from rendering
            event.setCanceled(true);
        }
    }
}
