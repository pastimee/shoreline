package com.caspian.installer;

import com.caspian.Installer;

import javax.swing.SwingWorker;
import javax.swing.SwingWorker.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class DownloadWorker extends SwingWorker<Integer, StateValue>
{
    // Selected installer file path. This file path is the installation path
    // of the loader.
    private final String selectedFile;

    /**
     *
     *
     * @param selectedFile
     */
    public DownloadWorker(String selectedFile)
    {
        this.selectedFile = selectedFile;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * <p>
     * Note that this method is executed only once.
     *
     * <p>
     * Note: this method is executed in a background thread.
     *
     * @return the computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    protected Integer doInBackground() throws Exception
    {
        // install loader
        try
        {
            Installer.LOGGER.info("Downloading loader ...");
            HttpURLConnection connection = (HttpURLConnection)
                    new URL("MOMENTUM-WEBSERVER-URL/loader.jar").openConnection();
            connection.setDoInput(true);

            // check HTTP response code
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                // HTTP input
                InputStream input = connection.getInputStream();
                long jarSize = connection.getContentLengthLong();
                if (jarSize > 0)
                {
                    // file download output
                    FileOutputStream output =
                            new FileOutputStream(selectedFile +
                                    File.separator + "momentum_loader_"
                                    + Installer.INSTALLER_VER + ".jar");

                    // write input to output
                    int read;
                    long total = 0; // total data read
                    byte[] buffer = new byte[4096];
                    while ((read = input.read(buffer)) != -1)
                    {
                        total += read;
                        output.write(buffer, 0, read);
                        setProgress((int) ((total * 100) / jarSize));
                    }

                    // install success
                    Installer.LOGGER.info("Installed loader!");
                    output.close();
                }

                input.close();
            }
        }

        // failed download/install
        catch (IOException ex)
        {
            Installer.LOGGER.error("Failed loader install! Please check " +
                    "connection ...");
            ex.printStackTrace();
        }

        return 0;
    }
}
