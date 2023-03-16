package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/**
 * Gives access to the {@link RenderGlobal} private fields and methods
 */
@Mixin(RenderGlobal.class)
public interface IRenderGlobal {

    @Accessor("damagedBlocks")
    Map<Integer, DestroyBlockProgress> getDamagedBlocks();
}
