package com.momentum.installer;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author linus
 * @since 02/22/2023
 */
public class DirectoryFilter extends FileFilter {

    /**
     * Whether the given file is accepted by this filter.
     *
     * @param f The given file
     */
    @Override
    public boolean accept(File f) {

        // check if given file is directory
        return f.isDirectory();
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     */
    @Override
    public String getDescription() {
        return "Directories only";
    }
}
