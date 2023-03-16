package com.momentum.impl.modules.render.holeesp;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 03/15/2023
 */
public class HoleEspModule extends Module {

    // hole options
    public final Option<Float> rangeOption =
            new Option<>("Range", "Range to render", 2.0f, 5.0f, 20.0f);
    public final Option<Float> sizeOption =
            new Option<>("Size", "Size to render", 0.0f, 1.0f, 1.0f);

    // listeners
    public final RenderWorldListener renderWorldListener =
            new RenderWorldListener(this);

    public HoleEspModule() {
        super("HoleESP", "Highlights blast resistant holes", ModuleCategory.RENDER);

        // options
        associate(
                rangeOption,
                sizeOption,
                bind,
                drawn
        );

        // listeners
        associate(
                renderWorldListener
        );
    }
}
