package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.forge.event.ItemUseEvent;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;

/**
 * @author linus
 * @since 02/13/2023
 */
public class ItemUseListener extends FeatureListener<NoSlowModule, ItemUseEvent> {
    protected ItemUseListener(NoSlowModule feature) {
        super(feature);
    }

    @Override
    public void invoke(ItemUseEvent event) {

        // not sneaking serverside
        if (!feature.serverSneaking) {

            // send sneaking packet when we use an item
            if (feature.isSlowed() && feature.airStrictOption.getVal()) {

                // update serverside sneak state
                feature.serverSneaking = true;
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
            }
        }
    }
}
