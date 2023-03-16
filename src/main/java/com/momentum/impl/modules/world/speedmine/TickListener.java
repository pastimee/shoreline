package com.momentum.impl.modules.world.speedmine;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;

/**
 * @author linus
 * @since 03/09/2023
 */
public class TickListener extends FeatureListener<SpeedmineModule, TickEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(SpeedmineModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // no reason to speedmine in creative mode, blocks break instantly
        if (!mc.player.capabilities.isCreativeMode) {

            // check if curr mine exists
            if (feature.mine != null && !mc.world.isAirBlock(feature.mine)) {

                // distance to mine
                double dist = mc.player.getDistanceSqToCenter(feature.mine);

                // limit re-mines
                if (feature.breaks > 1 && feature.strictOption.getVal()
                        || dist > feature.rangeOption.getVal() * feature.rangeOption.getVal()) {

                    // reset our block info
                    feature.mine = null;
                    feature.face = null;
                    feature.state = null;
                    feature.damage = 0.0f;
                    feature.breaks = 0;
                    return;
                }

                // tool slots
                int prev = mc.player.inventory.currentItem;
                int slot = feature.getBestTool(feature.state);

                // block is broken
                if (feature.damage >= 1.0f) {

                    // don't switch if already holding
                    if (prev != slot) {

                        // swap with window clicks
                        if (feature.strictOption.getVal()) {

                            // transaction id
                            short nextTransactionId = mc.player.openContainer.getNextTransactionID(mc.player.inventory);

                            // window click
                            ItemStack itemstack = mc.player.openContainer.slotClick(slot, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                            mc.player.connection.sendPacket(new CPacketClickWindow(
                                    mc.player.inventoryContainer.windowId, slot, mc.player.inventory.currentItem, ClickType.SWAP, itemstack, nextTransactionId));
                        }

                        // normal swap
                        else {

                            // update our current item and send a packet to the server
                            mc.player.inventory.currentItem = slot;
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                        }
                    }

                    // break the block
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(
                            Action.STOP_DESTROY_BLOCK, feature.mine, feature.face));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(
                            Action.ABORT_DESTROY_BLOCK, feature.mine, EnumFacing.UP));

                    // faster break
                    if (feature.fastOption.getVal()) {

                        // send start destroy packet
                        // instant replacements will break faster (i.e. surround)
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(
                                Action.START_DESTROY_BLOCK, feature.mine, feature.face));
                    }

                    // finish block break
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(
                            Action.STOP_DESTROY_BLOCK, feature.mine, feature.face));

                    // don't switch if already holding
                    if (prev != slot) {

                        // swap with window clicks
                        if (feature.strictOption.getVal()) {

                            // transaction id
                            short nextTransactionId = mc.player.openContainer.getNextTransactionID(mc.player.inventory);

                            // window click
                            ItemStack itemstack = mc.player.openContainer.slotClick(slot, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                            mc.player.connection.sendPacket(new CPacketClickWindow(
                                    mc.player.inventoryContainer.windowId, slot, mc.player.inventory.currentItem, ClickType.SWAP, itemstack, nextTransactionId));

                            // confirm packets
                            mc.player.connection.sendPacket(new CPacketConfirmTransaction(
                                    mc.player.inventoryContainer.windowId, nextTransactionId, true));
                        }

                        // normal swap
                        else {

                            // update our current item and send a packet to the server
                            mc.player.inventory.currentItem = prev;
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(prev));
                        }
                    }

                    // reset
                    feature.damage = 0.0f;
                    feature.breaks++;
                }

                // update block damage
                mc.player.inventory.currentItem = slot;
                feature.damage += feature.state.getPlayerRelativeBlockHardness(mc.player, mc.world, feature.mine);
                mc.player.inventory.currentItem = prev;
            }

            // not currently mining
            else {

                // reset
                feature.damage = 0.0f;
            }
        }
    }
}
