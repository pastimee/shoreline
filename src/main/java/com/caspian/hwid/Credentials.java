package com.caspian.hwid;

import com.caspian.Installer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Credential manager for verifying user {@link HWIDReader} and credentials
 *
 * @author linus
 * @since 1.0
 *
 *
 */
public class Credentials
{
    // user credentials
    public static String USER_NAME = "guest_user";
    public static String USER_ROLE = "guest";
    public static Boolean USER_ADMIN = false;
    public static Boolean USER_BANNED = false;    
    public static Boolean USER_IS_LINUS = false;

    /**
     * Returns <tt>true</tt> if the users HWID was contained in the webserver
     *
     * @return <tt>true</tt> if the user has a valid HWID
     */
    public static boolean init()
    {
        try
        {
            Installer.LOGGER.info("Credential login verification process " +
                    "starting ...");
            Installer.LOGGER.info("Connecting to Momentum webserver ...");
            URLConnection connection =
                    new URL("MOMENTUM-WEBSERVER-URL/cred.json").openConnection();
            connection.setDoInput(true);
            InputStream input = connection.getInputStream();
            BufferedReader streamReader =
                    new BufferedReader(new InputStreamReader(input,
                            StandardCharsets.UTF_8));
            List<String> lines =
                    streamReader.lines().collect(Collectors.toList());
            // contains a pair of user HWID and username split by ":"
            Map<String, String[]> info = new HashMap<>();
            for (String s : lines)
            {
                String[] prop = s.split(":");
                info.put(prop[0], Arrays.copyOfRange(prop, 1, prop.length));
            }

            Installer.LOGGER.info("Reading HWID ...");
            String hwid = HWIDReader.getHardwareId();
            Installer.LOGGER.info("Checking HWID ...");
            if (!hwid.equalsIgnoreCase("ERROR"))
            {
                if (info.containsKey(hwid))
                {
                    Installer.LOGGER.info("HWID success!");
                    String[] properties = info.get(hwid);
                    USER_NAME = properties[0];
                    USER_ROLE = properties[1];
                    USER_ADMIN = properties[0].startsWith("admin_");
                    USER_IS_LINUS = properties[0].startsWith("admin_linus_");
                    return true;
                }
            }

            Installer.LOGGER.info("HWID failure!");
            JOptionPane.showMessageDialog(Installer.INSTALLER, "HWID " +
                    "failure! Contact *** for HWID reset.", "Error",
                            JOptionPane.ERROR_MESSAGE);
            // Installer.closeWindow();
            return true;
        }

        // HWID read error
        catch (Exception e)
        {
            Installer.LOGGER.error("An error occurred! " + e.getClass().getName());
            e.printStackTrace();
            return true;
        }
    }
}
