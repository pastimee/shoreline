package com.momentum.asm.mixins.vanilla.renderer.tileentity;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.tileentity.RenderSignTextEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntitySignRenderer.class)
public class MixinTileEntitySignRenderer {

    /**
     * Called when the sign tile entity is being rendered
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntitySignRenderer;getFontRenderer()Lnet/minecraft/client/gui/FontRenderer;", shift = Shift.AFTER), cancellable = true)
    private void onRender(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {

        // post the render sign text event
        RenderSignTextEvent renderSignTextEvent = new RenderSignTextEvent();
        Momentum.EVENT_BUS.dispatch(renderSignTextEvent);

        // cancel sign text rendering
        if (renderSignTextEvent.isCanceled()) {
            ci.cancel();

            // reset gl states
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();

            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }
}
