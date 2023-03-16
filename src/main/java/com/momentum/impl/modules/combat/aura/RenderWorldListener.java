package com.momentum.impl.modules.combat.aura;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.render.GlUtil;
import com.momentum.impl.events.forge.RenderWorldEvent;
import com.momentum.impl.init.Modules;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author linus
 * @since 03/07/2023
 */
public class RenderWorldListener extends FeatureListener<AuraModule, RenderWorldEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderWorldListener(AuraModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderWorldEvent event) {

        // render overlay on target
        if (feature.renderOption.getVal()) {

            // check if target exists
            if (feature.target != null) {

                // target bounding box
                AxisAlignedBB bb = feature.target.getEntityBoundingBox();

                // render
                GlUtil.box(bb, Modules.COLOR_MODULE.getColorInt(40));
                // GlUtil.boundingBox(bb, 1.5f, Modules.COLOR_MODULE.getColorInt());
            }
        }
    }
}
