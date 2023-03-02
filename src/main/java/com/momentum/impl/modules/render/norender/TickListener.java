package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

/**
 * @author linus
 * @since 03/01/2023
 */
public class TickListener extends FeatureListener<NoRenderModule, TickEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected TickListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(TickEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // remove items from world
        if (feature.noItemsOption.getVal() == NoItems.REMOVE) {

            // search all loading entities
            for (Entity e : mc.world.getLoadedEntityList()) {

                // entity is dropped item
                if (e instanceof EntityItem) {

                    // remove entity
                    mc.addScheduledTask(() -> mc.world.removeEntity(e));
                }
            }
        }
    }
}
