package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link RenderManager} private fields
 */
@Mixin(RenderManager.class)
public interface IRenderManager {

    @Accessor("renderPosX")
    double getRenderX();

    @Accessor("renderPosY")
    double getRenderY();

    @Accessor("renderPosZ")
    double getRenderZ();
}
