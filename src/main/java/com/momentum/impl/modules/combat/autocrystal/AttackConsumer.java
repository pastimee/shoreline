package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.api.thread.Consumer;
import com.momentum.api.util.Wrapper;
import net.minecraft.entity.item.EntityEnderCrystal;

import java.util.Queue;

/**
 * @author linus
 * @since 03/14/2023
 */
public class AttackConsumer extends Consumer<AutoCrystalModule, EntityEnderCrystal>
        implements Wrapper {

    /**
     * Initializes the queue
     *
     * @param feature      The feature
     * @param sharedObject The shared queue object
     */
    public AttackConsumer(AutoCrystalModule feature, Queue<EntityEnderCrystal> sharedObject) {
        super(feature, sharedObject);
    }

    @Override
    public EntityEnderCrystal call() {

        // calc attack
        EntityEnderCrystal c = sharedObject.poll();

        // check found
        if (c != null) {

            // run async
            mc.addScheduledTask(() -> {

                // check attack ready
                if (isReady()) {

                    // attack
                    if (feature.attack(c)) {

                        // reset attack time
                        feature.crystalLast.reset();
                    }
                }
            });
        }

        // return crystal
        return c;
    }

    /**
     * Checks if the module is ready to place
     *
     * @return Place ready
     */
    private boolean isReady() {

        // place delay
        float delay = (feature.attackSpeedOption.getMax() - feature.attackSpeedOption.getVal()) * 50;

        // passed delay
        return feature.crystalLast.passed((long) delay);
    }
}
