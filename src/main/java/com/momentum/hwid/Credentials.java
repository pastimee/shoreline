package com.momentum.hwid;

import com.momentum.Installer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class Credentials
{
    /**
     * Returns <tt>true</tt> if the users HWID was contained in the webserver
     *
     * @return <tt>true</tt> if the user has a valid HWID
     */
    public static boolean verify()
    {
        // attempt to verify credential
        try
        {
            // connect to webserver
            Installer.LOGGER.info("Credential login verification process " +
                    "starting ...");
            Installer.LOGGER.info("Connecting to Momentum webserver ...");
            URLConnection connection =
                    new URL("MOMENTUM-WEBSERVER-URL").openConnection();
            connection.setDoInput(true);
            InputStream input = connection.getInputStream();
            BufferedReader streamReader =
                    new BufferedReader(new InputStreamReader(input,
                            StandardCharsets.UTF_8));
            List<String> lines =
                    streamReader.lines().collect(Collectors.toList());

            // will contain a pair of user HWID and username split by ":"
            Map<String, String> info = new HashMap<>();
            for (String s : lines)
            {
                String[] pair = s.split(":");
                info.put(pair[0], pair[1]);
            }

            // read HWID
            Installer.LOGGER.info("Reading HWID ...");
            String hwid = HWID.getHardwareId();

            // check HWID
            Installer.LOGGER.info("Checking HWID ...");
            if (!hwid.equalsIgnoreCase("ERROR"))
            {
                if (info.containsKey(hwid))
                {
                    Installer.LOGGER.info("HWID success!");
                    return true;
                }
            }
            Installer.LOGGER.info("HWID failure!");
            // JOptionPane.showMessageDialog(this.getLoadingFrame(), "HWID " +
            //        "Failure! Contact ... for HWID reset.", "Error",
            //                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // HWID read error
        catch (Exception e)
        {
            Installer.LOGGER.error("An error occurred! " + e.getClass().getName());
            e.printStackTrace();
            return false;
        }
    }
}
