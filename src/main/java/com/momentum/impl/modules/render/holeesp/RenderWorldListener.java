package com.momentum.impl.modules.render.holeesp;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.render.GlUtil;
import com.momentum.impl.events.forge.RenderWorldEvent;
import com.momentum.impl.handlers.hole.Hole;
import com.momentum.impl.handlers.hole.HoleType;
import com.momentum.impl.init.Handlers;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

/**
 * @author linus
 * @since 03/15/2023
 */
public class RenderWorldListener extends FeatureListener<HoleEspModule, RenderWorldEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderWorldListener(HoleEspModule feature) {
        super(feature);
    }

    /**
     * Called when an event is posted by the event bus
     *
     * @param event The event
     */
    @Override
    public void invoke(RenderWorldEvent event) {

        // holes in world
        Collection<Hole> holes = Handlers.HOLE_HANDLER.getHoles();
        for (Hole h : holes) {

            // position
            BlockPos pos = h.getPos();

            // check dist
            double dist = mc.player.getDistanceSqToCenter(pos);
            if (dist <
                    feature.rangeOption.getVal() * feature.rangeOption.getVal()) {

                // obby single
                if (h.getHoleType() == HoleType.RESISTANT) {

                    // render
                    GlUtil.box(new AxisAlignedBB(pos), 0x5bff0000);
                    GlUtil.boundingBox(new AxisAlignedBB(pos), 1.5f, 0xff0000);
                }

                // bedrock single
                else if (h.getHoleType() == HoleType.UNBREAKABLE) {

                    // render
                    GlUtil.box(new AxisAlignedBB(pos), 0x5bff0000);
                    GlUtil.boundingBox(new AxisAlignedBB(pos), 1.5f, 0xff0000);
                }

                // mixed single
                else if (h.getHoleType() == HoleType.MIXED) {

                    // render
                    GlUtil.box(new AxisAlignedBB(pos), 0x5bff0000);
                    GlUtil.boundingBox(new AxisAlignedBB(pos), 1.5f, 0xff0000);
                }
            }
        }
    }
}
