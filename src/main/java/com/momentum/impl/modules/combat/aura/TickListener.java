package com.momentum.impl.modules.combat.aura;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import com.momentum.impl.init.Handlers;

/**
 * @author linus
 * @since 03/06/2023
 */
public class TickListener extends FeatureListener<AuraModule, TickEvent> {

    protected TickListener(AuraModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // search target
        feature.target = feature.getTarget();

        // attack before sending position/rotation update packets
        if (feature.timingOption.getVal() == Timing.VANILLA) {

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
