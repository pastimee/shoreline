package com.caspian.installer;

import com.formdev.flatlaf.FlatClientProperties;
import com.caspian.Installer;
import com.caspian.hwid.Credentials;

import javax.swing.*;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class LoginGui extends JPanel
{
    /**
     * User login gui
     */
    public LoginGui()
    {
        // clear layout
        setSize(200, 200);
        setLayout(null);
        JLabel userlabel = new JLabel("Username");
        userlabel.putClientProperty("FlatLaf.styleClass", "h3");
        userlabel.setBounds(getX() + 130, getY() + 160, 100, 20);
        userlabel.setVisible(true);
        add(userlabel);
        JTextField username = new JFormattedTextField();
        username.setBounds(getX() + 212, getY() + 158, 150, 27);
        username.setVisible(true);
        add(username);
        JLabel passlabel = new JLabel("Password");
        passlabel.putClientProperty("FlatLaf.styleClass", "h3");
        passlabel.setBounds(getX() + 130, getY() + username.getHeight() + 170,
                100, 20);
        passlabel.setVisible(true);
        add(passlabel);
        JTextField password = new JPasswordField();
        password.putClientProperty(FlatClientProperties.STYLE,
                "showRevealButton: true");
        password.setBounds(getX() + 212, getY() + username.getHeight() + 168,
                150, 27);
        password.setVisible(true);
        add(password);
        JButton login = new JButton("Login");
        login.setToolTipText("Login with given credentials");
        login.putClientProperty("FlatLaf.styleClass", "h3");
        login.setDisplayedMnemonicIndex(0);
        login.setBounds(getX() + 282, getY() + username.getHeight() +
                password.getHeight() + 177, 80, 27);
        login.setVisible(true);
        add(login);
        login.addActionListener(e ->
        {
            // verify credentials
            if (Credentials.init())
            {
                InstallerGui installer = new InstallerGui();
                installer.setVisible(true);
                Installer.add(installer);
                setVisible(false);
            }
        });
        JCheckBox remember = new JCheckBox("Remember me");
        remember.setMnemonic('A');
        remember.setBounds(getX() + 128, getY() + username.getHeight() +
                password.getHeight() + 180, 200, 20);
        remember.setVisible(true);
        add(remember);
        JLabel copyright = new JLabel("© 2023 Caspian Development");
        copyright.setBounds(getX() + 300, getY() + 302, 500, 20);
        copyright.setVisible(true);
        JLabel version =
                new JLabel("Caspian_Installer_v" + Installer.INSTALLER_VER);
        version.setBounds(getX() + 7, getY() + 302, 500, 20);
        version.setVisible(true);
        add(copyright);
        add(version);
    }
}
