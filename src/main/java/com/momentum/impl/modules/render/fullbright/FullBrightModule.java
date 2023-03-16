package com.momentum.impl.modules.render.fullbright;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

/**
 * @author linus
 * @since 02/21/2023
 */
public class FullBrightModule extends Module {

    // brightness options
    public final Option<BrightMode> modeOption =
            new Option<>("Mode", "Brightness mode", BrightMode.POTION);

    // listeners
    public final UpdateListener updateListener =
            new UpdateListener(this);

    // previous info
    public float pbright;
    public int pduration;

    public FullBrightModule() {
        super("FullBright", "Maximizes world brightness", ModuleCategory.RENDER);

        // options
        associate(
                modeOption,
                bind,
                drawn
        );

        // listeners
        associate(
                updateListener
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // save previous brightness
        pbright = mc.gameSettings.gammaSetting;

        // save previous night vision
        if (mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {

            // save duration
            pduration = mc.player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // remove night vision
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION);

        // check if we previously had night vision
        if (pduration > 0) {

            // reapply previous night vision
            mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, pduration));
            pduration = 0;
        }

        // reset brightness
        mc.gameSettings.gammaSetting = pbright;
        pbright = 0;
    }
}
