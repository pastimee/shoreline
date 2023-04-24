package com.caspian;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.caspian.installer.InstallerFrame;

import java.awt.*;
import java.awt.event.WindowEvent;

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
    // installer version
    public static final String INSTALLER_VER = "1.0";

    // installer logger
    public static Logger LOGGER;

    // main installer frame.
    public static InstallerFrame INSTALLER;

    /**
     *
     *
     * @param args
     */
    public static void main(String[] args)
    {
        // Installer installer = new Installer();
        // installer.start();
    }

    public void start()
    {
        LOGGER = LogManager.getLogger("Caspian-Installer");
        LOGGER.info("Initializing installer ...");
        // set up flatlaf mac dark look and feel
        FlatMacDarkLaf.setup();
        INSTALLER = new InstallerFrame();
        INSTALLER.setVisible(true);
    }

    /**
     * Close the Installer window
     */
    public static void closeWindow()
    {
        INSTALLER.dispatchEvent(new WindowEvent(INSTALLER,
                WindowEvent.WINDOW_CLOSING));
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
