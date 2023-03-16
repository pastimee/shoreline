package com.momentum.api.util.block;

import com.momentum.api.util.Wrapper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author linus
 * @since 03/13/2023
 */
public class BlockScanner implements Wrapper {

    /**
     * Gets all nearby blocks in range
     *
     * @param center The center
     * @param range The range
     * @return All nearby blocks in range
     */
    public static Collection<BlockPos> getBlocks(BlockPos center, float range) {

        // blocks list
        final List<BlockPos> blocks =
                new ArrayList<>();

        // surrounding blocks
        for (float x = 0; x <= range; x++) {
            for (float y = 0; y <= range; y++) {
                for (float z = 0; z <= range; z++) {

                    // the current position
                    BlockPos pos = center.add(x, y, z);

                    // range check
                    if (mc.player.getDistanceSqToCenter(pos) >
                            range * range) {
                        continue;
                    }

                    // add pos to list
                    blocks.add(pos);
                }
            }
        }

        // return block list
        return blocks;
    }

    /**
     * Returns whether a player can stand on the position
     *
     * @param pos The position
     * @return Whether a player can stand on the position
     */
    public static boolean canStandOn(BlockPos pos) {

        // check air
        return mc.world.isAirBlock(pos)
                && !mc.world.isAirBlock(pos.add(0, -1, 0))
                && mc.world.isAirBlock(pos.add(0, 1, 0))
                && mc.world.isAirBlock(pos.add(0, 2, 0));
    }
}
