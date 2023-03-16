package com.momentum.impl.modules.world.speedmine;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.render.GlUtil;
import com.momentum.impl.events.forge.RenderWorldEvent;
import com.momentum.impl.init.Modules;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author linus
 * @since 03/09/2023
 */
public class RenderWorldListener extends FeatureListener<SpeedmineModule, RenderWorldEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderWorldListener(SpeedmineModule feature) {
        super(feature);
    }
    
    @Override
    public void invoke(RenderWorldEvent event) {

        // no reason to render in creative mode, blocks break instantly
        if (!mc.player.capabilities.isCreativeMode) {

            // check if curr mine exists
            if (feature.mine != null
                    && !mc.world.isAirBlock(feature.mine)) {
                
                // block state
                IBlockState state = mc.world.getBlockState(feature.mine);

                // box of the mine
                AxisAlignedBB mine = state.getSelectedBoundingBox(mc.world, feature.mine);

                // center of the box
                Vec3d center = mine.getCenter();

                // shrink to center
                AxisAlignedBB shrink = new AxisAlignedBB(center.x, center.y, center.z, center.x, center.y, center.z);

                // grow box by damage
                float dmg = MathHelper.clamp(feature.damage, 0.0f, 1.0f);
                AxisAlignedBB grow = shrink.grow(
                        (mine.minX - mine.maxX) * 0.5f * dmg,
                        (mine.minY - mine.maxY) * 0.5f * dmg,
                        (mine.minZ - mine.maxZ) * 0.5f * dmg
                );

                // render mine
                GlUtil.box(grow, dmg > 0.95f ? Modules.COLOR_MODULE.getColorInt(120) : 0x77ff0000);
                GlUtil.boundingBox(grow, 1.5f, dmg > 0.95f ? Modules.COLOR_MODULE.getColorInt() : 0xff0000);
            }
        }
    }
}
