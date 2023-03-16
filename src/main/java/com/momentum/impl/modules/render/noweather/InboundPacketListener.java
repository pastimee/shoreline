package com.momentum.impl.modules.render.noweather;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.network.play.server.SPacketTimeUpdate;

/**
 * @author linus
 * @since 03/08/2023
 */
public class InboundPacketListener extends FeatureListener<NoWeatherModule, InboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected InboundPacketListener(NoWeatherModule feature) {
        super(feature);
    }

    @Override
    public void invoke(InboundPacketEvent event) {

        // packet for world time updates
        if (event.getPacket() instanceof SPacketTimeUpdate) {

            // cancel time updates
            event.setCanceled(true);
        }
    }
}
