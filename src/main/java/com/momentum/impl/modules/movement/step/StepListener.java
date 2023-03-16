package com.momentum.impl.modules.movement.step;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import com.momentum.asm.mixins.vanilla.accessors.ITimer;
import com.momentum.impl.events.vanilla.entity.StepEvent;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linus
 * @since 02/20/2023
 */
public class StepListener extends FeatureListener<StepModule, StepEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected StepListener(StepModule feature) {
        super(feature);
    }

    @Override
    public void invoke(StepEvent event) {

        // NCP bypass
        if (feature.modeOption.getVal() == StepMode.NORMAL) {

            // current step
            double step = event.getAxisAlignedBB().minY - mc.player.posY;

            // check if current step is valid
            // do not step if we're on the ground or the step height is greater than our max
            if (step <= 0 || step > feature.heightOption.getVal()) {
                return;
            }

            // one block jump
            double[] packets = new double[] {
                    0.42, step < 1.0 && step > 0.8 ? 0.753 : 0.75, 1.0,
                    1.16, 1.23, 1.2
            };

            // two block stop
            if (step >= 2) {

                // two block jump
                packets = new double[] {
                        0.42, 0.78, 0.63, 0.51,
                        0.9, 1.21, 1.45, 1.43
                };
            }

            // use timer to slow down motion
            if (feature.useTimerOption.getVal()) {

                // update timer
                // Modules.TIMER_MODULE.provide(step > 1.0 ? 0.15f : 0.35f);
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f / (step > 1.0 ? 0.15f : 0.35f));
                feature.timer = true;
            }

            // send our NCP offsets
            for (int i = 0; i < (step > 1 ? packets.length : 2); i++) {

                // send position packet
                mc.player.connection.sendPacket(new CPacketPlayer.Position(
                        mc.player.posX, mc.player.posY + packets[i], mc.player.posZ, false));
            }
        }
    }
}
