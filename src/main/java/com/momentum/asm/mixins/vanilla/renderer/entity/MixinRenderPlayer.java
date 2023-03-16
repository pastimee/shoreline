package com.momentum.asm.mixins.vanilla.renderer.entity;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import com.momentum.api.util.rotation.Rotation;
import com.momentum.impl.events.vanilla.renderer.entity.RenderNametagEvent;
import com.momentum.impl.init.Handlers;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer implements Wrapper {

    // rotation values
    private float renderPitch, renderYaw, renderHeadYaw, 
            prevRenderHeadYaw, prevRenderPitch, prevRenderYawOffset, prevPrevRenderYawOffset;

    @Inject(method = "doRender", at = @At(value = "HEAD"))
    private void onDoRenderHead(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        
        // set server rotations before rendering
        if (entity == mc.player) {
            
            // save values
            prevRenderHeadYaw = entity.prevRotationYawHead;
            prevRenderPitch = entity.prevRotationPitch;
            renderPitch = entity.rotationPitch;
            renderYaw = entity.rotationYaw;
            renderHeadYaw = entity.rotationYawHead;
            prevPrevRenderYawOffset = entity.prevRenderYawOffset;
            prevRenderYawOffset = entity.renderYawOffset;
            
            // serverside rotation
            // set client rotation values to serverside rotations
            Rotation server = Handlers.ROTATION_HANDLER.getRotation();
            entity.rotationYaw = server.getYaw();
            entity.rotationYawHead = server.getYaw();
            entity.prevRotationYawHead = server.getYaw();
            entity.prevRenderYawOffset = server.getYaw();
            entity.renderYawOffset = server.getYaw();
            entity.rotationPitch = server.getPitch();
            entity.prevRotationPitch = server.getPitch();
        }
    }

    @Inject(method = "doRender", at = @At(value = "RETURN"))
    private void onDoRenderReturn(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {

        // check player
        if (entity == mc.player) {

            // reset rotation values
            entity.rotationPitch = renderPitch;
            entity.rotationYaw = renderYaw;
            entity.rotationYawHead = renderHeadYaw;
            entity.prevRotationYawHead = prevRenderHeadYaw;
            entity.prevRotationPitch = prevRenderPitch;
            entity.renderYawOffset = prevRenderYawOffset;
            entity.prevRenderYawOffset = prevPrevRenderYawOffset;
        }
    }
    

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
