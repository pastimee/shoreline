package com.momentum.impl.modules.combat.aura;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.util.entity.EntityUtil;
import com.momentum.api.util.render.Formatter;
import com.momentum.api.util.rotation.Rotation;
import com.momentum.api.util.rotation.RotationUtil;
import com.momentum.api.util.time.Timer;
import com.momentum.api.util.world.RaytraceUtil;
import com.momentum.asm.mixins.vanilla.accessors.IEntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

import java.util.TreeMap;

/**
 * @author linus
 * @since 03/02/2023
 */
public class AuraModule extends Module {

    // aura options
    public final Option<AuraMode> modeOption =
            new Option<>("Mode", "Targeting mode", AuraMode.SWITCH);
    public final Option<Timing> timingOption =
            new Option<>("Timing", "Attack timing mode", Timing.SEQUENTIAL);
    public final Option<Priority> priorityOption =
            new Option<>("Priority", "Targeting priority mode", Priority.SMART);
    public final Option<Boolean> respawnDisableOption =
            new Option<>("RespawnDisable", "Disable on respawn", true);
    public final Option<Boolean> hitDelayOption =
            new Option<>("HitDelay", new String[] {"AttackDelay"},"1.9+ attack delay", true);
    public final Option<TpsSync> tpsSyncOption =
            new Option<>("TpsSync", "Syncs attacks to server TPS", TpsSync.AVERAGE);
    public final Option<Boolean> throughWallsOption =
            new Option<>("ThroughWalls", "Attack through walls", true);
    public final Option<RaytraceMode> raytraceOption =
            new Option<>("Raytrace", "Raytracing method", RaytraceMode.EYES);
    public final Option<Boolean> stopSprintOption =
            new Option<>("StopSprinting", "Stops sprinting before attacking", true);

    // rotate options
    public final Option<Boolean> rotateOption =
            new Option<>("Rotate", "Rotate to target", true);
    public final Option<Boolean> yawStepOption =
            new Option<>("YawStep", "Apply yaw step to rotation", false);
    public final Option<Integer> yawStepThresholdOption =
            new Option<>("YawStepThreshold", "Maximum yaw step", 0, 25, 180);
    public final Option<Integer> yawStepTicksOption =
            new Option<>("YawStepTicks", "Yaw step ticks", 0, 2, 5);

    // render options
    public final Option<Boolean> renderOption =
            new Option<>("Render", "Render overlay on target", true);

    // filter options
    public final Option<Boolean> invisiblesOption =
            new Option<>("Invisibles", "Target invisibles", true);
    public final Option<Boolean> playersOption =
            new Option<>("Players","Target players", true);
    public final Option<Boolean> monstersOption =
            new Option<>("Monsters", new String[] {"Hostiles"}, "Target monsters", true);
    public final Option<Boolean> neutralsOption =
            new Option<>("Neutrals", "Target neutrals", true);
    public final Option<Boolean> animalsOption =
            new Option<>("Animals", new String[] {"Passives"}, "Target animals", true);
    public final Option<Boolean> boatsOption =
            new Option<>("Boats", "Target boats", false);
    public final Option<Boolean> shulkerBulletsOption =
            new Option<>("ShulkerBullets", "Target shulker boats", true);
    public final Option<Boolean> armorCheckOption =
            new Option<>("ArmorCheck", "Checks target's armor", false);

    // misc options
    public final Option<Boolean> autoBlockOption =
            new Option<>("AutoBlock", "Automatically shield blocks", true);
    public final Option<Integer> fovOption =
            new Option<>("FOV", "Maximum FOV", 1, 180, 180);

    // attack speeds
    public final Option<Float> attackSpeedOption =
            new Option<>("AttackSpeed", new String[] {"CPS"}, "Speed for attacks", 0.0f, 20.0f, 20.0f);
    public final Option<Float> randomSpeedOption =
            new Option<>("RandomSpeed", "Random speed for attacks", 0.0f, 0.0f, 10.0f);
    public final Option<Integer> ticksExistedOption =
            new Option<>("TicksExisted", "Ticks existed speed", 0, 50, 200);

