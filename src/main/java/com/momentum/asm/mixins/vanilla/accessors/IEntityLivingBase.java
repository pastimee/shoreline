package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Gives access to the {@link EntityLivingBase} private fields and methods
 */
@Mixin(EntityLivingBase.class)
public interface IEntityLivingBase {

    @Invoker("getArmSwingAnimationEnd")
    int hookGetArmSwingAnimationEnd();
}
