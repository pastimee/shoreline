package com.momentum.impl.modules.combat.autototem;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import com.momentum.impl.modules.combat.autototem.AutoTotemModule;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityStatus;

/**
 * @author linus
 * @since 02/19/2023
 */
public class InboundPacketListener extends FeatureListener<AutoTotemModule, InboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected InboundPacketListener(AutoTotemModule feature) {
        super(feature);
    }

    // totem instant replace impl
    @Override
    public void invoke(InboundPacketEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // packet for entity status updates
        if (event.getPacket() instanceof SPacketEntityStatus) {

            // packet from event
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();

            // totem pop opcode
            if (packet.getOpCode() == 35) {

                // entity that popped
                Entity entity = packet.getEntity(mc.world);

                // player has popped
                if (entity == mc.player) {

                    // can't switch while we are in a screen
                    if (mc.currentScreen == null) {

                        // item slot
                        int slot = -1;

                        // search all slots
                        for (int i = 44; i > 8; i--) {

                            // item stack in slot
                            ItemStack sl = mc.player.inventoryContainer.getSlot(i).getStack();

                            // item in slot matches the desired offhand item
                            if (sl.getItem() == Items.TOTEM_OF_UNDYING) {

                                // mark slot
                                slot = i;
                            }
                        }

                        // found slot
                        if (slot != -1) {

                            // pickup item
                            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);

                            // move the item to the offhand
                            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

                            // item stack held by mouse in inventory
                            ItemStack curr = mc.player.inventory.getItemStack();

                            // item already in offhand
                            // we know need to deal with returning it to inventor
                            if (!curr.isEmpty()) {

                                // find a slot to return to
                                int rslot = -1;

                                // search inventory slots for empty slot
                                for (int i = 9; i < 45; i++) {

                                    // stack in slot
                                    ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();

                                    // slot is empty
                                    if (stack.isEmpty()) {

                                        // mark slot
                                        rslot = i;
                                    }
                                }

                                // check if found return slot
                                if (rslot != -1) {

                                    // move the item in the offhand to the return slot
                                    mc.playerController.windowClick(0, rslot, 0, ClickType.PICKUP, mc.player);
                                    mc.playerController.updateController();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
