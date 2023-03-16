package com.momentum.impl.modules.render.noweather;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.util.render.Formatter;

/**
 * @author linus
 * @since 03/08/2023
 */
public class NoWeatherModule extends Module {

    // weather options
    public final Option<Weather> weatherOption =
            new Option<>("Weather", new String[] {"Mode"}, "World weather", Weather.CLEAR);
    public final Option<Integer> timeOption =
            new Option<>("Time", "World time", 0, 6000, 24000);

    // listeners
    public final OptionUpdateListener optionUpdateListener =
            new OptionUpdateListener(this);
    public final InboundPacketListener inboundPacketListener =
            new InboundPacketListener(this);

    public NoWeatherModule() {
        super("NoWeather", "Removes weather rendering", ModuleCategory.RENDER);

        // options
        associate(
                weatherOption,
                timeOption,
                bind,
                drawn
        );

        // listeners
        associate(
                optionUpdateListener,
                inboundPacketListener
        );
    }

    @Override
    public String getData() {

        // weather mode
        return Formatter.formatEnum(weatherOption.getVal());
    }
}
