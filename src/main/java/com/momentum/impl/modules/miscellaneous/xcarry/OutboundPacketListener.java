package com.momentum.impl.modules.miscellaneous.xcarry;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.ICPacketCloseWindow;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import net.minecraft.network.play.client.CPacketCloseWindow;

/**
 * @author linus
 * @since 02/26/2023
 */
public class OutboundPacketListener extends FeatureListener<XCarryModule, OutboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected OutboundPacketListener(XCarryModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OutboundPacketEvent event) {

        // packet for closing windows
        if (event.getPacket() instanceof CPacketCloseWindow) {

            // packet from event
            CPacketCloseWindow packet = (CPacketCloseWindow) event.getPacket();

            // prevent the client from sending the packet that lets the server
            // know when you've closed your inventory
            if (((ICPacketCloseWindow) packet).getWindowId() == mc.player.inventoryContainer.windowId) {

                // cancel packet
                event.setCanceled(true);
            }
        }
    }
}
