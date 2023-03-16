package com.momentum.impl.handlers;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.api.macro.Macro;
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
        Momentum.EVENT_BUS.subscribe(new Listener<KeyInputEvent>() {

            @Override
            public void invoke(KeyInputEvent event) {

                // check all macros
                for (Macro m : Momentum.MACRO_REGISTRY.getData()) {

                    // check if macro's key is pressed
                    int key = m.getKey();
                    if (Keyboard.isKeyDown(key) && !Keyboard.isKeyDown(Keyboard.KEY_NONE)) {

                        // run the macro
                        m.onPress();
                    }
                }
            }
        });
    }

    /**
     * Sets the macro key
     *
     * @param label The macro label
     * @param key The new key
     */
    public void setKey(String label, int key) {

        // macro from label
        Macro m = Momentum.MACRO_REGISTRY.lookup(label);

        // update key
        m.setKey(key);
    }
}
