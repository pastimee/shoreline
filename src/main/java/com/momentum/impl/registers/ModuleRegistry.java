package com.momentum.impl.registers;

import com.momentum.api.module.Module;
import com.momentum.api.registry.Registry;
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
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.util.Collection;

/**
 * Manages all client modules
 *
 * @author linus
 * @since 01/09/2023
 */
public class ModuleRegistry extends Registry<Module> {

    /**
     * Initializes module instances
     */
    public ModuleRegistry() {

        // initialize modules
        register(

                // COMBAT
                new AuraModule(),
                new AutoBowReleaseModule(),
                new AutoCrystalModule(),
                new AutoTotemModule(),
                new CriticalsModule(),

                // EXPLOIT
                new AntiHungerModule(),
                new PingSpoofModule(),

                // MISCELLANEOUS
                new AutoRespawnModule(),
                new TimerModule(),
                new XCarryModule(),

                // MOVEMENT
                new FastFallModule(),
                new NoSlowModule(),
                new SpeedModule(),
                new SprintModule(),
                new StepModule(),
                new VelocityModule(),

                // RENDER
                new ChamsModule(),
                new FullBrightModule(),
                new HoleEspModule(),
                new NametagsModule(),
                new NoRenderModule(),
                new NoRotateModule(),
                new NoWeatherModule(),
                new SkyboxModule(),
                new ViewClipModule(),

                // WORLD
                new FastPlaceModule(),
                new SpeedmineModule(),

                // CLIENT
                new ClickGuiModule(),
                new ColorModule(),
                new HudModule()
        );
    }
}
