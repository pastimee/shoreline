package com.momentum.impl.modules.movement.velocity;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.asm.mixins.vanilla.accessors.ISPacketEntityVelocity;
import com.momentum.asm.mixins.vanilla.accessors.ISPacketExplosion;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

/**
 * @author linus
 * @since 01/09/2023
 */
public class InboundPacketListener extends FeatureListener<VelocityModule, InboundPacketEvent> {
    protected InboundPacketListener(VelocityModule module) {
        super(module);
    }

    @Override
    public void invoke(InboundPacketEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // packet for velocity caused by factors that are not explosions
        if (event.getPacket() instanceof SPacketEntityVelocity) {

            // received packet
            SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();

            // only apply to own player
            if (packet.getEntityID() == mc.player.getEntityId()) {

                // if our settings are 0, then we can cancel this packet
                if (feature.horizontalOption.getVal() == 0 && feature.verticalOption.getVal() == 0) {
                    event.setCanceled(true);
                }

                // apply velocity modifier
                else {

                    // if we want to modify the velocity, then we update the packet's values
                    if (packet.getEntityID() == mc.player.getEntityId()) {

                        // motion from the packet
                        int motionX = packet.getMotionX() / 100;
                        int motionY = packet.getMotionY() / 100;
                        int motionZ = packet.getMotionZ() / 100;

                        // modify motion
                        ((ISPacketEntityVelocity) packet).setMotionX(motionX * feature.horizontalOption.getVal());
                        ((ISPacketEntityVelocity) packet).setMotionY(motionY * feature.verticalOption.getVal());
                        ((ISPacketEntityVelocity) packet).setMotionZ(motionZ * feature.horizontalOption.getVal());
                    }
                }
            }
        }

        // packet for velocity caused by explosions
        if (event.getPacket() instanceof SPacketExplosion) {

            // received packet
            SPacketExplosion packet = (SPacketExplosion) event.getPacket();

            // if our settings are 0, then we can cancel this packet
            if (feature.horizontalOption.getVal() == 0 && feature.verticalOption.getVal() == 0) {
                event.setCanceled(true);
            }

            // apply velocity modifier
            else {

                // if we want to modify the velocity, then we update the packet's values
                // motion from the packet
                float motionX = packet.getMotionX() / 100;
                float motionY = packet.getMotionY() / 100;
                float motionZ = packet.getMotionZ() / 100;

                // modify motion
                ((ISPacketExplosion) packet).setMotionX(motionX * feature.horizontalOption.getVal());
                ((ISPacketExplosion) packet).setMotionY(motionY * feature.verticalOption.getVal());
                ((ISPacketExplosion) packet).setMotionZ(motionZ * feature.horizontalOption.getVal());
            }
        }

        // packet for being pulled by fishhooks
        if (event.getPacket() instanceof SPacketEntityStatus && ((SPacketEntityStatus) event.getPacket()).getOpCode() == 31) {
            if (feature.fishHookOption.getVal()) {

                // get the entity that is pulling us
                Entity entity = ((SPacketEntityStatus) event.getPacket()).getEntity(mc.world);

                // check if it's a fishhook
                if (entity instanceof EntityFishHook) {

                    // cancel the pull
                    EntityFishHook entityFishHook = (EntityFishHook) entity;
                    if (entityFishHook.caughtEntity.equals(mc.player)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
