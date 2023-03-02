package com.momentum.asm.mixins.vanilla.renderer.entity;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.entity.RenderNametagEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

    /**
     * Called when the entity nametag is rendered
     */
    @Inject(method = "renderEntityName", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderEntityName(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo ci) {

        // post the render nametag event
        RenderNametagEvent renderNametagEvent = new RenderNametagEvent();
        Momentum.EVENT_BUS.dispatch(renderNametagEvent);

        // cancel render
        if (renderNametagEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
