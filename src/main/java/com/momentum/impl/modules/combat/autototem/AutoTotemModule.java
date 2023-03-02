package com.momentum.impl.modules.combat.autototem;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author linus
 * @since 02/19/2023
 */
public class AutoTotemModule extends Module {

    // auto totem options
    public final Option<TotemMode> offhandOption =
            new Option<>("Offhand", new String[] {"Mode", "OffhandMode"}, "Item to replace in offhand", TotemMode.TOTEM);
    public final Option<Integer> healthOption =
            new Option<>("Health", new String[] {"LethalHealth"}, "Health to move totem into offhand", 0, 10, 20);
    public final Option<Boolean> offhandOverrideOption =
            new Option<>("OffhandOverride", "Uses crapples when absorption effect is active", false);
    public final Option<Boolean> crappleOption =
            new Option<>("Crapple", "Uses crapples when absorption effect is active", false);

    // listeners
    public final TickListener tickListener =
            new TickListener(this);
    public final InboundPacketListener inboundPacketListener =
            new InboundPacketListener(this);

    // info
    public int totems;

    public AutoTotemModule() {
        super("AutoTotem", new String[] {"Offhand", "AutoOffhand"}, "Replaces totems in the offhand", ModuleCategory.COMBAT);

        // options
        associate(
                offhandOption,
                healthOption,
                offhandOverrideOption,
                crappleOption,
                bind,
                drawn
        );

        // listeners
        associate(
                tickListener,
                inboundPacketListener
        );
    }

    /**
     * Checks if a given item is in the offhand
     *
     * @param in The item
     * @return Whether a given item is in the offhand
     */
    public boolean isInOffhand(ItemStack in) {

        // item in the offhand
        ItemStack offhand = mc.player.getHeldItemOffhand();

        // two types of gapples so we need to check each one
        if (in.getItem() == Items.GOLDEN_APPLE) {

            // holding golden apple
            if (offhand.getItem() == Items.GOLDEN_APPLE) {

                // check if given item is a god apple
                // check if equal
                return in.hasEffect() == offhand.hasEffect();
            }
        }

        // check if they are equal
        return offhand.getItem() == in.getItem();
    }

    @Override
    public String getData() {

        // show how many totems are in the inventory
        return String.valueOf(totems);
    }
}
