package com.caspian.installer;

import com.caspian.Installer;
import net.fabricmc.installer.util.Utils;

import javax.swing.*;
import java.net.URL;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class InstallerFrame extends JFrame
{
    /**
     * The main installer frame
     */
    public InstallerFrame()
    {
        // setup frame
        setTitle("Caspian Installer " + Installer.INSTALLER_VER);
        setSize(500, 350);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        URL icon = Utils.class.getClassLoader().getResource(
                "assets/icons/caspian_icon.png");
        if (icon != null)
        {
            setIconImage(new ImageIcon(icon).getImage());
        }

        // update defaults
        // UIDefaults defaults = UIManager.getDefaults();
        // defaults.put("activeCaption", new ColorUIResource(Color.darkGray));
        // defaults.put("activeCaptionText", new ColorUIResource(Color.white));
        // JFrame.setDefaultLookAndFeelDecorated(true);

        // display the login gui
        LoginGui loginGui = new LoginGui();
        add(loginGui);
        loginGui.setVisible(true);

        // this method displays the JFrame to center position of a screen
        setLocationRelativeTo(null);
    }
}
