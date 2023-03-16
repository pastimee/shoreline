package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.network.play.client.CPacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to {@link CPacketConfirmTransaction} private methods/field
 */
@Mixin(CPacketConfirmTransaction.class)
public interface ICPacketConfirmTransaction {

    @Accessor("uid")
    void setUid(short uid);
}
