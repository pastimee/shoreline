package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.Momentum;
import com.momentum.api.thread.Producer;
import com.momentum.api.util.Wrapper;
import com.momentum.impl.managers.Relation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;

import java.util.Queue;
import java.util.TreeMap;

/**
 * @author linus
 * @since 03/14/2023
 */
public class AttackProducer extends Producer<AutoCrystalModule, EntityEnderCrystal>
        implements Wrapper {

    /**
     * Initializes the queue
     *
     * @param feature The feature
     * @param sharedObject The shared queue object
     */
    public AttackProducer(AutoCrystalModule feature,
                          Queue<EntityEnderCrystal> sharedObject) {
        super(feature, sharedObject);
    }

    @Override
    public EntityEnderCrystal call() {

        // calc attack
        DamageSource<EntityEnderCrystal> s = getAttack();

        // check found
        if (s != null) {

            // attack entity
            EntityEnderCrystal a = s.getSource();

            // offer to shared queue
            if (!sharedObject.contains(a)
                    && sharedObject.offer(a)) {

                // return attack
                return a;
            }
        }

        // not found
        return null;
    }

    /**
     * Gets the best attack
     *
     * @return The best attack
     */
    protected DamageSource<EntityEnderCrystal> getAttack() {

        // map of valid targets
        // sorted by natural ordering of keys
        // Using tree map allows time complexity of O(1)
        TreeMap<Double, DamageSource> valid = new TreeMap<>();

        // loaded entities in world
        for (Entity e : mc.world.loadedEntityList) {

            // entity exists
            if (e == null || e.isDead) {
                continue;
            }

            // target needs to exist in the world longer
            if (e.ticksExisted <
                    feature.ticksExistedOption.getVal()) {
                continue;
            }

            // entity check
            if (e instanceof EntityEnderCrystal) {

                // check duplicate
                if (feature.crystals.contains((EntityEnderCrystal) e)) {

                    // already in attack queue
                    continue;
                }

                // distance to crystal
                // range checks
                float dist = mc.player.getDistance(e);
                if (dist > feature.attackRangeOption.getVal()) {

                    // out of range
                    continue;
                }

                // visibility
                boolean visible = feature.isVisible(e);
                if (!visible && dist > feature.attackWallRangeOption.getVal()) {

                    // out of wall range
                    continue;
                }

                // check local damage
                // damage to player
                double ldmg = ExplosionCalc.calc((EntityEnderCrystal) e, mc.player);
                if (!mc.player.capabilities.isCreativeMode
                        && ldmg > feature.maxLocalDamageOption.getVal()) {
                    continue;
                }

                // targets
                for (Entity e2 : mc.world.loadedEntityList) {

                    // entity exists
                    if (e2 == null || e2.isDead) {
                        continue;
                    }

                    // only target living base entities
                    if (e2 instanceof EntityLivingBase) {

                        // invalid target
                        if (e2 == mc.player
                                || Momentum.RELATION_MANGER.isRelation(e2, Relation.FRIEND)) {
                            continue;
                        }

                        // check entity type
                        if (feature.isValidEntity(e2)) {

                            // distance to target
                            // range check
                            float dist2 = mc.player.getDistance(e2);
                            if (dist2 > feature.enemyRangeOption.getVal()) {

                                // out of range
                                continue;
                            }

                            // damage to target
                            double dmg = ExplosionCalc.calc((EntityEnderCrystal) e, e2);
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
                                    new DamageSource<>((EntityEnderCrystal) e, e2, dmg, edmg, ldmg));
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

        // no attack
        return null;
    }
}
