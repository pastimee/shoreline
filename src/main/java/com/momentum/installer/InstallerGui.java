package com.momentum.installer;

import com.formdev.flatlaf.FlatClientProperties;
import com.momentum.Main;
import com.momentum.util.SystemUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author linus
 * @since 02/22/2023
 */
public class InstallerGui extends JPanel {

    // mods folder
    private final File modsdir = new File(SystemUtil.isMac() ? System.getenv("Library") + "Application Support/minecraft/mods" : System.getenv("APPDATA") + "/.minecraft/mods");

    // version combo box
    private final JLabel verlabel;
    private final JComboBox ver;

    // install directory chooser
    private final JLabel dirlabel;
    private final JFormattedTextField directory;
    private final JFileChooser dir;

    // install button
    private final JButton install;

    // optifine installer
    private final JCheckBox optifine;

    /**
     * Mod installer gui
     */
    public InstallerGui() {

        // layout
        setSize(200, 200);
        setLayout(null);

        // file chooser label
        dirlabel = new JLabel("Directory");
        dirlabel.putClientProperty("FlatLaf.styleClass",
                "h3");
        dirlabel.setBounds(getX() + 30, getY() + 160, 100, 27);
        dirlabel.setVisible(true);
        add(dirlabel);

        // file chooser
        dir = new JFileChooser();
        dir.addChoosableFileFilter(new DirectoryFilter());
        dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // directory text field
        directory = new JFormattedTextField(modsdir.getAbsolutePath());

        // open button
        JButton open = new JButton(UIManager.getIcon("Tree.closedIcon"));
        open.setRolloverIcon(UIManager.getIcon("Tree.openIcon"));
        open.setToolTipText("Select directory");
        open.addActionListener(new ActionListener() {

            /**
             * Button is pressed
             */
            public void actionPerformed(ActionEvent e) {

                // choose new directory
                dir();
            }
        });
        JToolBar tools = new JToolBar();
        tools.addSeparator();
        tools.add(open);
        directory.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT,
                tools);
        directory.setBounds(getX() + 112, getY() + 160, 250, 27);
        directory.setVisible(true);

        // add all comps
        add(dir);
        add(directory);

        // version label
        verlabel = new JLabel("Version");
        verlabel.putClientProperty("FlatLaf.styleClass",
                "h3");
        verlabel.setBounds(getX() + 30, getY() + dirlabel.getHeight() + 170, 100, 27);
        verlabel.setVisible(true);
        add(verlabel);

        // version chooser
        ver = new JComboBox();
        ver.setEditable(false);
        ver.setBounds(getX() + 113, getY() + dirlabel.getHeight() + 171, 250, 27);
        ver.setModel(new DefaultComboBoxModel(new String[] {
                "1.0",
                "1.0-beta"
        }));
        ver.setVisible(true);
        add(ver);

        // install button
        install = new JButton("Install");
        install.setBounds(getX() + 373, getY() + dirlabel.getHeight() + 171, 100, 27);
        install.addActionListener(new ActionListener() {

            /**
             * Button pressed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                // open the loading gui
                LoadingGui loadingGui = new LoadingGui();
                loadingGui.setVisible(true);
                Main.add(loadingGui);

                // close current gui
                setVisible(false);
            }
        });
        install.setVisible(true);
        add(install);

        // optifine button
        optifine = new JCheckBox("Include Optifine");
        optifine.setMnemonic('A');
        optifine.setBounds(getX() + 27, getY() + dirlabel.getHeight() + verlabel.getHeight() + 180, 200, 20);
        optifine.setVisible(true);
        add(optifine);
    }

    /**
     * Updates the selected directory
     */
    private void dir() {

        // check if it is oke to show dialog
        if (dir.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            // set director to the selected file
            directory.setText(dir.getSelectedFile().getAbsolutePath());
        }
    }
}
