package com.momentum.impl.registers;

import com.momentum.api.module.Module;
import com.momentum.api.registry.Registry;
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
                new AutoBowReleaseModule(),
                new AutoTotemModule(),
                new CriticalsModule(),

                // EXPLOIT
                new AntiHungerModule(),

                // MISCELLANEOUS
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
                new FullBrightModule(),
                new NametagsModule(),
                new NoRenderModule(),

                // WORLD
                new FastPlaceModule(),

                // CLIENT
                new ClickGuiModule(),
                new ColorModule(),
                new HudModule()
        );
    }
}
