package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.RenderHudOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;

/**
 * @author linus
 * @since 02/14/2023
 */
public class RenderHudOverlayListener extends FeatureListener<NoRenderModule, RenderHudOverlayEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderHudOverlayListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderHudOverlayEvent event) {

        // check overlay
        if (event.getOverlayType() == OverlayType.FIRE && feature.fireOption.getVal()
                || event.getOverlayType() == OverlayType.BLOCK && feature.blockOverlayOption.getVal()
                || event.getOverlayType() == OverlayType.WATER && feature.waterOverlayOption.getVal()) {

            // cancel overlay
            event.setCanceled(true);
        }
    }
}
