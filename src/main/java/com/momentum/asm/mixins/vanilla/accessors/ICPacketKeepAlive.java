package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.network.play.client.CPacketKeepAlive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to {@link CPacketKeepAlive} private methods/field
 */
@Mixin(CPacketKeepAlive.class)
public interface ICPacketKeepAlive {

    @Accessor("key")
    void setKey(long key);
}
