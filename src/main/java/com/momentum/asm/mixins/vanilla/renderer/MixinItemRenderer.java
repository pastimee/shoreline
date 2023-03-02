package com.momentum.asm.mixins.vanilla.renderer;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.RenderHudOverlayEvent;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    /**
     * Called when the block is rendered in the hand
     */
    @Inject(method = "renderBlockInHand", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderBlockInHand(TextureAtlasSprite sprite, CallbackInfo ci) {

        // post the render hud overlay event
        RenderHudOverlayEvent renderHudOverlayEvent = new RenderHudOverlayEvent(OverlayType.BLOCK);
        Momentum.EVENT_BUS.dispatch(renderHudOverlayEvent);

        // prevent overlay from rendering
        if (renderHudOverlayEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * Called when the water overlay texture is rendered
     */
    @Inject(method = "renderWaterOverlayTexture", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderWaterOverlayTexture(float partialTicks, CallbackInfo ci) {

        // post the render hud overlay event
        RenderHudOverlayEvent renderHudOverlayEvent = new RenderHudOverlayEvent(OverlayType.WATER);
        Momentum.EVENT_BUS.dispatch(renderHudOverlayEvent);

        // prevent overlay from rendering
        if (renderHudOverlayEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * Called when the fire overlay texture is rendered
     */
    @Inject(method = "renderFireInFirstPerson", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderFireInFirstPerson(CallbackInfo ci) {

        // post the render hud overlay event
        RenderHudOverlayEvent renderHudOverlayEvent = new RenderHudOverlayEvent(OverlayType.FIRE);
        Momentum.EVENT_BUS.dispatch(renderHudOverlayEvent);

        // prevent overlay from rendering
        if (renderHudOverlayEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
