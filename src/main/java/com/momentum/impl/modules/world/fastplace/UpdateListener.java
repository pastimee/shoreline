package com.momentum.impl.modules.world.fastplace;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author linus
 * @since 02/19/2023
 */
public class UpdateListener extends FeatureListener<FastPlaceModule, UpdateEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected UpdateListener(FastPlaceModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdateEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // remove exp pickup cooldown
        mc.player.xpCooldown = 0;

        // current item
        Item curr = mc.player.getHeldItemMainhand().getItem();

        // checks if the item is selected
        if (mc.gameSettings.keyBindUseItem.isKeyDown() && feature.isItemSelected(curr)) {

            // attempt to fix item ghosting
            if (feature.ghostFixOption.getVal()) {

                // spam the use packet, NCP flags for CPacketPlayerTryUseItemOnBlock too fast
                // so we can use CPacketPlayerTryUseItem instead
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }

            // vanilla click delay
            else {

                // set our vanilla right click delay timer
                ((IMinecraft) mc).setRightClickDelayTimer(feature.delayOption.getVal());
            }
        }

        // dropping items
        if (mc.gameSettings.keyBindDrop.isKeyDown()) {

            // drops items faster
            if (feature.fastDropOption.getVal()) {

                // spam the drop item packet
                mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    }
}
