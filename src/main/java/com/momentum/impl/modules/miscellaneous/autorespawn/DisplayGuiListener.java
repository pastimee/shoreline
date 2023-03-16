package com.momentum.impl.modules.miscellaneous.autorespawn;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.DisplayGuiEvent;
import net.minecraft.client.gui.GuiGameOver;

/**
 * @author linus
 * @since 03/10/2023
 */
public class DisplayGuiListener extends FeatureListener<AutoRespawnModule, DisplayGuiEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected DisplayGuiListener(AutoRespawnModule feature) {
        super(feature);
    }

    @Override
    public void invoke(DisplayGuiEvent event) {

        // we have just died and the respawn screen has been displayed
        if (event.getGuiScreen() instanceof GuiGameOver) {

            // mark for respawn
            feature.respawn = true;
            feature.delay.reset();
        }
    }
}
