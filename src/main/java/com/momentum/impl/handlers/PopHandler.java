package com.momentum.impl.handlers;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.impl.events.vanilla.network.DisconnectEvent;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import com.momentum.impl.events.vanilla.world.EntityRemoveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages player totem pops
 *
 * @author linus
 * @since 03/01/2023
 */
public class PopHandler extends Handler {

    // pop map
    private final Map<EntityPlayer, Integer> pops = new ConcurrentHashMap<>();

    /**
     * Manages player totem pops
     */
    @SuppressWarnings("ConstantConditions")
    public PopHandler() {

        // pop counter impl
        Momentum.EVENT_BUS.subscribe(new Listener<InboundPacketEvent>() {

            @Override
            public void invoke(InboundPacketEvent event) {

                // server entity updates
                if (event.getPacket() instanceof SPacketEntityStatus) {

                    // packet from event
                    SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();

                    // op code for totem pop
                    if (packet.getOpCode() == 35) {

                        // popped entity
                        Entity popped = packet.getEntity(mc.world);

                        // check if popped entity exists
                        if (popped != null) {

                            // put in map
                            pops.put((EntityPlayer) popped, getPops((EntityPlayer) popped) + 1);
                        }
                    }
                }
            }
        });

        // death impl
        Momentum.EVENT_BUS.subscribe(new Listener<EntityRemoveEvent>() {

            @Override
            public void invoke(EntityRemoveEvent event) {

                // player removed
                if (event.getEntity() instanceof EntityPlayer) {

                    // remove the entity from the map
                    EntityPlayer remove = (EntityPlayer) event.getEntity();
                    pops.replace(remove, 0);
                }
            }
        });

        // disconnect impl
        Momentum.EVENT_BUS.subscribe(new Listener<DisconnectEvent>() {

            @Override
            public void invoke(DisconnectEvent event) {

                // clear pop map on disconnect
                pops.clear();
            }
        });
    }

    /**
     * Gets the number of times a given player has popped
     *
     * @param in The given player
     * @return The number of times a given player has popped
     */
    public int getPops(EntityPlayer in) {

        // get from pop map
        return pops.getOrDefault(in, 0);
    }
}
