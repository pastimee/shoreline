package com.momentum.impl.configs;

import com.moandjiezana.toml.Toml;
import com.momentum.Momentum;
import com.momentum.api.config.Config;
import com.momentum.impl.ui.ClickGuiScreen;
import com.momentum.impl.ui.Frame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author linus
 * @since 02/08/2023
 */
public class ClickGuiConfig extends Config<ClickGuiScreen> {


    /**
     * ClickGui Config
     */
    public ClickGuiConfig() {

        // add shutdown hook
        SHUTDOWN_HOOK.hook(() -> save());
    }

    @Override
    public void save() {

        // path
        Path f = main.resolve("clickgui.toml");

        // catches I0Exception
        try {

            // check if it already exists
            if (!Files.exists(f)) {

                // create the new file
                Files.createFile(f);
            }
        }

        // error when writing file
        catch (IOException e) {
            e.printStackTrace();
        }

        // file data
        StringBuilder data = new StringBuilder();

        // click gui frames
        for (Frame frame : Momentum.CLICK_GUI.getFrames()) {

            // frame data
            String d = toData(frame);

            // add to data
            data.append(d);
        }

        // do not allow two streams to write to the same file at once
        if (!isDirty(f)) {

            // our file output stream
            OutputStream stream = null;

            // catches IOException
            try {

                // add to our writing paths
                markDirty(f);

                // create our file output stream
                stream = new FileOutputStream(f.toFile());

                // write our bytes to the output stream
                stream.write(data.toString().getBytes(StandardCharsets.UTF_8), 0, data.length());
            }

            // error when writing file
            catch (IOException e) {
                e.printStackTrace();
            }

            // close the stream
            finally {

                // if the stream was created, we should close it
                if (stream != null) {

                    // catches IOException
                    try {
                        stream.close();
                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // remove from our paths we are writing to
            clean(f);
        }
    }

    @Override
    public void save(String in) {

        // default save
        save();
    }

    @Override
    public void load() {

        // file path
        Path f = main.resolve("clickgui.toml");

        // toml reader
        Toml data = new Toml().read(f.toFile());

        // click gui frames
        for (Frame frame : Momentum.CLICK_GUI.getFrames()) {

            // check data
            if (data.contains(frame.getCategory() + ".X") && data.contains(frame.getCategory() + ".Y")) {

                // position
                float x = data.getDouble(frame.getCategory() + ".X").floatValue();
                float y = data.getDouble(frame.getCategory() + ".Y").floatValue();

                // update frame position
                frame.position(x, y);
            }

            // check data
            if (data.contains(frame.getCategory() + ".Open")) {

                // open state
                boolean open = data.getBoolean(frame.getCategory() + "Open");

                // update frame open state
                frame.open(open);
            }
        }
    }

    @Override
    public void load(String in) {

        // default load
        load();
    }

    /**
     * Formats a frame into a .toml data structure
     *
     * @param f The frame
     * @return The formatted frame data
     */
    public String toData(Frame f) {

        // frame file data
        StringBuilder data = new StringBuilder();

        // build data
        data.append("[")
                .append(f.getCategory())
                .append("]")
                .append("\r\n")
                .append("X = ")
                .append(f.getX())
                .append("\r\n")
                .append("Y = ")
                .append(f.getY())
                .append("\r\n")
                .append("Open = ")
                .append(f.isOpen())
                .append("\r\n")
                .append("\r\n");

        // data
        return data.toString();
    }
}
