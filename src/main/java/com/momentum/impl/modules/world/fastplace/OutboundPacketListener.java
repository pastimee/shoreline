package com.momentum.impl.modules.world.fastplace;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.block.InteractBlocks;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

/**
 * @author linus
 * @since 02/19/2023
 */
public class OutboundPacketListener extends FeatureListener<FastPlaceModule, OutboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected OutboundPacketListener(FastPlaceModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OutboundPacketEvent event) {

        // packet for block placements
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {

            // block placement packet
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();

            // current item
            Item curr = mc.player.getHeldItemMainhand().getItem();

            // check if the held item is valid
            if (feature.isItemSelected(curr)) {

                // cancel CPacketPlayerTryUseItemOnBlock packets
                if (feature.ghostFixOption.getVal()) {

                    // interacting block
                    Block block = mc.world.getBlockState(packet.getPos()).getBlock();

                    // make sure we are not interacting with a sneak block
                    if (!InteractBlocks.canInteract(block)) {

                        // NCP flags for CPacketPlayerTryUseItemOnBlock too fast
                        // prevent packet from sending
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
