package com.momentum.impl.modules.render.fullbright;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

/**
 * @author linus
 * @since 02/21/2023
 */
public class UpdateListener extends FeatureListener<FullBrightModule, UpdateEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected UpdateListener(FullBrightModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdateEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // night vision brightness
        if (feature.modeOption.getVal() == BrightMode.POTION) {

            // reset gamma
            mc.gameSettings.gammaSetting = feature.pbright;

            // brightness potion effect
            PotionEffect bright = new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false);

            // apply night vision potion effect
            mc.player.addPotionEffect(bright);
        }

        // gamma settings
        else if (feature.modeOption.getVal() == BrightMode.GAMMA) {

            // reset potion effect
            mc.player.removePotionEffect(MobEffects.NIGHT_VISION);

            // check if we previously had night vision
            if (feature.pduration > 0) {

                // reapply previous night vision
                mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, feature.pduration));
            }

            // apply gamma
            mc.gameSettings.gammaSetting = 100;
        }
    }
}
