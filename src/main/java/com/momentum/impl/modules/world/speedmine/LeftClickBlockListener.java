package com.momentum.impl.modules.world.speedmine;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.block.ResistantBlocks;
import com.momentum.impl.events.forge.LeftClickBlockEvent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author linus
 * @since 03/09/2023
 */
public class LeftClickBlockListener extends FeatureListener<SpeedmineModule, LeftClickBlockEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected LeftClickBlockListener(SpeedmineModule feature) {
        super(feature);
    }

    @Override
    public void invoke(LeftClickBlockEvent event) {

        // no point applying speedmine in creative
        if (!mc.player.capabilities.isCreativeMode) {

            // left click info
            BlockPos pos = event.getPos();
            EnumFacing face = event.getFace();

            // left click block
            IBlockState state = mc.world.getBlockState(pos);

            // check if block is breakable
            if (ResistantBlocks.isBreakable(
                    state.getBlock())) {

                // update mine info
                feature.mine = pos;
                feature.face = face;
                feature.state = state;
                feature.damage = 0.0f;
                feature.breaks = 0;

                // packet mine the position
                mc.player.connection.sendPacket(new CPacketPlayerDigging(
                        Action.START_DESTROY_BLOCK, pos, face));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(
                        Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.UP));
            }
        }
    }
}
