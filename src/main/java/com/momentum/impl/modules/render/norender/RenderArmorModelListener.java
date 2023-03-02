package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.entity.layers.RenderArmorModelEvent;

/**
 * @author linus
 * @since 02/14/2023
 */
public class RenderArmorModelListener extends FeatureListener<NoRenderModule, RenderArmorModelEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderArmorModelListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderArmorModelEvent event) {

        // cancel armor rendering
        if (feature.armorOption.getVal()) {

            // armor model visibility = false
            event.setCanceled(true);
        }
    }
}
