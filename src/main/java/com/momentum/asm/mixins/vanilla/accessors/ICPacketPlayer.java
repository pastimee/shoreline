package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link CPacketPlayer} private fields
 */
@Mixin(CPacketPlayer.class)
public interface ICPacketPlayer {

    @Accessor("moving")
    boolean isMoving();

    @Accessor("rotating")
    boolean isRotating();

    @Accessor("onGround")
    void setOnGround(boolean onGround);
}
