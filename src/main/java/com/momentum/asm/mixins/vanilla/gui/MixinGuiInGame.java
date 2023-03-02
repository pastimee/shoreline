package com.momentum.asm.mixins.vanilla.gui;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.gui.RenderPotionOverlayEvent;
import com.momentum.impl.events.vanilla.gui.RenderPumpkinOverlayEvent;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiInGame {

    /**
     * Called when the potion effects are rendered onto the screen
     */
    @Inject(method = "renderPotionEffects", at = @At(value = "HEAD"), cancellable = true)
    private void renderPotionEffects(ScaledResolution resolution, CallbackInfo ci) {

        // post the render potion hud event
        RenderPotionOverlayEvent renderPotionOverlayEvent = new RenderPotionOverlayEvent();
        Momentum.EVENT_BUS.dispatch(renderPotionOverlayEvent);

        // prevent hud from rendering if the event is canceled
        if (renderPotionOverlayEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * Called when the pumpkin overlay is rendered
     */
    @Inject(method = "renderPumpkinOverlay", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo ci) {

        // post the render potion hud event
        RenderPumpkinOverlayEvent renderPumpkinOverlayEvent = new RenderPumpkinOverlayEvent();
        Momentum.EVENT_BUS.dispatch(renderPumpkinOverlayEvent);

        // prevent overlay from rendering if the event is canceled
        if (renderPumpkinOverlayEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
