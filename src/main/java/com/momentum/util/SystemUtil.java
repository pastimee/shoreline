package com.momentum.util;

import java.util.Locale;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class SystemUtil
{
    /**
     * Returns <tt>true</tt> if the current operating system is macOS
     *
     * @return <tt>true</tt> if the current os is macOS
     */
    public static boolean isMacOS()
    {
        // os name
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        return os.contains("mac");
    }
}
