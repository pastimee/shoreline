package com.momentum.asm.mixins.vanilla.renderer.entity;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.entity.RenderWitherSkullEvent;
import net.minecraft.client.renderer.entity.RenderWitherSkull;
import net.minecraft.entity.projectile.EntityWitherSkull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderWitherSkull.class)
public class MixinRenderWitherSkull {

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    private void onDoRender(EntityWitherSkull entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {

        // post the render wither skull event
        RenderWitherSkullEvent renderWitherSkullEvent = new RenderWitherSkullEvent();
        Momentum.EVENT_BUS.dispatch(renderWitherSkullEvent);

        // cancel redner
        if (renderWitherSkullEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
