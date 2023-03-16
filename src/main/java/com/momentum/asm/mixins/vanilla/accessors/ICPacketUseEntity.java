package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link CPacketUseEntity} private fields
 */
@Mixin(CPacketUseEntity.class)
public interface ICPacketUseEntity {

    @Accessor("entityId")
    int getEntityId();

    @Accessor("entityId")
    void setEntityId(int in);

    @Accessor("action")
    void setAction(Action in);
}
