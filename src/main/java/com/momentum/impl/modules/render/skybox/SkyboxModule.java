package com.momentum.impl.modules.render.skybox;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 03/09/2023
 */
public class SkyboxModule extends Module {

    // sky options
    public final Option<Boolean> fogOption =
            new Option<>("Fog", "Colors fog", true);

    // listeners
    public final RenderSkyColorListener renderSkyColorListener =
            new RenderSkyColorListener(this);
    public final RenderFogColorListener renderFogColorListener =
            new RenderFogColorListener(this);

    public SkyboxModule() {
        super("Skybox", "Changes the skybox color", ModuleCategory.RENDER);

        // options
        associate(
                fogOption,
                bind,
                drawn
        );

        // listeners
        associate(
                renderSkyColorListener,
                renderFogColorListener
        );
    }
}
