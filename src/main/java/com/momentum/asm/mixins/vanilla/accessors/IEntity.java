package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface IEntity {

    @Invoker("setFlag")
    void setFlag(int flag, boolean val);

    @Accessor("isInWeb")
    boolean isInWeb();
}
