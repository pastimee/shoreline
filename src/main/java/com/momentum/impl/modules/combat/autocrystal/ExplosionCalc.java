package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.api.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;

/**
 * Calculates explosion damage
 *
 * @author linus
 * @since 03/13/2023
 */
public class ExplosionCalc implements Wrapper {

    /**
     * Calculates the damage from the crystal onto the target
     *
     * @param c The crystal
     * @param t The target
     * @return The damage
     */
    public static double calc(EntityEnderCrystal c, Entity t) {

        return 0;
    }

    /**
     * Calculates the damage from the placement onto the target
     *
     * @param c The placement
     * @param t The target
     * @return The damage
     */
    public static double calc(BlockPos p, Entity t) {

        return 0;
    }
}
