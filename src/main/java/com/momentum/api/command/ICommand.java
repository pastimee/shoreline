package com.momentum.api.command;

/**
 * @author linus
 * @since 02/18/2023
 */
public interface ICommand {

    /**
     * Invokes the command
     *
     * @param args The command arguments
     */
    void invoke(String[] args);

    /**
     * Gets a correct use case
     *
     * @return The correct use case
     */
    String getUseCase();

    /**
     * Gets the maximum argument size
     *
     * @return The maximum argument size
     */
    int getArgSize();
}
