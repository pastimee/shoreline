package com.momentum.impl.modules.render.nametags;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.entity.RenderNametagEvent;

/**
 * @author linus
 * @since 03/01/2023
 */
public class RenderNametagListener extends FeatureListener<NametagsModule, RenderNametagEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderNametagListener(NametagsModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderNametagEvent event) {

        // cancel vanilla nametags from rendering
        event.setCanceled(true);
    }
}
