package com.momentum.asm.mixins.vanilla.entity.player;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.entity.player.EntityCollisionEvent;
import com.momentum.impl.events.vanilla.entity.player.PushedByWaterEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    /**
     * Dispatched when the {@link EntityPlayer} class checks if the player should be pushed by water
     */
    @Inject(method = "isPushedByWater", at = @At("HEAD"), cancellable = true)
    public void onIsPushedByWater(CallbackInfoReturnable<Boolean> cir) {

        // post the water push event
        PushedByWaterEvent pushedByWaterEvent = new PushedByWaterEvent();
        Momentum.EVENT_BUS.dispatch(pushedByWaterEvent);

        // cancel water push if the event is canceled
        if (pushedByWaterEvent.isCanceled()) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

    /**
     * Dispatched when the {@link EntityPlayer} class applies the entity collision velocity multiplier
     */
    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void onApplyEntityCollision(Entity entityIn, CallbackInfo ci) {

        // post the entity push event
        EntityCollisionEvent entityCollisionEvent = new EntityCollisionEvent();
        Momentum.EVENT_BUS.dispatch(entityCollisionEvent);

        // cancel entity push if the event is canceled
        if (entityCollisionEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
