package com.momentum.impl.modules.combat.criticals;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.time.Timer;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;

/**
 * @author linus
 * @since 02/21/2023
 */
public class OutboundPacketListener extends FeatureListener<CriticalsModule, OutboundPacketEvent> {

    // resend attack packet
    private CPacketUseEntity resend;

    // times
    public final Timer last =
            new Timer();

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected OutboundPacketListener(CriticalsModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OutboundPacketEvent event) {

        // packet for attacks
        if (event.getPacket() instanceof CPacketUseEntity) {

            // packet from event
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();

            // check packet action
            if (packet.getAction() == Action.ATTACK) {

                // entity we attacked
                Entity attacked = packet.getEntityFromWorld(mc.world);

                // make sure the packet is not one that we are resending
                if (resend == null && last.passed(500)) {

                    // check there was an entity that we attacked
                    if (attacked != null) {

                        // pause if attacking a crystal, helps compatability with AutoCrystal
                        if (attacked instanceof EntityEnderCrystal) {
                            return;
                        }

                        // cancel packet, we'll resend after we
                        event.setCanceled(true);
                        resend = packet;

                        // send packets for each of the offsets
                        for (float off : feature.modeOption.getVal().getOffsets()) {

                            // last packet on strict should confirm player position
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, false));
                        }

                        // resend packet
                        mc.player.connection.sendPacket(resend);
                        resend = null;

                        // add critical effects to the hit
                        mc.player.onCriticalHit(attacked);
                        last.reset();
                    }
                }
            }
        }
    }
}
