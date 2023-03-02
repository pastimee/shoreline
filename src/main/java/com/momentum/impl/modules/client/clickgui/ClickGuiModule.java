package com.momentum.impl.modules.client.clickgui;

import com.momentum.Momentum;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

/**
 * @author linus
 * @since 01/17/2023
 */
public class ClickGuiModule extends Module {

    public ClickGuiModule() {
        super("ClickGui", new String[] { "Gui" }, "Opens the ClickGui screen", ModuleCategory.CLIENT);

        // options
        associate(
                bind,
                drawn
        );

        // default bind is RSHIFT
        bind(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // open gui screen
        // must run on main thread
        mc.addScheduledTask(() -> mc.displayGuiScreen(Momentum.CLICK_GUI));
    }
}
