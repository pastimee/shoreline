package com.momentum;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.momentum.installer.InstallerFrame;

import java.awt.*;

/**
 * @author linus
 * @since 02/22/2023
 */
public class Main {

    // main installer frame
    private static InstallerFrame installer;

    /**
     * com.momentum.Main method
     *
     * @param args Invoker args
     */
    public static void main(String[] args) {

        // set up flatlaf look and feel
        FlatMacDarkLaf.setup();

        // display the installer frame
        installer = new InstallerFrame();
        installer.setVisible(true);
    }

    /**
     * Adds the given component
     *
     * @param comp The component to add
     */
    public static void add(Component comp) {

        // add to main installer frame
        installer.add(comp);
    }
}
