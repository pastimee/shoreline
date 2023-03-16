package com.momentum.impl.modules.render.noweather;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.client.OptionUpdateEvent;

/**
 * @author linus
 * @since 03/08/2023
 */
public class OptionUpdateListener extends FeatureListener<NoWeatherModule, OptionUpdateEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected OptionUpdateListener(NoWeatherModule feature) {
        super(feature);
    }

    @Override
    public void invoke(OptionUpdateEvent event) {

        // weather mode is updated
        if (event.getOption() == feature.weatherOption) {

            // update weather
            mc.world.setRainStrength(
                    feature.weatherOption.getVal().getWeatherId());
        }

        // time is updated
        else if (event.getOption() == feature.timeOption) {

            // update world time
            mc.world.setWorldTime(
                    feature.timeOption.getVal());
        }
    }
}
