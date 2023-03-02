package com.momentum.impl.modules.world.fastplace;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author linus
 * @since 02/18/2023
 */
public class FastPlaceModule extends Module {

    // whitelist
    private final Collection<Item> whitelist = Arrays.asList(
            Items.EXPERIENCE_BOTTLE,
            Items.SNOWBALL,
            Items.EGG
    );

    // blacklist
    private final Collection<Item> blacklist = Arrays.asList(
            Items.ENDER_EYE,
            Items.ENDER_PEARL
    );

    // selection options
    public final Option<ItemSelection> selectionOption =
            new Option<>("Selection", "Item selection", ItemSelection.WHITELIST);
    public final Option<Collection<Item>> whitelistOption =
            new Option<>("Whitelist", "Fast place item whitelist", whitelist);
    public final Option<Collection<Item>> blacklistOption =
            new Option<>("Blacklist", "Fast place item whitelist", blacklist);

    // anticheat options
    public final Option<Integer> delayOption =
            new Option<>("Delay", "Place delay", 0, 0, 4);
    public final Option<Boolean> ghostFixOption =
            new Option<>("GhostFix", "Fixes placement ghosting", false);
    public final Option<Boolean> fastDropOption =
            new Option<>("FastDrop", "Drop items faster", false);

    // listeners
    public final UpdateListener updateListener =
            new UpdateListener(this);
    public final OutboundPacketListener outboundPacketListener =
            new OutboundPacketListener(this);

    public FastPlaceModule() {
        super("FastPlace", new String[] {"FastExp", "FastXp"}, "Place faster.", ModuleCategory.WORLD);

        // options
        associate(
                selectionOption,
                whitelistOption,
                blacklistOption,
                ghostFixOption,
                fastDropOption,
                bind,
                drawn
        );

        // listener
        associate(
                updateListener,
                outboundPacketListener
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // reset our vanilla right click delay timer
        ((IMinecraft) mc).setRightClickDelayTimer(4);
    }

    /**
     * Checks if item is selected
     *
     * @param item The item to check
     * @return Whether the item is selected
     */
    protected boolean isItemSelected(Item item) {

        // whitelist selection/
        if (selectionOption.getVal() == ItemSelection.WHITELIST) {

            // check if item is in the whitelist
            return whitelist.contains(item);
        }

        // blacklist selection
        else if (selectionOption.getVal() == ItemSelection.BLACKLIST) {

            // check if item is not in the blacklist
            return !blacklist.contains(item);
        }

        // all items
        return true;
    }
}
