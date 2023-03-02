package com.momentum.api.config;

import com.momentum.api.registry.ILabel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Config implementation
 *
 * @author linus
 * @since 02/06/2023
 * @param <T> The config type
 */
public abstract class Config<T> implements IConfig<T>, ILabel {

    // shutdown hook
    protected static final ShutdownHook SHUTDOWN_HOOK = new ShutdownHook();

    // paths that are being written to
    private final List<Path> writing = new CopyOnWriteArrayList<>();

    // user home directory
    // running directory
    protected Path dir;
    protected Path rdir = Paths.get("");

    // sub directories
    protected Path main;
    protected Path modules;

    /**
     * Custom config
     */
    public Config() {

        // catches exception
        try {

            // user home dir
            dir = new File(System.getProperty("user.home")).toPath();
        }

        // handles the property DNE
        catch (Exception e) {

            // dir becomes running dir
            dir = rdir;
            e.printStackTrace();
        }

        // if we cannot write, minecraft always has access to the running directory, so we'll allow that
        if (dir == null || !Files.exists(dir) || !Files.isWritable(dir)) {
            dir = rdir;
        }

        // create directories
        main = dir.resolve("Momentum");
        modules = main.resolve("modules");

        // catches I0Exception
        try {

            // check if it already exists
            if (!Files.exists(main)) {

                // create directories
                Files.createDirectory(main);
            }

            // check if it already exists
            if (!Files.exists(modules)) {

                // create directories
                Files.createDirectory(modules);
            }
        }

        // error when writing file
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all configurations
     */
    @Override
    public abstract void save();

    /**
     * Saves a specific option profile
     *
     * @param in The option profile
     */
    @Override
    public abstract void save(String in);

    /**
     * Loads all configurations
     */
    @Override
    public abstract void load();

    /**
     * Loads a specific option profile
     *
     * @param in The option profile
     */
    @Override
    public abstract void load(String in);

    /**
     * Gets the registry label
     *
     * @return The registry label (Must be unique!)
     */
    @Override
    public String getLabel() {

        // class name with identifier
        String clazz = getClass().getSimpleName().toLowerCase();

        // class name
        clazz = clazz.substring(0, clazz.length() - 6);

        // create label
        return clazz + "_file";
    }

    /**
     * Mark path as writing
     *
     * @param in The path that is writing
     */
    public void markDirty(Path in) {

        // add to list of writing dirs
        writing.add(in);
    }

    /**
     * Removes from current writing dirs
     *
     * @param in The path to remove
     */
    public void clean(Path in) {

        // remove from list of writing dirs
        writing.remove(in);
    }

    /**
     * Checks if a given path is dirty
     *
     * @param in The path
     * @return Whether a given path is dirty
     */
    public boolean isDirty(Path in) {

        // check if currently writing
        return writing.contains(in);
    }
}
