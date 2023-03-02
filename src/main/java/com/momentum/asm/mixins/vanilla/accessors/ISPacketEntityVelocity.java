package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link SPacketEntityVelocity} private fields
 */
@Mixin(SPacketEntityVelocity.class)
public interface ISPacketEntityVelocity {

    @Accessor("motionX")
    void setMotionX(int x);

    @Accessor("motionY")
    void setMotionY(int y);

    @Accessor("motionZ")
    void setMotionZ(int z);
}
