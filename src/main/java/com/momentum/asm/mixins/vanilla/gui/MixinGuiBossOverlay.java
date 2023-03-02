package com.momentum.asm.mixins.vanilla.gui;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.gui.RenderBossOverlayEvent;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay {

    /**
     * Called when the boss health overlay is rendered
     */
    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    private void onRenderBossHealth(CallbackInfo ci) {

        // post render boss overlay event
        RenderBossOverlayEvent renderBossOverlayEvent = new RenderBossOverlayEvent();
        Momentum.EVENT_BUS.dispatch(renderBossOverlayEvent);

        // remove boss health from hud
        if (renderBossOverlayEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
