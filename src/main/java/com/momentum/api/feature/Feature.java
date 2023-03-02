package com.momentum.api.feature;

import com.momentum.api.util.Wrapper;

/**
 * Simple client feature
 *
 * @author linus
 * @since 01/09/2023
 */
public class Feature implements Wrapper {

    // feature info
    protected final String name;
    protected final String[] aliases;
    protected final String description;

    /**
     * Feature with aliases and description
     *
     * @param name        The name of the feature
     * @param description The description of the feature
     * @param aliases     The aliases of the feature
     */
    public Feature(String name, String[] aliases, String description) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }

    /**
     * Feature with no aliases
     *
     * @param name        The name of the feature
     * @param description The description of the feature
     */
    public Feature(String name, String description) {
        this(name, new String[] {}, description);
    }

    /**
     * Feature
     *
     * @param name The name of the feature
     */
    public Feature(String name) {
        this(name, new String[] {}, "No description.");
    }

    /**
     * Checks if a given name matches this feature
     *
     * @param in The given name
     * @return Whether the given name matches this feature
     */
    public boolean equalsIgnoreCase(String in) {

        // main name matches
        if (name.equalsIgnoreCase(in)) {
            return true;
        }

        // alias matches
        else if (aliases != null) {

            // check aliases
            for (String alias : aliases) {

                // alias match
                if (alias.equalsIgnoreCase(in)) {
                    return true;
                }
            }
        }

        // none match
        return false;
    }

    /**
     * Checks if a given text starts with this feature's name/aliases
     *
     * @param in The given text
     * @return Whether them given text starts with this feature's name/aliases
     */
    public int startsWith(String in) {

        // main name matches
        if (name.toLowerCase().startsWith(in.toLowerCase())) {

            // match
            return -1;
        }

        // alias matches
        else if (aliases != null) {

            // index through aliases
            for (int i = 0; i < aliases.length; i++) {

                // match
                if (aliases[i].toLowerCase().startsWith(in.toLowerCase())) {

                    // return index
                    return i;
                }
            }
        }

        // none match
        return -2;
    }

    /**
     * Gets the name of the feature
     *
     * @return The name of the feature
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a description of the feature
     *
     * @return A description of the feature
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the aliases of the feature
     *
     * @return The aliases of the feature
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Gets the specified alias of the feature
     *
     * @param in The index of the alias
     * @return The specified alias of the feature
     */
    public String getAlias(int in) {
        return aliases[in];
    }
}