    // ranges
    public final Option<Float> rangeOption =
            new Option<>("Range", "Range for attacks", 0.0f, 4.5f, 5.0f);
    public final Option<Float> wallRangeOption =
            new Option<>("WallRange", "Range for attacks through walls", 0.0f, 4.2f, 5.0f);

    // listeners
    public final TickListener tickListener =
            new TickListener(this);
    public final UpdateWalkingPlayerListener updateWalkingPlayerListener =
            new UpdateWalkingPlayerListener(this);
    public final EntityRemoveListener entityRemoveListener =
            new EntityRemoveListener(this);
    public final DisconnectListener disconnectListener =
            new DisconnectListener(this);
    public final RenderWorldListener renderWorldListener =
            new RenderWorldListener(this);

    // targeting
    protected Entity target;

    // rotation info
    protected Rotation rotation;
    protected double ticks;

    // last hit info
    protected final Timer last =
            new Timer();

    public AuraModule() {
        super("Aura", new String[] {"KillAura", "ForceField", "KA"}, "Attacks nearby entities", ModuleCategory.COMBAT);

        // options
        associate(
                modeOption,
                timingOption,
                priorityOption,
                respawnDisableOption,
                hitDelayOption,
                tpsSyncOption,
                throughWallsOption,
                raytraceOption,
                stopSprintOption,
                rotateOption,
                yawStepOption,
                yawStepThresholdOption,
                yawStepTicksOption,
                renderOption,
                invisiblesOption,
                playersOption,
                monstersOption,
                neutralsOption,
                animalsOption,
                boatsOption,
                shulkerBulletsOption,
                armorCheckOption,
                autoBlockOption,
                fovOption,
                attackSpeedOption,
                randomSpeedOption,
                ticksExistedOption,
                rangeOption,
                wallRangeOption,
                bind,
                drawn
        );

        // listeners
        associate(
                tickListener,
                updateWalkingPlayerListener,
                entityRemoveListener,
                disconnectListener,
                renderWorldListener
        );
    }

