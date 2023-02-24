package com.momentum.util;

import java.util.Locale;

/**
 * @author linus
 * @since 02/22/2023
 */
public class SystemUtil {

    /**
     * Checks if the current OS is MacOS
     *
     * @return Whether the current OS is MacOS
     */
    public static boolean isMac() {

        // os name
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        // check if the os name contains mac
        return os.contains("mac");
    }
}
