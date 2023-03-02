package com.momentum.asm.mixins.vanilla.particle;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.particle.RenderBarrierEvent;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Barrier.class)
public class MixinBarrier {

    /**
     * Called when the barrier particle is rendered
     */
    @Inject(method = "renderParticle", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {

        // post the render barrier event
        RenderBarrierEvent renderBarrierEvent = new RenderBarrierEvent();
        Momentum.EVENT_BUS.dispatch(renderBarrierEvent);

        // cancel particle rendering
        if (renderBarrierEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
