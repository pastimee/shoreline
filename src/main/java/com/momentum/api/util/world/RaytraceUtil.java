package com.momentum.api.util.world;

import com.momentum.api.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author linus
 * @since 03/02/2023
 */
public class RaytraceUtil implements Wrapper {

    /**
     * Whether an entity can see another given entity
     *
     * @param e1 The entity observing
     * @param e2 The entity being observed
     * @param y The y offset
     * @return Whether there is an obstruction between them
     */
    public static boolean canSee(Entity e1, Entity e2, float y) {

        // raytrace between points
        return mc.world.rayTraceBlocks(new Vec3d(e1.posX, e1.posY + e1.getEyeHeight(), e1.posZ),
                new Vec3d(e2.posX, e2.posY + y, e2.posZ), false, true, false) == null;
    }

    /**
     * Whether an entity can see a block position
     *
     * @param e1 The entity observing
     * @param e2 The entity being observed
     * @param y The y offset
     * @return Whether there is an obstruction between them
     */
    public static boolean canSee(Entity e, BlockPos p, float y) {

        // raytrace between points
        return mc.world.rayTraceBlocks(new Vec3d(e.posX, e.posY + e.getEyeHeight(), e.posZ),
                new Vec3d(p.getX() + 0.5, p.getY() + y, p.getZ() + 0.5), false, true, false) == null;
    }

    /**
     * Whether an entity can see another given entity
     *
     * @param e1 The entity observing
     * @param e2 The entity being observed
     * @return Whether there is an obstruction between them
     */
    public static Vec3d canSeeScan(Entity e1, Entity e2) {

        // entity box
        AxisAlignedBB box = e2.getEntityBoundingBox();

        // scan box
        for (double x = box.minX; x < box.maxX; x += 0.01) {

            // y
            for (double y = box.minY; y < box.maxY; y += 0.01) {

                // z
                for (double z = box.minZ; z < box.maxZ; z += 0.01) {

                    // vector to entity
                    Vec3d vec = new Vec3d(e2.posX + x, e2.posY + y, e2.posZ + z);

                    // raytrace between points
                    if (mc.world.rayTraceBlocks(new Vec3d(e1.posX, e1.posY + e1.getEyeHeight(), e1.posZ),
                            vec, false, true, false) == null) {

                        // found opening
                        return vec;
                    }
                }
            }
        }

        // could not see at all
        return null;
    }
}
