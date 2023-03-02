package com.momentum.impl.modules.movement.speed;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IEntity;
import com.momentum.impl.events.vanilla.entity.MoveEvent;
import com.momentum.impl.init.Modules;
import net.minecraft.init.MobEffects;

/**
 * @author linus
 * @since 02/13/2023
 */
public class MoveListener extends FeatureListener<SpeedModule, MoveEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected MoveListener(SpeedModule feature) {
        super(feature);
    }

    @Override
    public void invoke(MoveEvent event) {

        // liquid check
        if (!feature.speedInWaterOption.getVal() && (mc.player.isInWater() || mc.player.isInLava())) {
            return;
        }

        // web check
        if (((IEntity) mc.player).isInWeb()) {
            return;
        }

        // make sure the player can have speed applied
        if (mc.player.isOnLadder() || mc.player.capabilities.isFlying || mc.player.isElytraFlying()) {
            return;
        }

        // update movements
        event.setCanceled(true);

        // reset timer
        Modules.TIMER_MODULE.provide(1f);

        // base move speed
        double base = 0.2873;

        // speed potion effect
        if (mc.player.isPotionActive(MobEffects.SPEED)) {

            // potion amplifier
            double amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();

            // adjust base to account for potion effect
            base *= 1 + (0.2 * (amplifier + 1));
        }

        // slowness potion effect
        if (mc.player.isPotionActive(MobEffects.SLOWNESS)) {

            // potion amplifier
            double amplifier = mc.player.getActivePotionEffect(MobEffects.SLOWNESS).getAmplifier();

            // adjust base to account for potion effect
            base /= 1 + (0.2 * (amplifier + 1));
        }

        // check if player is moving
        if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {

            /*
             * Incredibly similar to sprint jumping, bypasses lots of anticheats as the movement is similar
             * to sprint jumping. Max speed: ~29 kmh with timer
             */
            if (feature.modeOption.getVal() == SpeedMode.STRAFE) {

                // use timer
                if (feature.useTimerOption.getVal()) {

                    // bypass is 1.088
                    Modules.TIMER_MODULE.provide(1.088f);
                }

                // start the motion
                if (feature.strafeStage == 1) {

                    // starting speed
                    feature.speed = 1.35 * base - 0.01;
                }

                // start jumping
                else if (feature.strafeStage == 2) {

                    // the jump height
                    double jump = 0.3999999463558197;

                    // scale jump speed if Jump Boost potion effect is active
                    // not really too useful for Speed like the other potion effects
                    if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {

                        // potion amplifier
                        double amplifier = mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();

                        // adjust jump to account for potion effect
                        jump += (amplifier + 1) * 0.1;
                    }

                    // jump
                    mc.player.motionY = jump;
                    event.setY(jump);

                    // since we just jumped, we can now move faster
                    // alternate acceleration ticks
                    feature.speed *= feature.accelerate ? 1.6835 : 1.395;
                }

                // start speeding when falling
                else if (feature.strafeStage == 3) {

                    // take into account our last tick's move speed
                    double scaledSpeed = 0.66 * (feature.distance - base);

                    // scale the move speed
                    feature.speed = feature.distance - scaledSpeed;

                    // we've just slowed down and need to alternate acceleration
                    feature.accelerate = !feature.accelerate;
                }

                // collision detection
                else {

                    // collision check
                    boolean coll = mc.player.collidedVertically
                            || !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, mc.player.motionY, 0)).isEmpty();

                    // reset stage
                    if (coll && feature.strafeStage > 0) {

                        // reset strafe stage
                        feature.strafeStage = mc.player.moveForward != 0 || mc.player.moveStrafing != 0 ? 1 : 0;
                    }

                    // collision speed
                    feature.speed = feature.distance - (feature.distance / 159);
                }

                // do not allow movements slower than base speed
                feature.speed = Math.max(feature.speed, base);

                // the current movement input values of the user
                float forward = mc.player.movementInput.moveForward;
                float strafe = mc.player.movementInput.moveStrafe;
                float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

                // check if player is inputting any movements
                if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) {

                    // if we're not inputting any movements, then we shouldn't be adding any motion
                    event.setX(0);
                    event.setZ(0);
                }

                // moving forward
                else if (forward != 0) {

                    // right
                    if (strafe > 0) {

                        // update yaw
                        yaw += forward > 0 ? -45 : 45;
                    }

                    // left
                    else if (strafe < 0) {

                        // update yaw
                        yaw += forward > 0 ? 45 : -45;
                    }

                    // update states
                    strafe = 0;
                    forward = forward > 0 ? 1 : -1;
                }

                // our facing values, according to movement not rotations
                double x = Math.cos(Math.toRadians(yaw));
                double z = -Math.sin(Math.toRadians(yaw));

                // new motion in comp
                double mx = (forward * feature.speed * z) + (strafe * feature.speed * x);
                double mz = (forward * feature.speed * x) - (strafe * feature.speed * z);

                // update motion
                event.setX(mx);
                event.setZ(mz);
                feature.strafeStage++;
            }

            /*
             * Strafe for NCP Updated
             * Max speed: ~26 or 27 kmh with timer
             */
            else if (feature.modeOption.getVal() == SpeedMode.STRAFE_STRICT) {

                // use timer
                if (feature.useTimerOption.getVal()) {

                    // bypass is 1.088
                    Modules.TIMER_MODULE.provide(1.088f);
                }

                // start the motion
                if (feature.strafeStage == 1) {

                    // starting speed
                    feature.speed = 1.35 * base - 0.01;
                }

                // start jumping
                else if (feature.strafeStage == 2) {

                    // the jump height
                    double jump = 0.3999999463558197;

                    // scale jump speed if Jump Boost potion effect is active
                    // not really too useful for Speed like the other potion effects
                    if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {

                        // potion amplifier
                        double amplifier = mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();

                        // adjust jump to account for potion effect
                        jump += (amplifier + 1) * 0.1;
                    }

                    // jump
                    mc.player.motionY = jump;
                    event.setY(jump);

                    // since we just jumped, we can now move faster
                    feature.speed *= 2.149;
                }

                // start speeding when falling
                else if (feature.strafeStage == 3) {

                    // take into account our last tick's move speed
                    double scaledSpeed = 0.66 * (feature.distance - base);

                    // scale the move speed
                    feature.speed = feature.distance - scaledSpeed;
                }

                // collision detection
                else {

                    // collision check
                    boolean coll = mc.player.collidedVertically ||
                            !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, mc.player.motionY, 0)).isEmpty();

                    // reset stage
                    if (coll && feature.strafeStage > 0) {

                        // reset strafe stage
                        feature.strafeStage = mc.player.moveForward != 0 || mc.player.moveStrafing != 0 ? 1 : 0;
                    }

                    // collision speed
                    feature.speed = feature.distance - (feature.distance / 159);
                }

                // do not allow movements slower than base speed
                feature.speed = Math.max(feature.speed, base);

                // base speeds
                double baseStrict = 0.465;
                double baseRestrict = 0.44;

                // scale move speed if Speed or Slowness potion effect is active
                if (mc.player.isPotionActive(MobEffects.SPEED)) {

                    // potion amplifier
                    double amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();

                    // adjust base to account for potion effect
                    baseStrict *= 1 + (0.2 * (amplifier + 1));
                    baseRestrict *= 1 + (0.2 * (amplifier + 1));
                }

                // slowness potion effect
                if (mc.player.isPotionActive(MobEffects.SLOWNESS)) {

                    // potion amplifier
                    double amplifier = mc.player.getActivePotionEffect(MobEffects.SLOWNESS).getAmplifier();

                    // adjust base to account for potion effect
                    baseStrict /= 1 + (0.2 * (amplifier + 1));
                    baseRestrict /= 1 + (0.2 * (amplifier + 1));
                }

                // do not allow movements slower than base speed
                // clamp the value based on the number of ticks passed
                feature.speed = Math.min(feature.speed, feature.timeout > 25 ? baseStrict : baseRestrict);

                // timeout for 50 ticks
                feature.timeout++;
                if (feature.timeout > 50) {

                    // reset timeout
                    feature.timeout = 0;
                }

                // the current movement input values of the user
                float forward = mc.player.movementInput.moveForward;
                float strafe = mc.player.movementInput.moveStrafe;
                float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

                // check if player is inputting any movements
                if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) {

                    // if we're not inputting any movements, then we shouldn't be adding any motion
                    event.setX(0);
                    event.setZ(0);
                }

                // moving forward
                else if (forward != 0) {

                    // right
                    if (strafe >= 1) {

                        // update yaw
                        yaw += forward > 0 ? -45 : 45;
                        strafe = 0;
                    }

                    // left
                    else if (strafe <= -1) {

                        // update yaw
                        yaw += forward > 0 ? 45 : -45;
                        strafe = 0;
                    }

                    // update states
                    forward = forward > 0 ? 1 : -1;
                }

                // our facing values, according to movement not rotations
                double x = Math.cos(Math.toRadians(yaw));
                double z = -Math.sin(Math.toRadians(yaw));

                // new motion in comp
                double mx = (forward * feature.speed * z) + (strafe * feature.speed * x);
                double mz = (forward * feature.speed * x) - (strafe * feature.speed * z);

                // update motion
                event.setX(mx);
                event.setZ(mz);
                feature.strafeStage++;
            }
        }

        else {

            // reset
            feature.speed = 0;
            feature.distance = 0;
            feature.accelerate = false;
            feature.timeout = 0;
            feature.strafeStage = 4;
        }
    }
}
