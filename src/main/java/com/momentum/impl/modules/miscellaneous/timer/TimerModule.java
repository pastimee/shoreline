package com.momentum.impl.modules.miscellaneous.timer;

import com.momentum.api.feature.IService;
import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import com.momentum.asm.mixins.vanilla.accessors.ITimer;
import com.momentum.impl.init.Handlers;

import java.util.List;

/**
 * @author linus
 * @since 02/13/2023
 */
public class TimerModule extends Module implements IService<Float> {

    // tick speed option
    public final Option<Float> ticksOption =
            new Option<>("Ticks", "Tick speed", 0.1f, 2.0f, 50.0f);
    public final Option<Boolean> tpsSyncOption =
            new Option<>("TpsSync", "Syncs game tick speed to server tick speed", false);

    // listeners
    public final OptionUpdateListener optionUpdateListener =
            new OptionUpdateListener(this);
    public final TickListener tickListener =
            new TickListener(this);

    // previous states
    public boolean penabled = enabled.getVal();
    public float pticksOption = ticksOption.getVal();

    public TimerModule() {
        super("Timer", "Changes client tick speed", ModuleCategory.MISCELLANEOUS);

        // options
        associate(
                ticksOption,
                tpsSyncOption,
                bind,
                drawn
        );

        // listeners
        associate(
                optionUpdateListener,
                tickListener
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // sync
        if (tpsSyncOption.getVal()) {

            // sync tick speed
            float sync = Math.max(Handlers.TICK_HANDLER.getTps() / 20.0f, 0.01f);
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f / sync);
        }

        // custom tick speed
        else {

            // reset tick length
            pticksOption = ticksOption.getVal();
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f / ticksOption.getVal());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // reset tick length
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
    }

    /**
     * Provides the service
     *
     * @param in The input
     */
    @Override
    public void provide(Float in) {

        // tick cannot be less than or equal 0
        if (in <= 0) {
            throw new IndexOutOfBoundsException();
        }

        // reset
        if (in == 1f) {

            // reset
            ticksOption.setVal(pticksOption);
            enabled.setVal(penabled);
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
            return;
        }

        // update tick length
        penabled = enabled.getVal();
        enable();

        // save old
        pticksOption = ticksOption.getVal();
        ticksOption.setVal(in);
    }

    /**
     * Queues to preform at a later time
     *
     * @param in The input
     * @param q  The q to wait
     */
    @Override
    public void queue(Float in, List<Float> q) {
        // no impl
    }

    @Override
    public String getData() {

        // display current ticks
        return String.valueOf(ticksOption.getVal());
    }
}
