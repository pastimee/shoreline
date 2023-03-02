package com.momentum.asm.mixins.vanilla.renderer.tileentity;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.tileentity.RenderBeaconBeamEvent;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBeaconRenderer.class)
public class MixinTileEntityBeaconRenderer {

    /**
     * Called when the beacon beam is rendered
     */
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    public void renderBeaconBeam(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {

        // post the render beacon beam event
        RenderBeaconBeamEvent renderBeaconBeamEvent = new RenderBeaconBeamEvent();
        Momentum.EVENT_BUS.dispatch(renderBeaconBeamEvent);

        // cancel render
        if (renderBeaconBeamEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
