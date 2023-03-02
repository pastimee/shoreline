package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.network.play.client.CPacketCloseWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link CPacketCloseWindow} private fields
 */
@Mixin(CPacketCloseWindow.class)
public interface ICPacketCloseWindow {

    @Accessor("windowId")
    int getWindowId();
}
