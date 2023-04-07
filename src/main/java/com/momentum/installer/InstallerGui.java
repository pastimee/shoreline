package com.momentum.installer;

import com.formdev.flatlaf.FlatClientProperties;
import com.momentum.Installer;
import com.momentum.util.SystemUtil;

import javax.swing.*;
import java.io.File;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class InstallerGui extends JPanel 
{
    // mods folder directory chooser components
    private final JFormattedTextField directory;
    private final JFileChooser dir;

    /**
     * Mod installer gui
     */
    public InstallerGui() 
    {
        // clear layout
        setSize(200, 200);
        setLayout(null);

        // file chooser label
        // install directory chooser
        JLabel dirLabel = new JLabel("Directory");
        dirLabel.putClientProperty("FlatLaf.styleClass", "h3");
        dirLabel.setBounds(getX() + 30, getY() + 160, 100, 27);
        dirLabel.setVisible(true);
        add(dirLabel);

        // file chooser
        dir = new JFileChooser();
        dir.addChoosableFileFilter(new DirectoryFilter());
        dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // directory text field
        // mods folder
        File modsdir = new File(SystemUtil.isMacOS() ? System.getenv(
                "Library") + "Application Support/minecraft/mods" :
                System.getenv("APPDATA") + "/.minecraft/mods");
        directory = new JFormattedTextField(modsdir.getAbsolutePath());

        // open button
        JButton open = new JButton(UIManager.getIcon("Tree.closedIcon"));
        open.setRolloverIcon(UIManager.getIcon("Tree.openIcon"));
        open.setToolTipText("Select directory");
        open.addActionListener(e ->
        {
            // choose new directory
            selectDirectory();
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
        // version combo box
        JLabel verlabel = new JLabel("Version");
        verlabel.putClientProperty("FlatLaf.styleClass", "h3");
        verlabel.setBounds(getX() + 30, getY() + dirLabel.getHeight() + 170,
                100, 27);
        verlabel.setVisible(true);
        add(verlabel);

        // version chooser
        JComboBox ver = new JComboBox();
        ver.setEditable(false);
        ver.setBounds(getX() + 113, getY() + dirLabel.getHeight() + 171,
                250, 27);
        ver.setModel(new DefaultComboBoxModel(new String[] 
                {
                    "1.0",
                    "1.0-beta"
                }
                ));
        ver.setVisible(true);
        add(ver);

        // install button
        // install button
        JButton install = new JButton("Install");
        install.setBounds(getX() + 373, getY() + dirLabel.getHeight() + 171,
                100, 27);
        install.addActionListener(e ->
        {
            // open the loading gui
            LoadingGui loadingGui = new LoadingGui();
            loadingGui.setVisible(true);
            Installer.add(loadingGui);

            // close current gui
            setVisible(false);
        });
        
        install.setVisible(true);
        add(install);

        // sodium button
        // sodium installer
        JCheckBox sodium = new JCheckBox("Include Sodium");
        sodium.setMnemonic('A');
        sodium.setBounds(getX() + 27, getY() + dirLabel.getHeight()
                + verlabel.getHeight() + 180, 200, 20);
        sodium.setVisible(true);
        add(sodium);
    }

    /**
     * Updates the selected directory
     */
    private void selectDirectory()
    {
        // set director to the selected file
        if (dir.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            directory.setText(dir.getSelectedFile().getAbsolutePath());
        }
    }
}
