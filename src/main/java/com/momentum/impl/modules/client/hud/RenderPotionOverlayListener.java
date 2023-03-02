package com.momentum.impl.modules.client.hud;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.gui.RenderPotionOverlayEvent;

/**
 * @author linus
 * @since 02/11/2023
 */
public class RenderPotionOverlayListener extends FeatureListener<HudModule, RenderPotionOverlayEvent> {
    protected RenderPotionOverlayListener(HudModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderPotionOverlayEvent event) {

        // hide potion hud
        if (feature.potionHudOption.getVal() == PotionHud.HIDE) {

            // prevent potion hud from rendering
            event.setCanceled(true);
        }
    }
}
