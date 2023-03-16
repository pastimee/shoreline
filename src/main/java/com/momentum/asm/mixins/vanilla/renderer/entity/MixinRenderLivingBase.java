package com.momentum.asm.mixins.vanilla.renderer.entity;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.entity.RenderModelEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase {

    // model base
    @Shadow
    protected ModelBase mainModel;

    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindEntityTexture(Lnet/minecraft/entity/Entity;)Z", shift = Shift.AFTER), cancellable = true)
    private void onRenderModel(EntityLivingBase entitylivingbaseIn, float limbSwing,
                                  float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {

        // post the render model event
        RenderModelEvent renderModelEvent = new RenderModelEvent(mainModel, entitylivingbaseIn, limbSwing,
                limbSwing, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        Momentum.EVENT_BUS.dispatch(renderModelEvent);

        // custom model render
        if (renderModelEvent.isCanceled()) {

            // cancel render
            ci.cancel();
        }
    }
}
