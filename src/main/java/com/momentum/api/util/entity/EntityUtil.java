package com.momentum.api.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;

/**
 * @author linus
 * @since 03/02/2023
 */
public class EntityUtil {

    /**
     * Check whether an entity is a passive mob
     * @param entity The entity to check
     * @return Whether an entity is a passive mob
     */
    public static boolean isPassiveMob(Entity entity) {

       // check it's entity properties
        return entity instanceof EntityWolf
                && !((EntityWolf) entity).isAngry()  // check if the wolf that is angry
                || entity instanceof EntityIronGolem
                && ((EntityIronGolem) entity).getRevengeTarget() == null // check if the iron golem is angry
                || entity instanceof EntityAgeable
                || entity instanceof EntityAmbientCreature
                || entity instanceof EntitySquid;
    }

    /**
     * Check whether an entity is a vehicle mob
     *
     * @param entity The entity to check
     * @return Whether an entity is a vehicle mob
     */
    public static boolean isVehicleMob(Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    /**
     * Check whether an entity is a hostile mob
     *
     * @param entity The entity to check
     * @return Whether an entity is a hostile mob
     */
    public static boolean isHostileMob(Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.MONSTER, false)
                && !EntityUtil.isNeutralMob(entity)) || entity instanceof EntitySpider;
    }

    /**
     * Check whether an entity is a neutral mob
     *
     * @param entity The entity to check
     * @return Whether an entity is a neutral mob
     */
    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie
                && !((EntityPigZombie) entity).isAngry() // check if the pigman is angry
                || entity instanceof EntityWolf
                && !((EntityWolf) entity).isAngry() // check if the wolf is angry
                || entity instanceof EntityEnderman
                && ((EntityEnderman) entity).isScreaming(); // check if the enderman is angry
    }
}
