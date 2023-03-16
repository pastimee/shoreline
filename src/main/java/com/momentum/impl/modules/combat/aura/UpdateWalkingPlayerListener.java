package com.momentum.impl.modules.combat.aura;

import com.momentum.api.event.EventStage;
import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.rotation.Rotation;
import com.momentum.api.util.rotation.RotationUtil;
import com.momentum.api.util.world.RaytraceUtil;
import com.momentum.impl.events.vanilla.entity.UpdateWalkingPlayerEvent;
import com.momentum.impl.init.Handlers;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author linus
 * @since 03/06/2023
 */
public class UpdateWalkingPlayerListener extends FeatureListener<AuraModule, UpdateWalkingPlayerEvent> {

    // current yaw step
    private float curr = -1;

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected UpdateWalkingPlayerListener(AuraModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdateWalkingPlayerEvent event) {

        // pre packet update
        if (event.getStage() == EventStage.PRE) {

            // rotation sys
            if (feature.rotateOption.getVal()) {

                // check that we have a target to rotate to
                if (feature.target != null) {

                    // cancel the existing rotations
                    event.setCanceled(true);

                    // last rotation has been completed
                    if (feature.rotation == null) {

                        // rotate to trace
                        if (feature.raytraceOption.getVal() == RaytraceMode.SCAN) {

                            // raytrace scan target
                            Vec3d trace = RaytraceUtil.canSeeScan(mc.player, feature.target);

                            // check if trace has found an opening
                            if (trace != null) {

                                // calc rotation
                                feature.rotation = RotationUtil.diff(mc.player.getPositionEyes(1.0f),
                                        trace);
                            }

                            // rotate to eyes
                            else {

                                // calc rotation
                                feature.rotation = RotationUtil.diff(mc.player.getPositionEyes(1.0f),
                                        feature.target.getPositionEyes(1.0f));
                            }
                        }

                        // rotate to vec
                        else {

                            // offset
                            float off = feature.raytraceOption.getVal() == RaytraceMode.EYES ?
                                    feature.target.getEyeHeight() : 0.0f;

                            // calc rotation
                            feature.rotation = RotationUtil.diff(mc.player.getPositionEyes(1.0f),
                                    feature.target.getPositionEyes(1.0f)
                                            .addVector(0.0f, off, 0.0f));
                        }
                    }

                    // target rotation
                    Rotation target = feature.rotation;

                    // slow down rotations
                    // ease into target rotation
                    if (feature.yawStepOption.getVal()) {

                        // rotation that we have serverside
                        Rotation server = Handlers.ROTATION_HANDLER.getRotation();

                        // wrapped yaw value
                        float yaw = MathHelper.wrapDegrees(server.getYaw());

                        // difference between current and upcoming rotation
                        float diff = target.getYaw() - yaw;

                        // should never be over 180 since the angles are at max 180 and if it's greater
                        // than 180 this means we'll be doing a less than ideal turn
                        // (i.e. current = 180, required = -180 -> the turn will be
                        // 360 degrees instead of just no turn since 180 and -180 are equivalent)
                        // at worst scenario, current = 90, required = -90 creates a turn of 180 degrees,
                        // so this will be our max
                        if (Math.abs(diff) > 180) {

                            // adjust yaw
                            float adjust = diff > 0 ? -360 : 360;
                            diff += adjust;
                        }

                        // need new yaw step
                        if (curr == -1) {

                            // calc current yaw step
                            curr = Math.abs(diff) / (feature.yawStepTicksOption.getVal() + 1);

                            // rotating too fast
                            if (curr > feature.yawStepThresholdOption.getVal()) {

                                // clamp
                                curr = feature.yawStepThresholdOption.getVal();
                            }
                        }

                        // rotate ticks
                        if (feature.ticks == 0) {

                            // calc ticks
                            feature.ticks = Math.ceil(Math.abs(diff) / curr);
                        }

                        // rotate
                        if (feature.ticks > 1) {

                            // rotation direction
                            float dir = diff > 0 ? 1.0f : -1.0f;

                            // current yaw step
                            float rot = yaw + (curr * dir);

                            // update rotation
                            event.setYaw(rot);
                            event.setPitch(target.getPitch());
                            feature.ticks--;
                        }

                        // finish rotation
                        else {

                            // update rotation
                            event.setYaw(target.getYaw());
                            event.setPitch(target.getPitch());
                            feature.rotation = null;
                            feature.ticks = 0;
                            curr = -1;
                        }
                    }

                    // instant rotate
                    else {

                        // update rotation
                        event.setYaw(target.getYaw());
                        event.setPitch(target.getPitch());
                        feature.rotation = null;
                    }
                }
            }
        }

        // find target before rotation
        if (event.getStage() == EventStage.POST &&
                feature.timingOption.getVal() == Timing.SEQUENTIAL) {

            // check if target exists
            if (feature.target != null) {

                // rotate wait
                if (feature.ticks == 0) {

                    // 1.9+ hit delay
                    if (feature.hitDelayOption.getVal()) {

                        // current tps
                        float tps = 0.0f;

                        // minimal tps sync
                        if (feature.tpsSyncOption.getVal() == TpsSync.MINIMAL) {

                            // minimum time
                            tps = Handlers.TICK_HANDLER.getMinimal();
                        }

                        // latest tps
                        else if (feature.tpsSyncOption.getVal() == TpsSync.LAST) {

                            // latest time
                            tps = Handlers.TICK_HANDLER.getLast();
                        }

                        // normal tps calc
                        else if (feature.tpsSyncOption.getVal() == TpsSync.AVERAGE) {

                            // average time
                            tps = Handlers.TICK_HANDLER.getTps();
                        }

                        // tick offset
                        float off = 20.0f - tps;

                        // wait for vanilla hit delay
                        if (mc.player.getCooledAttackStrength(0.5f - off) >= 1.0f) {

                            // attack target
                            if (feature.attack(feature.target)) {

                                // reset time
                                feature.last.reset();
                            }
                        }
                    }

                    // custom hit delay
                    else {

                        // attack delay
                        float factor = feature.attackSpeedOption.getMax() - feature.attackSpeedOption.getVal();
                        float delay = factor * 50.0f;

                        // check if time since last attack has cleared delay
                        if (feature.last.passed((long) delay)) {

                            // attack target
                            if (feature.attack(feature.target)) {

                                // reset time
                                feature.last.reset();
                            }
                        }
                    }
                }
            }
        }
    }
}
