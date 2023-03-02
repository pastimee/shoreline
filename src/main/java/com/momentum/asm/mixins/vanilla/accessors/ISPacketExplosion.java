package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link SPacketExplosion} private fields
 */
@Mixin(SPacketExplosion.class)
public interface ISPacketExplosion {

    @Accessor("motionX")
    void setMotionX(float x);

    @Accessor("motionY")
    void setMotionY(float y);

    @Accessor("motionZ")
    void setMotionZ(float z);
}
