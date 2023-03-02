package com.momentum.installer;

import com.formdev.flatlaf.FlatClientProperties;
import com.momentum.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author linus
 * @since 02/22/2023
 */
public class LoginGui extends JPanel {

    // username text field
    private final JLabel userlabel;
    private final JTextField username;

    // password text field
    private final JLabel passlabel;
    private final JTextField password;

    // login buttons
    private final JButton login;
    private final JCheckBox remember;

    /**
     * User login gui
     */
    public LoginGui() {

        // layout
        setSize(200, 200);
        setLayout(null);

        // username label
        userlabel = new JLabel("Username");
        userlabel.putClientProperty("FlatLaf.styleClass",
                "h3");
        userlabel.setBounds(getX() + 130, getY() + 160, 100, 20);
        userlabel.setVisible(true);
        add(userlabel);

        // password text field
        username = new JFormattedTextField();
        username.setBounds(getX() + 212, getY() + 158, 150, 27);
        username.setVisible(true);
        add(username);

        // password label
        passlabel = new JLabel("Password");
        passlabel.putClientProperty("FlatLaf.styleClass",
                "h3");
        passlabel.setBounds(getX() + 130, getY() + username.getHeight() + 170, 100, 20);
        passlabel.setVisible(true);
        add(passlabel);

        // password text field
        password = new JPasswordField();
        password.putClientProperty(FlatClientProperties.STYLE,
                "showRevealButton: true");
        password.setBounds(getX() + 212, getY() + username.getHeight() + 168, 150, 27);
        password.setVisible(true);
        add(password);

        // login button
        login = new JButton("Login");
        login.setToolTipText("Login with given credentials");
        login.putClientProperty("FlatLaf.styleClass",
                "h3");
        login.setDisplayedMnemonicIndex(0);
        login.setBounds(getX() + 282, getY() + username.getHeight() + password.getHeight() + 177, 80, 27);
        login.setVisible(true);
        add(login);

        // login button pressed
        login.addActionListener(new ActionListener() {

            /**
             * Action is performed on the button
             */
            public void actionPerformed(ActionEvent e) {

                // open the installer gui
                InstallerGui installer = new InstallerGui();
                installer.setVisible(true);
                Main.add(installer);

                // close current gui
                setVisible(false);
            }
        });

        // remember me button
        remember = new JCheckBox("Remember me");
        remember.setMnemonic('A');
        remember.setBounds(getX() + 128, getY() + username.getHeight() + password.getHeight() + 180, 200, 20);
        remember.setVisible(true);
        add(remember);
    }
}
