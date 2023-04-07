package com.momentum.hwid;

import java.security.MessageDigest;

/**
 * HWID (Hardware ID) Reader utilities
 *
 * @author linus
 * @since 1.0
 */
public class HWID
{
    /**
     * TODO: MAKE THIS BETTER
     * Returns the unique hardware id based on {@link #getSystemInfo()}
     *
     * @return The final HWID
     */
    public static String getHardwareId()
    {
        // try creating HWID
        String HWID = "ERROR";
        try
        {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(getSystemInfo().getBytes());
            StringBuffer sb = new StringBuffer();
            byte[] crypt = md.digest();
            for (byte b : crypt)
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                {
                    sb.append('0');
                }
                sb.append(hex);
            }

            HWID = sb.toString();
        }

        // error creating HWID
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return HWID;
    }

    /**
     * TODO: MAKE THIS BETTER
     * Returns the system info used for the HWID
     *
     * @return The system info
     */
    public static String getSystemInfo()
    {
        return System.getenv("os")
                + System.getProperty("os.name")
                + System.getProperty("os.arch")
                + System.getProperty("user.name")
                + System.getenv("SystemRoot")
                + System.getenv("HOMEDRIVE")
                + System.getenv("COMPUTERNAME")
                + System.getenv("PROCESSOR_LEVEL")
                + System.getenv("PROCESSOR_REVISION")
                + System.getenv("PROCESSOR_IDENTIFIER")
                + System.getenv("PROCESSOR_ARCHITECTURE")
                + System.getenv("PROCESSOR_ARCHITEW6432")
                + System.getenv("NUMBER_OF_PROCESSORS");
    }
}
