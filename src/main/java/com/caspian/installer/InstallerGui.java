package com.caspian.installer;

import com.formdev.flatlaf.FlatClientProperties;
import com.caspian.Installer;
import com.caspian.hwid.Credentials;
import com.caspian.util.SystemUtil;

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
    // Mods folder directory chooser components. Allows the user to select a
    // install path for the installation wizard.
    private final JFormattedTextField directory;
    private final JFileChooser dir;

    /**
     * Loader installer gui
     */
    public InstallerGui() 
    {
        // clear layout
        setSize(200, 200);
        setLayout(null);
        JLabel welcomerLabel = new JLabel("Welcome to Caspian Installer, "
                + Credentials.USER_NAME + "!");
        welcomerLabel.putClientProperty("FlatLaf.styleClass", "h4");
        welcomerLabel.setBounds(getX() + 30, getY() + 138, 400, 27);
        welcomerLabel.setVisible(true);
        add(welcomerLabel);
        JLabel dirLabel = new JLabel("Directory");
        dirLabel.putClientProperty("FlatLaf.styleClass", "h3");
        dirLabel.setBounds(getX() + 30, getY() + 163, 100, 27);
        dirLabel.setVisible(true);
        add(dirLabel);
        dir = new JFileChooser();
        dir.addChoosableFileFilter(new DirectoryFilter());
        dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File modsdir = SystemUtil.getModsDirectory().toFile();
        directory = new JFormattedTextField(modsdir.getAbsolutePath());
        JButton open = new JButton(UIManager.getIcon("Tree.closedIcon"));
        open.setRolloverIcon(UIManager.getIcon("Tree.openIcon"));
        open.setToolTipText("Select directory");
        open.addActionListener(e ->
        {
            selectDirectory();
        });
        JToolBar tools = new JToolBar();
        tools.addSeparator();
        tools.add(open);
        directory.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT,
                tools);
        directory.setBounds(getX() + 113, getY() + 163, 350, 27);
        directory.setVisible(true);
        add(dir);
        add(directory);
        JLabel verlabel = new JLabel("Version");
        verlabel.putClientProperty("FlatLaf.styleClass", "h3");
        verlabel.setBounds(getX() + 30, getY() + dirLabel.getHeight() + 170,
                100, 27);
        verlabel.setVisible(true);
        add(verlabel);
        JComboBox<String> ver = new JComboBox<>();
        ver.setEditable(false);
        ver.setBounds(getX() + 113, getY() + dirLabel.getHeight() + 171,
                250, 27);
        ver.setModel(new DefaultComboBoxModel<>(new String[]
                {
                    "1.0.0-a.1"
                }));
        ver.setVisible(true);
        add(ver);
        JButton install = new JButton("Install");
        install.setBounds(getX() + 373, getY() + dirLabel.getHeight() + 171,
                100, 27);
        install.addActionListener(e ->
        {
            DownloadWorker downloadWorker =
                    new DownloadWorker(dir.getSelectedFile().getAbsolutePath());
            downloadWorker.addPropertyChangeListener(ev ->
            {
                if (ev.getPropertyName().equalsIgnoreCase("progress"))
                {
                    setLoadingProgress((Integer) ev.getNewValue());
                }

                else if (ev.getNewValue() == SwingWorker.StateValue.DONE)
                {
                    try
                    {
                        downloadWorker.get();
                    }

                    // failed download
                    catch (Exception ex)
                    {
                        Installer.LOGGER.error("Failed to download loader" +
                                ".jar!");
                        ex.printStackTrace();
                    }
                }
            });
        });
        install.setVisible(true);
        add(install);
        JCheckBox sodium = new JCheckBox("Include Sodium");
        sodium.setMnemonic('A');
        sodium.setBounds(getX() + 27, getY() + dirLabel.getHeight()
                + verlabel.getHeight() + 180, 200, 20);
        sodium.setVisible(true);
        add(sodium);
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

    private void setLoadingProgress(Integer progress)
    {

    }

    /**
     * Updates the selected directory
     */
    private void selectDirectory()
    {
        if (dir.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            directory.setText(dir.getSelectedFile().getAbsolutePath());
        }
    }

    /*
     * Installs a <tt>.jar</tt> {@link File} in the param directory. Returns
     * <tt>true</tt> if the installed file replaced an existing file.
     *
     * @param jar The <tt>.jar</tt> file
     * @param name The file name
     * @param dir The installation directory
     * @return <tt>true</tt> if the installed file replaced an existing file
    private boolean installJar(File jar, String name, File dir)
    {
        // replace
        try
        {
            Files.copy(jar.toPath(), new File(dir + "/" + name).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        // file copy error
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // return replaced
        return Files.exists(dir.toPath().resolve(name));
    }
     */
}
