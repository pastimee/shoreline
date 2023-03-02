package com.momentum.impl.registers;

import com.momentum.Momentum;
import com.momentum.api.command.Command;
import com.momentum.api.module.Module;
import com.momentum.api.registry.Registry;
import com.momentum.impl.commands.SetCommand;

/**
 * Registry of commands
 *
 * @author linus
 * @since 02/18/2023
 */
public class CommandRegistry extends Registry<Command> {

    /**
     * Registry of commands
     */
    public CommandRegistry() {

        // create set command for each module
        for (Module m : Momentum.MODULE_REGISTRY.getData()) {

            // register
            register(new SetCommand(m));
        }

        // register commands
        register(

        );
    }
}
