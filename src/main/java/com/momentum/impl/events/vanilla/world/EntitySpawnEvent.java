package com.momentum.impl.events.vanilla.world;

import com.momentum.api.event.Event;
import net.minecraft.entity.Entity;

/**
 * Called when an entity is spawned in the world
 *
 * @author linus
 * @since 03/10/2023
 */
public class EntitySpawnEvent extends Event {

    // entity being spawned
    private final Entity entity;

    /**
     * Called when an entity is spawned in the world
     *
     * @param entity The entity being spawned
     */
    public EntitySpawnEvent(Entity entity) {
        this.entity = entity;
    }

    /**
     * Gets the spawned entity
     *
     * @return The spawned entity
     */
    public Entity getEntity() {
        return entity;
    }
}
