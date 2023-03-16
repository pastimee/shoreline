package com.momentum.impl.modules.render.viewclip;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 03/08/2023
 */
public class ViewClipModule extends Module {

    // clip options
    public final Option<Float> distanceOption =
            new Option<>("Distance", "Camera distance", 1.0f, 3.5f, 20.0f);

    // listeners
    public final OrientCameraListener orientCameraListener =
            new OrientCameraListener(this);

    public ViewClipModule() {
        super("ViewClip", new String[] {"CameraClip"}, "Clips the third person camera through blocks", ModuleCategory.RENDER);

        // options
        associate(
                distanceOption,
                bind,
                drawn
        );

        // listeners
        associate(
                orientCameraListener
        );
    }
}
