package com.momentum.impl.registers;

import com.momentum.Momentum;
import com.momentum.api.macro.Macro;
import com.momentum.api.module.Module;
import com.momentum.api.registry.Registry;

/**
 * Registry of macros
 *
 * @author linus
 * @since 03/01/2023
 */
public class MacroRegistry extends Registry<Macro> {

    /**
     * Registry of macros
     */
    public MacroRegistry() {

        // create set command for each module
        for (Module m : Momentum.MODULE_REGISTRY.getData()) {

            // register
            register(new Macro(m.getName(), m.getBind()) {

                // keybind impl
                @Override
                public void onPress() {

                    // toggle module
                    m.toggle();
                }
            });
        }

        // macros
        register(


        );
    }
}
