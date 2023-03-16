package com.momentum.impl.managers;

import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Manages user relations
 *
 * @author linus
 * @since 03/13/2023
 */
public class RelationManager {

    // backed by hash map
    private final Map<UUID, Relation> relations =
            new ConcurrentHashMap<>();

    /**
     * Adds a relation with a player
     *
     * @param p The player to add a relationship with
     * @param r The relationship to add
     */
    public void add(Entity p, Relation r) {

        // add to relations
        relations.put(p.getUniqueID(), r);
    }

    /**
     * Adds a relation with a player
     *
     * @param uuid The player UUID to add a relationship with
     * @param r The relationship to add
     */
    public void add(UUID uuid, Relation r) {

        // add to relations
        relations.put(uuid, r);
    }

    /**
     * Removes a relation from a player
     *
     * @param p The player to remove a relationship from
     * @param r The relationship to remove
     */
    public void remove(Entity p, Relation r) {

        // add to relations
        relations.remove(p.getUniqueID(), r);
    }


    /**
     * Checks if the relationship type matches
     *
     * @param p The player
     * @param r The relationship to check
     */
    public boolean isRelation(Entity p, Relation r) {

        // match
        return relations.get(p.getUniqueID()) == r;
    }

    /**
     * Gets the set of players with the given relation
     *
     * @param r The relation type
     * @return The set of players with the given relation
     */
    public Set<UUID> getPlayers(Relation r) {

        // matching players
        return relations.keySet()
                .stream()
                .filter(p -> relations.get(p) == r)
                .collect(Collectors.toSet());
    }
}
