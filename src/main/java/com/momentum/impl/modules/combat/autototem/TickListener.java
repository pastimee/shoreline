package com.momentum.impl.modules.combat.autototem;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.block.InteractBlocks;
import com.momentum.api.util.inventory.InventoryUtil;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * @author linus
 * @since 02/19/2023
 */
public class TickListener extends FeatureListener<AutoTotemModule, TickEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(AutoTotemModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // can't switch while we are in a screen
        if (mc.currentScreen == null) {

            // reset totem count
            feature.totems = 0;

            // desired offhand mode
            TotemMode offhand = feature.offhandOption.getVal();

            // override the item in the offhand
            if (feature.offhandOverrideOption.getVal()) {

                // override when holding a sword
                if (InventoryUtil.isHolding(ItemSword.class)) {

                    // interacting block
                    Block block = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();

                    // eat bind key is down
                    if (mc.gameSettings.keyBindUseItem.isKeyDown() && !InteractBlocks.canInteract(block)) {

                        // place gapple in offhand
                        offhand = TotemMode.GAPPLE;
                    }
                }
            }

            // check if the player is at risk of dying
            if (isInDanger()) {

                // place totem in offhand
                offhand = TotemMode.TOTEM;
            }

            // item slot
            int slot = -1;
            int crapple = -1;

            // search all slots
            for (int i = 45; i > 8; i--) {

                // item stack in slot
                ItemStack sl = mc.player.inventoryContainer.getSlot(i).getStack();

                // found totem
                if (sl.getItem() == Items.TOTEM_OF_UNDYING) {

                    // update totem count
                    feature.totems++;
                }

                // invalid slot
                if (i == 45) {
                    continue;
                }

                // item in slot matches the desired offhand item
                if (sl.getItem() == offhand.getItem()) {

                    // searching for gapples (require extra calc to check for crapples)
                    if (offhand == TotemMode.GAPPLE) {

                        // check if we need crapple or gapple
                        // in 1.12.2 this will restore all of our absorption hearts
                        if (feature.crappleOption.getVal() && !sl.hasEffect()
                                && mc.player.isPotionActive(MobEffects.ABSORPTION)) {

                            // mark slot
                            crapple = i;
                        }

                        // if we don't have absorption hearts then the crapple won't restore us back to
                        // full absorption hearts, so we can just take any gapple
                        else {

                            // mark slot
                            slot = i;
                        }
                    }

                    // item matches
                    else {

                        // mark slot
                        slot = i;
                    }
                }
            }

            // found crapple
            if (crapple != -1) {

                // item slot becomes crapple slot
                slot = crapple;
            }

            // found slot
            if (slot != -1) {

                // item stack in swap slot
                ItemStack swap = mc.player.inventoryContainer.getSlot(slot).getStack();

                // make sure the swap item is not already in the offhand
                if (!feature.isInOffhand(swap)) {

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

    /**
     * Checks if the player is at risk of dying
     *
     * @return Whether the player is at risk of dying
     */
    private boolean isInDanger() {

        // check if player can take dmg
        if (!mc.player.isCreative()) {

            // player health
            float health = mc.player.getHealth() + mc.player.getAbsorptionAmount();

            // low health
            if (health - feature.healthOption.getVal() < 0.5) {

                // the health option should ideally be adjusted from server to
                // server because the ttk will be different on every server
                return true;
            }

            // high fall
            else if (mc.player.fallDistance > 3 && !mc.player.isOverWater()) {

                // fall damage
                float fall = (mc.player.fallDistance - 3) / 2.0f;

                // check fall dmg
                return health - fall < 0.5;
            }
        }

        // passed all safety checks
        return false;
    }
}
