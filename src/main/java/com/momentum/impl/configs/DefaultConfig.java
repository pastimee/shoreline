package com.momentum.impl.configs;

import com.momentum.impl.configs.UserConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author linus
 * @since 02/03/2023
 */
public class DefaultConfig extends UserConfig {

    @Override
    public void save() {

        // save and update
        Path pmodules = modules;
        modules = main.resolve("defaults");

        // catches I0Exception
        try {

            // if already exists -> exit
            if (!Files.exists(modules) || !Files.isDirectory(modules)) {

                // create directories
                Files.createDirectory(modules);
            }
        }

        // error when writing file
        catch (IOException e) {
            e.printStackTrace();
        }

        // run save
        super.save();

        // reset
        modules = pmodules;
    }

    @Override
    public void save(String in) {

    }

    @Override
    public void load() {

        // save and update
        Path pmodules = modules;
        modules = main.resolve("defaults");

        // catches I0Exception
        try {

            // if already exists -> exit
            if (!Files.exists(modules) || !Files.isDirectory(modules)) {

                // create directories
                Files.createDirectory(modules);
            }
        }

        // error when writing file
        catch (IOException e) {
            e.printStackTrace();
        }

        // run save
        super.load();

        // reset
        modules = pmodules;
    }

    @Override
    public void load(String in) {

    }
}
