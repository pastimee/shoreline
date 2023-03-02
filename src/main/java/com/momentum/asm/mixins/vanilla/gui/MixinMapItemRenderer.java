package com.momentum.asm.mixins.vanilla.gui;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.gui.RenderMapEvent;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.world.storage.MapData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItemRenderer.class)
public class MixinMapItemRenderer {

    /**
     * Called when a map item is rendered
     */
    @Inject(method = "renderMap", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderMap(MapData mapdataIn, boolean noOverlayRendering, CallbackInfo ci) {

        // post the render map event
        RenderMapEvent renderMapEvent = new RenderMapEvent();
        Momentum.EVENT_BUS.dispatch(renderMapEvent);

        // prevent maps from rendering
        if (renderMapEvent.isCanceled()) {
            ci.cancel();
        }
    }
}

