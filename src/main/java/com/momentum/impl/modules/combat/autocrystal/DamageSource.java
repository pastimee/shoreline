package com.momentum.impl.modules.combat.autocrystal;

import net.minecraft.entity.Entity;

/**
 * Damage holder
 *
 * @author linus
 * @since 03/13/2023
 * @param <S> The source type
 */
public class DamageSource<S> {

    // damage source
    private final S source;

    // damaged
    private final Entity target;

    // damage
    private final double tdamage, edamage, ldamage;

    /**
     * Damage holder
     */
    public DamageSource(S source, Entity target, double tdamage,
                        double edamage, double ldamage) {
        this.source = source;
        this.target = target;
        this.tdamage = tdamage;
        this.edamage = edamage;
        this.ldamage = ldamage;
    }

    /**
     * Gets the damage source
     *
     * @return The damage source
     */
    public S getSource() {
        return source;
    }

    /**
     * Gets the target
     *
     * @return The target
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Gets the damage to the target
     *
     * @return The damage to the target
     */
    public double getTargetDamage() {
        return tdamage;
    }

    /**
     * Gets the extrapolated damage to the target
     *
     * @return The extrapolated damage to the target
     */
    public double getExtrapolatedDamage() {
        return edamage;
    }

    /**
     * Gets the damage to the player
     *
     * @return The damage to the player
     */
    public double getLocalDamage() {
        return ldamage;
    }
}
