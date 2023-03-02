package com.momentum.impl.init;

import com.momentum.Momentum;
import com.momentum.api.module.Module;
import com.momentum.impl.modules.client.clickgui.ClickGuiModule;
import com.momentum.impl.modules.client.color.ColorModule;
import com.momentum.impl.modules.client.hud.HudModule;
import com.momentum.impl.modules.combat.autobowrelease.AutoBowReleaseModule;
import com.momentum.impl.modules.combat.autototem.AutoTotemModule;
import com.momentum.impl.modules.combat.criticals.CriticalsModule;
import com.momentum.impl.modules.exploit.antihunger.AntiHungerModule;
import com.momentum.impl.modules.miscellaneous.timer.TimerModule;
import com.momentum.impl.modules.miscellaneous.xcarry.XCarryModule;
import com.momentum.impl.modules.movement.fastfall.FastFallModule;
import com.momentum.impl.modules.movement.noslow.NoSlowModule;
import com.momentum.impl.modules.movement.speed.SpeedModule;
import com.momentum.impl.modules.movement.sprint.SprintModule;
import com.momentum.impl.modules.movement.step.StepModule;
import com.momentum.impl.modules.movement.velocity.VelocityModule;
import com.momentum.impl.modules.render.fullbright.FullBrightModule;
import com.momentum.impl.modules.render.nametags.NametagsModule;
import com.momentum.impl.modules.render.norender.NoRenderModule;
import com.momentum.impl.modules.world.fastplace.FastPlaceModule;

/**
 * @author linus
 * @since 02/11/2023
 */
public class Modules {

    // MAINTAIN ORDER
    // module instances
    public static final AutoBowReleaseModule AUTOBOWRELEASE_MODULE;
    public static final AutoTotemModule AUTOTOTEM_MODULE;
    public static final CriticalsModule CRITICALS_MODULE;
    public static final AntiHungerModule ANTIHUNGER_MODULE;
    public static final TimerModule TIMER_MODULE;
    public static final XCarryModule XCARRY_MODULE;
    public static final FastFallModule FASTFALL_MODULE;
    public static final NoSlowModule NOSLOW_MODULE;
    public static final SpeedModule SPEED_MODULE;
    public static final SprintModule SPRINT_MODULE;
    public static final StepModule STEP_MODULE;
    public static final VelocityModule VELOCITY_MODULE;
    public static final FullBrightModule FULLBRIGHT_MODULE;
    public static final NametagsModule NAMETAGS_MODULE;
    public static final NoRenderModule NORENDER_MODULE;
    public static final FastPlaceModule FASTPLACE_MODULE;
    public static final ColorModule COLOR_MODULE;
    public static final ClickGuiModule CLICKGUI_MODULE;
    public static final HudModule HUD_MODULE;

    /**
     * Gets the registered module
     *
     * @param label The module label
     * @return The registered module
     */
    private static Module getRegisteredModule(String label) {

        // module from registry
        return Momentum.MODULE_REGISTRY.lookup(label);
    }

    static {

        // COMBAT
        AUTOBOWRELEASE_MODULE = (AutoBowReleaseModule) getRegisteredModule("autobowrelease_module");
        AUTOTOTEM_MODULE = (AutoTotemModule) getRegisteredModule("autototem_module");
        CRITICALS_MODULE = (CriticalsModule) getRegisteredModule("criticals_module");

        // EXPLOIT
        ANTIHUNGER_MODULE = (AntiHungerModule) getRegisteredModule("antihunger_module");

        // MISCELLANEOUS
        TIMER_MODULE = (TimerModule) getRegisteredModule("timer_module");
        XCARRY_MODULE = (XCarryModule) getRegisteredModule("xcarry_module");

        // MOVEMENT
        FASTFALL_MODULE = (FastFallModule) getRegisteredModule("fastfall_module");
        NOSLOW_MODULE = (NoSlowModule) getRegisteredModule("noslow_module");
        SPEED_MODULE = (SpeedModule) getRegisteredModule("speed_module");
        SPRINT_MODULE = (SprintModule) getRegisteredModule("sprint_module");
        STEP_MODULE = (StepModule) getRegisteredModule("step_module");
        VELOCITY_MODULE = (VelocityModule) getRegisteredModule("velocity_module");

        // RENDER
        FULLBRIGHT_MODULE = (FullBrightModule) getRegisteredModule("fullbright_module");
        NAMETAGS_MODULE = (NametagsModule) getRegisteredModule("nametags_module");
        NORENDER_MODULE = (NoRenderModule) getRegisteredModule("norender_module");

        // WORLD
        FASTPLACE_MODULE = (FastPlaceModule) getRegisteredModule("fastplace_module");

        // CLIENT
        CLICKGUI_MODULE = (ClickGuiModule) getRegisteredModule("clickgui_module");
        COLOR_MODULE = (ColorModule) getRegisteredModule("color_module");
        HUD_MODULE = (HudModule) getRegisteredModule("hud_module");
    }
}
