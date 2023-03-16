package com.momentum.impl.modules.movement.sprint;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IEntity;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;

/**
 * @author linus
 * @since 02/12/2023
 */
public class TickListener extends FeatureListener<SprintModule, TickEvent> {

    protected TickListener(SprintModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // check if player is moving
        if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {

            // check hunger level, cannot sprint if hunger level is too low
            if (mc.player.getFoodStats().getFoodLevel() > 6) {

                // update sprint state
                ((IEntity) mc.player).setFlag(3, feature.modeOption.getVal() == SprintMode.RAGE
                        || !mc.player.collidedHorizontally && mc.gameSettings.keyBindForward.isKeyDown());
            }
        }
    }
}
