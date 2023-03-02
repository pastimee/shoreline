package com.momentum.asm.mixins.vanilla.renderer.entity;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.entity.RenderEntityItemEvent;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    /**
     * Called when an item entity is rendered
     */
    @Inject(method = "doRender", at = @At(value = "HEAD"), cancellable = true)
    private void onDoRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {

        // post the render item event
        RenderEntityItemEvent renderEntityItemEvent = new RenderEntityItemEvent();
        Momentum.EVENT_BUS.dispatch(renderEntityItemEvent);

        // prevent render
        if (renderEntityItemEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
