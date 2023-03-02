package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.ICPacketPlayer;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linus
 * @since 02/13/2023
 */
public class OutboundPacketListener extends FeatureListener<NoSlowModule, OutboundPacketEvent> {
    protected OutboundPacketListener(NoSlowModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OutboundPacketEvent event) {

        // packet for player updates
        if (event.getPacket() instanceof CPacketPlayer) {

            // event packet
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();

            // check if we are moving
            if (((ICPacketPlayer) packet).isMoving()) {

                // check if we are slowed down
                if (feature.isSlowed()) {

                    // NCP bypass
                    // if (strict.getValue()) {
                    //    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, BlockPos.ORIGIN, EnumFacing.DOWN));
                    // }

                    // Updated NCP bypass
                    if (feature.strictOption.getVal()) {

                        // doesn't work on servers with switch timeout
                        // (i.e. silent switch patched servers)
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                    }
                }
            }
        }

        // packet for clicking window slots
        if (event.getPacket() instanceof CPacketClickWindow) {

            // Updated NCP bypass for inventory move
            if (feature.strictOption.getVal()) {

                // shield blocking
                if (mc.player.isActiveItemStackBlocking()) {

                    // stop blocking
                    mc.playerController.onStoppedUsingItem(mc.player);
                }

                // with NCP-Updated, we cannot use items while in inventories
                if (mc.player.isSneaking() || feature.serverSneaking) {

                    // stop sneaking before item use
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
                }

                // we also cannot be sprinting, because that'll also flag NCP-Updated
                if (mc.player.isSprinting()) {

                    // stop sprinting before window click
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SPRINTING));
                }
            }
        }
    }
}
