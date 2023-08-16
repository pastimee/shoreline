package com.caspian.client.api.config;

import com.caspian.client.Caspian;
import com.caspian.client.api.config.setting.*;
import com.caspian.client.api.event.EventStage;
import com.caspian.client.impl.event.config.ConfigUpdateEvent;
import com.google.gson.JsonObject;

import java.util.function.Supplier;

/**
 * Client Configuration which is saved to a local <tt>.json</tt> file. All
 * configs must be associated with a {@link ConfigContainer} which is
 * responsible for handling caching and saving.
 *
 * <p>All configs hold a modifiable value which can be changed in the
 * ClickGui or through Commands in the chat. The config value cannot be
 * <tt>null</tt>.</p>
 *
 * @author linus
 * @since 1.0
 *
 * @param <T> The config value type
 *
 * @see BooleanConfig
 * @see ColorConfig
 * @see EnumConfig
 * @see MacroConfig
 * @see NumberConfig
 * @see StringConfig
 */
public abstract class Config<T> implements Configurable
{
    // Config name is its UNIQUE identifier
    private final String name;
    // Concise config description, displayed in the ClickGui to help users
    // understand the properties that the config modifies.
    private final String desc;
    // Config value which modifies some property. This value is configured by
    // the user and saved to a local JSON file.
    private T value;
    // Parent container. All configs should be added to a config container,
    // otherwise they will not be saved locally.
    private ConfigContainer container;
    //
    private Supplier<Boolean> visible;

    /**
     * Initializes the config with a default value. This constructor should
     * not be used to initialize a configuration, instead use the explicit
     * definitions of the configs in {@link com.caspian.client.api.config.setting}.
     *
     * @param name  The unique config identifier
     * @param desc  The config description
     * @param value The default config value
     * @throws NullPointerException if value is <tt>null</tt>
     */
    public Config(String name, String desc, T value)
    {
        if (value == null)
        {
            throw new NullPointerException("Null values not supported");
        }
        this.name = name;
        this.desc = desc;
        this.value = value;
    }

    /**
     * Initializes the config with a default value. This constructor should
     * not be used to initialize a configuration, instead use the explicit
     * definitions of the configs in {@link com.caspian.client.api.config.setting}.
     *
     * @param name  The unique config identifier
     * @param desc  The config description
     * @param value The default config value
     * @param visible The visibility of the config
     * @throws NullPointerException if value is <tt>null</tt>
     */
    public Config(String name, String desc, T value, Supplier<Boolean> visible)
    {
        this(name, desc, value);
        this.visible = visible;
    }

    /**
     *
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     *
     * @return
     *
     * @see ConfigContainer#getName()
     */
    public String getId()
    {
        return String.format("%s-%s-config", container.getName().toLowerCase(),
                name.toLowerCase());
    }

    /**
     * Returns a detailed description of the property that the {@link Config}
     * value represents.
     *
     * @return The config value description
     */
    public String getDescription()
    {
        return desc;
    }

    /**
     * Returns the configuration value.
     *
     * @return The config value
     */
    public T getValue()
    {
        return value;
    }

    /**
     *
     *
     * @return
     */
    public ConfigContainer getContainer()
    {
        return container;
    }

    /**
     * Sets the current config value to the param value. The passed value
     * cannot be <tt>null</tt>.
     *
     * @param val The param value
     * @throws NullPointerException if value is <tt>null</tt>
     */
    public void setValue(final T val)
    {
        if (val == null)
        {
            throw new NullPointerException("Null values not supported");
        }
        final ConfigUpdateEvent event = new ConfigUpdateEvent(this);
        // PRE
        event.setStage(EventStage.PRE);
        Caspian.EVENT_HANDLER.dispatch(event);
        value = val;
        // POST
        event.setStage(EventStage.POST);
        Caspian.EVENT_HANDLER.dispatch(event);
    }

    /**
     * Initializes the {@link #container} field with the parent
     * {@link ConfigContainer}. This process will be handled everytime the
     * config is added to a container during the constructor of
     * {@link ConfigContainer#ConfigContainer(String)}.
     *
     * @param cont The parent container
     */
    public void setContainer(final ConfigContainer cont)
    {
        container = cont;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public JsonObject toJson()
    {
        JsonObject configObj = new JsonObject();
        configObj.addProperty("id", getId());
        return configObj;
    }
}


