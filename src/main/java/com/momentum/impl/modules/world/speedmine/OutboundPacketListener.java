package com.momentum.impl.modules.world.speedmine;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;

/**
 * @author linus
 * @since 03/09/2023
 */
public class OutboundPacketListener extends FeatureListener<SpeedmineModule, OutboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected OutboundPacketListener(SpeedmineModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OutboundPacketEvent event) {

        // packet for switching held item
        if (event.getPacket() instanceof CPacketHeldItemChange) {

            // on strict servers
            // switching items resets the mine
            feature.damage = 0.0f;
        }
    }
}
