package com.momentum.api.command;

import com.momentum.api.feature.Feature;
import com.momentum.api.registry.ILabel;

/**
 * @author linus
 * @since 02/18/2023
 */
public abstract class Command extends Feature implements ICommand, ILabel {

    /**
     * Command with aliases and description
     *
     * @param name        The name of the feature
     * @param aliases     The aliases of the feature
     * @param description The description of the feature
     */
    public Command(String name, String[] aliases, String description) {
        super(name, aliases, description);
    }

    /**
     * Command with description
     *
     * @param name        The name of the feature
     * @param description The description of the feature
     */
    public Command(String name, String description) {
        super(name, description);
    }

    /**
     * Default Command constructor
     *
     * @param name The name of the feature
     */
    public Command(String name) {
        super(name);
    }

    /**
     * Gets the registry label
     *
     * @return The registry label (Must be unique!)
     */
    @Override
    public String getLabel() {

        // create label
        return name.toLowerCase() + "_command";
    }

    /**
     * Invokes the command
     *
     * @param args The command arguments
     */
    public abstract void invoke(String[] args);

    /**
     * Gets a correct use case
     *
     * @return The correct use case
     */
    public abstract String getUseCase();

    /**
     * Gets the maximum argument size
     *
     * @return The maximum argument size
     */
    public abstract int getArgSize();
}
