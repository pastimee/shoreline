package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IGuiEditSign;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.asm.mixins.vanilla.accessors.IRenderGlobal;
import com.momentum.impl.events.vanilla.TickEvent;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntitySign;

import java.util.Map;

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

        // sign edit crash fix
        if (feature.signEditOption.getVal()) {

            // player is editing sign
            if (mc.currentScreen instanceof GuiEditSign) {

                // editing sign
                TileEntitySign sign = ((IGuiEditSign) mc.currentScreen).getTileSign();

                // blocks that are being broken
                Map<Integer, DestroyBlockProgress> breaking =
                        ((IRenderGlobal) mc.renderGlobal).getDamagedBlocks();

                // check contains
                for (DestroyBlockProgress progress : breaking.values()) {

                    // sign is being broken
                    if (sign.getPos() == progress.getPosition()) {

                        // exist gui
                        mc.player.closeScreen();
                        break;
                    }
                }
            }
        }
    }
}
