package com.momentum.impl.configs;

import com.moandjiezana.toml.Toml;
import com.momentum.Momentum;
import com.momentum.api.config.Config;
import com.momentum.impl.managers.Relation;
import com.momentum.impl.ui.Frame;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author linus
 * @since 03/13/2023
 */
public class FriendConfig extends Config<Relation> {

    /**
     * Friends Config
     */
    public FriendConfig() {

        // add shutdown hook
        SHUTDOWN_HOOK.hook(() -> save());
    }

    /**
     * Saves all configurations
     */
    @Override
    public void save() {

        // path
        Path f = main.resolve("friends.toml");

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
        StringBuilder data =
                new StringBuilder("Friends = [");

        // friends
        for (UUID uuid : Momentum.RELATION_MANGER.getPlayers(Relation.FRIEND)) {

            // frame data
            String d = toData(uuid);

            // add to data
            data.append(d);
        }

        // close list
        data.append("]");

        // do not allow two streams to write to the same file at once
        if (!isDirty(f)) {

            // our file output stream
            OutputStream stream = null;

            // catches IOException
            try {

                // add to our writing paths
                markDirty(f);

                // create our file output stream
                stream = Files.newOutputStream(f.toFile().toPath());

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

    /**
     * Saves a specific option profile
     *
     * @param in The option profile
     */
    @Override
    public void save(String in) {

        // default save
        save();
    }

    /**
     * Loads all configurations
     */
    @Override
    public void load() {

        // file path
        Path f = main.resolve("friends.toml");

        // toml reader
        Toml data = new Toml().read(f.toFile());

        // uuid list
        for (Object uuids : data.getList("Friends")) {

            // check type
            if (uuids instanceof String) {

                // add relation
                Momentum.RELATION_MANGER.add(
                        UUID.fromString((String) uuids), Relation.FRIEND);
            }
        }
    }

    /**
     * Loads a specific option profile
     *
     * @param in The option profile
     */
    @Override
    public void load(String in) {

        // default load
        load();
    }

    /**
     * Formats a uuid into a .toml data structure
     *
     * @param uuid The uuid
     * @return The formatted uuid data
     */
    public String toData(UUID uuid) {

        // frame file data
        StringBuilder data = new StringBuilder();

        // build data
        data.append("\r\n")
                .append(uuid.toString())
                .append(",")
                .append("\r\n");

        // data
        return data.toString();
    }
}
