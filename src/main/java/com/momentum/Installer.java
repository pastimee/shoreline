package com.momentum;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.momentum.installer.InstallerFrame;

import java.awt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class Installer
{
    // installer logger
    public static Logger LOGGER:

    // main installer frame
    private static InstallerFrame INSTALLER;

    public static void main(String[] args)
    {
        LOGGER = LogManager.getLogger("Momentum-Installer");
        LOGGER.info("Initializing installer ...");

        // set up flatlaf mac dark look and feel
        FlatMacDarkLaf.setup();

        // display the installer frame
        INSTALLER = new InstallerFrame();
        INSTALLER.setVisible(true);
    }

    /**
     * Adds a {@link Component} to the installer frame
     *
     * @param comp The component
     */
    public static void add(Component comp)
    {
        INSTALLER.add(comp);
    }
}
