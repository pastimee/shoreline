package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.world.EntitySpawnEvent;
import net.minecraft.entity.item.EntityFireworkRocket;

/**
 * @author linus
 * @since 03/10/2023
 */
public class EntitySpawnListener extends FeatureListener<NoRenderModule, EntitySpawnEvent> {

    // violations
    private int fireworkVls;
    private long fireworkLast;

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected EntitySpawnListener(NoRenderModule feature) {
        super(feature);

        // init time
        fireworkLast = System.currentTimeMillis();
    }

    @Override
    public void invoke(EntitySpawnEvent event) {

        // firework is spawned
        if (event.getEntity() instanceof EntityFireworkRocket) {

            // rested
            if (fireworkLast > 2000) {

                // reset
                fireworkVls = 0;
            }

            // reset last
            fireworkLast = System.currentTimeMillis();

            // firework lag fix
            if (feature.fireworkLagOption.getVal()) {

                // firework entity
                EntityFireworkRocket fireworkRocket = (EntityFireworkRocket) event.getEntity();

                // make sure rocket is not attached to an entity
                if (!fireworkRocket.isAttachedToEntity()) {

                    // last rest period was less than 100 millis
                    if (fireworkLast < 100) {

                        // add violations
                        fireworkVls++;
                    }
                }

                // too many violations in short time period
                if (fireworkVls > 10) {

                    // prevent firework entities from spawning
                    event.setCanceled(true);
                }
            }
        }
    }
}
