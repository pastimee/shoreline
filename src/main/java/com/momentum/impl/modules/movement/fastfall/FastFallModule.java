package com.momentum.impl.modules.movement.fastfall;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 02/21/2023
 */
public class FastFallModule extends Module {

    // fall options
    public final Option<Float> heightOption =
            new Option<>("Height", "Maximum fall height", 0.5f, 2.0f, 10.0f);
    public final Option<FallType> typeOption =
            new Option<>("Type", new String[] {"Mode"}, "Fall type", FallType.STEP);
    public final Option<Integer> shiftTicksOption =
            new Option<>("ShiftTicks", "Ticks to shift", 1, 3, 5);

    // listeners
    public final UpdatePlayerSpListener updatePlayerSpListener =
            new UpdatePlayerSpListener(this);
    public final TickListener tickListener =
            new TickListener(this);
    public final MoveListener moveListener =
            new MoveListener(this);

    // previous onGround state
    public boolean pground;

    // move lock
    public boolean lock;
    public int ticks;

    public FastFallModule() {
        super("FastFall", new String[] {"ReverseStep", "HoleTP"}, "Fall faster", ModuleCategory.MOVEMENT);

        // options
        associate(
                heightOption,
                typeOption,
                shiftTicksOption,
                bind,
                drawn
        );

        // listeners
        associate(
                updatePlayerSpListener,
                tickListener,
                moveListener
        );
    }

    /**
     * Gets the player's height from the ground
     *
     * @return The player's height from the ground
     */
    public double getHeightFromGround() {

        // check all blocks within the max height
        for (double h = 0; h < heightOption.getVal() + 0.5; h += 0.01) {

            // check if the fall area is empty
            if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -h, 0)).isEmpty()) {

                // the height from the ground
                return h;
            }
        }

        // too high to fall
        return -1;
    }
}
