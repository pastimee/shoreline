package com.momentum.impl.events.vanilla.world;

import com.momentum.api.event.Event;
import net.minecraft.entity.Entity;

/**
 * Called when an entity is removed from the world
 *
 * @author linus
 * @since 03/01/2023
 */
public class EntityRemoveEvent extends Event {

    // entity being removed
    private final Entity entity;

    /**
     * Called when an entity is removed from the world
     *
     * @param entity The entity being removed
     */
    public EntityRemoveEvent(Entity entity) {
        this.entity = entity;
    }

    /**
     * Gets the removed entity
     *
     * @return The removed entity
     */
    public Entity getEntity() {
        return entity;
    }
}
