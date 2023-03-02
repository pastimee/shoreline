package com.momentum.impl.modules.combat.autobowrelease;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.inventory.InventoryUtil;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import com.momentum.impl.init.Handlers;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author linus
 * @since 02/20/2023
 */
public class TickListener extends FeatureListener<AutoBowReleaseModule, TickEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(AutoBowReleaseModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // make sure we are holding a bow and drawing it
        if (InventoryUtil.isHolding(Items.BOW) && mc.player.isHandActive()) {

            // use ticks
            float use = mc.player.getHeldItemMainhand().getMaxItemUseDuration() - mc.player.getItemInUseCount();

            // tick offset
            float off = feature.tpsSyncOption.getVal() ? 20.0f - Handlers.TICK_HANDLER.getTps() : 0.0f;

            // make sure we've held it for at least a minimum of specified ticks
            if (use - off > feature.ticksOption.getVal()) {

                // release bow packets
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                // mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                mc.player.stopActiveHand();
            }
        }
    }
}
