package com.momentum.asm.mixins.vanilla.entity;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {

    /**
     * Called when an entity is updated
     */
    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onOnUpdate(CallbackInfo ci) {

        // post the update event
        UpdateEvent updateEvent = new UpdateEvent();
        Momentum.EVENT_BUS.dispatch(updateEvent);

        // prevent updating if event is canceled
        if (updateEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
