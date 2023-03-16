package com.momentum.impl.modules.world.speedmine;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * @author linus
 * @since 03/09/2023
 */
public class SpeedmineModule extends Module {

    // mine options
    public final Option<Float> rangeOption =
            new Option<>("Range", "Range for mine", 0.0f, 4.5f, 5.0f);
    public final Option<Boolean> fastOption =
            new Option<>("Fast", "Fast mine", false);
    public final Option<Boolean> strictOption =
            new Option<>("Strict", "Strict mine", false);

    // listeners
    public final LeftClickBlockListener leftClickBlockListener =
            new LeftClickBlockListener(this);
    public final OutboundPacketListener outboundPacketListener =
            new OutboundPacketListener(this);
    public final TickListener tickListener =
            new TickListener(this);
    public final RenderWorldListener renderWorldListener =
            new RenderWorldListener(this);

    // mine info
    protected BlockPos mine;
    protected EnumFacing face;
    protected IBlockState state;
    protected float damage;
    protected int breaks;

    public SpeedmineModule() {
        super("Speedmine", new String[] {"PacketMine"}, "Mines faster", ModuleCategory.WORLD);

        // options
        associate(
                rangeOption,
                fastOption,
                strictOption,
                bind,
                drawn
        );

        // listeners
        associate(
                leftClickBlockListener,
                outboundPacketListener,
                tickListener,
                renderWorldListener
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // reset our block info
        mine = null;
        face = null;
        state = null;
        damage = 0.0f;
        breaks = 0;
    }

    /**
     * Searches the most efficient tool for a specified position
     *
     * @param state The {@link IBlockState} position to find the most efficient tool for
     * @return The most efficient tool for the parameter position
     */
    protected int getBestTool(IBlockState state) {

        // the efficient slot
        int slot = -1;

        // find the most efficient item
        float best = 0.0f;

        // iterate through item in the hotbar
        for (int i = 0; i < 9; i++) {

            // make sure stack is not empty
            if (!mc.player.inventory.getStackInSlot(i).isEmpty()) {

                // destroy speed
                float breakSpeed = mc.player.inventory.getStackInSlot(i).getDestroySpeed(state);

                // make sure the block is breakable
                if (breakSpeed > 1.0f) {

                    // scale by efficiency enchantment
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, mc.player.inventory.getStackInSlot(i)) > 0) {

                        // efficiency lvl
                        float eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, mc.player.inventory.getStackInSlot(i));

                        // efficiency modifier
                        breakSpeed += eff * eff + 1.0f;
                    }

                    // if it's greater than our best break speed, mark our new most efficient tool
                    if (breakSpeed > best) {
                        best = breakSpeed;
                        slot = i;
                    }
                }
            }
        }

        // return the most efficient item
        if (slot != -1) {
            return slot;
        }

        // fallback
        return mc.player.inventory.currentItem;
    }

    @Override
    public String getData() {

        // damage progress
        float dmg = MathHelper.clamp(damage, 0.0f, 1.0f);
        String progress = String.valueOf(dmg);

        // round
        return progress.substring(0, progress.indexOf(".") + 2);
    }
}
