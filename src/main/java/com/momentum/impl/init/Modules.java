package com.momentum.impl.init;

import com.momentum.Momentum;
import com.momentum.api.module.Module;
import com.momentum.impl.modules.client.clickgui.ClickGuiModule;
import com.momentum.impl.modules.client.color.ColorModule;
import com.momentum.impl.modules.client.hud.HudModule;
import com.momentum.impl.modules.combat.aura.AuraModule;
import com.momentum.impl.modules.combat.autobowrelease.AutoBowReleaseModule;
import com.momentum.impl.modules.combat.autocrystal.AutoCrystalModule;
import com.momentum.impl.modules.combat.autototem.AutoTotemModule;
import com.momentum.impl.modules.combat.criticals.CriticalsModule;
import com.momentum.impl.modules.exploit.antihunger.AntiHungerModule;
import com.momentum.impl.modules.exploit.pingspoof.PingSpoofModule;
import com.momentum.impl.modules.miscellaneous.autorespawn.AutoRespawnModule;
import com.momentum.impl.modules.miscellaneous.timer.TimerModule;
import com.momentum.impl.modules.miscellaneous.xcarry.XCarryModule;
import com.momentum.impl.modules.movement.fastfall.FastFallModule;
import com.momentum.impl.modules.movement.noslow.NoSlowModule;
import com.momentum.impl.modules.movement.speed.SpeedModule;
import com.momentum.impl.modules.movement.sprint.SprintModule;
import com.momentum.impl.modules.movement.step.StepModule;
import com.momentum.impl.modules.movement.velocity.VelocityModule;
import com.momentum.impl.modules.render.chams.ChamsModule;
import com.momentum.impl.modules.render.fullbright.FullBrightModule;
import com.momentum.impl.modules.render.holeesp.HoleEspModule;
import com.momentum.impl.modules.render.nametags.NametagsModule;
import com.momentum.impl.modules.render.norender.NoRenderModule;
import com.momentum.impl.modules.render.norotate.NoRotateModule;
import com.momentum.impl.modules.render.noweather.NoWeatherModule;
import com.momentum.impl.modules.render.skybox.SkyboxModule;
import com.momentum.impl.modules.render.viewclip.ViewClipModule;
import com.momentum.impl.modules.world.fastplace.FastPlaceModule;
import com.momentum.impl.modules.world.speedmine.SpeedmineModule;

/**
 * @author linus
 * @since 02/11/2023
 */
public class Modules {

    // MAINTAIN ORDER
    // module instances
    public static final AuraModule AURA_MODULE;
    public static final AutoBowReleaseModule AUTOBOWRELEASE_MODULE;
    public static final AutoCrystalModule AUTOCRYSTAL_MODULE;
    public static final AutoTotemModule AUTOTOTEM_MODULE;
    public static final CriticalsModule CRITICALS_MODULE;
    public static final AntiHungerModule ANTIHUNGER_MODULE;
    public static final PingSpoofModule PINGSPOOF_MODULE;
    public static final AutoRespawnModule AUTORESPAWN_MODULE;
    public static final TimerModule TIMER_MODULE;
    public static final XCarryModule XCARRY_MODULE;
    public static final FastFallModule FASTFALL_MODULE;
    public static final NoSlowModule NOSLOW_MODULE;
    public static final SpeedModule SPEED_MODULE;
    public static final SprintModule SPRINT_MODULE;
    public static final StepModule STEP_MODULE;
    public static final VelocityModule VELOCITY_MODULE;
    public static final ChamsModule CHAMS_MODULE;
    public static final FullBrightModule FULLBRIGHT_MODULE;
    public static final HoleEspModule HOLEESP_MODULE;
    public static final NametagsModule NAMETAGS_MODULE;
    public static final NoRenderModule NORENDER_MODULE;
    public static final NoRotateModule NOROTATE_MODULE;
    public static final NoWeatherModule NOWEATHER_MODULE;
    public static final SkyboxModule SKYBOX_MODULE;
    public static final ViewClipModule VIEWCLIP_MODULE;
    public static final FastPlaceModule FASTPLACE_MODULE;
    public static final SpeedmineModule SPEEDMINE_MODULE;
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
        AURA_MODULE = (AuraModule) getRegisteredModule("aura_module");
        AUTOBOWRELEASE_MODULE = (AutoBowReleaseModule) getRegisteredModule("autobowrelease_module");
        AUTOCRYSTAL_MODULE = (AutoCrystalModule) getRegisteredModule("autocrystal_module");
        AUTOTOTEM_MODULE = (AutoTotemModule) getRegisteredModule("autototem_module");
        CRITICALS_MODULE = (CriticalsModule) getRegisteredModule("criticals_module");

        // EXPLOIT
        ANTIHUNGER_MODULE = (AntiHungerModule) getRegisteredModule("antihunger_module");
        PINGSPOOF_MODULE = (PingSpoofModule) getRegisteredModule("pingspoof_module");

        // MISCELLANEOUS
        AUTORESPAWN_MODULE = (AutoRespawnModule) getRegisteredModule("autorespawn_module");
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
        CHAMS_MODULE = (ChamsModule) getRegisteredModule("chams_module");
        FULLBRIGHT_MODULE = (FullBrightModule) getRegisteredModule("fullbright_module");
        HOLEESP_MODULE = (HoleEspModule) getRegisteredModule("holeesp_module");
        NAMETAGS_MODULE = (NametagsModule) getRegisteredModule("nametags_module");
        NORENDER_MODULE = (NoRenderModule) getRegisteredModule("norender_module");
        NOROTATE_MODULE = (NoRotateModule) getRegisteredModule("norotate_module");
        NOWEATHER_MODULE = (NoWeatherModule) getRegisteredModule("noweather_module");
        SKYBOX_MODULE = (SkyboxModule) getRegisteredModule("skybox_module");
        VIEWCLIP_MODULE = (ViewClipModule) getRegisteredModule("viewclip_module");

        // WORLD
        FASTPLACE_MODULE = (FastPlaceModule) getRegisteredModule("fastplace_module");
        SPEEDMINE_MODULE = (SpeedmineModule) getRegisteredModule("speedmine_module");

        // CLIENT
        CLICKGUI_MODULE = (ClickGuiModule) getRegisteredModule("clickgui_module");
        COLOR_MODULE = (ColorModule) getRegisteredModule("color_module");
        HUD_MODULE = (HudModule) getRegisteredModule("hud_module");
    }
}
