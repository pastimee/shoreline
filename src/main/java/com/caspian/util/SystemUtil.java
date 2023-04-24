package com.caspian.util;

import java.io.File;
import java.nio.file.Path;
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

    /**
     * Returns the {@link Path} to the %APPDATA directory
     *
     * @return The path to %APPDATA dir
     */
    public static Path getAppDataDirectory()
    {
        // os name
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
        {
            return new File(System.getenv("APPDATA")).toPath();
        }

        else if (os.contains("mac"))
        {
            return new File(System.getProperty("user.home")
                    + "/Library/Application Support").toPath();
        }

        else if (os.contains("nux"))
        {
            return new File(System.getProperty("user.home")).toPath();
        }

        else
        {
            return new File(System.getProperty("user.dir")).toPath();
        }
    }

    /**
     * Returns the mods directory in the .minecraft folder
     *
     * @return The mods dir
     */
    public static Path getModsDirectory()
    {
        // mods folder
        final Path APPDATA = getAppDataDirectory();
        return isMacOS() ? APPDATA.resolve("minecraft").resolve("mods") :
                APPDATA.resolve(".minecraft").resolve("mods");
    }
}
