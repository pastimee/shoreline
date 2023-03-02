package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.RenderItemActivationEvent;
import net.minecraft.init.Items;

/**
 * @author linus
 * @since 02/14/2023
 */
public class RenderItemActivationListener extends FeatureListener<NoRenderModule, RenderItemActivationEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderItemActivationListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderItemActivationEvent event) {

        // cancel totem pop animations
        if (feature.totemAnimationOption.getVal()) {

            // activated item is totem
            if (event.getItem() != null && event.getItem() == Items.TOTEM_OF_UNDYING) {

                // cancel anim
                event.setCanceled(true);
            }
        }
    }
}
