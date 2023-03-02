package com.momentum.impl.handlers;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.api.module.Module;
import com.momentum.impl.events.vanilla.KeyInputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Manages module functionality
 *
 * @author linus
 * @since 02/11/2023
 */
public class MacroHandler extends Handler {

    /**
     * Manages module functionality
     */
    public MacroHandler() {

        // keybind impl
        associate(new Listener<KeyInputEvent>() {

            @Override
            public void invoke(KeyInputEvent event) {

                // check all modules for toggle
                for (Module m : Momentum.MODULE_REGISTRY.getData()) {

                    // check if the module is toggle-able
                    // check if module's bind key is pressed
                    int bind = m.getBind();
                    if (Keyboard.isKeyDown(bind) && !Keyboard.isKeyDown(Keyboard.KEY_NONE)) {

                        // toggle the feature
                        m.toggle();
                    }
                }
            }
        });
    }
}