    /**
     * Attack a given entity
     *
     * @param in The entity to attack
     * @return Whether the attack was successful
     */
    protected boolean attack(Entity in) {

        // make sure the target actually exists
        if (in == null || in.isDead) {

            // attack was not successful
            return false;
        }

        // player shield state
        // blocking with a shield
        boolean block = false;
        if (autoBlockOption.getVal()) {

            // update shield state
            block = mc.player.getHeldItemOffhand().getItem() instanceof ItemShield
                    && mc.player.isActiveItemStackBlocking();

            // start block
            if (block) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(
                        CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        new BlockPos(mc.player),
                        EnumFacing.getFacingFromVector((float) mc.player.posX, (float) mc.player.posY, (float) mc.player.posZ)));
            }
        }

        // on strict anticheat configs, you need to stop sprinting/sneaking before attacking
        // keeping consistent with vanilla behavior
        // player sprint/sneak state
        boolean sprint = false;
        boolean sneak = false;
        if (stopSprintOption.getVal()) {

            // update sprint state
            sprint = mc.player.isSprinting();

            // stop sprinting when attacking an entity
            if (sprint) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player,
                        Action.STOP_SPRINTING));
                // mc.player.setSprinting(false);
            }

            // update sneak state
            sneak = mc.player.isSneaking();

            // stop sneaking when attacking an entity
            if (sneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player,
                        Action.STOP_SNEAKING));
                // mc.player.setSneaking(false);
            }
        }

        // send attack packet
        mc.player.connection.sendPacket(new CPacketUseEntity(in));
        mc.player.resetCooldown();

        // swing with packets
        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));

        // held item stack
        ItemStack stack = mc.player.getHeldItem(
                EnumHand.MAIN_HAND);

        // check stack
        if (!stack.isEmpty()) {

            // swing item
            if (!stack.getItem().onEntitySwing(mc.player, stack)) {

                // check swing progress
                if (!mc.player.isSwingInProgress
                        || mc.player.swingProgressInt >= ((IEntityLivingBase) mc.player).hookGetArmSwingAnimationEnd() / 2
                        || mc.player.swingProgressInt < 0) {

                    // apply swing progress
                    mc.player.swingProgressInt = -1;
                    mc.player.isSwingInProgress = true;
                    mc.player.swingingHand = EnumHand.MAIN_HAND;

                    // send animation packet
                    if (mc.player.world instanceof WorldServer) {
                        ((WorldServer) mc.player.world).getEntityTracker().sendToTracking(mc.player,
                                new SPacketAnimation(mc.player, 0));
                    }
                }
            }
        }

        // reset shield state
        if (block) {
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
        }

        // reset sprint state
        if (sprint) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player,
                    Action.START_SPRINTING));
            // mc.player.setSprinting(true);
        }

        // reset sneak state
        if (sneak) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player,
                    Action.START_SNEAKING));
            // mc.player.setSneaking(true);
        }

        // attack successful
        return true;
    }

    /**
     * Gets a new target
     *
     * @return The new target
     */
    protected Entity getTarget() {

        // don't search again if curr is still valid
        if (modeOption.getVal() == AuraMode.SINGLE) {

            // check if curr is still valid
            if (target != null && isTarget(target)) {

                // return curr
                return target;
            }
        }

        // map of valid targets
        // sorted by natural ordering of keys
        // Using tree map allows time complexity of O(1)
        TreeMap<Double, Entity> valid = new TreeMap<>();

        // search
        for (Entity e : mc.world.loadedEntityList) {

            // exclude player from target list
            if (e == mc.player) {
                continue;
            }

            // relegated to AutoCrystal
            if (e instanceof EntityEnderCrystal) {
                continue;
            }

            // check if entity is valid target
            if (isTarget(e)) {

                // entity heuristic, used as key in map
                double h = getHeuristic(e);

                // mark target
                valid.put(h, e);
            }
        }

        // valid targets list has targets
        if (!valid.isEmpty()) {

            // best target
            Entity best = valid.lastEntry().getValue();

            // check if target still exists
            if (best != null && !best.isDead) {

                // return target
                return best;
            }
        }

        // no target found
        return null;
    }

    /**
     * Checks if a given entity can be a target
     *
     * @param in The entity
     * @return Whether a given entity can be a target
     */
    protected boolean isTarget(Entity in) {

        // entity check
        if (isValidEntity(in)) {

            // invisible entity
            if (in.isInvisible() && !invisiblesOption.getVal()) {

                // cannot attack invisible entities
                return false;
            }

            // check armor
            if (armorCheckOption.getVal()) {

                // entity check
                if (in instanceof EntityPlayer) {

                    // empty slots
                    ItemStack helmet = ((EntityPlayer) in).inventory.armorInventory.get(0);
                    ItemStack chest = ((EntityPlayer) in).inventory.armorInventory.get(1);
                    ItemStack pants = ((EntityPlayer) in).inventory.armorInventory.get(2);
                    ItemStack boots = ((EntityPlayer) in).inventory.armorInventory.get(3);

                    // check armor
                    if (helmet.getItem() instanceof ItemAir
                            || chest.getItem() instanceof ItemAir
                            || pants.getItem() instanceof ItemAir
                            || boots.getItem() instanceof ItemAir) {

                        // don't attack entities without armor
                        return false;
                    }
                }
            }

            // range checks
            double dist = mc.player.getDistance(in);
            if (dist > rangeOption.getVal()) {

                // out of range
                return false;
            }

            // visibility
            boolean visible = isVisible(in);
            if (!visible && (!throughWallsOption.getVal() ||
                    dist > wallRangeOption.getVal())) {

                // out of wall range
                return false;
            }

            // fov
            double diff = RotationUtil.yawTo(mc.player.getPositionEyes(1.0f),
                    in.getPositionEyes(1.0f));

            if (Math.abs(diff) > fovOption.getVal()) {

                // out of fov range
                return false;
            }

            // target needs to exist in the world longer
            if (in.ticksExisted <
                    ticksExistedOption.getVal()) {
                return false;
            }

            // valid
            return true;
        }

        // invalid target entity type
        return false;
    }

    /**
     * Checks if a given entity is visible to the player
     *
     * @param in The entity
     * @return Whether a given entity is visible to the player
     */
    protected boolean isVisible(Entity in) {

        // scan raytrace
        if (raytraceOption.getVal() == RaytraceMode.SCAN) {

            // raytrace
            Vec3d trace = RaytraceUtil.canSeeScan(mc.player, in);

            // check bounding box
            return trace != null;
        }

        else {

            // offset
            float off = raytraceOption.getVal() ==
                    RaytraceMode.EYES ? in.getEyeHeight() : 0.0f;

            // raytrace offset
            return RaytraceUtil.canSee(mc.player, in, off);
        }
    }

    /**
     * Gets the sorting key
     *
     * @param in The value
     * @return The sorting key
     */
    private double getHeuristic(Entity in) {

        // health
        if (priorityOption.getVal() == Priority.HEALTH) {

            // living entity
            if (in instanceof EntityLivingBase) {

                // entity health
                float health = ((EntityLivingBase) in).getHealth() + ((EntityLivingBase) in).getAbsorptionAmount();

                // sorted by lowest
                return -health;
            }
        }

        // distance
        else if (priorityOption.getVal() == Priority.DISTANCE) {

            // dist to entity
            double dist = mc.player.getDistance(in);

            // sorted by lowest
            return -dist;
        }

        // entity type
        else if (priorityOption.getVal() == Priority.SMART) {

            // heuristic val
            float h = 0.0f;

            // entity health
            float health = 0.0f;

            // living entity
            if (in instanceof EntityLivingBase) {

                // entity health
                health = ((EntityLivingBase) in).getHealth() + ((EntityLivingBase) in).getAbsorptionAmount();
            }

            // dist to entity
            double dist = mc.player.getDistance(in);

            // priority
            float prio = 0.0f;

            // highest prio is players
            if (in instanceof EntityPlayer
                    && playersOption.getVal()) {

                // 5 prio
                prio = 5.0f;
            }

            // second highest prio is monsters
            else if (EntityUtil.isHostileMob(in)
                    && monstersOption.getVal()) {

                // 4 prio
                prio = 4.0f;
            }

            // third highest prio is neutrals
            else if (EntityUtil.isNeutralMob(in)
                    && neutralsOption.getVal()) {

                // 3 prio
                prio = 3.0f;
            }

            // 4th highest prio is passives
            else if (EntityUtil.isPassiveMob(in)
                    && animalsOption.getVal()) {

                // 2 prio
                prio = 2.0f;
            }

            // 4th highest prio is vehicles
            else if (EntityUtil.isVehicleMob(in)
                    && boatsOption.getVal()) {

                // 1 prio
                prio = 1.0f;
            }

            // 5 prio
            h += prio;
            h += prio / Math.max(dist, 0.0001f);
            h += prio * (health / 36.0f);

            // sorted by lowest
            return -h;
        }

        // unreachable
        return 0;
    }

    /**
     * Checks entity type
     *
     * @param in The entity
     * @return Whether the entity is a valid entity type
     */
    private boolean isValidEntity(Entity in) {

        // entity check
        return in instanceof EntityPlayer && playersOption.getVal()
                || EntityUtil.isHostileMob(in) && monstersOption.getVal()
                || EntityUtil.isNeutralMob(in) && neutralsOption.getVal()
                || EntityUtil.isPassiveMob(in) && animalsOption.getVal()
                || EntityUtil.isVehicleMob(in) && boatsOption.getVal()
                || in instanceof EntityShulkerBullet && shulkerBulletsOption.getVal();
    }

    @Override
    public String getData() {

        // data is mode
        return Formatter.formatEnum(modeOption.getVal());
    }
}
