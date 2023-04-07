package com.momentum.installer;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class DirectoryFilter extends FileFilter
{
    /**
     * Return <tt>true</tt> if the given file is accepted by this filter.
     *
     * @param f <tt>true</tt> if the given file is accepted by this filter.
     */
    @Override
    public boolean accept(File f)
    {
        // check if given file is directory
        return f.isDirectory();
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     *
     * @return The description of this filter.
     */
    @Override
    public String getDescription()
    {
        return "Directories only";
    }
}
