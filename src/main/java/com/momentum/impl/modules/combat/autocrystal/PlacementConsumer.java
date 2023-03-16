package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.api.thread.Consumer;
import com.momentum.api.util.Wrapper;
import net.minecraft.util.math.BlockPos;

import java.util.Queue;

/**
 * @author linus
 * @since 03/14/2023
 */
public class PlacementConsumer extends Consumer<AutoCrystalModule, BlockPos>
        implements Wrapper {

    /**
     * Initializes the queue
     *
     * @param feature      The feature
     * @param sharedObject The shared queue object
     */
    public PlacementConsumer(AutoCrystalModule feature,
                             Queue<BlockPos> sharedObject) {
        super(feature, sharedObject);
    }

    @Override
    public BlockPos call() {

        // calc placement
        BlockPos pos = sharedObject.poll();

        // check found
        if (pos != null) {

            // run async
            mc.addScheduledTask(() -> {

                // check place ready
                if (isReady()) {

                    // place
                    if (feature.place(pos)) {

                        // reset timers
                        feature.placementLast.reset();
                    }
                }
            });
        }

        // return placement
        return pos;
    }

    /**
     * Checks if the module is ready to place
     *
     * @return Place ready
     */
    private boolean isReady() {

        // place delay
        float delay = (feature.placeSpeedOption.getMax() - feature.placeSpeedOption.getVal()) * 50;

        // passed delay
        return feature.placementLast.passed((long) delay);
    }
}
