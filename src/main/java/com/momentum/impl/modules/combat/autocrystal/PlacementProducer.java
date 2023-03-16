package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.Momentum;
import com.momentum.api.thread.Producer;
import com.momentum.api.util.Wrapper;
import com.momentum.api.util.block.BlockScanner;
import com.momentum.impl.managers.Relation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Queue;
import java.util.TreeMap;

/**
 * @author linus
 * @since 03/14/2023
 */
public class PlacementProducer extends Producer<AutoCrystalModule, BlockPos>
        implements Wrapper {

    /**
     * Initializes the queue
     *
     * @param feature The feature
     * @param sharedObject The shared queue object
     */
    public PlacementProducer(AutoCrystalModule feature,
                             Queue<BlockPos> sharedObject) {
        super(feature, sharedObject);
    }

    @Override
    public BlockPos call() {

        // calc placement
        DamageSource<BlockPos> s = getPlacement();

        // check found
        if (s != null) {

            // placement pos
            BlockPos pos = s.getSource();

            // offer to shared queue
            if (!sharedObject.contains(pos)
                    && sharedObject.offer(pos)) {

                // return placement
                return pos;
            }
        }

        // not found
        return null;
    }

    /**
     * Gets the best placement
     *
     * @return The best placement
     */
    protected DamageSource<BlockPos> getPlacement() {

        // map of valid targets
        // sorted by natural ordering of keys
        // Using tree map allows time complexity of O(1)
        TreeMap<Double, DamageSource> valid = new TreeMap<>();

        // sphere in range
        float r = Math.max(feature.placeRangeOption.getVal(), feature.strictPlaceRangeOption.getVal());
        Collection<BlockPos> range = BlockScanner.getBlocks(mc.player.getPosition(), r);

        // check placements in range
        for (BlockPos p : range) {

            // check if valid placement
            if (feature.onUseItemEnderCrystal(mc.world, p)
                    != EnumActionResult.FAIL) {

                // check duplicate
                if (feature.placements.contains(p)) {

                    // already in placement queue
                    continue;
                }

                // distance to center
                double dist = mc.player.getDistanceSqToCenter(p);
                if (dist > feature.placeRangeOption.getVal() * feature.placeRangeOption.getVal()) {

                    // out of range
                    continue;
                }

                // visibility
                boolean visible = feature.isVisible(p);
                if (!visible &&
                        dist > feature.placeWallRangeOption.getVal() * feature.placeWallRangeOption.getVal()) {

                    // out of wall range
                    continue;
                }

                // check local damage
                // damage to player
                double ldmg = ExplosionCalc.calc(p, mc.player);
                if (!mc.player.capabilities.isCreativeMode
                        && ldmg > feature.maxLocalDamageOption.getVal()) {
                    continue;
                }

                // targets
                for (Entity e : mc.world.loadedEntityList) {

                    // entity exists
                    if (e == null || e.isDead) {
                        continue;
                    }

                    // only target living base entities
                    if (e instanceof EntityLivingBase) {

                        // invalid target
                        if (e == mc.player
                                || Momentum.RELATION_MANGER.isRelation(e, Relation.FRIEND)) {
                            continue;
                        }

                        // check entity type
                        if (feature.isValidEntity(e)) {

                            // distance to target
                            // range check
                            float dist2 = mc.player.getDistance(e);
                            if (dist2 > feature.enemyRangeOption.getVal()) {

                                // out of range
                                continue;
                            }

                            // damage to target
                            double dmg = ExplosionCalc.calc(p, e);
                            double edmg = dmg * feature.lethalMultiplierOption.getVal(); // extrapolated damage

                            // check safety balance
                            // local to target dmg ratio
                            double balance = ldmg / dmg;
                            if (!mc.player.capabilities.isCreativeMode
                                    && balance > feature.safetyBalanceOption.getVal()) {
                                continue;
                            }

                            // put in valid targets
                            valid.put(dmg,
                                    new DamageSource<>(p, e, dmg, edmg, ldmg));
                        }

                        // check valid list
                        if (!valid.isEmpty()) {

                            // best crystal
                            DamageSource best = valid.lastEntry().getValue();

                            // target health
                            float health = ((EntityLivingBase) best.getTarget()).getHealth()
                                    + ((EntityLivingBase) best.getTarget()).getAbsorptionAmount();

                            // check target damage
                            if (best.getTargetDamage() < 1.5f
                                    || best.getTargetDamage() < feature.minDamageOption.getVal()
                                    || health - best.getExtrapolatedDamage() > 0.5f) {

                                // failed min requirements
                                return null;
                            }

                            // return best dmg source
                            return best;
                        }
                    }
                }
            }
        }

        // no placement
        return null;
    }
}
