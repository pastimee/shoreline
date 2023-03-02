package com.momentum.impl.modules.movement.step;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.asm.mixins.vanilla.accessors.ITimer;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;
import net.minecraft.entity.Entity;

/**
 * @author linus
 * @since 02/20/2023
 */
public class UpdateListener extends FeatureListener<StepModule, UpdateEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected UpdateListener(StepModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdateEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // reset step height
        mc.player.stepHeight = 0.6f;

        // make sure player is on ground
        if (mc.player.onGround) {

            // we are using timer
            if (feature.timer) {

                // reset timer
                // Modules.TIMER_MODULE.provide(1.0f);
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
                feature.timer = false;
            }

            // update our player's step height
            mc.player.stepHeight = feature.heightOption.getVal();
        }

        // the riding entity
        Entity riding = mc.player.getRidingEntity();

        // check if the riding entity exists
        if (riding != null) {

            // reset step height
            riding.stepHeight = feature.isAbstractHorse(riding) ? 1.0f : 0.5f;

            // make sure riding entity is on ground
            if (riding.onGround) {

                // update our riding entity's step height
                riding.stepHeight = 256.0f;
            }
        }
    }
}
