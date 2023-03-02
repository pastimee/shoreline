package com.momentum.api.config;

import java.util.HashSet;
import java.util.Set;

/**
 * @author linus
 * @since 02/06/2023
 */
public class ShutdownHook extends Thread {

    // hooks
    private final Set<Runnable> hooks = new HashSet<>();

    /**
     * Adds ShutdownHook to Runtime
     */
    public ShutdownHook() {

        // add shutdown hook to runtime
        Runtime.getRuntime()
                .addShutdownHook(this);
    }

    /**
     * Runs on game shutdown
     */
    @Override
    public void run() {

        // run hooks
        for (Runnable h : hooks) {

            // run
            h.run();
        }
    }

    /**
     * Hooks onto shutdown
     *
     * @param in Runnable to hook
     */
    public void hook(Runnable in) {

        // add to hooks
        hooks.add(in);
    }
}
